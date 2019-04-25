package com.gwac.action;

import com.gwac.dao.FitsFileCutDAO;
import com.gwac.dao.FitsFileCutRefDAO;
import com.gwac.dao.CrossObjectDao;
import com.gwac.dao.CrossRecordDao;
import com.gwac.dao.OtTypeDao;
import com.gwac.model.FitsFileCut;
import com.gwac.model.FitsFileCutRef;
import com.gwac.model.CrossObject;
import com.gwac.model.OtType;
import com.gwac.model.UserInfo;
import com.gwac.util.CommonFunction;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
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
public class GetCrossObjectDetailJson extends ActionSupport implements SessionAware {

  private static final long serialVersionUID = -3454448234588641394L;
  private static final Log log = LogFactory.getLog(GetCrossObjectDetailJson.class);

  private Map<String, Object> session;

  @Resource
  private CrossObjectDao obDao;
  @Resource
  private CrossRecordDao otorDao;
  @Resource
  private OtTypeDao ottDao;
  /**
   * 查询条件
   */
  private Long coId;
  private Boolean queryHis;
  /**
   * 返回结果
   */
  private CrossObject ot2;
  private String ffcList;
  private FitsFileCutRef ffcRef;
  private String otOpticalVaration;
  private String otxyVaration;
  private String otxyTempVaration;
  private String dataRootWebMap;
  private UserInfo userInfo;
  private List<OtType> otTypes;

  @Actions({
    @Action(value = "/get-crossobj-detail-json", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {

    dataRootWebMap = getText("gwacDataRootDirectoryWebmap");

    if (session.containsKey("userInfo")) {
      userInfo = (UserInfo) session.get("userInfo");
    }

    otTypes = ottDao.findAll();
    List<Integer> tlist = obDao.hisOrCurExist(coId);
    if (!tlist.isEmpty()) {
      Integer his = tlist.get(0);
      queryHis = his == 1;
      ot2 = obDao.getCrossObjectById(coId, queryHis);
      ffcList = otorDao.getCutImageByOtId(coId, queryHis);

      if (ot2 != null) {
	String tmp[] = otorDao.getOtOpticalVaration(ot2, queryHis).split("=");
	otOpticalVaration = tmp[0];
	otxyVaration = tmp[1];
	otxyTempVaration = tmp[2];
      } else {
	otOpticalVaration = "[]";
	otxyVaration = "[]";
	otxyTempVaration = "[]";
      }
    } else {
      ffcList = "";
      otOpticalVaration = "[]";
      otxyVaration = "[]";
      otxyTempVaration = "[]";
    }

    return "json";
  }

  /**
   * @return the ffcList
   */
  public String getFfcList() {
    return ffcList;
  }

  /**
   * @return the otOpticalVaration
   */
  public String getOtOpticalVaration() {
    return otOpticalVaration;
  }

  /**
   * @return the queryHis
   */
  public Boolean getQueryHis() {
    return queryHis;
  }

  /**
   * @param queryHis the queryHis to set
   */
  public void setQueryHis(Boolean queryHis) {
    this.queryHis = queryHis;
  }

  /**
   * @return the ot2
   */
  public CrossObject getOt2() {
    return ot2;
  }

  /**
   * @return the ffcRef
   */
  public FitsFileCutRef getFfcRef() {
    return ffcRef;
  }

  /**
   * @return the dataRootWebMap
   */
  public String getDataRootWebMap() {
    return dataRootWebMap;
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
   * @return the otTypes
   */
  public List<OtType> getOtTypes() {
    return otTypes;
  }

  /**
   * @return the otxyVaration
   */
  public String getOtxyVaration() {
    return otxyVaration;
  }

  /**
   * @return the otxyTempVaration
   */
  public String getOtxyTempVaration() {
    return otxyTempVaration;
  }

  /**
   * @param coId the coId to set
   */
  public void setCoId(Long coId) {
    this.coId = coId;
  }

}
