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
  @Action(value = "/get-field-ids", results = {
    @Result(name = "success", type = "json")})})
public class GetFieldIds extends ActionSupport {

  private static final long serialVersionUID = 5078264279068565903L;
  private static final Log log = LogFactory.getLog(GetFieldIds.class);
  
  private String groupId;

  @Resource
  private ObservationSkyDao osDao = null;
  private List<String> fieldIds;

  @SuppressWarnings("unchecked")
  public String execute() {

    fieldIds = osDao.getAllSkyName(groupId);

    return SUCCESS;
  }

  /**
   * @param groupId the groupId to set
   */
  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  /**
   * @return the fieldIds
   */
  public List<String> getFieldIds() {
    return fieldIds;
  }


}
