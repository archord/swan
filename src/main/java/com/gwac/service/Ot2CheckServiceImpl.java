/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

import com.gwac.dao.CVSQueryDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.model.OtLevel2;
import com.gwac.model2.Cvs;
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
  private CVSQueryDao cvsDao;
  private OtLevel2Dao ot2Dao;

  public void searchOT2() {
    float searchRadius = (float)30.0 / (60 * 60);
    float mag = 14;
    searchOT2(searchRadius, mag);
  }

  /**
   * 在searchRadius范围内，返回距离最小的目标
   *
   * @param searchRadius 搜索半径
   * @param mag 搜索的最大星等
   * @return
   */
  @Override
  public void searchOT2(float searchRadius, float mag) {

    List<OtLevel2> ot2s = ot2Dao.findAll();
    for (OtLevel2 t2 : ot2s) {
      long startTime = System.nanoTime();
      Cvs tcvs = matchOt2InCvs(t2, searchRadius, mag);
      long endTime = System.nanoTime();
      String cvsName = "";
      if (tcvs != null) {
        cvsName = tcvs.getCvsid() + " " + tcvs.getRadeg() + " " + tcvs.getDedeg() + " " + tcvs.getMag();
      }
      log.debug("ot2 " + t2.getName() + " query time " + 1.0 * (endTime - startTime) / 1e9 + " seconds, detail " + cvsName);
    }
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
    System.out.println("matched cvs "+ cvss.size());
    double minDis = mag;
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

}
