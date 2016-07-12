/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.job;

import com.gwac.dao.MatchTableDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtLevel2MatchDao;
import com.gwac.dao.OtTmplWrongDao;
import com.gwac.model.MatchTable;
import com.gwac.model.OtLevel2;
import com.gwac.model.OtLevel2Match;
import com.gwac.model.OtTmplWrong;
import com.gwac.util.CommonFunction;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author xy
 */
public class OtTmplServiceImpl implements OtTmplService {

  private static final Log log = LogFactory.getLog(OtTmplServiceImpl.class);

  private static boolean running = true;
  private Boolean isBeiJingServer;
  private Boolean isTestServer;
  private float ot2Searchbox;
  private OtLevel2Dao ot2Dao;
  private OtTmplWrongDao ottwDao;
  private MatchTableDao mtDao;
  private OtLevel2MatchDao ot2mDao;

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
//      generateOtTmpl('4'); //未匹配，假OT
//      generateOtTmpl('1'); //真OT
//      rematchAllOt2();
      generateOtTmpl2('4');
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

  public void generateOtTmpl2(char otClass) {

    List<String> dateStrs = ot2Dao.getAllDateStr();
    log.debug("total days: " + dateStrs.size());
    for (String dateStr : dateStrs) {
      List<OtLevel2> ot2s = ot2Dao.getLv2OTByDateAndOTClass(dateStr, otClass);
      log.debug("date: " + dateStr + ", ot2 number: " + ot2s.size());

      for (OtLevel2 tot2 : ot2s) {
        log.debug("processing " + tot2.getName());
        if (tot2.getRa() + 999 < CommonFunction.MINFLOAT || tot2.getDec() + 999 < CommonFunction.MINFLOAT) {
          log.error("name=" + tot2.getName() + ", ra and dec -999 error!");
          continue;
        }

        OtTmplWrong otw = new OtTmplWrong();
        otw.setOtId(tot2.getOtId());
        otw.setName(tot2.getName());
        otw.setRa(tot2.getRa());
        otw.setDec(tot2.getDec());
        otw.setMag(tot2.getMag());
        otw.setIndexId((long) 0);
        otw.setDataProduceMethod(tot2.getDataProduceMethod());
        otw.setFirstFoundTimeUtc(tot2.getFoundTimeUtc());
        otw.setLastFoundTimeUtc(tot2.getFoundTimeUtc());
        otw.setMatchedTotal(1);
        otw.setIsValid(Boolean.TRUE);
        otw.setOttId(tot2.getOtType());
        otw.setOtClass(otClass);
        otw.setRadius((float) 0);

        double searchbox = ot2Searchbox;
        List<OtTmplWrong> matchedOttws = new ArrayList();
        int times = 0;

        int tMchNum = 0;
        while (true) {
          if (times++ > 20) {
            log.error("match too many times 21");
          }
          List<OtTmplWrong> mot2s = ottwDao.searchOT2TmplWrong2(tot2, (float) (searchbox), 0);
          int matchNum = mot2s.size();
          if (matchNum > tMchNum) {
            tMchNum = matchNum;
            matchedOttws.clear();
            for (OtTmplWrong obj : mot2s) {
              double tDis = CommonFunction.getGreatCircleDistance(tot2.getRa(), tot2.getDec(), obj.getRa(), obj.getDec());
              if (tDis < searchbox) {
                matchedOttws.add(obj);
              }
            }

            if (matchedOttws.size() > 0) {
              matchedOttws.add(otw);
              OtTmplWrong avgOttw = getAvgPosition(matchedOttws);
              tot2.setRa(avgOttw.getRa());
              tot2.setDec(avgOttw.getDec());
              float maxDist = getMaxDist(matchedOttws, avgOttw);
              searchbox = maxDist + ot2Searchbox;
            } else {
              ottwDao.save(otw);
              break;
            }
          } else {
            ottwDao.save(otw);
            break;
          }
        }
        log.debug("match " + times + " times, final match number " + tMchNum);

        if (matchedOttws.size() > 1) {
          OtTmplWrong avgOttw = getAvgPosition(matchedOttws);
          float maxDist = getMaxDist(matchedOttws, avgOttw);

          int totalMchNum = 0;
          OtTmplWrong firstOttw = null, tottw2 = null;
          boolean flag = true;
          for (OtTmplWrong obj : matchedOttws) {
            totalMchNum += obj.getMatchedTotal();
            if (obj.getOtId() == 0) {
              matchedOttws.remove(obj);
              continue;
            }
            if (flag) {
              firstOttw = obj;
              tottw2 = obj;
              flag = false;
              continue;
            }
            if (obj.getFirstFoundTimeUtc().before(firstOttw.getFirstFoundTimeUtc())) {
              firstOttw = obj;
            }
            if (obj.getLastFoundTimeUtc().after(tottw2.getLastFoundTimeUtc())) {
              tottw2 = obj;
            }
          }
          firstOttw.setLastFoundTimeUtc(tottw2.getLastFoundTimeUtc());
          firstOttw.setMatchedTotal(totalMchNum);
          firstOttw.setRa(avgOttw.getRa());
          firstOttw.setDec(avgOttw.getDec());
          firstOttw.setRadius(maxDist);
          matchedOttws.remove(firstOttw);
          ottwDao.update(firstOttw);
          log.debug("finally match " + firstOttw.getName() + ", remove obj number: " + matchedOttws.size());
          for (OtTmplWrong obj : matchedOttws) {
            ottwDao.delete(obj);
          }
        }
      }
    }

  }

