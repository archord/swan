/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import com.gwac.activemq.OTFollowMessageCreator;
import com.gwac.dao.CameraDao;
import com.gwac.dao.FollowUpObjectDao;
import com.gwac.dao.FollowUpObservationDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.ScienceObjectDao;
import com.gwac.dao.UserInfoDAO;
import com.gwac.dao.WebGlobalParameterDao;
import com.gwac.model.Camera;
import com.gwac.model.FollowUpObject;
import com.gwac.model.FollowUpObservation;
import com.gwac.model.OtLevel2;
import com.gwac.model.ScienceObject;
import com.gwac.model.UserInfo;
import com.gwac.model4.OtLevel2FollowParameter;
import com.gwac.service.SendMessageService;
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
@Service(value = "sciObjTriggerService")
public class ScienceObjectTriggerServiceImpl implements BaseService {

  private static final Log log = LogFactory.getLog(ScienceObjectTriggerServiceImpl.class);
  private static boolean running = true;

  @Resource
  private FollowUpObservationDao fupObsDao;
  @Resource
  private FollowUpObjectDao fupObjDao;
  @Resource
  private ScienceObjectDao sciObjDao;
  @Resource
  private WebGlobalParameterDao wgpdao;
  @Resource
  private UserInfoDAO userDao;
  @Resource
  private OtLevel2Dao ot2Dao;
  @Resource
  private CameraDao cameraDao;
  @Resource(name = "sendMsg2WeChat")
  private SendMessageService sendMsgService;

  @Resource
  private JmsTemplate jmsTemplate;
  @Resource(name = "otFollowDest")
  private Destination otFollowDest;

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
      checkObjects();
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
   */
  public void checkObjects() {

    boolean autoFollowUp = Boolean.parseBoolean(wgpdao.getValueByName("AutoFollowUp"));
    if (!autoFollowUp) {
      return;
    }

    String chatId = "gwac003"; //GWAC_OT_gft_alert 
    Integer fupStage2StartTime = Integer.parseInt(wgpdao.getValueByName("fupStage2StartTime")); //第二次后随距离第一次后随的时间，单位分钟

    List<ScienceObject> sciObjs = sciObjDao.getByStatus(1);
    log.debug("get ScienceObject: " + sciObjs.size());
    for (ScienceObject sciObj : sciObjs) {

      OtLevel2 ot2 = ot2Dao.getOtLevel2ByName(sciObj.getName(), false);
      if (null != ot2) {
        Date discoveryTimeUtc = sciObj.getDiscoveryTimeUtc();
        Date curDate = new Date();
        double diffMinutes = (curDate.getTime() - discoveryTimeUtc.getTime()) / (1000 * 60.0) - 8 * 60;
        log.debug("check " + ot2.getName() + ", diffMinutes: " + diffMinutes + ", status: " + sciObj.getStatus());
        if (sciObj.getStatus() == 1) {
          if (diffMinutes < 60) {//超过60分钟的，目标很大可能是来自于自动后随关闭后的目标，这种目标不用触发警报
            if (sciObj.getTriggerStatus() == 1) {
              String tmsg = String.format("Auto Trigger 60CM Telescope:\n"
                      + "%s %s in Stage1\n"
                      + "gwacMag:%.2f, firstObsMag:%.2f\n"
                      + "usnoRMag:%.2f, usnoBMag:%.2f, usnoIMag:%.2f",
                      sciObj.getName(), sciObj.getType(),
                      ot2.getMag(), sciObj.getMag(),
                      sciObj.getFoundUsnoR2(), sciObj.getFoundUsnoB2(), sciObj.getFoundUsnoI());
              sendMsgService.send(tmsg, chatId);
              sciObj.setTriggerStatus(2);
              sciObjDao.update(sciObj);
            }
            if (diffMinutes > fupStage2StartTime) {
              autoFollowUp(sciObj, ot2);
            }
          } else {
            sciObj.setAutoObservation(false);
            sciObjDao.update(sciObj);
          }
        }
      } else {
        sciObj.setAutoObservation(false);
        sciObjDao.update(sciObj);
      }
    }
  }

