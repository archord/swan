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
public class GetOtDetailJson extends ActionSupport implements SessionAware {

  private static final long serialVersionUID = -3454448234588641394L;
  private static final Log log = LogFactory.getLog(GetOtDetailJson.class);

  private Map<String, Object> session;

  private FitsFileCutDAO ffcDao;
  private FitsFileCutRefDAO ffcrDao;
  private OtLevel2Dao obDao;
  private OtObserveRecordDAO otorDao;
  private OtTypeDao ottDao;
  /**
   * 查询条件
   */
  private String otName;
  private Boolean queryHis;
  /**
   * 返回结果
   */
  private OtLevel2 ot2;
  private List<FitsFileCut> ffcList;
  private FitsFileCutRef ffcRef;
  private String otOpticalVaration;
  private String otPositionVaration;
  private String dataRootWebMap;
  private UserInfo userInfo;
  private List<OtType> otTypes;

  @Actions({
    @Action(value = "/get-ot-detail-json", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {

    dataRootWebMap = getText("gwac.data.root.directory.webmap");
        
    if(session.containsKey("userInfo")){
      userInfo = (UserInfo)session.get("userInfo");
    }

    otTypes=ottDao.findAll();
    List<Integer> tlist = obDao.hisOrCurExist(otName);
    if (!tlist.isEmpty()) {
      Integer his = tlist.get(0);
      queryHis = his == 1;
      ot2 = obDao.getOtLevel2ByName(getOtName(), queryHis);
      ffcList = ffcDao.getCutImageByOtId(getOt2().getOtId(), queryHis);

      List<FitsFileCutRef> ffcrs = ffcrDao.getCutImageByOtId(getOt2().getOtId());
      if (ffcrs != null && ffcrs.size() > 0) {
        ffcRef = ffcrs.get(0);
      }
      String tmp[] = otorDao.getOtOpticalVaration(ot2, queryHis).split("=");
      otOpticalVaration = tmp[0];
      otPositionVaration = tmp[1];
    } else {
      ffcList = new ArrayList();
      otOpticalVaration = "[]";
      otPositionVaration = "[]";
    }

    return "json";
  }

  /**
   * @return the ffcList
   */
  public List<FitsFileCut> getFfcList() {
    return ffcList;
  }

  /**
   * @param ffcDao the ffcDao to set
   */
  public void setFfcDao(FitsFileCutDAO ffcDao) {
    this.ffcDao = ffcDao;
  }

  /**
   * @return the otName
   */
  public String getOtName() {
    return otName;
  }

  /**
   * @param obDao the obDao to set
   */
  public void setObDao(OtLevel2Dao obDao) {
    this.obDao = obDao;
  }

  /**
   * @param ffcrDao the ffcrDao to set
   */
  public void setFfcrDao(FitsFileCutRefDAO ffcrDao) {
    this.ffcrDao = ffcrDao;
  }

  /**
   * @return the otOpticalVaration
   */
  public String getOtOpticalVaration() {
    return otOpticalVaration;
  }

  /**
   * @param otorDao the otorDao to set
   */
  public void setOtorDao(OtObserveRecordDAO otorDao) {
    this.otorDao = otorDao;
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
   * @return the otPositionVaration
   */
  public String getOtPositionVaration() {
    return otPositionVaration;
  }

  /**
   * @param otName the otName to set
   */
  public void setOtName(String otName) {
    this.otName = otName;
  }

  /**
   * @return the ot2
   */
  public OtLevel2 getOt2() {
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
