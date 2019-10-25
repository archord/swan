/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service2;

import com.gwac.dao.CameraDao;
import com.gwac.dao2.CVSQueryDao;
import com.gwac.dao.CcdPixFilterDao;
import com.gwac.dao.CrossObjectDao;
import com.gwac.dao.CrossObjectMatchDao;
import com.gwac.dao.CrossTaskDao;
import com.gwac.dao2.MergedOtherDao;
import com.gwac.dao2.MinorPlanetDao;
import com.gwac.dao.MatchTableDao;
import com.gwac.dao.OtTmplWrongDao;
import com.gwac.dao2.Rc3Dao;
import com.gwac.dao2.UsnoCatalogDao;
import com.gwac.model.CrossObject;
import com.gwac.model.CrossObjectMatch;
import com.gwac.model.CrossTask;
import com.gwac.model.MatchTable;
import com.gwac.model2.Cvs;
import com.gwac.model2.MergedOther;
import com.gwac.model2.MinorPlanet;
import com.gwac.model2.Rc3;
import com.gwac.model3.UsnoCatalog;
import com.gwac.util.CommonFunction;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 二级OT查询，验证二级OT是否是已知星，变星，小行星等 目前在北京不能验证
 *
 * @author xy
 */
@Service(value = "crossObjectCheckService")
public class CrossObjectCheckServiceImpl implements Ot2CheckService {

  private static final Log log = LogFactory.getLog(CrossObjectCheckServiceImpl.class);

  @Value("#{syscfg.gwacServerBeijing}")
  private Boolean isBeiJingServer;
  @Value("#{syscfg.gwacServerTest}")
  private Boolean isTestServer;

  float mergedSearchbox = (float) 0.0;
  float cvsSearchbox = (float) 0.0;
  float rc3Searchbox = (float) 0.0;
  float minorPlanetSearchbox = (float) 0.0;
  float ot2Searchbox = (float) 0.0;
  float usnoSearchbox = (float) 0.0;
  float usnoSearchbox2 = (float) 0.0;
  float mergedMag = (float) 0.0;
  float cvsMag = (float) 0.0;
  float rc3MinMag = (float) 0.0;
  float rc3MaxMag = (float) 0.0;
  float minorPlanetMag = (float) 0.0;
  float usnoMag = (float) 0.0;
  float usnoMag2 = (float) 0.0;

  @Resource
  private CrossObjectDao ot2Dao;
  @Resource
  private MatchTableDao mtDao;
  @Resource
  private CrossObjectMatchDao ot2mDao;

  @Resource
  private CVSQueryDao cvsDao;
  @Resource
  private MergedOtherDao moDao;
  @Resource
  private MinorPlanetDao mpDao;
  @Resource
  private Rc3Dao rc3Dao;
  @Resource
  private UsnoCatalogDao usnoDao;
  @Resource
  private CrossTaskDao crossTaskDao;
  
  @Override
  public void searchOT2(Long otId) {

    CrossObject ot2 = ot2Dao.getById(otId);
    searchOT2(ot2);
  }

