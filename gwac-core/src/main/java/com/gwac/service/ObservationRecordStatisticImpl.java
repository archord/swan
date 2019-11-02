/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

import com.gwac.dao.ObservationRecordStatisticDao;
import com.gwac.linefind.CommonFunction;
import com.gwac.model.ObservationRecordStatistic;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 *
 * @author xy
 */
@Service
public class ObservationRecordStatisticImpl {

  @Resource
  private ObservationRecordStatisticDao observationRecordStatisticDao;

  public Boolean checkIsInbox(float ctrRa, float ctrDec, float searchRadius, float tRa, float tDec) {

    Boolean isIn = false;
    float maxDec = 0;
    if (ctrDec >= 0) {
      maxDec = ctrDec + searchRadius;
    } else {
      maxDec = Math.abs(ctrDec) + searchRadius;
    }
    double maxRa = 360;
    double minRa = 0;
    if (maxDec < 85) {
      double tr = searchRadius / Math.cos(maxDec * Math.PI / 180);
      maxRa = ctrRa + tr;
      minRa = ctrRa - tr;
    }
    if (tRa >= minRa && tRa <= maxRa) {
      double tdis = CommonFunction.getGreatCircleDistance(ctrRa, ctrDec, tRa, tDec);
      if(tdis<=searchRadius){
	isIn = true;
      }
    }

    return isIn;
  }

  public List<ObservationRecordStatistic> query(String startDate, String endDate, float cRa, float cDec, float radius) {

    float halfSearchBox1 = (float) (12.5 / 2);
    float halfSearchBox2 = (float) (12.5 * 1.414 / 2);
    List<ObservationRecordStatistic> tRec = observationRecordStatisticDao.query(startDate, endDate, cRa, cDec, radius);
    for (ObservationRecordStatistic tobj : tRec) {
      
      Boolean isIn1 = checkIsInbox(tobj.getCenterRa(), tobj.getCenterDec(), halfSearchBox1, cRa, cDec);
      Boolean isIn2 = checkIsInbox(tobj.getCenterRa(), tobj.getCenterDec(), halfSearchBox2, cRa, cDec);
      
    }

    return tRec;
  }

}