  public void otTmplDailyUpdate(char otClass) {

    List<OtLevel2> ot2s = ot2Dao.getTodayAll();
    for (OtLevel2 tot2 : ot2s) {
      if (tot2.getRa() + 999 < CommonFunction.MINFLOAT || tot2.getDec() + 999 < CommonFunction.MINFLOAT) {
        log.error("name=" + tot2.getName() + ", ra and dec -999 error!");
        continue;
      }

      List<OtTmplWrong> mot2s = ottwDao.searchOT2TmplWrong(tot2, ot2Searchbox, 0);
      int matchNum = mot2s.size();
      if (matchNum > 1) {
        log.warn("name=" + tot2.getName() + ",match multi template star: " + matchNum + "searchbox=" + ot2Searchbox);
      }

      double minDis = ot2Searchbox;
      OtTmplWrong matchedOT2 = null;

      for (OtTmplWrong obj : mot2s) {
        double tDis = CommonFunction.getGreatCircleDistance(tot2.getRa(), tot2.getDec(), obj.getRa(), obj.getDec());
        if (matchNum > 1) {
          log.warn("name=" + obj.getName() + ", distance=" + tDis);
        }
        if (tDis < minDis) {
          minDis = tDis;
          matchedOT2 = obj;
        }
      }
      if (matchedOT2 == null) {
        OtTmplWrong otw = new OtTmplWrong();
        otw.setOtId(tot2.getOtId());
        otw.setName(tot2.getName());
        otw.setRa(tot2.getRa());
        otw.setDec(tot2.getDec());
        otw.setMag(tot2.getMag());
        otw.setIndexId((long) 0);
        otw.setDataProduceMethod(tot2.getDataProduceMethod());
        otw.setFirstFoundTimeUtc(tot2.getFoundTimeUtc());
        otw.setLastFoundTimeUtc(tot2.getFoundTimeUtc());
        otw.setMatchedTotal(1);
        otw.setIsValid(Boolean.TRUE);
        otw.setOttId(tot2.getOtType());
        otw.setOtClass(otClass);
        otw.setRadius((float) 0);
        ottwDao.save(otw);
      } else {
        matchedOT2.setLastFoundTimeUtc(tot2.getFoundTimeUtc());
        matchedOT2.setMatchedTotal(matchedOT2.getMatchedTotal() + 1);
        ottwDao.update(matchedOT2);
      }
    }

  }

