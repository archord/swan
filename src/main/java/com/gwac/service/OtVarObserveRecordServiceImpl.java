/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

import com.gwac.activemq.OTCheckMessageCreator;
import com.gwac.dao.DataProcessMachineDAO;
import com.gwac.dao.FitsFileCutDAO;
import com.gwac.dao.FitsFileCutRefDAO;
import com.gwac.dao.FitsFileDAO;
import com.gwac.dao.OTCatalogDao;
import com.gwac.dao.ObservationSkyDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtNumberDao;
import com.gwac.dao.OtObserveRecordDAO;
import com.gwac.dao.UploadFileUnstoreDao;
import com.gwac.model.FitsFile;
import com.gwac.model.FitsFileCut;
import com.gwac.model.FitsFileCutRef;
import com.gwac.model.OTCatalog;
import com.gwac.model.ObservationSky;
import com.gwac.model.OtLevel2;
import com.gwac.model.OtObserveRecord;
import com.gwac.model.UploadFileUnstore;
import java.util.Date;
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
@Service(value = "otVarObserveRecordService")
public class OtVarObserveRecordServiceImpl implements OtObserveRecordService {

  private static final Log log = LogFactory.getLog(OtVarObserveRecordServiceImpl.class);

  @Resource
  private OTCatalogDao otcDao;
  @Resource
  private OtNumberDao otnDao;
  @Resource
  private OtLevel2Dao otLv2Dao;
  @Resource
  private FitsFileDAO ffDao;
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
  @Value("#{syscfg.gwacDataCutimagesDirectory}")
  private String cutIDir;
  @Value("#{syscfg.gwacErrorbox}")
  private float errorBox;
  @Value("#{syscfg.gwacSuccessiveImageNumber}")
  private int successiveImageNumber;
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
  @Resource
  private Destination otCheckDest;

