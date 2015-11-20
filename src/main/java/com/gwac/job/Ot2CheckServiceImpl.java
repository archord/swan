/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import com.gwac.dao.CVSQueryDao;
import com.gwac.dao.MergedOtherDao;
import com.gwac.dao.MinorPlanetDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtLevel2MatchDao;
import com.gwac.dao.MatchTableDao;
import com.gwac.dao.Rc3Dao;
import com.gwac.model.OtLevel2;
import com.gwac.model.OtLevel2Match;
import com.gwac.model.MatchTable;
import com.gwac.model2.Cvs;
import com.gwac.model2.MergedOther;
import com.gwac.model2.MinorPlanet;
import com.gwac.model2.Rc3;
import com.gwac.util.CommonFunction;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 二级OT查询，验证二级OT是否是已知星，变星，小行星等 目前在北京不能验证
 *
 * @author xy
 */
public class Ot2CheckServiceImpl implements Ot2CheckService {

  private static final Log log = LogFactory.getLog(Ot2CheckServiceImpl.class);

  private float mergedSearchbox;
  private float cvsSearchbox;
  private float rc3Searchbox;
  private float minorPlanetSearchbox;
  private float ot2Searchbox;

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

  private MatchTableDao mtDao;
  private OtLevel2MatchDao ot2mDao;

  private static boolean running = true;
  private Boolean isBeiJingServer;
  private Boolean isTestServer;

  @Override
  public void startJob() {

//    if (isBeiJingServer || isTestServer) {
//      return;
//    }

    if (running == true) {
      log.debug("start job...");
      running = false;
    } else {
      log.warn("job is running, jump this scheduler.");
      return;
    }

    long startTime = System.nanoTime();
    try {//JDBCConnectionException or some other exception
      searchOT2();
    } catch (Exception ex) {
      log.error("Job error", ex);
    } finally {
      if (running == false) {
        running = true;
      }
    }
    long endTime = System.nanoTime();
    log.debug("job consume " + 1.0 * (endTime - startTime) / 1e9 + " seconds.");
  }

