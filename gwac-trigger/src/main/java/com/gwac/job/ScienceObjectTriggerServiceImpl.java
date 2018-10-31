/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import com.gwac.activemq.OTFollowMessageCreator;
import com.gwac.dao.FollowUpObjectDao;
import com.gwac.dao.FollowUpObservationDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.ScienceObjectDao;
import com.gwac.dao.UserInfoDAO;
import com.gwac.dao.WebGlobalParameterDao;
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

    String chatId = "gwac003"; //GWAC_OT_gft_alert 
    Integer fupStage2StartTime = Integer.parseInt(wgpdao.getValueByName("fupStage2StartTime")); //第二次后随距离第一次后随的时间，单位分钟
    Integer fupStage3StartTime = Integer.parseInt(wgpdao.getValueByName("fupStage3StartTime")); //第三次后随距离第一次后随的时间，单位分钟
    Integer fupStage3StopTime = Integer.parseInt(wgpdao.getValueByName("fupStage3StopTime")); //过了fupStage3StopTime时间之后，该目标还未进入到stage3，则该目标很大可能性为假目标，将该目标移除的自动检测后随任务列表
    Float fupStage3MagDiff = Float.parseFloat(wgpdao.getValueByName("fupStage3MagDiff")); //

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
          if (sciObj.getTriggerStatus() == 1) {
            String tmsg = String.format("Auto Trigger 60CM Telescope:\n%s %s in Stage1.\n", sciObj.getName(), sciObj.getType());
            sendMsgService.send(tmsg, chatId);
            sciObj.setTriggerStatus(2);
            sciObjDao.update(sciObj);
          }
          if (diffMinutes > fupStage2StartTime) {
//          List<FollowUpObservation> fupObs = fupObsDao.getBySciObjId(sciObj.getSoId());
//          FollowUpObservation lastFupObs = fupObs.get(fupObs.size() - 1);
//          autoFollowUp(sciObj, lastFupObs.getOtId());
            autoFollowUp(sciObj, ot2.getOtId());
          }
        } else if (sciObj.getStatus() == 2) {
          if (sciObj.getTriggerStatus() == 2) {
            List<FollowUpObject> fupObjs = fupObjDao.getByOtId(ot2.getOtId(), false);
            for (FollowUpObject fupObj : fupObjs) {
              double diffMag = Math.abs(fupObj.getLastMag() - fupObj.getFoundMag());
              if (diffMag > fupStage3MagDiff) {
                log.debug("check " + ot2.getName() + ", diffMag: " + diffMag + ", status: " + sciObj.getStatus());
                String tmsg = String.format("Auto Trigger 60CM Telescope:\n%s %s in Stage2\nusnoRMag:%.2f, firstObsMag:%.2f, lastObsMag:%.2f\n",
                        sciObj.getName(), sciObj.getType(), fupObj.getR2(), fupObj.getFoundMag(), fupObj.getLastMag());
                sendMsgService.send(tmsg, chatId);
                sciObj.setTriggerStatus(3);
                sciObjDao.update(sciObj);
                break;
              }
            }
          }
          if ((diffMinutes > fupStage3StartTime) && (sciObj.getTriggerStatus() == 3)) {
            autoFollowUp(sciObj, ot2.getOtId());
          }else if ((diffMinutes > fupStage3StopTime) && (sciObj.getTriggerStatus() == 2)) {//长时间在阶段2未进入到阶段3，很大可能性为假目标，停止自动观测
            sciObj.setAutoObservation(false);
          }
        } else {
          log.debug(sciObj.getName() + " in stage" + sciObj.getStatus() + " stop AutoObservation.");
          sciObj.setAutoObservation(false);
          sciObjDao.update(sciObj);
        }
      }
    }
  }

  public void autoFollowUp(ScienceObject sciObj, long ot2Id) {

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
    ot2fp.setFollowName(String.format("%s_%03d", sciObj.getName(), sciObj.getFupCount()));
    ot2fp.setDec(sciObj.getPointDec());
    ot2fp.setRa(sciObj.getPointRa());
    ot2fp.setExpTime(Short.parseShort(exposeDuration));
    ot2fp.setFilter(filter);
    ot2fp.setFrameCount(Integer.parseInt(frameCount));
    ot2fp.setTelescope(Short.parseShort(telescope));
    ot2fp.setPriority(Integer.parseInt(priority));
    ot2fp.setOtName(sciObj.getName());
    ot2fp.setUserName("gwac");

    UserInfo user = userDao.getUserByLoginName("gwac");
    FollowUpObservation fo = new FollowUpObservation();
    fo.setBackImageCount(0);
    fo.setDec(ot2fp.getDec());
    fo.setEpoch(ot2fp.getEpoch());
    fo.setExposeDuration((short) ot2fp.getExpTime());
    fo.setFoObjCount((short) 0);
    fo.setFrameCount((short) ot2fp.getFrameCount());
    fo.setImageType(ot2fp.getImageType());
    fo.setOtId(ot2Id);
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

    if (sciObj.getStatus() == 1) {
      sciObj.setStatus(2);
      sciObj.setFupCount(sciObj.getFupCount() + 1);
      sciObjDao.update(sciObj);

      fo.setFilter(ot2fp.getFilter());
      fo.setFoName(String.format("%s_%03d", sciObj.getName(), sciObj.getFupCount()));
      fupObsDao.save(fo);
      MessageCreator tmc = new OTFollowMessageCreator(ot2fp);
      jmsTemplate.send(otFollowDest, tmc);
      log.debug(ot2fp.getTriggerMsg());

    } else if (sciObj.getStatus() == 2) {
      int fupCount = sciObj.getFupCount();
      sciObj.setStatus(3);
      sciObj.setFupCount(sciObj.getFupCount() + 3);
      sciObjDao.update(sciObj);

      String[] filterArray = filter.split(",");
      for (int i = 0; i < filterArray.length; i++) {
        String tf = filterArray[i];
        fo.setFilter(tf);
        fo.setFoName(String.format("%s_%03d", sciObj.getName(), fupCount + i + 1));
        fupObsDao.save(fo);
        MessageCreator tmc = new OTFollowMessageCreator(ot2fp);
        jmsTemplate.send(otFollowDest, tmc);
        log.debug(ot2fp.getTriggerMsg());
      }
    }
  }

}
