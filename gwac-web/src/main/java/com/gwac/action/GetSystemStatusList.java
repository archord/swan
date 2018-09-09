package com.gwac.action;

import com.gwac.dao.SystemStatusMonitorDao;
import com.gwac.linefind.CommonFunction;
import com.gwac.model.SystemStatusMonitor;
import com.opensymphony.xwork2.ActionSupport;
import java.util.*;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;

@Actions({
  @Action(value = "/get-system-status", results = {
    @Result(name = "success", type = "json")})})
public class GetSystemStatusList extends ActionSupport {

  private static final long serialVersionUID = 3073694279058510593L;
  private static final Log log = LogFactory.getLog(GetSystemStatusList.class);

  private List<SystemStatusMonitor> datas;
  private String nowDateStr;
  @Resource
  private SystemStatusMonitorDao ssmDao = null;

  @SuppressWarnings("unchecked")
  public String execute() {

    log.debug("get SystemStatusMonitor");

    datas = ssmDao.getAllStatus();
    nowDateStr = CommonFunction.getDateTimeString2(new Date());

    return SUCCESS;
  }

  /**
   * @return the datas
   */
  public List<SystemStatusMonitor> getDatas() {
    return datas;
  }

  /**
   * @return the nowDateStr
   */
  public String getNowDateStr() {
    return nowDateStr;
  }

}
