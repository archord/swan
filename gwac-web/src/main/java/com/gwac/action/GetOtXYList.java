package com.gwac.action;

import com.gwac.dao.DataProcessMachineDAO;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtObserveRecordDAO;
import com.gwac.model.DataProcessMachine;
import com.gwac.model.OtLevel2;
import com.gwac.model.OtObserveRecord;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.util.*;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;

@Actions({
  @Action(value = "/get-ot-xy-list", results = {
    @Result(name = "success", type = "json")})})
public class GetOtXYList extends ActionSupport {

  private static final long serialVersionUID = 5078264279068585793L;
  private static final Log log = LogFactory.getLog(GetOtXYList.class);
  private List<OtObserveRecord> otLv1;
  private List<OtLevel2> otLv2;
  private List<OtLevel2> otLv2Cur;
  private List<OtLevel2> otLv2Mch;
  @Resource
  private OtLevel2Dao otDao = null;
  @Resource
  private OtObserveRecordDAO oorDao = null;
  private List<DataProcessMachine> dpms;
  @Resource
  private DataProcessMachineDAO dpmDao;

  private String dateStr;
  private String dataDisk;
  private float masterUsage;

  @SuppressWarnings("unchecked")
  public String execute() {

    if (dateStr != null && !dateStr.isEmpty()) {
      String queryDate = dateStr.substring(2, 4) + dateStr.substring(5, 7) + dateStr.substring(8);
//      System.out.println("queryDate="+queryDate);
      otLv1 = new ArrayList();
      otLv2 = otDao.getNCurOccurLv2OTByDate(queryDate);
      otLv2Cur = otDao.getCurOccurLv2OTByDate(queryDate);
      otLv2Mch = otDao.getMatchedLv2OTByDate(queryDate);
    } else {
      otLv1 = oorDao.getLatestNLv1OT(40); //40 2000
      otLv2 = otDao.getNCurOccurLv2OT();
      otLv2Cur = otDao.getCurOccurLv2OT();
      otLv2Mch = otDao.getMatchedLv2OT();
    }
    dpms = dpmDao.getAllDpms();
    setMasterUsage();
    return SUCCESS;
  }

  public void setMasterUsage() {
    if (dataDisk != null && !dataDisk.isEmpty()) {
      File file = new File(dataDisk);
      if (file.exists()) {
        long totalSpace = file.getTotalSpace(); //total disk space in bytes.
        long freeSpace = file.getFreeSpace(); //unallocated / free disk space in bytes.
        masterUsage = (float) 1.0 * (totalSpace - freeSpace) / totalSpace;
      }else{
        masterUsage = (float) 1.0;
      }
    } else {
      masterUsage = (float) 1.0;
    }
  }

  /**
   * @return the otLv1
   */
  public List<OtObserveRecord> getOtLv1() {
    return otLv1;
  }

  /**
   * @param otLv1 the otLv1 to set
   */
  public void setOtLv1(List<OtObserveRecord> otLv1) {
    this.otLv1 = otLv1;
  }

  /**
   * @return the otLv2
   */
  public List<OtLevel2> getOtLv2() {
    return otLv2;
  }

  /**
   * @param otLv2 the otLv2 to set
   */
  public void setOtLv2(List<OtLevel2> otLv2) {
    this.otLv2 = otLv2;
  }

  /**
   * @return the otLv2Cur
   */
  public List<OtLevel2> getOtLv2Cur() {
    return otLv2Cur;
  }

  /**
   * @param otLv2Cur the otLv2Cur to set
   */
  public void setOtLv2Cur(List<OtLevel2> otLv2Cur) {
    this.otLv2Cur = otLv2Cur;
  }

  /**
   * @return the dpms
   */
  public List<DataProcessMachine> getDpms() {
    return dpms;
  }

  /**
   * @param dpms the dpms to set
   */
  public void setDpms(List<DataProcessMachine> dpms) {
    this.dpms = dpms;
  }

  /**
   * @return the masterUsage
   */
  public float getMasterUsage() {
    return masterUsage;
  }

  /**
   * @param masterUsage the masterUsage to set
   */
  public void setMasterUsage(float masterUsage) {
    this.masterUsage = masterUsage;
  }

  /**
   * @param dataDisk the dataDisk to set
   */
  public void setDataDisk(String dataDisk) {
    this.dataDisk = dataDisk;
  }

  /**
   * @param dateStr the dateStr to set
   */
  public void setDateStr(String dateStr) {
    this.dateStr = dateStr;
  }

  /**
   * @return the otLv2Mch
   */
  public List<OtLevel2> getOtLv2Mch() {
    return otLv2Mch;
  }
}
