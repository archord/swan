package com.gwac.action;

import com.gwac.dao.FitsFileCutDAO;
import com.gwac.dao.FitsFileCutRefDAO;
import com.gwac.dao.OtObserveRecordDAO;
import com.gwac.dao.OtTmplWrongDao;
import com.gwac.dao.OtTypeDao;
import com.gwac.model.FitsFileCut;
import com.gwac.model.FitsFileCutRef;
import com.gwac.model.OtTmplWrong;
import com.gwac.model.OtType;
import com.gwac.model.UserInfo;
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
public class GetOtHistoryDetailJson extends ActionSupport implements SessionAware {

  private static final long serialVersionUID = -3454448234588641394L;
  private static final Log log = LogFactory.getLog(GetOtHistoryDetailJson.class);

  private Map<String, Object> session;

  @Resource
  private FitsFileCutDAO ffcDao;
  @Resource
  private FitsFileCutRefDAO ffcrDao;
  @Resource
  private OtTmplWrongDao ot2tmplDao;
  @Resource
  private OtObserveRecordDAO otorDao;
  @Resource
  private OtTypeDao ottDao;
  /**
   * 查询条件
   */
  private String otName;
  private Boolean queryHis;
  /**
   * 返回结果
   */
  private OtTmplWrong ot2Tmpl;
  private List<FitsFileCut> ffcList;
  private FitsFileCutRef ffcRef;
  private String otOpticalVaration;
  private String dataRootWebMap;
  private UserInfo userInfo;
  private List<OtType> otTypes;

  @Actions({
    @Action(value = "/get-ot-history-detail-json", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {

    dataRootWebMap = getText("gwacDataRootDirectoryWebmap");

    if (otName == null || otName.isEmpty()) {
      ffcList = new ArrayList();
      return "json";
    }

    if (session.containsKey("userInfo")) {
      userInfo = (UserInfo) session.get("userInfo");
    }

    queryHis = true;
    otTypes = ottDao.findAll();

    ot2Tmpl = ot2tmplDao.getOtTmplByName(otName);
    if (ot2Tmpl != null) {
      ffcList = ffcDao.getCutImageByOtId(ot2Tmpl.getOtId(), queryHis);

      List<FitsFileCutRef> ffcrs = ffcrDao.getCutImageByOtId(ot2Tmpl.getOtId());
      if (ffcrs != null && ffcrs.size() > 0) {
        ffcRef = ffcrs.get(0);
      }
      otOpticalVaration = otorDao.getOt2TmplOpticalVaration(ot2Tmpl);
    } else {
      ffcList = new ArrayList();
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
   * @return the otName
   */
  public String getOtName() {
    return otName;
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
   * @param otName the otName to set
   */
  public void setOtName(String otName) {
    this.otName = otName;
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
   * @return the ot2Tmpl
   */
  public OtTmplWrong getOt2Tmpl() {
    return ot2Tmpl;
  }

}
