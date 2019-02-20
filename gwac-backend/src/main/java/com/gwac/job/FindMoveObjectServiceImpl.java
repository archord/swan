/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import com.gwac.dao.CameraDao;
import com.gwac.dao.MoveObjectDao;
import com.gwac.dao.MoveObjectRecordDao;
import com.gwac.dao.ObservationSkyDao;
import com.gwac.dao.OtObserveRecordDAO;
import com.gwac.linefind.FindMoveObject;
import com.gwac.linefind.HoughtPoint;
import com.gwac.linefind.LineObject;
import com.gwac.linefind.LineParameterConfig;
import com.gwac.model.Camera;
import com.gwac.model.OtObserveRecordMovObj;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 数据转移，清空需要频繁查询的表，这些表只存储当天的最新数据，之后将表中数据移动到历史表
 *
 * @author xy
 */
@Service(value = "findMoveObjectService")
public class FindMoveObjectServiceImpl implements BaseService {

  private static final Log log = LogFactory.getLog(FindMoveObjectServiceImpl.class);
  private static boolean running = true;

  @Resource
  private OtObserveRecordDAO oorDao;
  @Resource(name = "moveObjectDao")
  private MoveObjectDao moveObjectDao;
  @Resource(name = "moveObjectRecordDao")
  private MoveObjectRecordDao moveObjectRecordDao;
  @Resource
  private ObservationSkyDao observationSkyDao;
  @Resource
  private CameraDao camDao;

  @Value("#{syscfg.gwacServerBeijing}")
  private Boolean isBeiJingServer;
  @Value("#{syscfg.gwacServerTest}")
  private Boolean isTestServer;
  @Value("#{syscfg.gwacMovObjListCacheDirectory}")
  private String movObjCacheDir;
  @Value("#{syscfg.gwacDataRootDirectory}")
  private String rootDir;

  private final LineParameterConfig parmG;
  private final LineParameterConfig parmM;
  private final int validLineMinPoint;
  private final Map<Integer, FindMoveObject> fmoMaps;
  private final Map<Integer, Long> oorMaps;
  private List<Camera> tcams;

  public FindMoveObjectServiceImpl() {

    validLineMinPoint = 5;
    fmoMaps = new HashMap();
    oorMaps = new HashMap();

    int imgWidthG = 4196;
    int imgHeightG = 4136;
    int imgWidthM = 3056;
    int imgHeightM = 3056;

    parmG = new LineParameterConfig(imgWidthG, imgHeightG);
    parmM = new LineParameterConfig(imgWidthM, imgHeightM);

    tcams = new ArrayList();
  }

//  @Scheduled(cron = "0/1 * *  * * ? ")
  @Override
  public void startJob() {

    if (isBeiJingServer || isTestServer) {
      return;
    }

    long startTime = System.nanoTime();

    processRealTime();

    long endTime = System.nanoTime();
    log.debug("job consume " + 1.0 * (endTime - startTime) / 1e9 + " seconds.");
  }

