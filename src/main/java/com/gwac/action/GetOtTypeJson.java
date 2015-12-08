package com.gwac.action;

import com.gwac.dao.FitsFileCutDAO;
import com.gwac.dao.FitsFileCutRefDAO;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtObserveRecordDAO;
import com.gwac.dao.OtTypeDao;
import com.gwac.model.FitsFileCut;
import com.gwac.model.FitsFileCutRef;
import com.gwac.model.OtLevel2;
import com.gwac.model.OtType;
import com.gwac.model.UserInfo;
import com.gwac.util.CommonFunction;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
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
public class GetOtTypeJson extends ActionSupport implements SessionAware {

  private static final long serialVersionUID = -3454448234588641394L;
  private static final Log log = LogFactory.getLog(GetOtTypeJson.class);

  private Map<String, Object> session;

  private OtTypeDao ottDao;
  /**
   * 返回结果
   */
  private UserInfo userInfo;
  private List<OtType> otTypes;

  @Actions({
    @Action(value = "/get-ot-type-json", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {
        
    if(session.containsKey("userInfo")){
      userInfo = (UserInfo)session.get("userInfo");
    }
    otTypes=ottDao.getOtTypes();
    return "json";
  }

  @Override
  public void setSession(Map<String, Object> map) {
    this.session = map;
  }

  /**
   * @return the userInfo
   */
  public UserInfo getUserInfo() {
    return userInfo;
  }

  /**
   * @param ottDao the ottDao to set
   */
  public void setOttDao(OtTypeDao ottDao) {
    this.ottDao = ottDao;
  }

  /**
   * @return the otTypes
   */
  public List<OtType> getOtTypes() {
    return otTypes;
  }

}
