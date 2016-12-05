/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.job;

import com.gwac.dao.MatchTableDao;
import com.gwac.dao.OorTmpDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtLevel2MatchDao;
import com.gwac.dao.OtObserveRecordDAO;
import com.gwac.dao.OtTmplWrongDao;
import com.gwac.model.MatchTable;
import com.gwac.model.OorTmp;
import com.gwac.model.OtLevel2;
import com.gwac.model.OtLevel2Match;
import com.gwac.model.OtObserveRecord;
import com.gwac.model.OtTmplWrong;
import com.gwac.util.CommonFunction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author xy
 */
@Service(value = "otTmplService")
public class OtTmplServiceImpl implements BaseService {

  private static final Log log = LogFactory.getLog(OtTmplServiceImpl.class);
  private static boolean running = true;
  
  @Value("#{syscfg.gwacServerBeijing}")
  private Boolean isBeiJingServer;
  @Value("#{syscfg.gwacServerTest}")
  private Boolean isTestServer;
  @Value("#{syscfg.mingwacOt2hisSearchbox}")
  private float ot2Searchbox;
  @Resource
  private OtLevel2Dao ot2Dao;
  @Resource
  private OtTmplWrongDao ottwDao;
  @Resource
  private MatchTableDao mtDao;
  @Resource
  private OtLevel2MatchDao ot2mDao;
  @Resource
  private OtObserveRecordDAO oorDao;
  @Resource
  private OorTmpDao oorTmpDao;

