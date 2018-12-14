/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import com.gwac.activemq.OTFollowMessageCreator;
import com.gwac.dao.CameraDao;
import com.gwac.dao.FollowUpObservationDao;
import com.gwac.dao.Ot2StreamNodeTimeDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.UserInfoDAO;
import com.gwac.dao.WebGlobalParameterDao;
import com.gwac.model.Camera;
import com.gwac.model.FollowUpObservation;
import com.gwac.model.OtLevel2;
import com.gwac.model.UserInfo;
import com.gwac.model4.OtLevel2FollowParameter;
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
 * 解析一级OT列表文件，计算二级OT，切图，模板切图。
 *
 * @author xy
 */
@Service(value = "ot2ReFollowUP")
public class OT2ReFollowUpServiceImpl implements BaseService {

  private static final Log log = LogFactory.getLog(OT2ReFollowUpServiceImpl.class);
  private static boolean running = true;

  @Resource
  private WebGlobalParameterDao webGlobalParameterDao;
  @Resource
  private OtLevel2Dao ot2Dao;
  @Resource
  private FollowUpObservationDao foDao;
  @Resource
  private UserInfoDAO userDao;
  @Resource
  private Ot2StreamNodeTimeDao ot2StreamNodeTimeDao;
  @Resource
  private CameraDao cameraDao;

  @Resource
  private JmsTemplate jmsTemplate;
  @Resource
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
      ot2FollowUp();
    } catch (Exception ex) {
      log.error("Job error", ex);
    } finally {
      if (running == false) {
	running = true;
      }
    }
    long endTime = System.nanoTime();
    double time1 = 1.0 * (endTime - startTime) / 1e9;
    log.debug("job consume: parse image status parameter " + time1 + ".");
  }

  /**
   * 解析图像状态参数文件 StringUtils.isNumericSpace("-12") return false
   * StringUtils.isNumericSpace("12.2") return false
   */
  public void ot2FollowUp() {

    String parmValue = webGlobalParameterDao.getValueByName("AutoFollowUp");
    if (!parmValue.equalsIgnoreCase("true")) {
      return;
    }

    List<OtLevel2> ot2s = ot2Dao.getUnFollowOT2();
    for (OtLevel2 ot2 : ot2s) {

      log.debug("ot2ReFollowUP: " + ot2.getName());

//    if ((ot2.getDataProduceMethod() == '1' && ot2.getIsMatch() == 1)
//            || (ot2.getDataProduceMethod() == '8' && ot2.getIsMatch() == 2 && ot2.getRc3Match() > 0)) {
      if ((ot2.getDataProduceMethod() == '1')) {
	ot2StreamNodeTimeDao.updateLookUpTime(ot2.getOtId());

	ot2.setFoCount((short) (ot2.getFoCount() + 1));
	ot2Dao.updateFoCount(ot2);

	String filter = webGlobalParameterDao.getValueByName("Filter");
	String frameCount = webGlobalParameterDao.getValueByName("FrameCount");
	String exposeDuration = webGlobalParameterDao.getValueByName("ExposeDuration");
	String telescope = webGlobalParameterDao.getValueByName("Telescope");
	String priority = webGlobalParameterDao.getValueByName("Priority");

	OtLevel2FollowParameter ot2fp = new OtLevel2FollowParameter();
	ot2fp.setFollowName(String.format("%s_%03d", ot2.getName(), ot2.getFoCount()));
	ot2fp.setDec(ot2.getDec());
	ot2fp.setRa(ot2.getRa());
	ot2fp.setExpTime(Short.parseShort(exposeDuration));
	ot2fp.setFilter(filter);
	ot2fp.setFrameCount(Integer.parseInt(frameCount));
	ot2fp.setTelescope(Short.parseShort(telescope));
	ot2fp.setPriority(Integer.parseInt(priority));
	ot2fp.setOtName(ot2.getName());

	Camera tcam = cameraDao.getById(ot2.getDpmId());
	if (tcam != null) {
	  ot2fp.setUserName(tcam.getName());
	} else {
	  ot2fp.setUserName("gwac");
	}

	UserInfo user = userDao.getUserByLoginName("gwac");
	FollowUpObservation fo = new FollowUpObservation();
	fo.setBackImageCount(0);
	fo.setDec(ot2fp.getDec());
	fo.setEpoch(ot2fp.getEpoch());
	fo.setExposeDuration((short) ot2fp.getExpTime());
	fo.setFilter(ot2fp.getFilter());
	fo.setFoName(ot2fp.getFollowName());
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
	fo.setAutoLoop(1);
	fo.setLimitMag((float) -1.0);
	foDao.save(fo);

	MessageCreator tmc = new OTFollowMessageCreator(ot2fp);
	jmsTemplate.send(otFollowDest, tmc);
	log.debug(ot2fp.getTriggerMsg());
      }
    }
  }

}
