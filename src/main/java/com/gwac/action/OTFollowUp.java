package com.gwac.action;

import com.gwac.dao.OtLevel2Dao;
import com.gwac.model.OtLevel2;
import com.gwac.model.OtLevel2FollowParameter;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.SessionAware;

/**
 * 目前已经被GetOtLevel2List取代
 *
 * @author xy
 */
@Result(name = "success", type = "json")
public class OTFollowUp extends ActionSupport {

  private static final long serialVersionUID = 1437264279538543593L;
  private static final Log log = LogFactory.getLog(OTFollowUp.class);

  private OtLevel2FollowParameter ot2fp;

  @SuppressWarnings("unchecked")
  public String execute() {
    
    String serverIP = getText("gwac.follow.server.ip");

    return SUCCESS;
  }

}
