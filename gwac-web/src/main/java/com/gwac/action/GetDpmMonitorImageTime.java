package com.gwac.action;

import com.gwac.dao.DataProcessMachineDAO;
import com.gwac.model.DataProcessMachine;
import com.opensymphony.xwork2.ActionSupport;
import java.util.*;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.SessionAware;

@Actions({
  @Action(value = "/get-dpm-monitor-image-time", results = {
    @Result(name = "success", type = "json")})})
public class GetDpmMonitorImageTime extends ActionSupport implements SessionAware {

  private static final long serialVersionUID = 1358264279068585793L;
  private static final Log log = LogFactory.getLog(GetDpmMonitorImageTime.class);
  private Map<String, Object> session;
  private List<DataProcessMachine> dpms;
  @Resource
  private DataProcessMachineDAO dpmDao;

  @SuppressWarnings("unchecked")
  public String execute() {
    setDpms(dpmDao.findAll());
    return SUCCESS;
  }

  public void setSession(Map<String, Object> session) {
    this.session = session;
  }

  /**
   * @param dpms the dpms to set
   */
  public void setDpms(List<DataProcessMachine> dpms) {
    this.dpms = dpms;
  }

  /**
   * @return the dpms
   */
  public List<DataProcessMachine> getDpms() {
    return dpms;
  }

}
