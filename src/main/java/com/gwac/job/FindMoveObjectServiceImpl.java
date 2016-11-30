/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import com.gwac.dao.DataProcessMachineDAO;
import com.gwac.dao.MoveObjectDao;
import com.gwac.dao.MoveObjectRecordDao;
import com.gwac.dao.ObservationSkyDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtObserveRecordDAO;
import com.gwac.linefind.DrawObject;
import com.gwac.linefind.HoughFrame;
import com.gwac.linefind.HoughTransform;
import com.gwac.linefind.HoughtPoint;
import com.gwac.linefind.LineObject;
import com.gwac.model.DataProcessMachine;
import com.gwac.model.MoveObject;
import com.gwac.model.MoveObjectRecord;
import com.gwac.model.ObservationSky;
import com.gwac.model.OtObserveRecord;
import java.util.ArrayList;
import java.util.List;
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
  private OtLevel2Dao otlv2Dao;
  @Resource
  private OtObserveRecordDAO oorDao;
  @Resource(name = "moveObjectDao")
  private MoveObjectDao moveObjectDao;
  @Resource(name = "moveObjectRecordDao")
  private MoveObjectRecordDao moveObjectRecordDao;
  @Resource
  private ObservationSkyDao observationSkyDao;
  @Resource
  private DataProcessMachineDAO dpmDao;

  @Value("#{syscfg.gwacServerBeijing}")
  private Boolean isBeiJingServer;
  @Value("#{syscfg.gwacServerTest}")
  private Boolean isTestServer;

  private int imgWidth = 3072;
  private int imgHeight = 3072;
  private int thetaSize = 180;
  private int rhoSize = 100;
  private int thetaRange = 36;
  private int rhoRange = 10;
  private int maxHoughFrameNunmber = 10;
  private int validLineMinPoint = 5;
  private float maxDistance = 100;
  private int minValidPoint = 3;
  private float rhoErrorTimes = (float) 0.2; //0.2

  int count = 1;

//  @Scheduled(cron = "0/1 * *  * * ? ")
  @Override
  public void startJob() {
    int aa = count++;
    log.warn(aa + " start, move object number: ");
    processAllDay();
    log.warn(aa + " end, move object number: ");
  }

  public void processAllDay() {

    List<String> dateStrs = otlv2Dao.getAllDateStr();
    List<ObservationSky> skys = observationSkyDao.findAll();
    List<DataProcessMachine> dpms = dpmDao.findAll();
    log.debug("total days: " + dateStrs.size());
    for (String dateStr : dateStrs) {
      for (DataProcessMachine dpm : dpms) {
        for (ObservationSky sky : skys) {
          List<OtObserveRecord> oors = oorDao.getOt1ByDateDpmSkyId(dateStr, dpm.getDpmId(), sky.getSkyId());
          int ot1num = oors.size();
          log.debug("day=" + dateStr + ", dmp=" + dpm.getDpmId() + ", sky=" + sky.getSkyId() + ", ot1num=" + ot1num);
          if (ot1num > 0) {
            processOneDay(oors, dateStr, dpm.getDpmId(), sky.getSkyId());
          }
        }
      }
      break;
    }

//    String dateStr = "160928";
//    int dpmId = 6;
//    int skyId = 11;
//    List<OtObserveRecord> oors = oorDao.getOt1ByDateDpmSkyId(dateStr, dpmId, skyId);
//    System.out.println("size:" + oors.size());
//    processOneDay(oors, dateStr, dpmId, skyId);
  }

  public void processOneDay(List<OtObserveRecord> oors, String dateStr, int dpmId, int skyId) {

    HoughTransform ht = new HoughTransform(imgWidth, imgHeight, thetaSize, rhoSize, thetaRange, rhoRange, maxHoughFrameNunmber, minValidPoint, maxDistance, rhoErrorTimes, validLineMinPoint);

    int lastFrameNumber = 0;
    int frameCount = 0;
    int pNum = 0;
    for (OtObserveRecord oor : oors) {
      if (lastFrameNumber != oor.getFfNumber()) {
        lastFrameNumber = oor.getFfNumber();
        ht.endFrame();
      }
      ht.historyAddPoint(oor);
      ht.lineAddPoint(oor);

      pNum++;

    }

    ht.endAllFrame();

    ArrayList<LineObject> mvObjs = ht.getMvObjs();
    ArrayList<LineObject> fastObjs = ht.getFastObjs();
    ArrayList<LineObject> singleFrameObjs = ht.getSingleFrameObjs();
    log.debug("mvObjs:" + mvObjs.size() + ", fastObjs:" + fastObjs.size() + ", singleFrameObjs:" + singleFrameObjs.size());

    int idx = 1;
    for (LineObject obj : mvObjs) {
      if (obj.pointNumber >= validLineMinPoint) {
        saveLineObject('1', obj, dateStr, dpmId, skyId);
        log.debug(idx++);
      }
    }
    for (LineObject obj : fastObjs) {
      if (obj.pointNumber >= validLineMinPoint) {
        saveLineObject('2', obj, dateStr, dpmId, skyId);
        log.debug(idx++);
      }
    }
    for (LineObject obj : singleFrameObjs) {
      if (obj.pointNumber >= validLineMinPoint) {
        saveLineObject('3', obj, dateStr, dpmId, skyId);
        log.debug(idx++);
      }
    }

//    ht.saveLine2(outPath);
//    DrawObject dObj = new DrawObject(ht);
//    dObj.drawObjsAll("E:\\" + dateStr + "-" + dpmId + "-" + skyId + ".png");
  }

  /**
   *
   * @param moveType 1，出现多帧，一帧一点，少数帧多个点；2，出现多帧，一帧多点；3，出现1帧，一帧多点
   * @param obj
   * @param dateStr
   * @param dpmId
   * @param skyId
   */
  public void saveLineObject(char moveType, LineObject obj, String dateStr, int dpmId, int skyId) {

    MoveObject mObj = new MoveObject();
    mObj.setDateStr(dateStr);
    mObj.setDpmId(dpmId);
    mObj.setSkyId(skyId);
    mObj.setTotalFrameNumber((short) obj.frameList.size());
    mObj.setAvgFramePointNumber((float) (obj.pointNumber * 1.0 / mObj.getTotalFrameNumber()));
    mObj.setFirstFrameNum((short) obj.firstFrameNumber);
    mObj.setFirstFrameTime(obj.firstPoint.getDateUtc());
    mObj.setLastFrameNum((short) obj.lastFrameNumber);
    mObj.setLastFrameTime(obj.lastPoint.getDateUtc());
    mObj.setMovType(moveType);

    moveObjectDao.save(mObj);
    for (HoughtPoint hp : obj.pointList) {
      MoveObjectRecord mor = new MoveObjectRecord();
      mor.setMovId(mObj.getMovId());
      mor.setOorId(hp.getOorId());
      mor.setSpeedX(hp.getxSpeedt());
      mor.setSpeedY(hp.getySpeedt());
      moveObjectRecordDao.save(mor);
    }

  }

}