  public void autoFollowUp(ScienceObject sciObj, OtLevel2 ot2) {

    String filter = "";
    String frameCount = "";
    String exposeDuration = "";
    String telescope = "";
    String priority = "";
    if (sciObj.getStatus() == 1) {
      filter = wgpdao.getValueByName("fupStage2Filter");
      frameCount = wgpdao.getValueByName("fupStage2FrameCount");
      exposeDuration = wgpdao.getValueByName("fupStage2ExposeDuration");
      telescope = wgpdao.getValueByName("fupStage2Telescope");
      priority = wgpdao.getValueByName("fupStage2Priority");
    } else if (sciObj.getStatus() == 2) {
      filter = wgpdao.getValueByName("fupStage3Filter");
      frameCount = wgpdao.getValueByName("fupStage3FrameCount");
      exposeDuration = wgpdao.getValueByName("fupStage3ExposeDuration");
      telescope = wgpdao.getValueByName("fupStage3Telescope");
      priority = wgpdao.getValueByName("fupStage3Priority");
    }

    OtLevel2FollowParameter ot2fp = new OtLevel2FollowParameter();
    ot2fp.setDec(sciObj.getPointDec());
    ot2fp.setRa(sciObj.getPointRa());
    ot2fp.setExpTime(Short.parseShort(exposeDuration));
    ot2fp.setFilter(filter);
    ot2fp.setFrameCount(Integer.parseInt(frameCount));
    ot2fp.setTelescope(Short.parseShort(telescope));
    ot2fp.setPriority(Integer.parseInt(priority));
    ot2fp.setOtName(sciObj.getName());
    ot2fp.setUserName("gwac");

    Camera tcam = cameraDao.getById(ot2.getDpmId());
    if (tcam != null) {
      ot2fp.setUserName(tcam.getName());
    }

    UserInfo user = userDao.getUserByLoginName("gwac");
    FollowUpObservation fo = new FollowUpObservation();
    fo.setBackImageCount(0);
    fo.setDec(ot2fp.getDec());
    fo.setEpoch(ot2fp.getEpoch());
    fo.setExposeDuration((short) ot2fp.getExpTime());
    fo.setFoObjCount((short) 0);
    fo.setFrameCount((short) ot2fp.getFrameCount());
    fo.setImageType(ot2fp.getImageType());
    fo.setOtId(ot2.getOtId());
    fo.setPriority((short) ot2fp.getPriority());
    fo.setRa(ot2fp.getRa());
    if (user != null) {
      fo.setUserId(user.getUiId());
    }
    fo.setTriggerTime(new Date());
    fo.setTriggerType('0'); //0:AUTO; 1:MANUAL, 2:PLANNING
    fo.setTelescopeId(ot2fp.getTelescope());
    fo.setBeginTime(fo.getTriggerTime());
    fo.setExecuteStatus('1');
    fo.setProcessResult('0');
    fo.setObjName(ot2fp.getOtName().trim());
    fo.setSoId(sciObj.getSoId());
    fo.setAutoLoop(2);

    if (sciObj.getStatus() == 1) {

      int tnum = fupObsDao.countByObjName(sciObj.getName());

      String[] filterArray = filter.split(",");
      for (int i = 0; i < filterArray.length; i++) {
        tnum++;
        String foName = String.format("%s_%03d", sciObj.getName(), tnum);
        String tf = filterArray[i];
        fo.setFoId(0);
        fo.setFilter(tf);
        fo.setFoName(foName);
        fupObsDao.save(fo);

        ot2fp.setFollowName(foName);
        ot2fp.setFilter(tf);
        MessageCreator tmc = new OTFollowMessageCreator(ot2fp);
        jmsTemplate.send(otFollowDest, tmc);
        log.debug(ot2fp.getTriggerMsg());
      }
      sciObj.setStatus(2);
      sciObj.setFupCount(tnum);
      sciObjDao.update(sciObj);

      ot2.setFoCount((short) tnum);
      ot2Dao.updateFoCount(ot2);
    }
  }

}
