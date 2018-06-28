/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

import com.gwac.dao.FollowUpFitsfileDao;
import com.gwac.dao.FollowUpObjectDao;
import com.gwac.dao.FollowUpObjectTypeDao;
import com.gwac.dao.FollowUpRecordDao;
import com.gwac.model.FollowUpFitsfile;
import com.gwac.model.FollowUpObject;
import com.gwac.model.FollowUpObjectType;
import com.gwac.model.FollowUpRecord;
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

/**
 *
 * @author xy
 */
@Result(name = "error", location = "/error.jsp")
@ExceptionMapping(exception = "java.lang.Exception", result = "error")
public class GetFollowUpObservationDetail extends ActionSupport {
  
  private static final long serialVersionUID = -3459574234588641124L;
  private static final Log log = LogFactory.getLog(GetFollowUpObservationDetail.class);

  @Resource
  private FollowUpFitsfileDao fufDao;
  @Resource
  private FollowUpRecordDao furDao;
  @Resource
  private FollowUpObjectTypeDao fuotDao;
  @Resource
  private FollowUpObjectDao fuoDao;

  private String foName;
  private List<Map<String, Object>> fitsList;
  private List<FollowUpObjectType> fuots;
  private List<FollowUpObject> fuos;

  @Actions({
    @Action(value = "/get-followup-obs-detail", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {
    
    log.debug("foName="+foName);

    String dataRootWebMap = getText("gwacDataRootDirectoryWebmap");
    fuots = fuotDao.getOtTypes();

    fitsList = new ArrayList();
    List<FollowUpFitsfile> fufs = fufDao.getByFoName(foName);
    fuos = fuoDao.getByFoName(foName, Boolean.TRUE);
    for (FollowUpFitsfile tfuf : fufs) {
      Map<String, Object> fufMap = new HashMap<>();
      fufMap.put("path", dataRootWebMap + "/" + tfuf.getFfPath() + "/");
      fufMap.put("fileName", tfuf.getFfName());
      List<FollowUpRecord> furs = furDao.getByFufId(tfuf.getFufId(), Boolean.TRUE);
      fufMap.put("records", furs);
      fitsList.add(fufMap);
    }
    return "json";
  }

  /**
   * @param foName the otName to set
   */
  public void setFoName(String foName) {
    this.foName = foName;
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
