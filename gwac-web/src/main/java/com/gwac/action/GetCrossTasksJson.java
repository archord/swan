package com.gwac.action;

import com.gwac.dao.CrossTaskDao;
import com.gwac.model.CrossTask;
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
public class GetCrossTasksJson extends ActionSupport {

  private static final long serialVersionUID = -3454448234588641394L;
  private static final Log log = LogFactory.getLog(GetCrossTasksJson.class);

  @Resource
  private CrossTaskDao dao;
  /**
   * 返回结果
   */
  private String dateStr;
  private List<CrossTask> objs;

  @Actions({
    @Action(value = "/get-cross-task-json", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {
        
    objs = dao.getObjects(dateStr);
    return "json";
  }


  /**
   * @param dateStr the dateStr to set
   */
  public void setDateStr(String dateStr) {
    this.dateStr = dateStr;
  }

  public List<CrossTask> getObjs() {
    return objs;
  }
}
