package com.gwac.action;

import com.gwac.dao.FitsFileCutDAO;
import com.gwac.dao.FitsFileCutRefDAO;
import com.gwac.dao.FollowUpFitsfileDao;
import com.gwac.dao.FollowUpObjectTypeDao;
import com.gwac.dao.FollowUpRecordDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtObserveRecordDAO;
import com.gwac.dao.OtTypeDao;
import com.gwac.model.FitsFileCut;
import com.gwac.model.FitsFileCutRef;
import com.gwac.model.FollowUpFitsfile;
import com.gwac.model.FollowUpObjectType;
import com.gwac.model.FollowUpRecord;
import com.gwac.model.OtLevel2;
import com.gwac.model.OtType;
import com.gwac.model.UserInfo;
import com.gwac.util.CommonFunction;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.HashMap;
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
public class ShowFollowupFitsList extends ActionSupport {

  private static final long serialVersionUID = -3454274234588641394L;
  private static final Log log = LogFactory.getLog(ShowFollowupFitsList.class);

  private OtLevel2Dao obDao;
  private FollowUpFitsfileDao fufDao;
  private FollowUpRecordDao furDao;
  private FollowUpObjectTypeDao fuotDao;

  private String otName;
  private List<Map<String, Object>> fitsList;
  private List<FollowUpObjectType> fuots;

  @Actions({
    @Action(value = "/get-followup-fits-list", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {

    String dataRootWebMap = getText("gwac.data.root.directory.webmap");
    fuots = fuotDao.getOtTypes();

    fitsList = new ArrayList();
    List<Integer> tlist = obDao.hisOrCurExist(otName);
    if (!tlist.isEmpty()) {
      Integer his = tlist.get(0);
      Boolean queryHis = his == 1;
      OtLevel2 ot2 = obDao.getOtLevel2ByName(otName, queryHis);
      List<FollowUpFitsfile> fufs = fufDao.getByOtId(ot2.getOtId());
      for (FollowUpFitsfile tfuf : fufs) {
        Map<String, Object> fufMap = new HashMap<>();
        fufMap.put("path", dataRootWebMap + "/" + tfuf.getFfPath() + "/");
        fufMap.put("fileName", tfuf.getFfName());
        List<FollowUpRecord> furs = furDao.getByFufId(tfuf.getFufId(), queryHis);
        fufMap.put("records", furs);
        fitsList.add(fufMap);
      }
    }

    return "json";
  }

  /**
   * @param obDao the obDao to set
   */
  public void setObDao(OtLevel2Dao obDao) {
    this.obDao = obDao;
  }

  /**
   * @param fufDao the fufDao to set
   */
  public void setFufDao(FollowUpFitsfileDao fufDao) {
    this.fufDao = fufDao;
  }

  /**
   * @param furDao the furDao to set
   */
  public void setFurDao(FollowUpRecordDao furDao) {
    this.furDao = furDao;
  }

  /**
   * @param fuotDao the fuotDao to set
   */
  public void setFuotDao(FollowUpObjectTypeDao fuotDao) {
    this.fuotDao = fuotDao;
  }

  /**
   * @param otName the otName to set
   */
  public void setOtName(String otName) {
    this.otName = otName;
  }

  /**
   * @return the results
   */
  public List<Map<String, Object>> getFitsList() {
    return fitsList;
  }

  /**
   * @return the fuots
   */
  public List<FollowUpObjectType> getFuots() {
    return fuots;
  }

}
