/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import com.gwac.activemq.OTCheckMessageCreator;
import com.gwac.dao.CameraDao;
import com.gwac.dao.CrossFileDao;
import com.gwac.dao.CrossObjectDao;
import com.gwac.dao.CrossRecordDao;
import com.gwac.dao.CrossTaskDao;
import com.gwac.dao.DataProcessMachineDAO;
import com.gwac.dao.FitsFile2DAO;
import com.gwac.dao.FitsFileCutDAO;
import com.gwac.dao.FitsFileCutRefDAO;
import com.gwac.dao.OTCatalogDao;
import com.gwac.dao.ObservationSkyDao;
import com.gwac.dao.Ot2StreamNodeTimeDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtNumberDao;
import com.gwac.dao.OtObserveRecordDAO;
import com.gwac.dao.OtTypeDao;
import com.gwac.dao.UploadFileUnstoreDao;
import com.gwac.dao.WebGlobalParameterDao;
import com.gwac.model.Camera;
import com.gwac.model.CrossFile;
import com.gwac.model.CrossTask;
import com.gwac.model.FitsFile2;
import com.gwac.model.FitsFileCut;
import com.gwac.model.FitsFileCutRef;
import com.gwac.model.Ot2StreamNodeTime;
import com.gwac.model4.OTCatalog;
import com.gwac.model.OtLevel2;
import com.gwac.model.OtObserveRecord;
import com.gwac.model.UploadFileUnstore;
import com.gwac.service.SendMessageService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.jms.Destination;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

@Service(value = "crossTaskRecordService")
public class CrossTaskRecordServiceImpl implements CrossTaskRecordService {

  private static final Log log = LogFactory.getLog(CrossTaskRecordServiceImpl.class);

  @Resource
  private CrossTaskDao crossTaskDao;
  @Resource
  private CrossObjectDao crossObjectDao;
  @Resource
  private CrossRecordDao crossRecordDao;
  @Resource
  private CrossFileDao crossFileDao;
  @Resource
  private UploadFileUnstoreDao ufuDao;

  @Value("#{syscfg.gwacDataRootDirectory}")
  private String rootPath;
  @Value("#{syscfg.gwacDataCutimagesDirectory}")
  private String cutIDir;
  @Value("#{syscfg.gwacErrorbox}")
  private float errorBox;
  @Value("#{syscfg.gwacErrorbox2}")
  private float errorBox2;
  @Value("#{syscfg.gwacSuccessiveImageNumber}")
  private int successiveImageNumber;
  @Value("#{syscfg.gwacSuccessiveImageNumber2}")
  private int successiveImageNumber2;
  @Value("#{syscfg.gwacFirstNMarkNumber}")
  private int firstNMarkNumber;
  @Value("#{syscfg.gwacOccurImageNumber}")
  private int occurNumber;

  private static boolean running = true;
  @Value("#{syscfg.gwacServerBeijing}")
  private Boolean isBeiJingServer;
  @Value("#{syscfg.gwacServerTest}")
  private Boolean isTestServer;

  @Resource
  private JmsTemplate jmsTemplate;
  @Resource(name = "otCheckDest")
  private Destination otCheckDest;

