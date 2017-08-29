package com.gwac.action;

import com.gwac.dao.ObservationSkyDao;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.Result;

@Result(name = "error", location = "/error.jsp")
@ExceptionMapping(exception = "java.lang.Exception", result = "error")
public class GetSkyListJson extends ActionSupport {

  private static final long serialVersionUID = -3454448234583441394L;
  private static final Log log = LogFactory.getLog(GetSkyListJson.class);

  @Resource
  private ObservationSkyDao dao;
  private List<String> objs;

  @Actions({
    @Action(value = "/get-sky-list-json", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {

    objs = dao.getAllSkyName();
    return "json";
  }

  /**
   * @return the objs
   */
  public List<String> getObjs() {
    return objs;
  }

}
