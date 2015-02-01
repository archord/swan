/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

import com.gwac.dao.CVSQueryDao;
import com.gwac.dao.MergedOtherDao;
import com.gwac.dao.MinorPlanetDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtLevel2MatchDao;
import com.gwac.dao.OtTypeDao;
import com.gwac.dao.Rc3Dao;
import com.gwac.model.OtLevel2;
import com.gwac.model.OtLevel2Match;
import com.gwac.model.OtType;
import com.gwac.model2.Cvs;
import com.gwac.model2.MergedOther;
import com.gwac.util.CommonFunction;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author xy
 */
public class Ot2CheckServiceImpl implements Ot2CheckService {

  private static final Log log = LogFactory.getLog(Ot2CheckServiceImpl.class);

  private float mergedSearchbox;
  private float cvsSearchbox;
  private float rc3Searchbox;
  private float minorPlanetSearchbox;

  private float mergedMag;
  private float cvsMag;
  private float rc3MinMag;
  private float rc3MaxMag;
  private float minorPlanetMag;

  private OtLevel2Dao ot2Dao;
  private CVSQueryDao cvsDao;
  private MergedOtherDao moDao;
  private MinorPlanetDao mpDao;
  private Rc3Dao rc3Dao;
  
  private OtTypeDao ottDao;
  private OtLevel2MatchDao ot2mDao;

  public void searchOT2() {

    List<OtLevel2> ot2s = ot2Dao.getUnMatched();
    for (OtLevel2 ot2 : ot2s) {
      
      OtLevel2Match ot2m = new OtLevel2Match();
      ot2m.setOtId(ot2.getOtId()); 
      
      Boolean flag = false;
      long startTime = System.nanoTime();
      Cvs tcvs = matchOt2InCvs(ot2, cvsSearchbox, cvsMag);
      if (tcvs != null) {
        OtType ott = ottDao.getOtTypeByTableName("cvs");
        ot2m.setOtTypeId(ott.getOtTypeId());
        ot2m.setMatchId(tcvs.getIdnum());
        ot2m.setRa(tcvs.getRadeg());
        ot2m.setDec(tcvs.getDedeg());
        ot2m.setMag(tcvs.getMag());
        ot2mDao.save(ot2m);
        
        String cvsInfo = tcvs.getCvsid() + " " + tcvs.getRadeg() + " " + tcvs.getDedeg() + " " + tcvs.getMag();
        log.debug("cvsInfo: " + cvsInfo);
        flag = true;
      }

      MergedOther tmo = matchOt2InMergedOther(ot2, mergedSearchbox, mergedMag);
      if (tmo != null) {
        OtType ott = ottDao.getOtTypeByTableName("merged_other");
        ot2m.setOtTypeId(ott.getOtTypeId());
        ot2m.setMatchId(tmo.getIdnum());
        ot2m.setRa(tmo.getRadeg());
        ot2m.setDec(tmo.getDedeg());
        ot2m.setMag(tmo.getMag());
        ot2mDao.save(ot2m);
        
        String moInfo = tmo.getIdnum() + " " + tmo.getRadeg() + " " + tmo.getDedeg() + " " + tmo.getMag();
        log.debug("moInfo: " + moInfo);
        flag = true;
      }
      long endTime = System.nanoTime();
      if (flag) {
        ot2.setIsMatch((short)2);
        ot2Dao.updateIsMatch(ot2);
        log.debug("ot2 " + ot2.getName() + " query time " + 1.0 * (endTime - startTime) / 1e9 + " seconds.");
      }

    }
    log.debug("end searchOT2.");
  }

  /**
   * 在cvs表中查找ot2的匹配星，在searchRadius范围内，返回距离最小的目标
   *
   * @param ot2
   * @param searchRadius 搜索半径
   * @param mag 搜索的最大星等
   * @return
   */
  public Cvs matchOt2InCvs(OtLevel2 ot2, float searchRadius, float mag) {

    List<Cvs> cvss = cvsDao.queryByOt2(ot2, searchRadius, mag);
    double minDis = searchRadius;
    Cvs minCvs = null;
    for (Cvs cvs : cvss) {
      double tDis = CommonFunction.getGreatCircleDistance(ot2.getRa(), ot2.getDec(), cvs.getRadeg(), cvs.getDedeg());
      if (tDis < minDis) {
        minDis = tDis;
        minCvs = cvs;
      }
    }
    return minCvs;
  }

  public MergedOther matchOt2InMergedOther(OtLevel2 ot2, float searchRadius, float mag) {

    List<MergedOther> objs = moDao.queryByOt2(ot2, searchRadius, mag);
    double minDis = searchRadius;
    MergedOther minObj = null;
    for (MergedOther obj : objs) {
      double tDis = CommonFunction.getGreatCircleDistance(ot2.getRa(), ot2.getDec(), obj.getRadeg(), obj.getDedeg());
      if (tDis < minDis) {
        minDis = tDis;
        minObj = obj;
      }
    }
    return minObj;
  }

