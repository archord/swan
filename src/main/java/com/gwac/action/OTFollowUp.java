package com.gwac.action;

import com.gwac.activemq.OTFollowMessageCreator;
import com.gwac.activemq.OTListMessageCreator;
import com.gwac.dao.FollowUpObservationDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.UserInfoDAO;
import com.gwac.model.FollowUpObservation;
import com.gwac.model.OtLevel2;
import com.gwac.model.OtLevel2FollowParameter;
import com.gwac.model.UserInfo;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.util.*;
import javax.jms.Destination;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * 目前已经被GetOtLevel2List取代
 *
 * @author xy
 */
@Action(value = "otFollowUp", results = {
  @Result(name = "success", type = "json")})
public class OTFollowUp extends ActionSupport implements SessionAware {

  private static final long serialVersionUID = 1437264279538543593L;
  private static final Log log = LogFactory.getLog(OTFollowUp.class);

  private Map<String, Object> session;

  private String result = "";
  private OtLevel2FollowParameter ot2fp;

  private FollowUpObservationDao foDao;
  private OtLevel2Dao ot2Dao;
  private UserInfoDAO userDao;
  private JmsTemplate jmsTemplate;
  private Destination otFollowDest;

  @SuppressWarnings("unchecked")
  public String execute() {

    if (ot2fp != null) {
      
      UserInfo user = userDao.getUserByLoginName(ot2fp.getUserName());
      if (user != null) {
        OtLevel2 ot2 = ot2Dao.getOtLevel2ByName(ot2fp.getOtName(), false);
        ot2.setFoCount((short) (ot2.getFoCount() + 1));
//      ot2Dao.update(ot2);
        ot2Dao.updateFoCount(ot2);
        ot2fp.setFollowName(String.format("%s_%03d", ot2fp.getOtName(), ot2.getFoCount()));

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
        fo.setUserId(user.getUiId());
        fo.setTriggerTime(new Date());
        fo.setTriggerType("MANUAL"); //MANUAL AUTO
        fo.setTelescopeId(ot2fp.getTelescope());
        foDao.save(fo);

        MessageCreator tmc = new OTFollowMessageCreator(ot2fp);
        jmsTemplate.send(otFollowDest, tmc);
        log.debug(getOt2fp().getTriggerMsg());
        setResult("success!");
      } else {
        setResult("cannot followup, please login!");
      }
    }

    return SUCCESS;
  }

  /**
   * @param foDao the foDao to set
   */
  public void setFoDao(FollowUpObservationDao foDao) {
    this.foDao = foDao;
  }

  /**
   * @param ot2Dao the ot2Dao to set
   */
  public void setOt2Dao(OtLevel2Dao ot2Dao) {
    this.ot2Dao = ot2Dao;
  }

  /**
   * @param userDao the userDao to set
   */
  public void setUserDao(UserInfoDAO userDao) {
    this.userDao = userDao;
  }

  @Override
  public void setSession(Map<String, Object> map) {
    this.session = session;
  }

  /**
   * @return the ot2fp
   */
  public OtLevel2FollowParameter getOt2fp() {
    return ot2fp;
  }

  /**
   * @return the result
   */
  public String getResult() {
    return result;
  }

  /**
   * @param result the result to set
   */
  public void setResult(String result) {
    this.result = result;
  }

  /**
   * @param ot2fp the ot2fp to set
   */
  public void setOt2fp(OtLevel2FollowParameter ot2fp) {
    this.ot2fp = ot2fp;
  }

  /**
   * @param jmsTemplate the jmsTemplate to set
   */
  public void setJmsTemplate(JmsTemplate jmsTemplate) {
    this.jmsTemplate = jmsTemplate;
  }

  /**
   * @param otFollowDest the otFollowDest to set
   */
  public void setOtFollowDest(Destination otFollowDest) {
    this.otFollowDest = otFollowDest;
  }

}
