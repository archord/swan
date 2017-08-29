package com.gwac.action;

import com.gwac.dao.FollowUpFitsfileDao;
import com.gwac.dao.FollowUpObjectDao;
import com.gwac.dao.FollowUpObjectTypeDao;
import com.gwac.dao.FollowUpRecordDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.model.FollowUpFitsfile;
import com.gwac.model.FollowUpObject;
import com.gwac.model.FollowUpObjectType;
import com.gwac.model.FollowUpRecord;
import com.gwac.model.OtLevel2;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.Result;

@Result(name = "error", location = "/error.jsp")
@ExceptionMapping(exception = "java.lang.Exception", result = "error")
public class ShowFollowupFitsList extends ActionSupport {

  private static final long serialVersionUID = -3454274234588641394L;
  private static final Log log = LogFactory.getLog(ShowFollowupFitsList.class);

  @Resource
  private OtLevel2Dao obDao;
  @Resource
  private FollowUpFitsfileDao fufDao;
  @Resource
  private FollowUpRecordDao furDao;
  @Resource
  private FollowUpObjectTypeDao fuotDao;
  @Resource
  private FollowUpObjectDao fuoDao;

  private String otName;
  private List<Map<String, Object>> fitsList;
  private List<FollowUpObjectType> fuots;
  private List<FollowUpObject> fuos;

  @Actions({
    @Action(value = "/get-followup-fits-list", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {

    String dataRootWebMap = getText("gwacDataRootDirectoryWebmap");
    fuots = fuotDao.getOtTypes();

    fitsList = new ArrayList();
    List<Integer> tlist = obDao.hisOrCurExist(otName);
    if (!tlist.isEmpty()) {
      Integer his = tlist.get(0);
      Boolean queryHis = his == 1;
      OtLevel2 ot2 = obDao.getOtLevel2ByName(otName, queryHis);
      List<FollowUpFitsfile> fufs = fufDao.getByOtId(ot2.getOtId());
      fuos = fuoDao.getByOtId(ot2.getOtId(), queryHis);
      for (FollowUpFitsfile tfuf : fufs) {
        Map<String, Object> fufMap = new HashMap<>();
        fufMap.put("path", dataRootWebMap + "/" + tfuf.getFfPath() + "/");
        fufMap.put("fileName", tfuf.getFfName());
        List<FollowUpRecord> furs = furDao.getByFufId(tfuf.getFufId(), queryHis);
        fufMap.put("records", furs);
        fitsList.add(fufMap);
      }
    }else{
      fuos = new ArrayList();
    }

    return "json";
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

  /**
   * @return the fuos
   */
  public List<FollowUpObject> getFuos() {
    return fuos;
  }

}