  /**
   * @return the cvsDao
   */
  public CVSQueryDao getCvsDao() {
    return cvsDao;
  }

  /**
   * @param cvsDao the cvsDao to set
   */
  public void setCvsDao(CVSQueryDao cvsDao) {
    this.cvsDao = cvsDao;
  }

  /**
   * @return the ot2Dao
   */
  public OtLevel2Dao getOt2Dao() {
    return ot2Dao;
  }

  /**
   * @param ot2Dao the ot2Dao to set
   */
  public void setOt2Dao(OtLevel2Dao ot2Dao) {
    this.ot2Dao = ot2Dao;
  }

  /**
   * @return the moDao
   */
  public MergedOtherDao getMoDao() {
    return moDao;
  }

  /**
   * @param moDao the moDao to set
   */
  public void setMoDao(MergedOtherDao moDao) {
    this.moDao = moDao;
  }

  /**
   * @return the mpDao
   */
  public MinorPlanetDao getMpDao() {
    return mpDao;
  }

  /**
   * @param mpDao the mpDao to set
   */
  public void setMpDao(MinorPlanetDao mpDao) {
    this.mpDao = mpDao;
  }

  /**
   * @return the rc3Dao
   */
  public Rc3Dao getRc3Dao() {
    return rc3Dao;
  }

  /**
   * @param rc3Dao the rc3Dao to set
   */
  public void setRc3Dao(Rc3Dao rc3Dao) {
    this.rc3Dao = rc3Dao;
  }

  /**
   * @return the mergedSearchbox
   */
  public float getMergedSearchbox() {
    return mergedSearchbox;
  }

  /**
   * @param mergedSearchbox the mergedSearchbox to set
   */
  public void setMergedSearchbox(float mergedSearchbox) {
    this.mergedSearchbox = mergedSearchbox;
  }

  /**
   * @return the cvsSearchbox
   */
  public float getCvsSearchbox() {
    return cvsSearchbox;
  }

  /**
   * @param cvsSearchbox the cvsSearchbox to set
   */
  public void setCvsSearchbox(float cvsSearchbox) {
    this.cvsSearchbox = cvsSearchbox;
  }

  /**
   * @return the rc3Searchbox
   */
  public float getRc3Searchbox() {
    return rc3Searchbox;
  }

  /**
   * @param rc3Searchbox the rc3Searchbox to set
   */
  public void setRc3Searchbox(float rc3Searchbox) {
    this.rc3Searchbox = rc3Searchbox;
  }

  /**
   * @return the minorPlanetSearchbox
   */
  public float getMinorPlanetSearchbox() {
    return minorPlanetSearchbox;
  }

  /**
   * @param minorPlanetSearchbox the minorPlanetSearchbox to set
   */
  public void setMinorPlanetSearchbox(float minorPlanetSearchbox) {
    this.minorPlanetSearchbox = minorPlanetSearchbox;
  }

  /**
   * @return the mergedMag
   */
  public float getMergedMag() {
    return mergedMag;
  }

  /**
   * @param mergedMag the mergedMag to set
   */
  public void setMergedMag(float mergedMag) {
    this.mergedMag = mergedMag;
  }

  /**
   * @return the cvsMag
   */
  public float getCvsMag() {
    return cvsMag;
  }

  /**
   * @param cvsMag the cvsMag to set
   */
  public void setCvsMag(float cvsMag) {
    this.cvsMag = cvsMag;
  }

  /**
   * @return the rc3MinMag
   */
  public float getRc3MinMag() {
    return rc3MinMag;
  }

  /**
   * @param rc3MinMag the rc3MinMag to set
   */
  public void setRc3MinMag(float rc3MinMag) {
    this.rc3MinMag = rc3MinMag;
  }

  /**
   * @return the rc3MaxMag
   */
  public float getRc3MaxMag() {
    return rc3MaxMag;
  }

  /**
   * @param rc3MaxMag the rc3MaxMag to set
   */
  public void setRc3MaxMag(float rc3MaxMag) {
    this.rc3MaxMag = rc3MaxMag;
  }

  /**
   * @return the minorPlanetMag
   */
  public float getMinorPlanetMag() {
    return minorPlanetMag;
  }

  /**
   * @param minorPlanetMag the minorPlanetMag to set
   */
  public void setMinorPlanetMag(float minorPlanetMag) {
    this.minorPlanetMag = minorPlanetMag;
  }

  /**
   * @return the ottDao
   */
  public OtTypeDao getOttDao() {
    return ottDao;
  }

  /**
   * @param ottDao the ottDao to set
   */
  public void setOttDao(OtTypeDao ottDao) {
    this.ottDao = ottDao;
  }

  /**
   * @return the ot2mDao
   */
  public OtLevel2MatchDao getOt2mDao() {
    return ot2mDao;
  }

  /**
   * @param ot2mDao the ot2mDao to set
   */
  public void setOt2mDao(OtLevel2MatchDao ot2mDao) {
    this.ot2mDao = ot2mDao;
  }

}
