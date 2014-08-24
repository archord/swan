/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

import com.gwac.dao.FitsFileCutDAO;
import com.gwac.dao.FitsFileDAO;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.model.FitsFileCut;
import com.gwac.model.OtLevel2;
import com.gwac.model.UploadFileRecord;

/**
 *
 * @author xy
 */
public class FitsFileCutServiceImpl implements FitsFileCutService {

  private OtLevel2Dao obDao;
  private FitsFileDAO ffDao;
  private FitsFileCutDAO ffcDao;

  /**
   *
   * @param obj
   */
  public void storeFitsFileCutInfo(UploadFileRecord obj) {
    
    if (obj.getFileType() == '4') {
      String cutImg = obj.getFileName();
      String orgImg = cutImg.substring(0, 26) + ".fit";
      String identifyStr = orgImg.substring(0, 21);
      int number = Integer.parseInt(cutImg.substring(22, 26));
      int x = Integer.parseInt(cutImg.substring(31, 35));
      int y = Integer.parseInt(cutImg.substring(36, 40));
      
      OtLevel2 ob = new OtLevel2();
      ob.setIdentify(identifyStr);
      ob.setXtemp((float)x);
      ob.setYtemp((float)y);
      
      FitsFileCut ffc = new FitsFileCut();
      ffc.setFileName(cutImg);
      ffc.setStorePath(orgImg);
      ffc.setNumber(number);
      ffc.setOtId(ob.getOtId());
    }
  }

  /**
   * @return the obDao
   */
  public OtLevel2Dao getObDao() {
    return obDao;
  }

  /**
   * @param obDao the obDao to set
   */
  public void setObDao(OtLevel2Dao obDao) {
    this.obDao = obDao;
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
}
