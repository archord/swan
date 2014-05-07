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
import com.gwac.model.OTCatalog;
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
        List<OTCatalog> otcs = otcDao.getOTCatalog(ufu.getStorePath()+"/"+ufu.getFileName());
        System.out.println("******************************************size="+otcs.size());
        for (OTCatalog otc : otcs) {
          String fileDate = otc.getFileDate();
          System.out.println("******************************************fileDate="+fileDate);
          int otNumber = otnDao.getNumberByDate(fileDate);
          System.out.println("******************************************otNumber="+otNumber);
          String otName = String.format("%s_%05d", fileDate, otNumber);
          System.out.println("******************************************otName="+otName);
        }
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
