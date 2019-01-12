/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import com.gwac.activemq.OTCheckMessageCreator;
import com.gwac.dao.DataProcessMachineDAO;
import com.gwac.dao.FitsFile2DAO;
import com.gwac.dao.FitsFileCutDAO;
import com.gwac.dao.FitsFileCutRefDAO;
import com.gwac.dao.OTCatalogDao;
import com.gwac.dao.ObservationSkyDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtNumberDao;
import com.gwac.dao.OtObserveRecordDAO;
import com.gwac.dao.UploadFileUnstoreDao;
import com.gwac.model.FitsFile2;
import com.gwac.model.FitsFileCut;
import com.gwac.model4.OTCatalog;
import com.gwac.model.OtLevel2;
import com.gwac.model.OtObserveRecord;
import com.gwac.model.UploadFileUnstore;
import java.util.List;
import javax.annotation.Resource;
import javax.jms.Destination;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

/**
 * 解析一级OT列表文件，计算二级OT，切图，模板切图。
 *
 * @author xy
 */
@Service(value = "otDiffObserveRecordService")
public class OtDiffObserveRecordServiceImpl implements OtObserveRecordService {

  private static final Log log = LogFactory.getLog(OtDiffObserveRecordServiceImpl.class);

  @Resource
  private OTCatalogDao otcDao;
  @Resource
  private OtNumberDao otnDao;
  @Resource
  private OtLevel2Dao otLv2Dao;
  @Resource
  private FitsFile2DAO ff2Dao;
  @Resource
  private FitsFileCutDAO ffcDao;
  @Resource
  private OtObserveRecordDAO otorDao;
  @Resource
  private DataProcessMachineDAO dpmDao;
  @Resource
  private FitsFileCutRefDAO ffcrDao;
  @Resource
  private ObservationSkyDao skyDao;
  @Resource
  private UploadFileUnstoreDao ufuDao;

  @Value("#{syscfg.gwacDataRootDirectory}")
  private String rootPath;
  @Value("#{syscfg.gwacDataDiffOtImagesDirectory}")
  private String cutIDir;
  @Value("#{syscfg.gwacErrorbox}")
  private float errorBox;
  @Value("#{syscfg.gwacSuccessiveImageNumber}")
  private int successiveImageNumber;
  @Value("#{syscfg.gwacFirstNMarkNumber}")
  private int firstNMarkNumber;
  @Value("#{syscfg.gwacOccurCutimageNumber}")
  private int cutOccurNumber;

  private static boolean running = true;
  @Value("#{syscfg.gwacServerBeijing}")
  private Boolean isBeiJingServer;
  @Value("#{syscfg.gwacServerTest}")
  private Boolean isTestServer;

  @Resource
  private JmsTemplate jmsTemplate;
  @Resource(name = "otCheckDest")
  private Destination otCheckDest;

  public void startJob() {

//    if (isTestServer) {
//      return;
//    }
    if (running == true) {
      log.debug("start job...");
      running = false;
    } else {
      log.warn("job is running, jump this scheduler.");
      return;
    }

    log.debug("cutOccurNumber=" + cutOccurNumber);

    long startTime = System.nanoTime();
    try {//JDBCConnectionException or some other exception

      List<UploadFileUnstore> ufus = ufuDao.getSubOTLevel1File();
      log.debug("size=" + ufus.size());
      if (!ufus.isEmpty()) {
        for (UploadFileUnstore ufu : ufus) {
          parseLevel1Ot(ufu.getUfuId(), ufu.getStorePath(), ufu.getFileName());
        }
      }
    } catch (Exception ex) {
      log.error("Job error", ex);
    } finally {
      if (running == false) {
        running = true;
      }
    }
    long endTime = System.nanoTime();
    double time1 = 1.0 * (endTime - startTime) / 1e9;
    log.debug("job consume: parse cut ot list " + time1 + ".");
  }

