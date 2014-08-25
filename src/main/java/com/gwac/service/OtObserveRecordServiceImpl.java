/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

import com.gwac.dao.DataProcessMachineDAO;
import com.gwac.dao.FitsFileCutDAO;
import com.gwac.dao.FitsFileDAO;
import com.gwac.dao.OTCatalogDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtNumberDao;
import com.gwac.dao.OtObserveRecordDAO;
import com.gwac.dao.UploadFileUnstoreDao;
import com.gwac.model.DataProcessMachine;
import com.gwac.model.FitsFile;
import com.gwac.model.FitsFileCut;
import com.gwac.model.OTCatalog;
import com.gwac.model.OtLevel2;
import com.gwac.model.OtObserveRecord;
import com.gwac.model.UploadFileUnstore;
import java.util.List;

/**
 *
 * @author xy
 */
public class OtObserveRecordServiceImpl implements OtObserveRecordService {

  private UploadFileUnstoreDao ufuDao;
  private OTCatalogDao otcDao;
  private OtNumberDao otnDao;
  private OtLevel2Dao otbDao;
  private FitsFileDAO ffDao;
  private FitsFileCutDAO ffcDao;
  private OtObserveRecordDAO otorDao;
  private DataProcessMachineDAO dpmDao;

  public List<OtObserveRecord> getOtOR() {
    return otorDao.findAll();
  }

  public void storeOTCatalog() {

    List<UploadFileUnstore> ufus = ufuDao.findAll();
    if (ufus != null) {
      for (UploadFileUnstore ufu : ufus) {

        List<OTCatalog> otcs = otcDao.getOTCatalog(ufu.getStorePath() + "/" + ufu.getFileName());
        for (OTCatalog otc : otcs) {

          String orgImg = otc.getImageName();
          String otListPath = ufu.getStorePath();
          String fileDate = otc.getFileDate();
          String dpmName = "M"+ orgImg.substring(3, 5);
          int number = Integer.parseInt(orgImg.substring(22, 26));
          
          DataProcessMachine dpm = getDpmDao().getDpmByName(dpmName);
          if(dpm.getCurProcessNumber() < number){
            dpm.setCurProcessNumber(number);
            getDpmDao().update(dpm);
          }

          FitsFile ff = new FitsFile();
          ff.setFileName(orgImg);
          ffDao.save(ff);

          OtObserveRecord oort = new OtObserveRecord();
          oort.setRaD(otc.getRaD());
          oort.setDecD(otc.getDecD());
          oort.setX(otc.getX());
          oort.setY(otc.getY());
          oort.setXTemp(otc.getXTemp());
          oort.setYTemp(otc.getYTemp());
          oort.setDateUt(otc.getDateUt());
          oort.setFlux(otc.getFlux());
          oort.setFlag(otc.getFlag());
          oort.setFlagChb(otc.getFlagChb());
          oort.setBackground(otc.getBackground());
          oort.setThreshold(otc.getThreshold());
          oort.setMagAper(otc.getMagAper());
          oort.setMagerrAper(otc.getMagerrAper());
          oort.setEllipticity(otc.getEllipticity());
          oort.setClassStar(otc.getClassStar());
          oort.setOtFlag(otc.getOtFlag());

          oort.setFfId(ff.getFfId());

          if (!otorDao.exist(oort)) {

            OtLevel2 ob = new OtLevel2();
            ob.setRa(otc.getRaD());
            ob.setDec(otc.getDecD());
            ob.setFoundTimeUtc(otc.getDateUt());
            ob.setIdentify(orgImg.substring(0, 21));
            ob.setXtemp(otc.getXTemp());
            ob.setYtemp(otc.getYTemp());
            ob.setLastFfNumber(number);
            ob.setTotal(1);
            ob.setSuccOccurTimes((short)1);
            ob.setMaxSuccOccurTimes((short)0);

            /**
             * 判断该OT是否存在，如果不存在则为该OT取名，并插入数据库，如果存在则取出该OT的ID
             * 为了保证OT切图命名的唯一性，约定该OT所有切图的后缀（X和Y）都以OT首次出现帧中OT
             * 所对应的X、Y坐标为准（解决四舍五入问题）。
             */
            if (otbDao.exist(ob)) {
              OtLevel2 tOb = otbDao.getById(ob.getOtId());
              if (tOb.getLastFfNumber() < ob.getLastFfNumber()) {
                tOb.setLastFfNumber(ob.getLastFfNumber());
              }
              tOb.setTotal(tOb.getTotal()+1);
              otbDao.update(ob);
            } else {
              int otNumber = otnDao.getNumberByDate(fileDate);
              String otName = String.format("%s_%05d", fileDate, otNumber);
              ob.setName(otName);
              otbDao.save(ob);
            }
            oort.setOtId(ob.getOtId());

            int xtemp = Math.round(ob.getXtemp());
            int ytemp = Math.round(ob.getYtemp());
            String cutImg = String.format("%s_OT_X%04dY%04d.fit",
                    orgImg.substring(0, orgImg.indexOf('.')), xtemp, ytemp);
            FitsFileCut ffc = new FitsFileCut();
            ffc.setStorePath(otListPath.substring(0, otListPath.lastIndexOf('/')) + "/cutimages");
            ffc.setFileName(cutImg);
            ffc.setOtId(ob.getOtId());
            ffc.setNumber(number);
            ffc.setFfId(ff.getFfId());
            ffcDao.save(ffc);
            oort.setFfcId(ffc.getFfcId());

            otorDao.save(oort);
          }
        }
        ufuDao.delete(ufu);
      }
    }
  }
  
  void updateOTStatus(){
    
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
   * @return the ufuDao
   */
  public UploadFileUnstoreDao getUfuDao() {
    return ufuDao;
  }

  /**
   * @param ufuDao the ufuDao to set
   */
  public void setUfuDao(UploadFileUnstoreDao ufuDao) {
    this.ufuDao = ufuDao;
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
   * @return the otbDao
   */
  public OtLevel2Dao getOtbDao() {
    return otbDao;
  }

  /**
   * @param otbDao the otbDao to set
   */
  public void setOtbDao(OtLevel2Dao otbDao) {
    this.otbDao = otbDao;
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
}
