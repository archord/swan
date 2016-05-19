/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.job;

import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtTmplWrongDao;
import com.gwac.model.OtLevel2;
import com.gwac.model.OtTmplWrong;
import com.gwac.util.CommonFunction;
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
      generateOtTmpl('4');
      generateOtTmpl('1');
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

  public void rematchAllOt2() {

  }

  /**
   * ot_class的取值有5种： 0，未分类 1，真OT 2，动OT 3，错OT 4，假OT。
   * 考虑到大部分假的都未分类，为能建立完善的OT2历史模板，现在将“未分类”的值设置为4
   * @param otClass
   */
  public void generateOtTmpl(char otClass) {
    
    List<String> dateStrs = ot2Dao.getAllDateStr();
    log.debug("total days: " + dateStrs.size());
    for (String dateStr : dateStrs) {
      List<OtLevel2> ot2s = ot2Dao.getLv2OTByDateAndOTClass(dateStr, otClass);
      log.debug("date: " + dateStr + ", ot2 number: " + ot2s.size());

      for (OtLevel2 tot2 : ot2s) {
        List<OtTmplWrong> mot2s = ottwDao.searchOT2TmplWrong(tot2, ot2Searchbox, 0);
        int matchNum = mot2s.size();
        if (matchNum > 1) {
          log.warn("otId=" + tot2.getOtId() + ",match multi template star: " + matchNum + "searchbox=" + ot2Searchbox);
        }

        double minDis = ot2Searchbox;
        OtTmplWrong matchedOT2 = null;

        for (OtTmplWrong obj : mot2s) {
          double tDis = CommonFunction.getGreatCircleDistance(tot2.getRa(), tot2.getDec(), obj.getRa(), obj.getDec());
          if (matchNum > 1) {
            log.warn("otId=" + obj.getOtId() + ", distance=" + tDis);
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
          otw.setHealpixId((long) 0);
          otw.setDataProduceMethod(tot2.getDataProduceMethod());
          otw.setFirstFoundTimeUtc(tot2.getFoundTimeUtc());
          otw.setLastFoundTimeUtc(tot2.getFoundTimeUtc());
          otw.setMatchedTotal(1);
          otw.setIsValid(Boolean.TRUE);
          otw.setOttId(tot2.getOtType());
          otw.setOtClass(otClass);
          ottwDao.save(otw);
        } else {
          log.debug("success match template star, update.");
          matchedOT2.setLastFoundTimeUtc(tot2.getFoundTimeUtc());
          matchedOT2.setMatchedTotal(matchedOT2.getMatchedTotal() + 1);
          ottwDao.update(matchedOT2);
        }
      }
    }

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

}
