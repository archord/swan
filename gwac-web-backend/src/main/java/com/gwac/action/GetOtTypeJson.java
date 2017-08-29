package com.gwac.action;

import com.gwac.dao.OtTypeDao;
import com.gwac.model.OtType;
import com.gwac.model.UserInfo;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.SessionAware;

@Result(name = "error", location = "/error.jsp")
@ExceptionMapping(exception = "java.lang.Exception", result = "error")
public class GetOtTypeJson extends ActionSupport implements SessionAware {

  private static final long serialVersionUID = -3454448234588641394L;
  private static final Log log = LogFactory.getLog(GetOtTypeJson.class);

  private Map<String, Object> session;

  @Resource
  private OtTypeDao ottDao;
  /**
   * 返回结果
   */
  private List<OtType> otTypes;

  @Actions({
    @Action(value = "/get-ot-type-json", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {
        
    otTypes=ottDao.getOtTypes();
    return "json";
  }

  @Override
  public void setSession(Map<String, Object> map) {
    this.session = map;
  }

  /**
   * @return the otTypes
   */
  public List<OtType> getOtTypes() {
    return otTypes;
  }

}
