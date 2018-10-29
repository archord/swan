/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import com.gwac.activemq.OTFollowMessageCreator;
import com.gwac.dao.FollowUpObjectDao;
import com.gwac.dao.FollowUpObjectTypeDao;
import com.gwac.dao.FollowUpObservationDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.ScienceObjectDao;
import com.gwac.dao.UserInfoDAO;
import com.gwac.model.FollowUpObject;
import com.gwac.model.FollowUpObjectType;
import com.gwac.model.FollowUpObservation;
import com.gwac.model.OtLevel2;
import com.gwac.model.ScienceObject;
import com.gwac.model.UserInfo;
import com.gwac.model4.OtLevel2FollowParameter;
import com.gwac.util.CommonFunction;
import java.util.List;
import javax.annotation.Resource;
import javax.jms.Destination;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
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
  private OtLevel2Dao ot2Dao;
  @Resource
  private UserInfoDAO userDao;
  @Resource
  private FollowUpObjectTypeDao fuotDao;
  @Resource
  private ScienceObjectDao sciObjDao;

  @Resource
  private JmsTemplate jmsTemplate;
  @Resource(name = "otFollowDest")
  private Destination otFollowDest;

  @Value("#{syscfg.gwacFollowTriggerPreSendTime}")
  private int gwacFollowTriggerPreSendTime; //seconds
  @Value("#{syscfg.gwacServerBeijing}")
  private Boolean isBeiJingServer;
  @Value("#{syscfg.gwacServerTest}")
  private Boolean isTestServer;

  private double catasLongMagDiff = 1.0;
  private double catasShortMagDiff = 0.5;
  private float miniMinRecordNum = 3;
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

    FollowUpObservation fupObs = fupObsDao.getById(fupObsId);
    List<FollowUpObservation> fupObss = fupObsDao.getByFoId(fupObsId);
    List<FollowUpObject> fupObjs = fupObjDao.getByFupObsId(fupObsId, true);

    //第N>1次后随
    if (fupObss.size() > 1) {
      for (FollowUpObservation tobj : fupObss) {
        if (null != tobj.getSoId()) {
          fupObs.setSoId(tobj.getSoId());
          fupObsDao.update(fupObs);
          break;
        }
      }
      if(null == fupObs.getSoId()){//前N-1次后随，都没有发现真目标，检查这次的所有目标
        checkFupObjs(fupObjs, fupObs);
      }
    } else {
      if(null == fupObs.getSoId()){//第1次后随，第一次上传结果，检查所有目标
        checkFupObjs(fupObjs, fupObs);
      }
    }

    Long sciObjId = fupObs.getSoId();
    if (null == sciObjId || sciObjId == 0) {
    } else {
      ScienceObject sciObj = sciObjDao.getById(sciObjId);
    }

  }
  
  public void checkFupObjs(List<FollowUpObject> fupObjs, FollowUpObservation fupObs){
    
      for (FollowUpObject tobj : fupObjs) {
        if (tobj.getFuoTypeId() == catasId) {
          float magDiff = tobj.getFoundMag() - tobj.getR2();
          if (magDiff > catasLongMagDiff) {
            ScienceObject sciObj = new ScienceObject();
            sciObj.setRa(tobj.getLastRa());
            sciObj.setDec(tobj.getLastDec());
            sciObj.setDiscoveryTimeUtc(tobj.getStartTimeUtc());
            sciObj.setFollowupTimes(1);
            sciObj.setIsTrue(true);
            sciObj.setLastObsTimeUtc(tobj.getLastTimeUtc());
            sciObj.setMag(tobj.getFoundMag());
            sciObj.setName(fupObs.getObjName());
            sciObj.setStatus(1);
            sciObj.setType("CATAS");
            sciObjDao.save(sciObj);
            fupObsDao.updateSciObjId(fupObs.getFoId(), sciObj.getSoId());
            break;
          }
        } else if (tobj.getFuoTypeId() == miniotId && tobj.getRecordTotal() >= miniMinRecordNum) {
          ScienceObject sciObj = new ScienceObject();
          sciObj.setRa(tobj.getLastRa());
          sciObj.setDec(tobj.getLastDec());
          sciObj.setDiscoveryTimeUtc(tobj.getStartTimeUtc());
          sciObj.setFollowupTimes(1);
          sciObj.setIsTrue(true);
          sciObj.setLastObsTimeUtc(tobj.getLastTimeUtc());
          sciObj.setMag(tobj.getFoundMag());
          sciObj.setName(fupObs.getObjName());
          sciObj.setStatus(1);
          sciObj.setType("MINIOT");
          sciObjDao.save(sciObj);
          fupObsDao.updateSciObjId(fupObs.getFoId(), sciObj.getSoId());
          break;
        }
      }
  }

  public void autoFollowUp() {

  }

  public void sendMessage() {

  }

}
