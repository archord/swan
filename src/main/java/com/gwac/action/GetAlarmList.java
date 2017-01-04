package com.gwac.action;

import com.gwac.dao.MultimediaResourceDao;
import com.gwac.model.MultimediaResource;
import com.gwac.model.UserInfo;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.SessionAware;

@Result(name = "error", location = "/error.jsp")
@ExceptionMapping(exception = "java.lang.Exception", result = "error")
public class GetAlarmList extends ActionSupport {

  private static final long serialVersionUID = -3454448234588641394L;
  private static final Log log = LogFactory.getLog(GetAlarmList.class);

  private MultimediaResourceDao mrDao;
  /**
   * 返回结果
   */
  private List<MultimediaResource> multimediaResources;

  @Actions({
    @Action(value = "/get-ot-alarm", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {

    multimediaResources = mrDao.findAll();
    return "json";
  }

  /**
   * @param mrDao the mrDao to set
   */
  public void setMrDao(MultimediaResourceDao mrDao) {
    this.mrDao = mrDao;
  }

  /**
   * @return the multimediaResources
   */
  public List<MultimediaResource> getMultimediaResources() {
    return multimediaResources;
  }

}
