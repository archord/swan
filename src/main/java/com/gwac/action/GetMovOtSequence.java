package com.gwac.action;

import com.gwac.dao.DataProcessMachineDAO;
import com.gwac.dao.MoveObjectDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtObserveRecordDAO;
import com.gwac.model.DataProcessMachine;
import com.gwac.model.OtLevel2;
import com.gwac.model.OtObserveRecord;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.util.*;
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
  
  private OtObserveRecordDAO oorDao = null;
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
     minDate = (Date)objs[0];
     maxDate = (Date)objs[1];
     minNum = (Integer)objs[2];
     maxNum = (Integer)objs[3];

    motList = movObjDao.getMoveObjsByDate(dateStr);
    ot1List = movObjDao.getNotMatchOTByDate(dateStr);
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

}
