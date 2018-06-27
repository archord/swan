/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import com.gwac.activemq.OTFollowMessageCreator;
import com.gwac.dao.FollowUpObservationDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.UserInfoDAO;
import com.gwac.model.FollowUpObservation;
import com.gwac.model.OtLevel2;
import com.gwac.model.UserInfo;
import com.gwac.model4.OtLevel2FollowParameter;
import com.gwac.util.CommonFunction;
import java.util.Date;
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
@Service(value = "fuoTriggerService")
public class FollowUpObservationTriggerServiceImpl implements BaseService {

  private static final Log log = LogFactory.getLog(FollowUpRecordServiceImpl.class);
  private static boolean running = true;

  @Resource
  private FollowUpObservationDao fuoDao;
  @Resource
  private OtLevel2Dao ot2Dao;
  @Resource
  private UserInfoDAO userDao;

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
      triggerAll();
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

  public void triggerAll() {

    fuoDao.updateLatePlan();

    List<FollowUpObservation> fuos = fuoDao.getUnTriggeredByTime(gwacFollowTriggerPreSendTime);
    for (FollowUpObservation fuo : fuos) {
      OtLevel2FollowParameter ot2fp = new OtLevel2FollowParameter();
      if (fuo.getBeginTime() != null) {
        ot2fp.setBegineTime(CommonFunction.getDateTimeString2(fuo.getBeginTime()));
      } else {
        ot2fp.setBegineTime("-1");
      }
      ot2fp.setDec(fuo.getDec());
      if (fuo.getEndTime() != null) {
        ot2fp.setEndTime(CommonFunction.getDateTimeString2(fuo.getEndTime()));
      } else {
        ot2fp.setEndTime("-1");
      }
      ot2fp.setEpoch(fuo.getEpoch());
      ot2fp.setExpTime(fuo.getExposeDuration());
      ot2fp.setFilter(fuo.getFilter());
      ot2fp.setFollowName(fuo.getFoName());
      ot2fp.setFrameCount(fuo.getFrameCount());
      ot2fp.setImageType(fuo.getImageType());
      if (fuo.getOtId() != null) {
        OtLevel2 ot2 = ot2Dao.getById(fuo.getOtId());
        ot2fp.setOtName(ot2.getName());
      } else {
        ot2fp.setOtName("NotGWACOT");
      }
      ot2fp.setPriority(fuo.getPriority());
      ot2fp.setRa(fuo.getRa());
      ot2fp.setTelescope(fuo.getTelescopeId());
      ot2fp.setTriggerType(fuo.getTriggerType());
      
      UserInfo user = userDao.getById(fuo.getUserId().longValue());
      ot2fp.setUserName(user.getName());

      MessageCreator tmc = new OTFollowMessageCreator(ot2fp);
      jmsTemplate.send(otFollowDest, tmc);
      log.debug(ot2fp.getTriggerMsg());

      fuo.setExecuteStatus('1');
      fuoDao.update(fuo);
    }

  }

}