  public void processRealTime() {

    if (tcams.isEmpty()) {
      tcams = camDao.findAll();
    }

    Object[] objs = oorDao.getMinMaxDateOt1();
    if (objs != null && objs.length == 4 && objs[0] != null && objs[1] != null && objs[2] != null && objs[3] != null) {
      Date minDate = (Date) objs[0];
      Date maxDate = (Date) objs[1];
      Integer minNum = (Integer) objs[2];
      Integer maxNum = (Integer) objs[3];

      String dateStr = "";
      int tIdx = 1;
      StringBuilder sb2 = new StringBuilder();

      for (Camera tcam : tcams) {
	Integer camId = tcam.getCameraId();
	FindMoveObject fmo = fmoMaps.get(camId);
	if (fmo == null) {
	  String camType = tcam.getCameraType();
	  if (camType.equals("JFoV")) {
	    fmo = new FindMoveObject(parmG);
	  } else {
	    fmo = new FindMoveObject(parmM);
	  }
	  fmoMaps.put(camId, fmo);
	}

	Long startOorId = oorMaps.get(camId);
	if (startOorId == null) {
	  startOorId = (long) 0;
	  oorMaps.put(camId, startOorId);
	}
	List<OtObserveRecordMovObj> oors = oorDao.getOt1ByOorId(camId, startOorId);
	if (!oors.isEmpty()) {
	  if (dateStr.isEmpty()) {
	    dateStr = oors.get(0).getDateStr();
	  }

	  int lastFrameNumber = 0;
	  List<OtObserveRecordMovObj> singleFrame = new ArrayList<>();
	  for (OtObserveRecordMovObj oor : oors) {
	    oor.setX(oor.getXTemp());
	    oor.setY(oor.getYTemp());
	    if (lastFrameNumber != oor.getFfNumber()) {
	      lastFrameNumber = oor.getFfNumber();
	      fmo.addFrame(singleFrame);
	      singleFrame.clear();
	    }
	    singleFrame.add(oor);
	  }
	  oorMaps.put(camId, oors.get(oors.size() - 1).getOorId());

	  fmo.addFrame(singleFrame);
	  fmo.endAllFrame();

	  log.debug(tcam.getName() + " total " + fmo.mvObjs.size() + " objs.");
	  for (LineObject obj : fmo.mvObjs) {
	    if (obj.pointNumber >= validLineMinPoint && obj.isValidLine()) {
	      String tstr = lineObject2Str(obj, tIdx);
	      sb2.append(tstr);
	      sb2.append(",");
	    }
	    tIdx++;
	  }
	}
      }
      String tstr = sb2.toString();
      if (!tstr.isEmpty()) {
	StringBuilder sb = new StringBuilder();
	sb.append("{\"maxDate\":\"");
	sb.append(maxDate);
	sb.append("\",\"maxNum\":");
	sb.append(maxNum);
	sb.append(",\"minDate\":\"");
	sb.append(minDate);
	sb.append("\",\"minNum\":");
	sb.append(minNum);
	sb.append(",\"motList\":[");
	sb.append(tstr.substring(0, tstr.length()-1));
	sb.append("]}");
	FileOutputStream out = null;
	try {
	  String tpath = rootDir + "/" + movObjCacheDir;
	  File destDir = new File(tpath);
	  if (!destDir.exists()) {
	    destDir.mkdirs();
	  }
//	String fullname = tpath + "/" + dateStr + ".json";
	  String fullname = tpath + "/today.json";
	  log.debug(fullname);
	  out = new FileOutputStream(new File(fullname));
	  out.write(sb.toString().getBytes());
	  out.close();
	} catch (FileNotFoundException ex) {
	  log.error(ex);
	} catch (IOException ex) {
	  log.error(ex);
	} finally {
	  try {
	    if (out != null) {
	      out.close();
	    }
	  } catch (IOException ex) {
	    log.error(ex);
	  }
	}
      }
    }
  }

  /**
   * 1，出现多帧，一帧一点，少数帧多个点；2，出现多帧，一帧多点；3，出现1帧，一帧多点
   *
   * @param obj
   * @param tIdx
   */
  public String lineObject2Str(LineObject obj, int tIdx) {

    StringBuilder sb = new StringBuilder();
    sb.append("{\"mov_id\":");
    sb.append(tIdx);
    sb.append(",\"tt_frm_num\":");
    sb.append(obj.frameList.size());
    sb.append(",\"mov_type\":\"");
    sb.append(obj.lineType);
    sb.append("\",\"mov_detail\":[");
    int i = 0;
    int total = obj.pointList.size();
    for (HoughtPoint hp : obj.pointList) {
      sb.append("{\"ff_number\":");
      sb.append(hp.getFrameNumber());
      sb.append(",\"ra_d\":");
      sb.append(hp.getRa());
      sb.append(",\"dec_d\":");
      sb.append(hp.getDec());
      sb.append(",\"x\":");
      sb.append(hp.getX());
      sb.append(",\"y\":");
      sb.append(hp.getY());
      sb.append(",\"ffName\":\"");
      sb.append(hp.getFfName());
      sb.append("\"}");
      i++;
      if (i < total) {
	sb.append(",");
      }
    }
    sb.append("]}");
    return sb.toString();
  }

}