  @Override
  public void startJob() {

    if (isTestServer) {
      return;
    }
    
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

//      generateOtTmpl2('4');
//      generateOtTmpl2('1');
//      rematchAllOt2();
//      findOT1();
      otTmplDailyUpdate('4');
      otTmplDailyUpdate('1');
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

  public void otTmplDailyUpdate(char otClass) {

    List<OtLevel2> ot2s = ot2Dao.getTodayOt2(otClass);

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
        List<OtTmplWrong> mot2s = ottwDao.searchOT2TmplWrong(tot2, (float) (searchbox), 0);
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
            searchbox = maxDist * 2 + ot2Searchbox;
          } else {
            ottwDao.save(otw);
            break;
          }
        } else {
          if (matchNum == 0) {
            ottwDao.save(otw);
          }
          break;
        }
      }
      log.debug("match " + times + " times, final match number " + tMchNum);

      if (matchedOttws.size() > 1) {
        OtTmplWrong avgOttw = getAvgPosition(matchedOttws);
        float maxDist = getMaxDist(matchedOttws, avgOttw);

        int totalMchNum = 0;
        OtTmplWrong firstOttw = null, tottw2 = null, toRemove = null;
        boolean flag = true;
        for (OtTmplWrong obj : matchedOttws) {
          totalMchNum += obj.getMatchedTotal();
          if (obj.getOtId() == tot2.getOtId()) {
            toRemove = obj;
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
        matchedOttws.remove(toRemove);

        firstOttw.setLastFoundTimeUtc(tottw2.getLastFoundTimeUtc());
        firstOttw.setMatchedTotal(totalMchNum);
        firstOttw.setRa(avgOttw.getRa());
        firstOttw.setDec(avgOttw.getDec());
        firstOttw.setRadius(maxDist);
        matchedOttws.remove(firstOttw);
        ottwDao.update(firstOttw);
        log.debug("finally match " + firstOttw.getName() + ", remove obj number: " + matchedOttws.size());
        for (OtTmplWrong obj : matchedOttws) {
          ot2mDao.updateOt2HisMatchId(tot2.getOtId(), obj.getOtId(), firstOttw.getOtId());
          ottwDao.delete(obj);
        }
      }
    }
  }

  public void rematchAllOt2() {
    
    boolean history = true;

    List<String> dateStrs = ot2Dao.getAllDateStr(history);
    log.debug("total days: " + dateStrs.size());

    MatchTable ott = mtDao.getMatchTableByTypeName("ot_level2_his");
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
        double minDis = ot2Searchbox;
        Map<OtTmplWrong, Double> tOT2Hism = new HashMap();
        for (OtTmplWrong obj : mot2s) {
          double tDis = CommonFunction.getGreatCircleDistance(tot2.getRa(), tot2.getDec(), obj.getRa(), obj.getDec());
          if (tDis < minDis) {
            tOT2Hism.put(obj, tDis);
          }
        }

        Boolean hisType = false;
        for (Map.Entry<OtTmplWrong, Double> entry : tOT2Hism.entrySet()) {
          OtTmplWrong ootw = (OtTmplWrong) entry.getKey();
          Double distance = (Double) entry.getValue();

          OtLevel2Match ot2m = new OtLevel2Match();
          ot2m.setOtId(tot2.getOtId());
          ot2m.setMtId(ott.getMtId());
          ot2m.setMatchId(Long.valueOf(ootw.getOtId()));
          ot2m.setRa(ootw.getRa());
          ot2m.setDec(ootw.getDec());
          ot2m.setMag(ootw.getMag());
          ot2m.setDistance(distance.floatValue());
          ot2mDao.save(ot2m);

          if (!hisType && ootw.getOtClass() == '1') {
            tot2.setOtType(ootw.getOttId()); //ot2匹配到多个ot2历史目标时，类别设置为“真暂现源”中最后一个的类别
            hisType = true;
          }
        }
        if (hisType) {
          ot2Dao.updateOTTypeHis(tot2);
        }
        if (tOT2Hism.size() > 0) {
          tot2.setOt2HisMatch((short) tOT2Hism.size());
        }
        ot2Dao.updateOt2HisMatchHis(tot2);
//        log.debug(tot2.getName() + " match ot2 number:" + tOT2Hism.size());
      }
    }
  }

  /**
   * @param otClass ot_class的取值有5种： 0，未分类 1，真OT 2，动OT 3，错OT 4，假OT。
   * 考虑到大部分假的都未分类，为能建立完善的OT2历史模板，现在将“未分类”的值设置为4
   */
  public void generateOtTmpl(char otClass) {
    
    boolean history = true;

    List<String> dateStrs = ot2Dao.getAllDateStr(history);
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

  /**
   * 对不同CCD同一天区的OT1进行匹配，目的是找出同一时刻出现的OT
   */
  public void findOT1() {

    List<String> dateStrs = oorDao.getAllDateStr();
    log.debug("total days: " + dateStrs.size());

    for (String dateStr : dateStrs) {
      List<OtObserveRecord> oors = oorDao.getOt1ByDate(dateStr);
      log.debug("date: " + dateStr + ", ot1 number: " + oors.size());

      int matchNum = 0;
      int removeNum = 0;
      while (oors.size() > 0) {
        OtObserveRecord tot1 = oors.get(0);
        double searchbox = ot2Searchbox;
        List<OtObserveRecord> mot2s = oorDao.searchOT2TmplWrong(tot1, (float) (searchbox), 0);

        List<OtObserveRecord> matchedObjs = new ArrayList();
        for (OtObserveRecord obj : mot2s) {
          double tDis = CommonFunction.getGreatCircleDistance(tot1.getRaD(), tot1.getDecD(), obj.getRaD(), obj.getDecD());
          if (tDis < searchbox) {
            matchedObjs.add(obj);
          }
        }
        int tsize = matchedObjs.size();
        if (tsize > 0) {
          log.debug("ot1id:" + tot1.getOorId() + ", match " + tsize);
        }
        for (OtObserveRecord toot2 : matchedObjs) {
          OorTmp oorTmp = new OorTmp();
          oorTmp.setOor1Id(tot1.getOorId());
          oorTmp.setOor1Ra(tot1.getRaD());
          oorTmp.setOor1Dec(tot1.getDecD());
          oorTmp.setOor1Mag(tot1.getMagAper());
          oorTmp.setOor2Id(toot2.getOorId());
          oorTmp.setOor2Ra(toot2.getRaD());
          oorTmp.setOor2Dec(toot2.getDecD());
          oorTmp.setOor2Mag(toot2.getMagAper());
          oorTmp.setOor1DpmId(tot1.getDpmId());
          oorTmp.setOor1DateUt(tot1.getDateUt());
          oorTmp.setOor2DpmId(toot2.getDpmId());
          oorTmp.setOor2DateUt(toot2.getDateUt());
          oorTmpDao.save(oorTmp);
        }
        matchNum++;
        removeNum += tsize;
        oors.remove(tot1);
        oors.removeAll(matchedObjs);
      }
      log.debug("date: " + dateStr + ",matchNum:" + matchNum + ", removeNum:" + removeNum + ", total:" + (matchNum + removeNum));
    }
  }

  /**
   * generateOtTmpl：匹配半径内有多个目标，取最近的一个为匹配对象。 generateOtTmpl2：
   * 1，匹配半径内的多个目标，都认为是同一个目标； 2，对这些目标进行合并，删除晚出现的目标；
   * 3，将所有目标位置的平均值作为该目标的值，所有目标与均值坐标最大距离作为目标半径 4，以均值坐标和匹配半径+目标半径，再次进行匹配
   * 5，直到匹配个数不增加或匹配总次数超过20次，完成当前目标的匹配，开始下一个目标的匹配
   *
   * @param otClass
   */
  public void generateOtTmpl2(char otClass) {
    
    boolean history = true;

    List<String> dateStrs = ot2Dao.getAllDateStr(history);
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
          List<OtTmplWrong> mot2s = ottwDao.searchOT2TmplWrong(tot2, (float) (searchbox), 0);
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
              OtTmplWrong avgOttw = getAvgPosition(matchedOttws); //应该取ot2历史模板模板对应的所有ot2取平均值
              tot2.setRa(avgOttw.getRa());
              tot2.setDec(avgOttw.getDec());
              float maxDist = getMaxDist(matchedOttws, avgOttw); //应该取ot2历史模板模板对应的所有ot2，计算最远距离
              searchbox = maxDist * 2 + ot2Searchbox;
            } else {
              ottwDao.save(otw);
              break;
            }
          } else {
            if (matchNum == 0) {
              ottwDao.save(otw);
            }
            break;
          }
        }
        log.debug("match " + times + " times, final match number " + tMchNum);

        if (matchedOttws.size() > 1) {
          OtTmplWrong avgOttw = getAvgPosition(matchedOttws); //应该取ot2历史模板模板对应的所有ot2取平均值
          float maxDist = getMaxDist(matchedOttws, avgOttw); //应该取ot2历史模板模板对应的所有ot2，计算最远距离

          int totalMchNum = 0;
          OtTmplWrong firstOttw = null, tottw2 = null, toRemove = null;
          boolean flag = true;
          for (OtTmplWrong obj : matchedOttws) {
            totalMchNum += obj.getMatchedTotal();
            if (obj.getOtId() == 0) {
              toRemove = obj;
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
          matchedOttws.remove(toRemove);

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

  public OtTmplWrong getAvgPosition(Map<Float, Float> ottws) {
    OtTmplWrong avgOttw = new OtTmplWrong();
    float avgRa = (float) 0;
    float avgDec = (float) 0;
    Iterator<Map.Entry<Float, Float>> iterator = ottws.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<Float, Float> entry = iterator.next();
      avgRa += entry.getKey();
      avgDec += entry.getValue();
    }
    avgOttw.setRa(avgRa / ottws.size());
    avgOttw.setDec(avgDec / ottws.size());
    return avgOttw;
  }

  public float getMaxDist(Map<Float, Float> ottws, OtTmplWrong obj) {

    Double maxDist = 0.0;
    boolean first = true;
    Iterator<Map.Entry<Float, Float>> iterator = ottws.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<Float, Float> entry = iterator.next();
      double tDis = CommonFunction.getGreatCircleDistance(entry.getKey(), entry.getValue(), obj.getRa(), obj.getDec());
      if (first) {
        maxDist = tDis;
        first = false;
      } else if (maxDist < tDis) {
        maxDist = tDis;
      }
    }
    return maxDist.floatValue();
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

}