  /**
   * 解析一级OT列表文件，得出二级OT，切图文件名称，二级OT模板切图名称
   *
   * @param ufuId
   * @param storePath
   * @param fileName
   */
  @Override
  public void parseLevel1Ot(long ufuId, String storePath, String fileName, String taskName) {

    if (storePath != null && fileName != null && taskName != null) {
      
      CrossTask ct = crossTaskDao.getByName(taskName);
      if(ct==null){
	return;
      }

      CrossFile cf = new CrossFile();
      cf.setCtId(ct.getCtId());
      cf.setFileName(fileName);
      crossFileDao.save(cf);
      if (cf.getCfId()==0) {
	return;
      }
      String fileDate = fileName.substring(fileName.lastIndexOf('_') + 1, fileName.lastIndexOf('T'));
      String ccdType = fileName.substring(0, 1);
      int number = ff2.getFfNumber();
      int dpmId = ff2.getCamId();

      Camera tcam = cameraDao.getById(dpmId);
      if (tcam.getStatus() != 3) {
	return;
      }

      List<OTCatalog> otcs = otcDao.getOT1Catalog(rootPath + "/" + storePath + "/" + fileName);
      log.debug(fileName + ", otlv1 size:" + otcs.size());

      List<OtLevel2> ot2s = new ArrayList();
      for (OTCatalog otc : otcs) {

	String otListPath = storePath;

	OtLevel2 otLv2 = new OtLevel2();
	otLv2.setRa(otc.getRaD());
	otLv2.setDec(otc.getDecD());
	otLv2.setFoundTimeUtc(otc.getDateUt());
	otLv2.setIdentify(fileName.substring(0, 4));
	otLv2.setXtemp(otc.getXTemp());
	otLv2.setYtemp(otc.getYTemp());
	otLv2.setLastFfNumber(number);
	otLv2.setDpmId(dpmId);
	otLv2.setDateStr(fileDate);
	otLv2.setAllFileCutted(false);
	otLv2.setSkyId(ff2.getSkyId().shortValue());
	otLv2.setDataProduceMethod('1');    //星表匹配一级OT
	otLv2.setMag(otc.getMagAper());

	OtObserveRecord oor = new OtObserveRecord();
	oor.setOtId((long) 0);
	oor.setFfcId((long) 0);
	oor.setFfId(ff2.getFfId());
	oor.setRaD(otc.getRaD());
	oor.setDecD(otc.getDecD());
	oor.setX(otc.getX());
	oor.setY(otc.getY());
	oor.setXTemp(otc.getXTemp());
	oor.setYTemp(otc.getYTemp());
	oor.setDateUt(otc.getDateUt());
	oor.setFlux(otc.getFlux());
	oor.setFlag(otc.getFlag());
	//oor.setFlagChb(otc.getFlagChb());
	oor.setBackground(otc.getBackground());
	oor.setThreshold(otc.getThreshold());
	oor.setMagAper(otc.getMagAper());
	oor.setMagerrAper(otc.getMagerrAper());
	oor.setEllipticity(otc.getEllipticity());
	oor.setClassStar(otc.getClassStar());
	//oor.setOtFlag(otc.getOtFlag());
	oor.setFfNumber(number);
	oor.setDateStr(fileDate);
	oor.setDpmId(dpmId);
	oor.setRequestCut(false);
	oor.setSuccessCut(false);
	oor.setSkyId(ff2.getSkyId().shortValue());
	oor.setDataProduceMethod('1');    //星表匹配一级OT
	oor.setTimeSubSecond(otc.getTimeSubSecond());

	//当前这条记录是与最近5幅之内的OT匹配，还是与当晚所有OT匹配，这里选择与当晚所有OT匹配
	//existInLatestN与最近5幅比较
	OtLevel2 tlv2 = otLv2Dao.existInAll(otLv2, errorBox);
	if (tlv2 == null) {
	  tlv2 = otLv2Dao.existInLatestN(otLv2, errorBox2, successiveImageNumber2);
	  if (tlv2 != null) {
	    tlv2.setOtType((short) 22); //慢速移动目标
	    log.debug("second match " + tlv2.getName());
	  }
	}
	if (tlv2 != null) {
	  log.debug("match ot2:" + tlv2.getOtId());
	  if (tlv2.getFirstFfNumber() > number) {
	    tlv2.setFirstFfNumber(number);
	    tlv2.setFoundTimeUtc(otLv2.getFoundTimeUtc());
	  } else {
	    tlv2.setLastFfNumber(otLv2.getLastFfNumber());
	    tlv2.setXtemp(otLv2.getXtemp());
	    tlv2.setYtemp(otLv2.getYtemp());
	    tlv2.setRa(otLv2.getRa());
	    tlv2.setDec(otLv2.getDec());
	    tlv2.setMag(otLv2.getMag());
	  }
	  tlv2.setTotal(tlv2.getTotal() + 1);
//          otLv2Dao.update(tlv2);
	  otLv2Dao.updateSomeRealTimeInfo(tlv2);

//          oor.setFfcId(ffc.getFfcId());
	  oor.setOtId(tlv2.getOtId());
	  otorDao.save(oor);
	} else {

	  otorDao.save(oor);
	  List<OtObserveRecord> oors = otorDao.matchLatestN(oor, errorBox, successiveImageNumber);
	  log.debug("match ot1(" + oor.getOorId() + ") record size:" + oors.size());
	  if (oors.size() >= occurNumber) {
	    OtObserveRecord oor1 = oors.get(0);

	    int otNumber = otnDao.getJfovNumberByDate(fileDate);
	    String otName = String.format("%s%s_C%05d", ccdType, fileDate, otNumber);
	    log.debug("generate new ot :" + otName + ", from file: " + fileName);

	    OtLevel2 tOtLv2 = new OtLevel2();
	    tOtLv2.setName(otName);
	    tOtLv2.setRa(oor1.getRaD());
	    tOtLv2.setDec(oor1.getDecD());
	    tOtLv2.setFoundTimeUtc(oor1.getDateUt());
	    tOtLv2.setIdentify(otLv2.getIdentify());
	    tOtLv2.setXtemp(oor1.getXTemp());
	    tOtLv2.setYtemp(oor1.getYTemp());
	    tOtLv2.setLastFfNumber(oors.get(oors.size() - 1).getFfNumber());  //已有序列的最大一个编号（最后一个），数据库中查询时，按照升序排列
	    tOtLv2.setTotal(oors.size());
	    tOtLv2.setDpmId(oor1.getDpmId());
	    tOtLv2.setDateStr(fileDate);
	    tOtLv2.setAllFileCutted(false);
	    tOtLv2.setFirstFfNumber(oor1.getFfNumber());  //已有序列的最小一个编号（第一个）
	    int secNum = oors.get(1).getFfNumber();
	    if (secNum - oor1.getFfNumber() == 1) {
	      tOtLv2.setFirstNMark(true); //借用first_n_mark字段,标识“首两帧连续出现”的OT2
	    } else {
	      tOtLv2.setFirstNMark(false);
	    }
	    tOtLv2.setCuttedFfNumber(0);
	    tOtLv2.setIsMatch((short) 0);
	    tOtLv2.setSkyId(oor1.getSkyId());
	    tOtLv2.setDataProduceMethod('1');    //星表匹配一级OT
	    tOtLv2.setFoCount((short) 0);
	    tOtLv2.setMag(oor1.getMagAper());
	    tOtLv2.setCvsMatch((short) 0);
	    tOtLv2.setRc3Match((short) 0);
	    tOtLv2.setMinorPlanetMatch((short) 0);
	    tOtLv2.setOt2HisMatch((short) 0);
	    tOtLv2.setOtherMatch((short) 0);
	    tOtLv2.setUsnoMatch((short) 0);
	    tOtLv2.setOtType((short) 0);
	    tOtLv2.setLookBackResult((short) 0);
	    tOtLv2.setFollowUpResult((short) 0);
	    tOtLv2.setLookBackCnn((float) -1);

	    otLv2Dao.save(tOtLv2);
	    ot2s.add(tOtLv2);

	    Ot2StreamNodeTime ot2SNT = new Ot2StreamNodeTime();
	    ot2SNT.setOtId(tOtLv2.getOtId());
	    ot2SNT.setOorId1(oor1.getOorId());
	    ot2SNT.setOorId2(oor.getOorId());
	    ot2StreamNodeTimeDao.save(ot2SNT);

	    String ffcrName = String.format("%s_%04d_ref", otName, tOtLv2.getFirstFfNumber());
	    log.debug("ffcrName=" + ffcrName);
	    log.debug("otId=" + tOtLv2.getOtId());

	    FitsFileCutRef ffcr = new FitsFileCutRef();
	    ffcr.setDpmId(Long.valueOf(tOtLv2.getDpmId()));
	    ffcr.setFfId(oor1.getFfId());
	    ffcr.setFileName(ffcrName);
	    ffcr.setOtId(tOtLv2.getOtId());
	    ffcr.setStorePath(otListPath.substring(0, otListPath.lastIndexOf('/')) + "/" + cutIDir);
	    ffcr.setRequestCut(false);
	    ffcr.setSuccessCut(false);
	    ffcrDao.save(ffcr);

	    for (OtObserveRecord tOor : oors) {
	      if (tOor.getOtId() != 0) {
		continue;
	      }
	      String cutImg = String.format("%s_%04d", tOtLv2.getName(), tOor.getFfNumber());
	      FitsFileCut ffc = new FitsFileCut();
	      ffc.setStorePath(otListPath.substring(0, otListPath.lastIndexOf('/')) + "/" + cutIDir);
	      ffc.setFileName(cutImg);
	      ffc.setOtId(tOtLv2.getOtId());
	      ffc.setNumber(tOor.getFfNumber());
	      ffc.setFfId(tOor.getFfId());
	      ffc.setDpmId((short) dpmId);
	      ffc.setImgX(tOor.getX());
	      ffc.setImgY(tOor.getY());
	      ffc.setRequestCut(false);
	      ffc.setSuccessCut(false);
	      ffc.setIsMissed(false);
	      ffc.setPriority((short) (tOor.getFfNumber() - tOtLv2.getFirstFfNumber()));
	      ffcDao.save(ffc);

	      tOor.setOtId(tOtLv2.getOtId());
	      tOor.setFfcId(ffc.getFfcId());
	      otorDao.update(tOor);
	    }
	  }
	}
      }

      Integer maxSingleFrameOT2Num = Integer.parseInt(wgpdao.getValueByName("MaxSingleFrameOT2Num"));
      short isMatch = 0;
      if (ot2s.size() >= maxSingleFrameOT2Num) {
	isMatch = 3;
	String chatId = "gwac003"; //GWAC_OT_gft_alert 
	String tmsg = String.format("OT2 filter:\n %s generate %d OT2\n", fitsName, ot2s.size());
	sendMsgService.send(tmsg, chatId);
      }
      for (OtLevel2 ot2 : ot2s) {
	if (isMatch == 0) {
	  MessageCreator tmc = new OTCheckMessageCreator(ot2);
	  jmsTemplate.send(otCheckDest, tmc);
	} else {
	  ot2.setIsMatch(isMatch);
	  otLv2Dao.updateIsMatch(ot2);
	}
      }

      ufuDao.updateProcessDoneTime(ufuId);
    }
  }

  public String getTriggerMsg(OtLevel2 ot2) {

    String epoch = "2000";
    int expTime = 1;
    int expNum = 10;
    String filter = "R";
    int priority = 10;

    StringBuilder tsb = new StringBuilder("append_plan mini-GWAC 2\n");
//    String msg = "append_object M151013_00001 150.1 -10.5 2000 LIGHT 1 10 R 10\n";
    tsb.append("append_object ");
    tsb.append(ot2.getName());
    tsb.append(" ");
    tsb.append(ot2.getRa() / 15); //小时
    tsb.append(" ");
    tsb.append(ot2.getDec());
    tsb.append(" ");
    tsb.append(epoch);
    tsb.append(" LIGHT ");
    tsb.append(expTime);
    tsb.append(" ");
    tsb.append(expNum);
    tsb.append(" ");
    tsb.append(filter);
    tsb.append(" ");
    tsb.append(priority);
    tsb.append("\n");

    return tsb.toString();
  }

}
