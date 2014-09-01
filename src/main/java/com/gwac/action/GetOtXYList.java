package com.gwac.action;

import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtObserveRecordDAO;
import com.gwac.model.OtLevel2;
import com.gwac.model.OtObserveRecord;
import com.opensymphony.xwork2.ActionSupport;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.SessionAware;

@Actions({
  @Action(value = "/get-ot-xy-list", results = {
    @Result(name = "success", type = "json")})})
public class GetOtXYList extends ActionSupport implements SessionAware {

  private static final long serialVersionUID = 5078264279068585793L;
  private static final Log log = LogFactory.getLog(GetOtXYList.class);
  private Map<String, Object> session;
  private List<OtObserveRecord> otLv1;
  private List<OtLevel2> otLv2;
  private List<OtLevel2> otLv2Cur;
  private OtLevel2Dao otDao = null;
  private OtObserveRecordDAO oorDao = null;

  @SuppressWarnings("unchecked")
  public String execute() {

    otLv1 = oorDao.getLatestNLv1OT(40);
    otLv2 = otDao.getNCurOccurLv2OT();
    otLv2Cur = otDao.getCurOccurLv2OT();
    System.out.println("otLv1:"+otLv1.size());
    System.out.println("otLv2:"+otLv2.size());
    System.out.println("otLv2Cur:"+otLv2Cur.size());
    return SUCCESS;
  }

  public void setSession(Map<String, Object> session) {
    this.session = session;
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
   * @param otDao the otDao to set
   */
  public void setOtDao(OtLevel2Dao otDao) {
    this.otDao = otDao;
  }

  /**
   * @param oorDao the oorDao to set
   */
  public void setOorDao(OtObserveRecordDAO oorDao) {
    this.oorDao = oorDao;
  }
}
