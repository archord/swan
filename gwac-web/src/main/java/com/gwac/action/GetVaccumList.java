package com.gwac.action;

import com.gwac.dao.CameraVacuumMonitorDao;
import com.gwac.dao.ImageStatusParameterDao;
import com.gwac.util.CommonFunction;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.Result;

@Result(name = "error", location = "/error.jsp")
@ExceptionMapping(exception = "java.lang.Exception", result = "error")
public class GetVaccumList extends ActionSupport {

  private static final long serialVersionUID = -3454448234583441394L;
  private static final Log log = LogFactory.getLog(GetVaccumList.class);

  private int days;
  private String ccd;
  @Resource
  private CameraVacuumMonitorDao dao;
  private String parStr;
  private String minDate;

  @Actions({
    @Action(value = "/get-vaccum-list", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {

    log.debug("days=" + days);

    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - days);
    Date tdate = cal.getTime();
    minDate = CommonFunction.getDateTimeString2(tdate);
    parStr = dao.getRecords(ccd, days);

    return "json";
  }

  /**
   * @return the parStr
   */
  public String getParStr() {
    return parStr;
  }

  /**
   * @return the minDate
   */
  public String getMinDate() {
    return minDate;
  }

  /**
   * @param days the days to set
   */
  public void setDays(int days) {
    this.days = days;
  }

  /**
   * @param ccd the ccd to set
   */
  public void setCcd(String ccd) {
    this.ccd = ccd;
  }

}