  void searchOT2(CrossObject ot2) {

    if (ot2.getRa() < 0 || ot2.getRa() > 360 || ot2.getDec() < -90 || ot2.getDec() > 90) {
      return;
    }
    log.debug("search ot2: " + ot2.getCoId());
    
    CrossTask crossTask = crossTaskDao.getById(ot2.getCtId());

    mergedSearchbox = crossTask.getMergedR();
    cvsSearchbox = crossTask.getCvsR();
    rc3Searchbox = crossTask.getRc3R();
    minorPlanetSearchbox = crossTask.getMinorPlanetR();
    ot2Searchbox = crossTask.getOt2HisR();
    usnoSearchbox = crossTask.getUsnoR1();
    usnoSearchbox2 = crossTask.getUsnoR2();
    mergedMag = crossTask.getMergedMag();
    cvsMag = crossTask.getCvsMag();
    rc3MinMag = crossTask.getRc3MinMag();
    rc3MaxMag = crossTask.getRc3MaxMag();
    minorPlanetMag = crossTask.getMinorPlanetMag();
    usnoMag = crossTask.getUsnoMag1();
    usnoMag2 = crossTask.getUsnoMag2();

    boolean mysqlCheck = true;
    Boolean flag = false;
    long allEndTime, allStartTime;
    MatchTable ott = null;

    allEndTime = System.nanoTime();
    if (mysqlCheck) {
      ott = mtDao.getMatchTableByTypeName("cvs");
      Map<Cvs, Double> tcvsm = matchOt2InCvs(ot2, cvsSearchbox, cvsMag);
      for (Map.Entry<Cvs, Double> entry : tcvsm.entrySet()) {
        Cvs tcvs = (Cvs) entry.getKey();
        Double distance = (Double) entry.getValue();
        CrossObjectMatch ot2m = new CrossObjectMatch();
        ot2m.setOtId(ot2.getCoId());
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
      if (tcvsm.size() > 0) {
        ot2.setCvsMatch(true);
        ot2Dao.updateCvsMatch(ot2);
      }

      Map<MergedOther, Double> tmom = matchOt2InMergedOther(ot2, mergedSearchbox, mergedMag);
      ott = mtDao.getMatchTableByTypeName("merged_other");
      for (Map.Entry<MergedOther, Double> entry : tmom.entrySet()) {
        MergedOther tmo = (MergedOther) entry.getKey();
        Double distance = (Double) entry.getValue();

        CrossObjectMatch ot2m = new CrossObjectMatch();
        ot2m.setOtId(ot2.getCoId());
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
      if (tmom.size() > 0) {
        ot2.setOtherMatch(true);
        ot2Dao.updateOtherMatch(ot2);
      }

      ott = mtDao.getMatchTableByTypeName("rc3");
      Map<Rc3, Double> trc3m = matchOt2InRc3(ot2, rc3Searchbox, rc3MinMag, rc3MaxMag);
      for (Map.Entry<Rc3, Double> entry : trc3m.entrySet()) {
        Rc3 trc3 = (Rc3) entry.getKey();
        Double distance = (Double) entry.getValue();

        CrossObjectMatch ot2m = new CrossObjectMatch();
        ot2m.setOtId(ot2.getCoId());
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
      if (trc3m.size() > 0) {
        ot2.setRc3Match(true);
        ot2Dao.updateRc3Match(ot2);
      }

      ott = mtDao.getMatchTableByTypeName("minor_planet");
      long tStartTime = System.nanoTime();
      Map<MinorPlanet, Double> tmpm = matchOt2InMinorPlanet(ot2, minorPlanetSearchbox, minorPlanetMag);//minorPlanetSearchbox
      for (Map.Entry<MinorPlanet, Double> entry : tmpm.entrySet()) {
        MinorPlanet tmp = (MinorPlanet) entry.getKey();
        Double distance = (Double) entry.getValue();

        CrossObjectMatch ot2m = new CrossObjectMatch();
        ot2m.setOtId(ot2.getCoId());
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
      if (tmpm.size() > 0) {
        ot2.setMinorPlanetMatch(true);
        ot2Dao.updateMinorPlanetMatch(ot2);
        ot2.setOtType((short) 2);
        ot2Dao.updateOtType(ot2);
      }
      long tEndTime = System.nanoTime();
      log.debug("search minor planet consume " + 1.0 * (tEndTime  - tStartTime) / 1e9 + " seconds.");
    }

    if (flag) {
      ot2.setIsMatch((short) 2); //匹配成功，找到匹配对应体
      ot2Dao.updateIsMatch(ot2);
    } 
//    else {//由定时查询改为消息队列查询后，这里需要主动更新
//      ot2.setIsMatch((short) 1); //匹配不成功，未找到匹配对应体
//      ot2Dao.updateIsMatch(ot2);
//    }
    allStartTime = System.nanoTime();
    log.debug("search ot2 " + ot2.getCoId() + " total consume " + 1.0 * (allStartTime  - allEndTime) / 1e9 + " seconds.");
  }

  /**
   * 在cvs表中查找ot2的匹配星，在searchRadius范围内，返回距离最小的目标
   *
   * @param ot2
   * @param searchRadius 搜索半径
   * @param mag 搜索的最大星等
   * @return
   */
  public Map<Cvs, Double> matchOt2InCvs(CrossObject ot2, float searchRadius, float mag) {

    List<Cvs> cvss = cvsDao.queryByOt2(ot2.getRa(), ot2.getDec(), searchRadius, mag);
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

  public Map<MergedOther, Double> matchOt2InMergedOther(CrossObject ot2, float searchRadius, float mag) {

    List<MergedOther> objs = moDao.queryByOt2(ot2.getRa(), ot2.getDec(), searchRadius, mag);
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

  public Map<Rc3, Double> matchOt2InRc3(CrossObject ot2, float searchRadius, float minMag, float maxMag) {

    float searchBoundary = 1; //1度，认为最大的星系半径为1度
    List<Rc3> objs = rc3Dao.queryByOt2(ot2.getRa(), ot2.getDec(), searchBoundary, minMag, maxMag);
    Rc3 minObj = null;
    double minDis = searchRadius;
    Map rst = new HashMap();
    for (Rc3 obj : objs) {
      double tDis = CommonFunction.getGreatCircleDistance(ot2.getRa(), ot2.getDec(), obj.getRadeg(), obj.getDedeg());
      if (obj.getD25() > CommonFunction.MINFLOAT) {
        minDis = obj.getD25() / 60; //d25单位为角分
      } else if (obj.getAngularSize() > CommonFunction.MINFLOAT) {
        minDis = obj.getAngularSize() / 60; //AngularSize单位为角分
      } else {
        minDis = searchRadius;
      }
      if (tDis < minDis) {
        rst.put(obj, tDis);
      }
    }
    return rst;
  }

  public Map<MinorPlanet, Double> matchOt2InMinorPlanet(CrossObject ot2, float searchRadius, float mag) {

    String tableName = getMinorPlanetTableName(ot2.getFoundTimeUtc());
    log.debug("tableName=" + tableName);
    Map rst = new HashMap();
    if (mpDao.tableExists(tableName)) {
      List<MinorPlanet> objs = mpDao.queryByOt2(ot2.getRa(), ot2.getDec(), searchRadius, mag, tableName);
      log.debug("minor planet number: " + objs.size());
      MinorPlanet minObj = null;
      double minDis = searchRadius;

      /**
       * 天 86400000.0 小时 3600000.0 分钟 60000.0
       */
      /**
       * Calendar内部存储的是UTC时间 Calendar的setTime和getTime都是本地时间
       * setTimeZone改变的是cal.get(Calendar.HOUR_OF_DAY)的值，默认是本地时区
       * cal.get(Calendar.HOUR_OF_DAY)，默认是本地时间，即和setTime的值相同
       * getTimeInMillis是相对于UTC（1970.1.1）
       */
      Calendar cal = Calendar.getInstance();
      cal.setTime(ot2.getFoundTimeUtc());
      double subDay = cal.get(Calendar.HOUR_OF_DAY) / 24.0 + cal.get(Calendar.MINUTE) / 24.0 / 60.0
              + cal.get(Calendar.SECOND) / 24.0 / 60.0 / 60;
      for (MinorPlanet obj : objs) {
        double preRa = obj.getLon() + obj.getDlon() * subDay / Math.cos(obj.getLat() * 0.017453293);
        double preDec = obj.getLat() + obj.getDlat() * subDay;
        double tDis = CommonFunction.getGreatCircleDistance(ot2.getRa(), ot2.getDec(), preRa, preDec);
//        if (obj.getIdnum() == 30 || obj.getIdnum() == 115 || obj.getIdnum() == 654) {
//          log.debug("start*******************************");
//          log.debug("ot2checkotname=" + ot2.getName());
//          log.debug("FoundTimeUtc=" + ot2.getFoundTimeUtc());
//          log.debug("subDay=" + subDay);
//          log.debug("Idnum=" + obj.getIdnum() + "Mpid=" + obj.getMpid() + "lat=" + obj.getLat() + "lon=" + obj.getLon() + "vmag=" + obj.getVmag());
//          log.debug("dis=" + tDis);
//          if (tDis < minDis) {
//            log.debug("ot2checkotname=" + ot2.getName() + " success #######################");
//          }
//          log.debug("end*******************************");
//        }
        if (tDis < minDis) {
          rst.put(obj, tDis);
        }
      }
    } else {
      log.warn("table " + tableName + " not exists!");
    }
    return rst;
  }

  //有两个亮于13星等的星
  public Map<UsnoCatalog, Double> matchOt2InUsnoCatalog2(CrossObject ot2) {

    log.debug("query usno");

    float tmag2 = 9;
    float tmag3 = 8;
    float tmag4 = 7;
    float tmag5 = 6;
    float tmag6 = 5;

    float tdis2 = (float) 0.025;
    float tdis3 = (float) 0.043333;
    float tdis4 = (float) 0.07166666;
    float tdis5 = (float) 0.125;
    float tdis6 = (float) 0.23;

    Map rst = new HashMap();
    double minDis = usnoSearchbox;
    UsnoCatalog tobj = null;
    List<String> tableNames = getUsnoTableNames(ot2, usnoSearchbox);
    for (String tName : tableNames) {
      if (usnoDao.tableExists(tName)) {
        List<UsnoCatalog> objs = usnoDao.queryByOt2(ot2.getRa(), ot2.getDec(), usnoSearchbox, usnoMag, tName);
        for (UsnoCatalog obj : objs) {
          double tDis = CommonFunction.getGreatCircleDistance(ot2.getRa(), ot2.getDec(), obj.getrAdeg(), obj.getdEdeg());
          if (tDis < minDis) {
            minDis = tDis;
            tobj = obj;
          }
        }
      } else {
        log.warn("table " + tName + " not exists!");
      }
    }
    if (tobj != null) {
      rst.put(tobj, minDis);
    }
    minDis = tdis2;
    tobj = null;
    if (rst.isEmpty()) {
      tableNames = getUsnoTableNames(ot2, tdis2);
      for (String tName : tableNames) {
        if (usnoDao.tableExists(tName)) {
          List<UsnoCatalog> objs = usnoDao.queryByOt2(ot2.getRa(), ot2.getDec(), tdis2, tmag2, tName);
          for (UsnoCatalog obj : objs) {
            double tDis = CommonFunction.getGreatCircleDistance(ot2.getRa(), ot2.getDec(), obj.getrAdeg(), obj.getdEdeg());
            if (tDis < minDis) {
              minDis = tDis;
              tobj = obj;
            }
          }
        } else {
          log.warn("table " + tName + " not exists!");
        }
      }
    }
    if (tobj != null) {
      rst.put(tobj, minDis);
    }

    minDis = tdis3;
    tobj = null;
    if (rst.isEmpty()) {
      tableNames = getUsnoTableNames(ot2, tdis3);
      for (String tName : tableNames) {
        if (usnoDao.tableExists(tName)) {
          List<UsnoCatalog> objs = usnoDao.queryByOt2(ot2.getRa(), ot2.getDec(), tdis3, tmag3, tName);
          for (UsnoCatalog obj : objs) {
            double tDis = CommonFunction.getGreatCircleDistance(ot2.getRa(), ot2.getDec(), obj.getrAdeg(), obj.getdEdeg());
            if (tDis < minDis) {
              minDis = tDis;
              tobj = obj;
            }
          }
        } else {
          log.warn("table " + tName + " not exists!");
        }
      }
    }
    if (tobj != null) {
      rst.put(tobj, minDis);
    }
    minDis = tdis4;
    tobj = null;
    if (rst.isEmpty()) {
      tableNames = getUsnoTableNames(ot2, tdis4);
      for (String tName : tableNames) {
        if (usnoDao.tableExists(tName)) {
          List<UsnoCatalog> objs = usnoDao.queryByOt2(ot2.getRa(), ot2.getDec(), tdis4, tmag4, tName);
          for (UsnoCatalog obj : objs) {
            double tDis = CommonFunction.getGreatCircleDistance(ot2.getRa(), ot2.getDec(), obj.getrAdeg(), obj.getdEdeg());
            if (tDis < minDis) {
              minDis = tDis;
              tobj = obj;
            }
          }
        } else {
          log.warn("table " + tName + " not exists!");
        }
      }
    }
    if (tobj != null) {
      rst.put(tobj, minDis);
    }
    minDis = tdis5;
    tobj = null;
    if (rst.isEmpty()) {
      tableNames = getUsnoTableNames(ot2, tdis5);
      for (String tName : tableNames) {
        if (usnoDao.tableExists(tName)) {
          List<UsnoCatalog> objs = usnoDao.queryByOt2(ot2.getRa(), ot2.getDec(), tdis5, tmag5, tName);
          for (UsnoCatalog obj : objs) {
            double tDis = CommonFunction.getGreatCircleDistance(ot2.getRa(), ot2.getDec(), obj.getrAdeg(), obj.getdEdeg());
            if (tDis < minDis) {
              minDis = tDis;
              tobj = obj;
            }
          }
        } else {
          log.warn("table " + tName + " not exists!");
        }
      }
    }
    if (tobj != null) {
      rst.put(tobj, minDis);
    }
    minDis = tdis6;
    tobj = null;
    if (rst.isEmpty()) {
      tableNames = getUsnoTableNames(ot2, tdis6);
      for (String tName : tableNames) {
        if (usnoDao.tableExists(tName)) {
          List<UsnoCatalog> objs = usnoDao.queryByOt2(ot2.getRa(), ot2.getDec(), tdis6, tmag6, tName);
          for (UsnoCatalog obj : objs) {
            double tDis = CommonFunction.getGreatCircleDistance(ot2.getRa(), ot2.getDec(), obj.getrAdeg(), obj.getdEdeg());
            if (tDis < minDis) {
              minDis = tDis;
              tobj = obj;
            }
          }
        } else {
          log.warn("table " + tName + " not exists!");
        }
      }
    }
    if (tobj != null) {
      rst.put(tobj, minDis);
    }
    return rst;
  }

  public Map<UsnoCatalog, Double> matchOt2InUsnoCatalog(CrossObject ot2) {

    Map rst = new HashMap();
    List<String> tableNames = getUsnoTableNames(ot2, usnoSearchbox);
    for (String tName : tableNames) {
      if (usnoDao.tableExists(tName)) {
        List<UsnoCatalog> objs = usnoDao.queryByOt2(ot2.getRa(), ot2.getDec(), usnoSearchbox, usnoMag, tName);
        double minDis = usnoSearchbox;
        for (UsnoCatalog obj : objs) {
          double tDis = CommonFunction.getGreatCircleDistance(ot2.getRa(), ot2.getDec(), obj.getrAdeg(), obj.getdEdeg());
          if (tDis < minDis) {
            rst.put(obj, tDis);
          }
        }
      } else {
        log.warn("table " + tName + " not exists!");
      }
    }
    if (rst.isEmpty()) {
      tableNames = getUsnoTableNames(ot2, usnoSearchbox2);
      for (String tName : tableNames) {
        if (usnoDao.tableExists(tName)) {
          List<UsnoCatalog> objs = usnoDao.queryByOt2(ot2.getRa(), ot2.getDec(), usnoSearchbox2, usnoMag2, tName);
          double minDis = usnoSearchbox2;
          for (UsnoCatalog obj : objs) {
            double tDis = CommonFunction.getGreatCircleDistance(ot2.getRa(), ot2.getDec(), obj.getrAdeg(), obj.getdEdeg());
            if (tDis < minDis) {
              rst.put(obj, tDis);
            }
          }
        } else {
          log.warn("table " + tName + " not exists!");
        }
      }
    }
    return rst;
  }

  public List<String> getUsnoTableNames(CrossObject ot2, float searchBox) {

    List<String> rst = new ArrayList();
    if (ot2.getDec() + 90 > 0 && ot2.getDec() - 90 < 0) {
      MatchTable ott = mtDao.getMatchTableByTypeName("usno");
      int maxIdx = (int) ((90 + ot2.getDec() + searchBox) * 10);
      int minIdx = (int) ((90 + ot2.getDec() - searchBox) * 10);
      for (int i = minIdx; i <= maxIdx; i++) {
        String tableName = String.format("%04d%s", i, ott.getMatchTableName());
        rst.add(tableName);
      }
    }
    return rst;
  }

  public String getMinorPlanetTableName(Date ot2Date) {
    MatchTable ott = mtDao.getMatchTableByTypeName("minor_planet");
    //String tableName = ott.getMatchTableName() + CommonFunction.getDateString(CommonFunction.getUTCDate(new Date()));
    String tableName = ott.getMatchTableName() + CommonFunction.getDateString(ot2Date);
    return tableName;
  }

  public void printConfigParameter() {
    log.debug("isBeiJingServer=" + isBeiJingServer);
    log.debug("isTestServer=" + isTestServer);
    log.debug("mergedSearchbox=" + mergedSearchbox);
    log.debug("cvsSearchbox=" + cvsSearchbox);
    log.debug("rc3Searchbox=" + rc3Searchbox);
    log.debug("minorPlanetSearchbox=" + minorPlanetSearchbox);
    log.debug("ot2Searchbox=" + ot2Searchbox);
    log.debug("usnoSearchbox=" + usnoSearchbox);
    log.debug("usnoSearchbox2=" + usnoSearchbox2);
    log.debug("mergedMag=" + mergedMag);
    log.debug("cvsMag=" + cvsMag);
    log.debug("rc3MinMag=" + rc3MinMag);
    log.debug("rc3MaxMag=" + rc3MaxMag);
    log.debug("minorPlanetMag=" + minorPlanetMag);
    log.debug("usnoMag=" + usnoMag);
    log.debug("usnoMag2=" + usnoMag2);
  }

  @Override
  public void startJob() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
