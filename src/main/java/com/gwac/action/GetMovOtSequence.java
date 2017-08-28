package com.gwac.action;

import com.gwac.dao.MoveObjectDao;
import com.gwac.dao.OtObserveRecordDAO;
import com.opensymphony.xwork2.ActionSupport;
import java.util.*;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;

@Actions({
  @Action(value = "/get-mov-ot-sequence-list", results = {
    @Result(name = "success", type = "json")})})
public class GetMovOtSequence extends ActionSupport {

  private static final long serialVersionUID = 5078264279068585793L;
  private static final Log log = LogFactory.getLog(GetMovOtSequence.class);

  private String dateStr;

  @Resource
  private OtObserveRecordDAO oorDao = null;
  @Resource
  private MoveObjectDao movObjDao = null;
  private String motList;
  private String ot1List;
  private Date minDate;
  private Date maxDate;
  private int minNum;
  private int maxNum;

  @SuppressWarnings("unchecked")
  public String execute() {

    Object[] objs = oorDao.getMinMaxDateOt1(dateStr);
    if (objs != null && objs.length == 4 && objs[0] != null && objs[1] != null && objs[2] != null && objs[3] != null) {
      minDate = (Date) objs[0];
      maxDate = (Date) objs[1];
      minNum = (Integer) objs[2];
      maxNum = (Integer) objs[3];
      motList = movObjDao.getMoveObjsByDate(dateStr);
      ot1List = movObjDao.getNotMatchOTByDate(dateStr);
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

}
