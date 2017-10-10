package com.gwac.action;

import com.gwac.dao.ObservationSkyDao;
import com.opensymphony.xwork2.ActionSupport;
import java.util.*;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;

@Actions({
  @Action(value = "/get-group-ids", results = {
    @Result(name = "success", type = "json")})})
public class GetGroupIds extends ActionSupport {

  private static final long serialVersionUID = 5078264279068565903L;
  private static final Log log = LogFactory.getLog(GetGroupIds.class);

  @Resource
  private ObservationSkyDao osDao = null;
  private List<String> groupIds;

  @SuppressWarnings("unchecked")
  public String execute() {

    groupIds = osDao.getAllGridId();

    return SUCCESS;
  }

  /**
   * @return the groupIds
   */
  public List<String> getGroupIds() {
    return groupIds;
  }


}
