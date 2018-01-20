package com.gwac.action;

import com.gwac.activemq.OTFollowMessageCreator;
import com.gwac.dao.FollowUpObservationDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtNumberDao;
import com.gwac.model.FollowUpObservation;
import com.gwac.model.OtLevel2;
import com.gwac.model4.OtLevel2FollowParameter;
import com.gwac.model.UserInfo;
import com.gwac.util.CommonFunction;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.util.*;
import javax.annotation.Resource;
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
@Action(value = "deleteOtFollowUp", results = {
  @Result(name = "success", type = "json")})
public class DeletOTFollowUp extends ActionSupport implements SessionAware {

  private static final long serialVersionUID = 1432664279539543517L;
  private static final Log log = LogFactory.getLog(DeletOTFollowUp.class);

  private Map<String, Object> session;
  private String foIds;
  private String result;

  @Resource
  private FollowUpObservationDao foDao;

  @Resource
  private JmsTemplate jmsTemplate;
  @Resource(name = "otFollowDest")
  private Destination otFollowDest;

  @SuppressWarnings("unchecked")
  public String execute() {

    if (foIds != null) {
      foDao.deleteByIds(foIds);
      result = "success!";
    } else {
      result = "foIds is empty!";
    }

    return SUCCESS;
  }

  @Override
  public void setSession(Map<String, Object> map) {
    this.session = map;
  }

  /**
   * @param foIds the foIds to set
   */
  public void setFoIds(String foIds) {
    this.foIds = foIds;
  }

  /**
   * @return the result
   */
  public String getResult() {
    return result;
  }

}
