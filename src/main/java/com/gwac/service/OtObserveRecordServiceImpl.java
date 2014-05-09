/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

import com.gwac.dao.FitsFileCutDAO;
import com.gwac.dao.FitsFileDAO;
import com.gwac.dao.OTCatalogDao;
import com.gwac.dao.OtBaseDao;
import com.gwac.dao.OtNumberDao;
import com.gwac.dao.OtObserveRecordDAO;
import com.gwac.dao.UploadFileUnstoreDao;
import com.gwac.model.FitsFile;
import com.gwac.model.FitsFileCut;
import com.gwac.model.OTCatalog;
import com.gwac.model.OtBase;
import com.gwac.model.OtObserveRecordTmp;
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
  private OtBaseDao otbDao;
  private FitsFileDAO ffDao;
  private FitsFileCutDAO ffcDao;
  private OtObserveRecordDAO otorDao;

  public List<OtObserveRecordTmp> getOtOR() {
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

          int xtemp = Math.round(otc.getXTemp());
          int ytemp = Math.round(otc.getYTemp());
          String cutImg = String.format("%s_OT_X%4dY%4d.fit",
                  orgImg.substring(0, orgImg.indexOf('.')), xtemp, ytemp);

          FitsFile ff = new FitsFile();
          ff.setFileName(orgImg);
          ffDao.save(ff);

          FitsFileCut ffc = new FitsFileCut();
          ffc.setStorePath(otListPath.substring(0, otListPath.lastIndexOf('/')) + "/cutimages");
          ffc.setFileName(cutImg);
          ffcDao.save(ffc);

          OtBase ob = new OtBase();
          ob.setRa(otc.getRaD());
          ob.setDec(otc.getDecD());
          ob.setFoundTimeUtc(otc.getDateUt());
          ob.setIdentify(orgImg.substring(0, 21));
          ob.setXtemp(otc.getXTemp());
          ob.setYtemp(otc.getYTemp());

          /**
           * 判断该OT是否存在，如果不存在则为该OT取名，并插入数据库 如果存在则取出该OT的ID
           */
          if (!otbDao.exist(ob)) {
            int otNumber = otnDao.getNumberByDate(fileDate);
            String otName = String.format("%s_%05d", fileDate, otNumber);
            ob.setName(otName);
            otbDao.save(ob);
          }

          OtObserveRecordTmp oort = new OtObserveRecordTmp();
          oort.setOtId(ob.getOtId());
          oort.setFfId(ff.getFfId());
          oort.setFfcId(ffc.getFfcId());
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
          otorDao.save(oort);
        }
        ufuDao.delete(ufu);
      }
    }
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
  public OtBaseDao getOtbDao() {
    return otbDao;
  }

  /**
   * @param otbDao the otbDao to set
   */
  public void setOtbDao(OtBaseDao otbDao) {
    this.otbDao = otbDao;
  }
}
