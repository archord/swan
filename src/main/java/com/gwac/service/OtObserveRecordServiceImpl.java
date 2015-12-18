/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

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
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 解析一级OT列表文件，计算二级OT，切图，模板切图。
 *
 * @author xy
 */
public class OtObserveRecordServiceImpl implements OtObserveRecordService {

  private static final Log log = LogFactory.getLog(OtObserveRecordServiceImpl.class);

  private OTCatalogDao otcDao;
  private OtNumberDao otnDao;
  private OtLevel2Dao otLv2Dao;
  private FitsFileDAO ffDao;
  private FitsFileCutDAO ffcDao;
  private OtObserveRecordDAO otorDao;
  private DataProcessMachineDAO dpmDao;
  private FitsFileCutRefDAO ffcrDao;
  private ObservationSkyDao skyDao;
  private UploadFileUnstoreDao ufuDao;

  private String rootPath;
  private String cutIDir;
  private float errorBox;
  private int successiveImageNumber;
  private int firstNMarkNumber;
  private int occurNumber;

  private static boolean running = true;
  private Boolean isBeiJingServer;
  private Boolean isTestServer;

  /**
   * 解析一级OT列表文件，得出二级OT，切图文件名称，二级OT模板切图名称
   *
   * @param ufuId
   * @param storePath
   * @param fileName
   */
  @Override
  public void parseLevel1Ot(long ufuId, String storePath, String fileName) {

    if (storePath != null && fileName != null) {

      List<OTCatalog> otcs = otcDao.getOT1Catalog(rootPath + "/" + storePath + "/" + fileName);
      for (OTCatalog otc : otcs) {

        String otListPath = storePath;
        String orgImg = otc.getImageName(); //M2_03_140630_1_255020_0024.fit
        String ccdType = orgImg.substring(0, 1); //"M"
        String fileDate = orgImg.substring(6, 12);  //140828
        String dpmName = ccdType + orgImg.substring(3, 5);
        int dpmId = Integer.parseInt(orgImg.substring(3, 5));  //应该在数据库中通过dpmName查询
        int number = Integer.parseInt(orgImg.substring(22, 26));
        String skyName = orgImg.substring(15, 21);
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
        otLv2.setDataProduceMethod('1');    //星表匹配一级OT
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
        oor.setDataProduceMethod('1');    //星表匹配一级OT

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
          otLv2Dao.update(tlv2);

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

          oor.setOtId(tlv2.getOtId());
          oor.setFfcId(ffc.getFfcId());
          otorDao.save(oor);
        } else {

          otorDao.save(oor);
          List<OtObserveRecord> oors = otorDao.matchLatestN(oor, errorBox, successiveImageNumber);
          log.debug("match ot1 record size:" + oors.size());
          if (oors.size() >= occurNumber) {
            OtObserveRecord oor1 = oors.get(0);

            int otNumber = otnDao.getNumberByDate(fileDate);
            String otName = String.format("%s%s_C%05d", ccdType, fileDate, otNumber);

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

            int firstRecordNumber = dpmDao.getFirstRecordNumber(dpmName);

            if (oor1.getFfNumber() - firstRecordNumber <= firstNMarkNumber) {
              tOtLv2.setFirstNMark(true);
            } else {
              tOtLv2.setFirstNMark(false);
            }
            otLv2Dao.save(tOtLv2);

            String ffcrName = String.format("%s_%04d_ref", otName, tOtLv2.getFirstFfNumber());
            log.debug("ffcrName=" + ffcrName);
            log.debug("otId=" + tOtLv2.getOtId());

            FitsFileCutRef ffcr = new FitsFileCutRef();
            ffcr.setDpmId(Long.valueOf(tOtLv2.getDpmId()));
            ffcr.setFfId(ff.getFfId());
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
              ffcDao.save(ffc);

              tOor.setOtId(tOtLv2.getOtId());
              tOor.setFfcId(ffc.getFfcId());
              otorDao.update(tOor);
            }
          }
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

  /**
   * @return the otorDao
   */
  public OtObserveRecordDAO getOtorDao() {
    return otorDao;
  }

  /**
   * @param otorDao the otorDao to set
   */
  public void setOtorDao(OtObserveRecordDAO otorDao) {
    this.otorDao = otorDao;
  }

  /**
   * @return the otcDao
   */
  public OTCatalogDao getOtcDao() {
    return otcDao;
  }

  /**
   * @param otcDao the otcDao to set
   */
  public void setOtcDao(OTCatalogDao otcDao) {
    this.otcDao = otcDao;
  }

  /**
   * @return the ffDao
   */
  public FitsFileDAO getFfDao() {
    return ffDao;
  }

  /**
   * @param ffDao the ffDao to set
   */
  public void setFfDao(FitsFileDAO ffDao) {
    this.ffDao = ffDao;
  }

  /**
   * @return the ffcDao
   */
  public FitsFileCutDAO getFfcDao() {
    return ffcDao;
  }

  /**
   * @param ffcDao the ffcDao to set
   */
  public void setFfcDao(FitsFileCutDAO ffcDao) {
    this.ffcDao = ffcDao;
  }

  /**
   * @return the otnDao
   */
  public OtNumberDao getOtnDao() {
    return otnDao;
  }

  /**
   * @param otnDao the otnDao to set
   */
  public void setOtnDao(OtNumberDao otnDao) {
    this.otnDao = otnDao;
  }

  /**
   * @return the dpmDao
   */
  public DataProcessMachineDAO getDpmDao() {
    return dpmDao;
  }

  /**
   * @param dpmDao the dpmDao to set
   */
  public void setDpmDao(DataProcessMachineDAO dpmDao) {
    this.dpmDao = dpmDao;
  }

  /**
   * @return the otLv2Dao
   */
  public OtLevel2Dao getOtLv2Dao() {
    return otLv2Dao;
  }

  /**
   * @param otLv2Dao the otLv2Dao to set
   */
  public void setOtLv2Dao(OtLevel2Dao otLv2Dao) {
    this.otLv2Dao = otLv2Dao;
  }

  /**
   * @return the rootPath
   */
  public String getRootPath() {
    return rootPath;
  }

  /**
   * @param rootPath the rootPath to set
   */
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  /**
   * @param errorBox the errorBox to set
   */
  public void setErrorBox(float errorBox) {
    this.errorBox = errorBox;
  }

  /**
   * @param successiveImageNumber the successiveImageNumber to set
   */
  public void setSuccessiveImageNumber(int successiveImageNumber) {
    this.successiveImageNumber = successiveImageNumber;
  }

  /**
   * @param occurNumber the occurNumber to set
   */
  public void setOccurNumber(int occurNumber) {
    this.occurNumber = occurNumber;
  }

  /**
   * @return the ffcrDao
   */
  public FitsFileCutRefDAO getFfcrDao() {
    return ffcrDao;
  }

  /**
   * @param ffcrDao the ffcrDao to set
   */
  public void setFfcrDao(FitsFileCutRefDAO ffcrDao) {
    this.ffcrDao = ffcrDao;
  }

  /**
   * @param isBeiJingServer the isBeiJingServer to set
   */
  public void setIsBeiJingServer(Boolean isBeiJingServer) {
    this.isBeiJingServer = isBeiJingServer;
  }

  /**
   * @param isTestServer the isTestServer to set
   */
  public void setIsTestServer(Boolean isTestServer) {
    this.isTestServer = isTestServer;
  }

  /**
   * @param firstNMarkNumber the firstNMarkNumber to set
   */
  public void setFirstNMarkNumber(int firstNMarkNumber) {
    this.firstNMarkNumber = firstNMarkNumber;
  }

  /**
   * @param skyDao the skyDao to set
   */
  public void setSkyDao(ObservationSkyDao skyDao) {
    this.skyDao = skyDao;
  }

  /**
   * @param cutIDir the cutIDir to set
   */
  public void setCutIDir(String cutIDir) {
    this.cutIDir = cutIDir;
  }

  /**
   * @param ufuDao the ufuDao to set
   */
  public void setUfuDao(UploadFileUnstoreDao ufuDao) {
    this.ufuDao = ufuDao;
  }
}