  public void searchOT2() {

    List<OtLevel2> ot2s = ot2Dao.getUnMatched();
    log.debug("ot2 size: "+ ot2s.size());
    for (OtLevel2 ot2 : ot2s) {

      Boolean flag = false;
      Map<Cvs, Double> tcvsm = matchOt2InCvs(ot2, cvsSearchbox, cvsMag);
      for (Map.Entry<Cvs, Double> entry : tcvsm.entrySet()) {
        Cvs tcvs = (Cvs) entry.getKey();
        Double distance = (Double) entry.getValue();
        MatchTable ott = mtDao.getMatchTableByTypeName("cvs");
        OtLevel2Match ot2m = new OtLevel2Match();
        ot2m.setOtId(ot2.getOtId());
        ot2m.setMtId(ott.getMtId());
        ot2m.setMatchId(Long.valueOf(tcvs.getIdnum()));
        ot2m.setRa(tcvs.getRadeg());
        ot2m.setDec(tcvs.getDedeg());
        ot2m.setMag(tcvs.getMag());
        ot2m.setDistance(distance.floatValue());
        ot2m.setD25(new Float(0));
        ot2mDao.save(ot2m);

        String cvsInfo = tcvs.getCvsid() + " " + tcvs.getRadeg() + " " + tcvs.getDedeg() + " " + tcvs.getMag();
        log.debug("cvsInfo: " + cvsInfo);
        flag = true;
      }
      if(tcvsm.size()>0){
        ot2.setCvsMatch((short)tcvsm.size());
        ot2Dao.updateCvsMatch(ot2);
      }

      Map<MergedOther, Double> tmom = matchOt2InMergedOther(ot2, mergedSearchbox, mergedMag);
      for (Map.Entry<MergedOther, Double> entry : tmom.entrySet()) {
        MergedOther tmo = (MergedOther) entry.getKey();
        Double distance = (Double) entry.getValue();

        MatchTable ott = getMtDao().getMatchTableByTypeName("merged_other");
        OtLevel2Match ot2m = new OtLevel2Match();
        ot2m.setOtId(ot2.getOtId());
        ot2m.setMtId(ott.getMtId());
        ot2m.setMatchId(Long.valueOf(tmo.getIdnum()));
        ot2m.setRa(tmo.getRadeg());
        ot2m.setDec(tmo.getDedeg());
        ot2m.setMag(tmo.getMag());
        ot2m.setDistance(distance.floatValue());
        ot2m.setD25(new Float(0));
        ot2mDao.save(ot2m);

        String moInfo = tmo.getIdnum() + " " + tmo.getRadeg() + " " + tmo.getDedeg() + " " + tmo.getMag();
        log.debug("moInfo: " + moInfo);
        flag = true;
      }
      if(tmom.size()>0){
        ot2.setOtherMatch((short)tmom.size());
        ot2Dao.updateOtherMatch(ot2);
      }

      Map<Rc3, Double> trc3m = matchOt2InRc3(ot2, rc3Searchbox, rc3MinMag, rc3MaxMag);
      for (Map.Entry<Rc3, Double> entry : trc3m.entrySet()) {
        Rc3 trc3 = (Rc3) entry.getKey();
        Double distance = (Double) entry.getValue();

        MatchTable ott = getMtDao().getMatchTableByTypeName("rc3");
        OtLevel2Match ot2m = new OtLevel2Match();
        ot2m.setOtId(ot2.getOtId());
        ot2m.setMtId(ott.getMtId());
        ot2m.setMatchId(Long.valueOf(trc3.getIdnum()));
        ot2m.setRa(trc3.getRadeg());
        ot2m.setDec(trc3.getDedeg());
        ot2m.setMag(trc3.getMvmag());
        ot2m.setDistance(distance.floatValue());
        ot2m.setD25(trc3.getD25());
        ot2mDao.save(ot2m);

        String moInfo = trc3.getIdnum() + " " + trc3.getRadeg() + " " + trc3.getDedeg() + " " + trc3.getMvmag();
        log.debug("rc3Info: " + moInfo);
        flag = true;
      }
      if(trc3m.size()>0){
        ot2.setRc3Match((short)trc3m.size());
        ot2Dao.updateRc3Match(ot2);
      }

      Map<MinorPlanet, Double> tmpm = matchOt2InMinorPlanet(ot2, minorPlanetSearchbox, minorPlanetMag);//minorPlanetSearchbox
      for (Map.Entry<MinorPlanet, Double> entry : tmpm.entrySet()) {
        MinorPlanet tmp = (MinorPlanet) entry.getKey();
        Double distance = (Double) entry.getValue();

        MatchTable ott = getMtDao().getMatchTableByTypeName("minor_planet");
        OtLevel2Match ot2m = new OtLevel2Match();
        ot2m.setOtId(ot2.getOtId());
        ot2m.setMtId(ott.getMtId());
        ot2m.setMatchId(Long.valueOf(tmp.getIdnum()));
        ot2m.setRa(tmp.getLon());
        ot2m.setDec(tmp.getLat());
        ot2m.setMag(tmp.getVmag());
        ot2m.setDistance(distance.floatValue());
        ot2m.setD25(new Float(0));
        ot2mDao.save(ot2m);

        String moInfo = tmp.getIdnum() + " " + tmp.getMpid() + " " + tmp.getLon() + " " + tmp.getLat();
        log.debug("moInfo: " + moInfo);
        flag = true;
      }
      if(tmpm.size()>0){
        ot2.setMinorPlanetMatch((short)tmpm.size());
        ot2Dao.updateMinorPlanetMatch(ot2);
      }

      Map<OtLevel2, Double> tOT2Hism = matchOt2His(ot2, ot2Searchbox, 0);
      for (Map.Entry<OtLevel2, Double> entry : tOT2Hism.entrySet()) {
        OtLevel2 tot2 = (OtLevel2) entry.getKey();
        Double distance = (Double) entry.getValue();

        MatchTable ott = getMtDao().getMatchTableByTypeName("ot_level2_his");
        OtLevel2Match ot2m = new OtLevel2Match();
        ot2m.setOtId(ot2.getOtId());
        ot2m.setMtId(ott.getMtId());
        ot2m.setMatchId(Long.valueOf(tot2.getOtId()));
        ot2m.setRa(tot2.getRa());
        ot2m.setDec(tot2.getDec());
        ot2m.setMag(tot2.getMag());
        ot2m.setDistance(distance.floatValue());
        ot2mDao.save(ot2m);
        flag = true;
      }
      if(tOT2Hism.size()>0){
        ot2.setOt2HisMatch((short)tOT2Hism.size());
        ot2Dao.updateOt2HisMatch(ot2);
      }

      if (flag) {
        ot2.setIsMatch((short) 2);
        ot2Dao.updateIsMatch(ot2);
      }
    }
  }

