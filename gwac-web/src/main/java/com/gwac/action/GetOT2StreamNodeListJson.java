package com.gwac.action;

import com.gwac.dao.Ot2StreamNodeTimeDao;
import com.gwac.util.CommonFunction;
import com.opensymphony.xwork2.ActionSupport;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.Result;

@Result(name = "error", location = "/error.jsp")
@ExceptionMapping(exception = "java.lang.Exception", result = "error")
public class GetOT2StreamNodeListJson extends ActionSupport {

  private static final long serialVersionUID = -3454448234583441394L;
  private static final Log log = LogFactory.getLog(GetOT2StreamNodeListJson.class);

  @Resource
  private Ot2StreamNodeTimeDao dao;
  private String parStr;
  private String minDate;

  @Actions({
    @Action(value = "/get-ot2-streamnode-list-json", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {

    parStr = dao.getJson();
    minDate = CommonFunction.getDateTimeString2(dao.getMinDate());

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

}