  public void rematchAllOt2() {

    List<String> dateStrs = ot2Dao.getAllDateStr();
    log.debug("total days: " + dateStrs.size());
    for (String dateStr : dateStrs) {
      List<OtLevel2> ot2s = ot2Dao.getOt2ByDate(dateStr);
      log.debug("date: " + dateStr + ", ot2 number: " + ot2s.size());

      for (OtLevel2 tot2 : ot2s) {
        if (tot2.getRa() + 999 < CommonFunction.MINFLOAT || tot2.getDec() + 999 < CommonFunction.MINFLOAT) {
          log.error("name=" + tot2.getName() + ", ra and dec -999 error!");
          continue;
        }

        //默认没匹配
        tot2.setOt2HisMatch((short) 0);

        List<OtTmplWrong> mot2s = ottwDao.searchOT2TmplWrong(tot2, ot2Searchbox, 0);
        int matchNum = mot2s.size();
        log.debug("name=" + tot2.getName() + ",match template star: " + matchNum + ", searchbox=" + ot2Searchbox);

        if (matchNum > 0) {
          double minDis = ot2Searchbox;
          OtTmplWrong mot2 = null;
          for (OtTmplWrong ottw : mot2s) {
            Double tDis = CommonFunction.getGreatCircleDistance(tot2.getRa(), tot2.getDec(), ottw.getRa(), ottw.getDec());
            log.debug("name=" + ottw.getName() + ", name=" + ottw.getName() + ", ra=" + ottw.getRa() + ", dec=" + ottw.getDec() + ", mag=" + ottw.getMag() + ", dist=" + tDis);
            if (tDis < minDis) {
              tDis = minDis;
              mot2 = ottw;
            }
          }
          if (mot2 != null) {
            if (mot2.getMatchedTotal() > 1) {
              Double tDis = CommonFunction.getGreatCircleDistance(tot2.getRa(), tot2.getDec(), mot2.getRa(), mot2.getDec());
              MatchTable ott = mtDao.getMatchTableByTypeName("ot_level2_his");
              OtLevel2Match ot2m = new OtLevel2Match();
              ot2m.setOtId(tot2.getOtId());
              ot2m.setMtId(ott.getMtId());
              ot2m.setMatchId(mot2.getOtId());
              ot2m.setRa(mot2.getRa());
              ot2m.setDec(mot2.getDec());
              ot2m.setMag(mot2.getMag());
              ot2m.setDistance(tDis.floatValue());
              ot2mDao.save(ot2m);
              //匹配成功后设为1
              tot2.setOt2HisMatch((short) 1);
            }
          }
        }

        ot2Dao.updateOt2HisMatch(tot2);
      }
    }
  }

