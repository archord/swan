package com.gwac.action;

import com.gwac.dao.MoveObjectDao;
import com.gwac.dao.OtObserveRecordDAO;
import com.opensymphony.xwork2.ActionSupport;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;

@Actions({
  @Action(value = "/get-mov-ot-sequence-list2", results = {
    @Result(name = "success", type = "json")})})
public class GetMovOtSequence2 extends ActionSupport {

  private static final long serialVersionUID = 5078264279068585793L;
  private static final Log log = LogFactory.getLog(GetMovOtSequence2.class);
  private final Calendar cal = Calendar.getInstance();

  private String dateStr;

  private OtObserveRecordDAO oorDao = null;
  private MoveObjectDao movObjDao = null;
  private String motList;
  private String ot1List;
  private Date minDate;
  private Date maxDate;
  private int minNum;
  private int maxNum;
  private int fromSecond;
  private int toSecond;
  private int reqTime;

  @SuppressWarnings("unchecked")
  @Override
  public String execute() {

    Object[] objs = oorDao.getMinMaxDateOt12(dateStr);
    if (objs != null && objs.length == 2 && objs[0] != null && objs[1] != null) {
      minDate = (Date) objs[0];
      maxDate = (Date) objs[1];
      minNum = 0;
      maxNum = (int) Math.ceil((maxDate.getTime() - minDate.getTime()) / 1000);

      cal.setTime(minDate);
      cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + reqTime);
      Date fromDate = cal.getTime();
      fromSecond = (int) Math.ceil((fromDate.getTime() - minDate.getTime()) / 1000);
      cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 24);
      Date toDate = cal.getTime();
      toSecond = (int) Math.ceil((toDate.getTime() - minDate.getTime()) / 1000);
      motList = movObjDao.getMoveObjsByDate(dateStr, fromDate, toDate);
      ot1List = "";
//      ot1List = movObjDao.getNotMatchOTByDate(dateStr);

      if (reqTime == 0) {
        reqTime = 1;
      }
    } else {
      minDate = null;
      maxDate = null;
      minNum = 0;
      maxNum = 0;
      motList = "";
      ot1List = "";
    }

    return SUCCESS;
  }

  /**
   * @param movObjDao the movObjDao to set
   */
  public void setMovObjDao(MoveObjectDao movObjDao) {
    this.movObjDao = movObjDao;
  }

  /**
   * @return the motList
   */
  public String getMotList() {
    return motList;
  }

  /**
   * @return the ot1List
   */
  public String getOt1List() {
    return ot1List;
  }

  /**
   * @param dateStr the dateStr to set
   */
  public void setDateStr(String dateStr) {
    this.dateStr = dateStr;
  }

  /**
   * @param oorDao the oorDao to set
   */
  public void setOorDao(OtObserveRecordDAO oorDao) {
    this.oorDao = oorDao;
  }

  /**
   * @return the minDate
   */
  public Date getMinDate() {
    return minDate;
  }

  /**
   * @return the maxDate
   */
  public Date getMaxDate() {
    return maxDate;
  }

  /**
   * @return the minNum
   */
  public int getMinNum() {
    return minNum;
  }

  /**
   * @return the maxNum
   */
  public int getMaxNum() {
    return maxNum;
  }

  /**
   * @return the fromSecond
   */
  public int getFromSecond() {
    return fromSecond;
  }

  /**
   * @return the toSecond
   */
  public int getToSecond() {
    return toSecond;
  }

  /**
   * @return the reqTime
   */
  public int getReqTime() {
    return reqTime;
  }

}
