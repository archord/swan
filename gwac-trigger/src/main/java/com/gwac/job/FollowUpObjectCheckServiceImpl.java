/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import com.gwac.dao.FollowUpObjectDao;
import com.gwac.dao.FollowUpObjectTypeDao;
import com.gwac.dao.FollowUpObservationDao;
import com.gwac.dao.ScienceObjectDao;
import com.gwac.dao.WebGlobalParameterDao;
import com.gwac.model.FollowUpObject;
import com.gwac.model.FollowUpObjectType;
import com.gwac.model.FollowUpObservation;
import com.gwac.model.ScienceObject;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author msw
 */
@Service(value = "fupObjCheckService")
public class FollowUpObjectCheckServiceImpl implements BaseService {

  private static final Log log = LogFactory.getLog(FollowUpObjectCheckServiceImpl.class);
  private static boolean running = true;

  @Resource
  private FollowUpObservationDao fupObsDao;
  @Resource
  private FollowUpObjectDao fupObjDao;
  @Resource
  private FollowUpObjectTypeDao fuotDao;
  @Resource
  private ScienceObjectDao sciObjDao;
  @Resource
  private WebGlobalParameterDao wgpdao;

  @Value("#{syscfg.gwacServerBeijing}")
  private Boolean isBeiJingServer;
  @Value("#{syscfg.gwacServerTest}")
  private Boolean isTestServer;

  private double fupStage1MagDiff = 1.0;
  private Integer fupStage1MinRecordNum = 3;
  private boolean autoFollowUp = true;
  private short checkId = 1;
  private short catasId = 2;
  private short miniotId = 3;
  private short newotId = 4;

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
      log.warn("do nothing");
    } catch (Exception ex) {
      log.error("Job error", ex);
    } finally {
      if (running == false) {
        running = true;
      }
    }
    long endTime = System.nanoTime();
    double time1 = 1.0 * (endTime - startTime) / 1e9;
    log.debug("job consume: FollowUpObservation " + time1 + ".");
  }

  /**
   * 后随观测阶段： 1，第一次后随，GWAC OT2触发，判断后随目标的类型 catas、MiniOT、newOT，以及其catas的星等变化值
   * 2，第二次后随，catas判断后随目标星等变化超过阈值T1，触发；MiniOT出现超过3次，触发 3，第三次后随，不做判断，定时10分钟后触发
   * 3，第四次后随，第三次后随目标星等变化超过阈值T2，触发 4，第N次后随，设置每个目标的专有后随计划
   *
   * @param fupObsId
   */
  public void checkObjects(long fupObsId) {

    getParm();

    FollowUpObservation fupObs = fupObsDao.getById(fupObsId);
    List<FollowUpObservation> fupObss = fupObsDao.getByFoId(fupObsId);
//    List<FollowUpObject> fupObjs = fupObjDao.getByFupObsId(fupObsId, true);
    List<FollowUpObject> fupObjs = fupObjDao.getByOtId(fupObs.getOtId(), false);

    //第N>1次后随
    if (fupObss.size() > 1) {
      for (FollowUpObservation tobj : fupObss) {
        if (null != tobj.getSoId()) {
          fupObs.setSoId(tobj.getSoId());
          fupObsDao.update(fupObs);
          break;
        }
      }
      if (null == fupObs.getSoId()) {//前N-1次后随，都没有发现真目标，检查这次的所有目标
        checkFupObjs(fupObjs, fupObs);
      }
    } else {
      //第1次后随，且还没有发现“真目标”触发再次后随，因而observation中的sci_obj_id还为空
      if (null == fupObs.getSoId()) {
        checkFupObjs(fupObjs, fupObs);
      }
    }

  }

  public void checkFupObjs(List<FollowUpObject> fupObjs, FollowUpObservation fupObs) {

    log.debug("check observation " + fupObs.getFoName() + " has " + fupObjs.size() + " objects.");

    for (FollowUpObject tobj : fupObjs) {
      if (tobj.getFuoTypeId() == catasId) {
        float magDiff = tobj.getFoundMag() - tobj.getR2();
        log.debug(tobj.getFuoName() + " magDiff=" + magDiff);
        if (magDiff > fupStage1MagDiff) {
          ScienceObject sciObj = new ScienceObject();
          sciObj.setPointRa(fupObs.getRa());
          sciObj.setPointDec(fupObs.getDec());
          sciObj.setObjRa(tobj.getLastRa());
          sciObj.setObjDec(tobj.getLastDec());
          sciObj.setDiscoveryTimeUtc(tobj.getStartTimeUtc());
          sciObj.setFollowupTimes(1);
          sciObj.setIsTrue(true);
          sciObj.setLastObsTimeUtc(tobj.getLastTimeUtc());
          sciObj.setMag(tobj.getFoundMag());
          sciObj.setName(fupObs.getObjName());
          sciObj.setStatus(1);
          sciObj.setTriggerStatus(1);
          sciObj.setType("CATAS");
          sciObj.setFupCount(1);
          sciObj.setAutoObservation(autoFollowUp);
          sciObjDao.save(sciObj);
          fupObsDao.updateSciObjId(fupObs.getObjName(), sciObj.getSoId());
          sciObjDao.updateFupCount(sciObj.getSoId());

          break;
        }
      } else if (tobj.getFuoTypeId() == miniotId && tobj.getRecordTotal() >= fupStage1MinRecordNum) {
        log.debug(tobj.getFuoName() + " totalRecord=" + tobj.getRecordTotal());
        ScienceObject sciObj = new ScienceObject();
        sciObj.setPointRa(fupObs.getRa());
        sciObj.setPointDec(fupObs.getDec());
        sciObj.setObjRa(tobj.getLastRa());
        sciObj.setObjDec(tobj.getLastDec());
        sciObj.setDiscoveryTimeUtc(tobj.getStartTimeUtc());
        sciObj.setFollowupTimes(1);
        sciObj.setIsTrue(true);
        sciObj.setLastObsTimeUtc(tobj.getLastTimeUtc());
        sciObj.setMag(tobj.getFoundMag());
        sciObj.setName(fupObs.getObjName());
        sciObj.setStatus(1);
        sciObj.setTriggerStatus(1);
        sciObj.setType("MINIOT");
        sciObj.setFupCount(1);
        sciObj.setAutoObservation(autoFollowUp);
        sciObjDao.save(sciObj);
        fupObsDao.updateSciObjId(fupObs.getObjName(), sciObj.getSoId());
        sciObjDao.updateFupCount(sciObj.getSoId());

        break;
      }
    }
  }

  public void getParm() {

    autoFollowUp = Boolean.parseBoolean(wgpdao.getValueByName("AutoFollowUp")); 
    fupStage1MagDiff = Double.parseDouble(wgpdao.getValueByName("fupStage1MagDiff"));
    fupStage1MinRecordNum = Integer.parseInt(wgpdao.getValueByName("fupStage1MinRecordNum"));

    FollowUpObjectType tfuot = fuotDao.getOtTypeByTypeName("CHECK");
    if (tfuot != null) {
      checkId = tfuot.getFuoTypeId();
    }
    tfuot = fuotDao.getOtTypeByTypeName("MINIOT");
    if (tfuot != null) {
      miniotId = tfuot.getFuoTypeId();
    }
    tfuot = fuotDao.getOtTypeByTypeName("CATAS");
    if (tfuot != null) {
      catasId = tfuot.getFuoTypeId();
    }
    tfuot = fuotDao.getOtTypeByTypeName("NEWOT");
    if (tfuot != null) {
      newotId = tfuot.getFuoTypeId();
    }
  }

}