  public Map<OtLevel2, Double> matchOt2His(OtLevel2 ot2, float searchRadius, float mag) {

    List<OtLevel2> objs = ot2Dao.searchOT2His(ot2, searchRadius, mag);
    double minDis = searchRadius;
    Map rst = new HashMap();
    for (OtLevel2 obj : objs) {
      double tDis = CommonFunction.getGreatCircleDistance(ot2.getRa(), ot2.getDec(), obj.getRa(), obj.getDec());
      if (tDis < minDis) {
        rst.put(obj, tDis);
      }
    }
    return rst;
  }

  /**
   * 在cvs表中查找ot2的匹配星，在searchRadius范围内，返回距离最小的目标
   *
   * @param ot2
   * @param searchRadius 搜索半径
   * @param mag 搜索的最大星等
   * @return
   */
  public Map<Cvs, Double> matchOt2InCvs(OtLevel2 ot2, float searchRadius, float mag) {

    List<Cvs> cvss = cvsDao.queryByOt2(ot2, searchRadius, mag);
    double minDis = searchRadius;
    Cvs minCvs = null;
    Map rst = new HashMap();
    for (Cvs cvs : cvss) {
      double tDis = CommonFunction.getGreatCircleDistance(ot2.getRa(), ot2.getDec(), cvs.getRadeg(), cvs.getDedeg());
      if (tDis < minDis) {
        rst.put(cvs, tDis);
      }
    }
    return rst;
  }

  public Map<MergedOther, Double> matchOt2InMergedOther(OtLevel2 ot2, float searchRadius, float mag) {

    List<MergedOther> objs = moDao.queryByOt2(ot2, searchRadius, mag);
    double minDis = searchRadius;
    MergedOther minObj = null;
    Map rst = new HashMap();
    for (MergedOther obj : objs) {
      double tDis = CommonFunction.getGreatCircleDistance(ot2.getRa(), ot2.getDec(), obj.getRadeg(), obj.getDedeg());
      if (tDis < minDis) {
        rst.put(obj, tDis);
      }
    }
    return rst;
  }

  public Map<Rc3, Double> matchOt2InRc3(OtLevel2 ot2, float searchRadius, float minMag, float maxMag) {

    List<Rc3> objs = rc3Dao.queryByOt2(ot2, searchRadius, minMag, maxMag);
    double minDis = searchRadius;
    Rc3 minObj = null;
    Map rst = new HashMap();
    for (Rc3 obj : objs) {
      double tDis = CommonFunction.getGreatCircleDistance(ot2.getRa(), ot2.getDec(), obj.getRadeg(), obj.getDedeg());
      if (tDis < minDis) {
        rst.put(obj, tDis);
      }
    }
    return rst;
  }

  public Map<MinorPlanet, Double> matchOt2InMinorPlanet(OtLevel2 ot2, float searchRadius, float mag) {

    String tableName = getMinorPlanetTableName();
    Map rst = new HashMap();
    if (mpDao.tableExists(tableName)) {
      List<MinorPlanet> objs = mpDao.queryByOt2(ot2, searchRadius, mag, tableName);
      MinorPlanet minObj = null;
      double minDis = searchRadius;
      float day = (float) CommonFunction.dateToJulian(CommonFunction.getUTCDate(new Date()));
      for (MinorPlanet obj : objs) {
        float subDay = day - obj.getMjd();
        double tDis = CommonFunction.getGreatCircleDistance(ot2.getRa(), ot2.getDec(), obj.getLon() + obj.getDlon() * subDay, obj.getLat() + obj.getDlat() * subDay);
        if (tDis < minDis) {
          rst.put(obj, tDis);
        }
      }
    } else {
      log.warn("table " + tableName + " not exists!");
    }
    return rst;
  }

  public String getMinorPlanetTableName() {
    MatchTable ott = getMtDao().getMatchTableByTypeName("minor_planet");
    return ott.getMatchTableName() + CommonFunction.getDateString(CommonFunction.getUTCDate(new Date()));
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
   * @return the mtDao
   */
  public MatchTableDao getMtDao() {
    return mtDao;
  }

  /**
   * @param mtDao the mtDao to set
   */
  public void setMtDao(MatchTableDao mtDao) {
    this.mtDao = mtDao;
  }

  /**
   * @param ot2Searchbox the ot2Searchbox to set
   */
  public void setOt2Searchbox(float ot2Searchbox) {
    this.ot2Searchbox = ot2Searchbox;
  }

}
