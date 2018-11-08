package com.gwac.action;

import com.gwac.dao.ObservationPlanDao;
import com.opensymphony.xwork2.ActionSupport;
import java.util.*;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;

@Actions({
  @Action(value = "/query-mount-obs-fieldId", results = {
    @Result(name = "success", type = "json")})})
public class QueryMountObsFieldId extends ActionSupport {

  private static final long serialVersionUID = 5078264279068565903L;
  private static final Log log = LogFactory.getLog(QueryMountObsFieldId.class);
  
  private String obsDate;
  private String unitId;

  @Resource
  private ObservationPlanDao dao = null;
  private List<String> fieldIds;

  @SuppressWarnings("unchecked")
  public String execute() {

    fieldIds = dao.getFieldId(obsDate, unitId);

    return SUCCESS;
  }

  /**
   * @return the fieldIds
   */
  public List<String> getFieldIds() {
    return fieldIds;
  }


  /**
   * @param obsDate the obsDate to set
   */
  public void setObsDate(String obsDate) {
    this.obsDate = obsDate;
  }

  /**
   * @param unitId the unitId to set
   */
  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

}
