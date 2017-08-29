package com.gwac.action;

import com.gwac.dao.DataProcessMachineDAO;
import com.gwac.dao.ObservationSkyDao;
import com.gwac.model.DataProcessMachine;
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
public class GetDpmListJson extends ActionSupport {

  private static final long serialVersionUID = -3454448234583441394L;
  private static final Log log = LogFactory.getLog(GetDpmListJson.class);

  @Resource
  private DataProcessMachineDAO dao;
  private List<DataProcessMachine> objs;

  @Actions({
    @Action(value = "/get-dpm-list-json", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {

    objs = dao.getAllDpms();
    return "json";
  }

  /**
   * @return the objs
   */
  public List<DataProcessMachine> getObjs() {
    return objs;
  }

}