  /**
   * 解析一级OT列表文件，得出二级OT，切图文件名称，二级OT模板切图名称
   *
   * @param storePath
   * @param fileName
   */
  @Override
  public void parseLevel1Ot(long ufuId, String storePath, String fileName) {

    log.debug("process file " + rootPath + "/" + storePath + "/" + fileName);

    if (storePath != null && fileName != null) {

      String fitsName = fileName.substring(0, fileName.indexOf('.')) + ".fit";
      FitsFile2 ff2 = ff2Dao.getByName(fitsName);
      if (ff2 == null) {
        ff2 = ff2Dao.getByNameHis(fitsName);
        if (ff2 == null) {
          log.warn("cannot find file in fits_file2 " + rootPath + "/" + storePath + "/" + fileName);
          return;
        }
      }
      String fileDate = fileName.substring(fileName.lastIndexOf('_') + 1, fileName.lastIndexOf('T'));
      String ccdType = fileName.substring(0, 1);
      int number = ff2.getFfNumber();
      int dpmId = ff2.getCamId();

      List<OTCatalog> otcs = otcDao.getDiffOT1Catalog(rootPath + "/" + storePath + "/" + fileName);
      for (OTCatalog otc : otcs) {

        String otListPath = storePath;

        OtLevel2 otLv2 = new OtLevel2();
        otLv2.setRa(otc.getRaD());
        otLv2.setDec(otc.getDecD());
        otLv2.setFoundTimeUtc(ff2.getGenTime());
        otLv2.setIdentify(fileName.substring(0, 4));
        otLv2.setLastFfNumber(number);
        otLv2.setDpmId(dpmId);
        otLv2.setDateStr(fileDate);
        otLv2.setAllFileCutted(true);
        otLv2.setSkyId(ff2.getSkyId().shortValue());
        otLv2.setDataProduceMethod('b');    //星表匹配一级OT
        otLv2.setXtemp(otc.getX());
        otLv2.setYtemp(otc.getY());
        otLv2.setMag(otc.getMagAper());

        String cutImg = otc.getCutImageName();
        FitsFileCut ffc = new FitsFileCut();
        ffc.setStorePath(otListPath.substring(0, otListPath.lastIndexOf('/')) + "/" + cutIDir);
        ffc.setFileName(cutImg);
        ffc.setNumber(number);
        ffc.setFfId(ff2.getFfId());
        ffc.setDpmId((short) dpmId);
        ffc.setImgX(otc.getX());
        ffc.setImgY(otc.getY());
        ffc.setRequestCut(true);
        ffc.setSuccessCut(true);
        ffc.setIsMissed(false);
        ffcDao.save(ffc);

        OtObserveRecord oor = new OtObserveRecord();
        oor.setOtId((long) 0);
        oor.setFfcId(ffc.getFfcId());
        oor.setFfId(ff2.getFfId());
        oor.setRaD(otc.getRaD());
        oor.setDecD(otc.getDecD());
        oor.setX(otc.getX());
        oor.setY(otc.getY());
        oor.setXTemp(otc.getX());
        oor.setYTemp(otc.getY());
        oor.setDateUt(ff2.getGenTime());
        oor.setMagAper(otc.getMagAper());
        oor.setMagerrAper(otc.getMagerrAper());
        oor.setFfNumber(number);
        oor.setDateStr(fileDate);
        oor.setDpmId(dpmId);
        oor.setRequestCut(true);
        oor.setSuccessCut(true);
        oor.setSkyId(ff2.getSkyId().shortValue());
        oor.setDataProduceMethod('b');    //星表匹配一级OT
        oor.setEllipticity(otc.getEllipticity());
        oor.setFlag(otc.getFlag());
        oor.setOtFlag(otc.getOtFlag());   //noMatch:1;match:0
        oor.setBackground(otc.getBackground());
        oor.setClassStar(otc.getClassStar());
        oor.setFlux(otc.getFlux());
        oor.setProbability(otc.getProbability());
	oor.setThreshold(otc.getThreshold());

        //当前这条记录是与最近5幅之内的OT匹配，还是与当晚所有OT匹配，这里选择与当晚所有OT匹配
        //existInLatestN与最近5幅比较
        OtLevel2 tlv2 = otLv2Dao.existInAll(otLv2, errorBox);
        if (tlv2 != null) {
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
          if (oor.getOtFlag() && tlv2.getLookBackResult()==2) {
            tlv2.setLookBackResult((short) 1);
            otLv2Dao.updateLookBackResult(tlv2);
          }
//          otLv2Dao.update(tlv2);
          otLv2Dao.updateSomeRealTimeInfo(tlv2);

          oor.setOtId(tlv2.getOtId());
          otorDao.save(oor);

          ffc.setOtId(tlv2.getOtId());
          ffcDao.update(ffc);
        } else {

          otorDao.save(oor);

          if (oor.getOtFlag()) {
//          if (false) {
	    
            int otNumber = otnDao.getSubNumberByDate(fileDate);
            String otName = String.format("%s%s_D%05d", ccdType, fileDate, otNumber);

            OtLevel2 tOtLv2 = new OtLevel2();
            tOtLv2.setName(otName);
            tOtLv2.setRa(oor.getRaD());
            tOtLv2.setDec(oor.getDecD());
            tOtLv2.setFoundTimeUtc(oor.getDateUt());
            tOtLv2.setIdentify(otLv2.getIdentify());
            tOtLv2.setLastFfNumber(oor.getFfNumber());
            tOtLv2.setTotal(1);
            tOtLv2.setDpmId(oor.getDpmId());
            tOtLv2.setDateStr(fileDate);
            tOtLv2.setAllFileCutted(true);
            tOtLv2.setFirstFfNumber(oor.getFfNumber());
            tOtLv2.setCuttedFfNumber(0);
            tOtLv2.setIsMatch((short) 0);
            tOtLv2.setSkyId(oor.getSkyId());
            tOtLv2.setDataProduceMethod('b');    //图像相减一级OT
            tOtLv2.setFirstNMark(false);
            tOtLv2.setXtemp(otc.getX());
            tOtLv2.setYtemp(otc.getY());
            tOtLv2.setFoCount((short) 0);
            tOtLv2.setMag(oor.getMagAper());
            tOtLv2.setCvsMatch((short) 0);
            tOtLv2.setRc3Match((short) 0);
            tOtLv2.setMinorPlanetMatch((short) 0);
            tOtLv2.setOt2HisMatch((short) 0);
            tOtLv2.setOtherMatch((short) 0);
            tOtLv2.setUsnoMatch((short) 0);
            tOtLv2.setOtType((short) 0);
            tOtLv2.setLookBackResult((short) 0);
            tOtLv2.setFollowUpResult((short) 0);
            tOtLv2.setProbability(oor.getProbability());

            otLv2Dao.save(tOtLv2);

            oor.setOtId(tOtLv2.getOtId());
            otorDao.update(oor);
            ffc.setOtId(tOtLv2.getOtId());
            ffcDao.update(ffc);

            MessageCreator tmc = new OTCheckMessageCreator(tOtLv2);
            jmsTemplate.send(otCheckDest, tmc);

          } else {
//            List<OtObserveRecord> oors = otorDao.existInAll(oor, errorBox);
            List<OtObserveRecord> oors = otorDao.matchLatestN(oor, errorBox, successiveImageNumber);
            if (oors.size() >= cutOccurNumber) {
              OtObserveRecord oor1 = oors.get(0);

              int otNumber = otnDao.getSubNumberByDate(fileDate);
              String otName = String.format("%s%s_D%05d", ccdType, fileDate, otNumber);

              OtLevel2 tOtLv2 = new OtLevel2();
              tOtLv2.setName(otName);
              tOtLv2.setRa(oor1.getRaD());
              tOtLv2.setDec(oor1.getDecD());
              tOtLv2.setFoundTimeUtc(oor1.getDateUt());
              tOtLv2.setIdentify(otLv2.getIdentify());
              tOtLv2.setLastFfNumber(oors.get(oors.size() - 1).getFfNumber());  //已有序列的最大一个编号（最后一个），数据库中查询时，按照升序排列
              tOtLv2.setTotal(oors.size());
              tOtLv2.setDpmId(oor1.getDpmId());
              tOtLv2.setDateStr(fileDate);
              tOtLv2.setAllFileCutted(true);
              tOtLv2.setFirstFfNumber(oor1.getFfNumber());  //已有序列的最小一个编号（第一个）
              tOtLv2.setCuttedFfNumber(0);
              tOtLv2.setIsMatch((short) 0);
              tOtLv2.setSkyId(oor1.getSkyId());
              tOtLv2.setDataProduceMethod('b');    //图像相减一级OT
              tOtLv2.setFirstNMark(false);
              tOtLv2.setXtemp(otc.getX());
              tOtLv2.setYtemp(otc.getY());
              tOtLv2.setFoCount((short) 0);
              tOtLv2.setMag(oor1.getMagAper());
              tOtLv2.setCvsMatch((short) 0);
              tOtLv2.setRc3Match((short) 0);
              tOtLv2.setMinorPlanetMatch((short) 0);
              tOtLv2.setOt2HisMatch((short) 0);
              tOtLv2.setOtherMatch((short) 0);
              tOtLv2.setUsnoMatch((short) 0);
              tOtLv2.setOtType((short) 0);
              tOtLv2.setLookBackResult((short) 2);
              tOtLv2.setFollowUpResult((short) 0);
              tOtLv2.setProbability(oor1.getProbability());

              otLv2Dao.save(tOtLv2);

              MessageCreator tmc = new OTCheckMessageCreator(tOtLv2);
              jmsTemplate.send(otCheckDest, tmc);

              for (OtObserveRecord tOor : oors) {
                if (tOor.getOtId() != 0) {
                  continue;
                }

                tOor.setOtId(tOtLv2.getOtId());
                otorDao.update(tOor);

                FitsFileCut tffc = ffcDao.getById(tOor.getFfcId());
                tffc.setOtId(tOtLv2.getOtId());
                ffcDao.update(tffc);
              }
            }
          }
        }
      }
      ufuDao.updateProcessDoneTime(ufuId);
    }
  }

}