  /**
   * @param otClass ot_class的取值有5种： 0，未分类 1，真OT 2，动OT 3，错OT 4，假OT。
   * 考虑到大部分假的都未分类，为能建立完善的OT2历史模板，现在将“未分类”的值设置为4
   */
  public void generateOtTmpl(char otClass) {

    List<String> dateStrs = ot2Dao.getAllDateStr();
    log.debug("total days: " + dateStrs.size());
    for (String dateStr : dateStrs) {
      List<OtLevel2> ot2s = ot2Dao.getLv2OTByDateAndOTClass(dateStr, otClass);
      log.debug("date: " + dateStr + ", ot2 number: " + ot2s.size());

      for (OtLevel2 tot2 : ot2s) {
        if (tot2.getRa() + 999 < CommonFunction.MINFLOAT || tot2.getDec() + 999 < CommonFunction.MINFLOAT) {
          log.error("name=" + tot2.getName() + ", ra and dec -999 error!");
          continue;
        }

        List<OtTmplWrong> mot2s = ottwDao.searchOT2TmplWrong(tot2, ot2Searchbox, 0);
        int matchNum = mot2s.size();
        if (matchNum > 1) {
          log.warn("name=" + tot2.getName() + ",match multi template star: " + matchNum + "searchbox=" + ot2Searchbox);
        }

        double minDis = ot2Searchbox;
        OtTmplWrong matchedOT2 = null;

        for (OtTmplWrong obj : mot2s) {
          double tDis = CommonFunction.getGreatCircleDistance(tot2.getRa(), tot2.getDec(), obj.getRa(), obj.getDec());
          if (matchNum > 1) {
            log.warn("name=" + obj.getName() + ", distance=" + tDis);
          }
          if (tDis < minDis) {
            minDis = tDis;
            matchedOT2 = obj;
          }
        }
        if (matchedOT2 == null) {
          OtTmplWrong otw = new OtTmplWrong();
          otw.setOtId(tot2.getOtId());
          otw.setName(tot2.getName());
          otw.setRa(tot2.getRa());
          otw.setDec(tot2.getDec());
          otw.setMag(tot2.getMag());
          otw.setIndexId((long) 0);
          otw.setDataProduceMethod(tot2.getDataProduceMethod());
          otw.setFirstFoundTimeUtc(tot2.getFoundTimeUtc());
          otw.setLastFoundTimeUtc(tot2.getFoundTimeUtc());
          otw.setMatchedTotal(1);
          otw.setIsValid(Boolean.TRUE);
          otw.setOttId(tot2.getOtType());
          otw.setOtClass(otClass);
          ottwDao.save(otw);
        } else {
          matchedOT2.setLastFoundTimeUtc(tot2.getFoundTimeUtc());
          matchedOT2.setMatchedTotal(matchedOT2.getMatchedTotal() + 1);
          ottwDao.update(matchedOT2);
        }
      }
    }

  }

  public OtTmplWrong getAvgPosition(List<OtTmplWrong> ottws) {
    OtTmplWrong avgOttw = new OtTmplWrong();
    float avgRa = (float) 0;
    float avgDec = (float) 0;
    for (OtTmplWrong ottw : ottws) {
      avgRa += ottw.getRa();
      avgDec += ottw.getDec();
    }
    avgOttw.setRa(avgRa / ottws.size());
    avgOttw.setDec(avgDec / ottws.size());
    return avgOttw;
  }

  public float getMaxDist(List<OtTmplWrong> ottws, OtTmplWrong obj) {

    Double maxDist = 0.0;
    boolean first = true;
    for (OtTmplWrong ottw : ottws) {
      double tDis = CommonFunction.getGreatCircleDistance(ottw.getRa(), ottw.getDec(), obj.getRa(), obj.getDec());
      if (first) {
        maxDist = tDis;
        first = false;
      } else if (maxDist < tDis) {
        maxDist = tDis;
      }
    }
    return maxDist.floatValue();
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
   * @param ot2Searchbox the ot2Searchbox to set
   */
  public void setOt2Searchbox(float ot2Searchbox) {
    this.ot2Searchbox = ot2Searchbox;
  }

  /**
   * @param ot2Dao the ot2Dao to set
   */
  public void setOt2Dao(OtLevel2Dao ot2Dao) {
    this.ot2Dao = ot2Dao;
  }

  /**
   * @param ottwDao the ottwDao to set
   */
  public void setOttwDao(OtTmplWrongDao ottwDao) {
    this.ottwDao = ottwDao;
  }

  /**
   * @param mtDao the mtDao to set
   */
  public void setMtDao(MatchTableDao mtDao) {
    this.mtDao = mtDao;
  }

  /**
   * @param ot2mDao the ot2mDao to set
   */
  public void setOt2mDao(OtLevel2MatchDao ot2mDao) {
    this.ot2mDao = ot2mDao;
  }

}