  /**
   * 解析一级OT列表文件，得出二级OT，切图文件名称，二级OT模板切图名称
   *
   * @param storePath
   * @param fileName
   */
  @Override
  public void parseLevel1Ot(long ufuId, String storePath, String fileName) {

    if (storePath != null && fileName != null) {

      List<OTCatalog> otcs = otcDao.getOT1VarCatalog(rootPath + "/" + storePath + "/" + fileName);
      for (OTCatalog otc : otcs) {

        String otListPath = storePath;
        String orgImg = otc.getImageName(); //M2_03_140630_1_255020_0024.fit
        String ccdType = orgImg.substring(0, 1); //"M"
        String fileDate = orgImg.substring(6, 12);  //140828
        String dpmName = ccdType + orgImg.substring(3, 5);
        int dpmId = Integer.parseInt(orgImg.substring(3, 5));  //应该在数据库中通过dpmName查询
        int number = Integer.parseInt(orgImg.substring(22, 26));
        String skyName = orgImg.substring(6, 12);
        ObservationSky sky = skyDao.getByName(skyName);

        FitsFile ff = new FitsFile();
        ff.setFileName(orgImg);
        ffDao.save(ff);

        OtLevel2 otLv2 = new OtLevel2();
        otLv2.setRa(otc.getRaD());
        otLv2.setDec(otc.getDecD());
        otLv2.setFoundTimeUtc(otc.getDateUt());
        otLv2.setIdentify(orgImg.substring(0, 21));
        otLv2.setXtemp(otc.getXTemp());
        otLv2.setYtemp(otc.getYTemp());
        otLv2.setLastFfNumber(number);
        otLv2.setDpmId(dpmId);
        otLv2.setDateStr(fileDate);
        otLv2.setAllFileCutted(false);
        otLv2.setSkyId(sky.getSkyId());
        otLv2.setDataProduceMethod('6');    //星表匹配变星
        otLv2.setMag(otc.getMagAper());

        OtObserveRecord oor = new OtObserveRecord();
        oor.setOtId((long) 0);
        oor.setFfcId((long) 0);
        oor.setFfId(ff.getFfId());
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
        oor.setSkyId(sky.getSkyId());
        oor.setDataProduceMethod('6');    //星表匹配变星
        oor.setDistance(otc.getDistance());
        oor.setDeltamag(otc.getDeltamag());

        //当前这条记录是与最近5幅之内的OT匹配，还是与当晚所有OT匹配，这里选择与当晚所有OT匹配
        //existInLatestN与最近5幅比较
        OtLevel2 tlv2 = otLv2Dao.existInAll(otLv2, errorBox);
        if (tlv2 != null) {
          if (tlv2.getFirstFfNumber() > number) {
            tlv2.setFirstFfNumber(number);
          }
          tlv2.setTotal(tlv2.getTotal() + 1);
          tlv2.setLastFfNumber(otLv2.getLastFfNumber());
          tlv2.setXtemp(otLv2.getXtemp());
          tlv2.setYtemp(otLv2.getYtemp());
          tlv2.setRa(otLv2.getRa());
          tlv2.setDec(otLv2.getDec());
          tlv2.setMag(otLv2.getMag());
//          otLv2Dao.update(tlv2);
          otLv2Dao.updateSomeRealTimeInfo(tlv2);

          if (false) {
            String cutImg = String.format("%s_%04d", tlv2.getName(), oor.getFfNumber());
            FitsFileCut ffc = new FitsFileCut();
            ffc.setStorePath(otListPath.substring(0, otListPath.lastIndexOf('/')) + "/" + cutIDir);
            ffc.setFileName(cutImg);
            ffc.setOtId(tlv2.getOtId());
            ffc.setNumber(number);
            ffc.setFfId(ff.getFfId());
            ffc.setDpmId((short) dpmId);
            ffc.setImgX(oor.getX());
            ffc.setImgY(oor.getY());
            ffc.setRequestCut(false);
            ffc.setSuccessCut(false);
            ffc.setIsMissed(false);
            ffcDao.save(ffc);
            oor.setFfcId(ffc.getFfcId());
          }

          oor.setOtId(tlv2.getOtId());
          otorDao.save(oor);
        } else {

          otorDao.save(oor);
          List<OtObserveRecord> oors = otorDao.matchLatestN(oor, errorBox, successiveImageNumber);
          if (oors.size() >= occurNumber) {
            OtObserveRecord oor1 = oors.get(0);

            int otNumber = otnDao.getNumberByDate(fileDate);
            String otName = String.format("%s%s_V%05d", ccdType, fileDate, otNumber);

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
            tOtLv2.setCuttedFfNumber(0);
            tOtLv2.setIsMatch((short) 0);
            tOtLv2.setSkyId(oor1.getSkyId());
            tOtLv2.setDataProduceMethod('6');    //星表匹配变星
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

            int firstRecordNumber = dpmDao.getFirstRecordNumber(dpmName);

            if (oor1.getFfNumber() - firstRecordNumber <= firstNMarkNumber) {
              tOtLv2.setFirstNMark(true);
            } else {
              tOtLv2.setFirstNMark(false);
            }
            otLv2Dao.save(tOtLv2);
            
            MessageCreator tmc = new OTCheckMessageCreator(tOtLv2);
            jmsTemplate.send(otCheckDest, tmc);

            String ffcrName = String.format("%s_%04d_ref", otName, tOtLv2.getFirstFfNumber());
            log.debug("ffcrName=" + ffcrName);
            log.debug("otId=" + tOtLv2.getOtId());

            if (false) {
              FitsFileCutRef ffcr = new FitsFileCutRef();
              ffcr.setDpmId(Long.valueOf(tOtLv2.getDpmId()));
              ffcr.setFfId(ff.getFfId());
              ffcr.setFileName(ffcrName);
              ffcr.setOtId(tOtLv2.getOtId());
              ffcr.setStorePath(otListPath.substring(0, otListPath.lastIndexOf('/')) + "/" + cutIDir);
              ffcr.setRequestCut(false);
              ffcr.setSuccessCut(false);
              ffcrDao.save(ffcr);
            }

            for (OtObserveRecord tOor : oors) {
              if (tOor.getOtId() != 0) {
                continue;
              }
              if (false) {
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
                ffcDao.save(ffc);
                tOor.setFfcId(ffc.getFfcId());
              }

              tOor.setOtId(tOtLv2.getOtId());
              otorDao.update(tOor);
            }
          }
        }
      }
      ufuDao.updateProcessDoneTime(ufuId);
    }
  }

}
