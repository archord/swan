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
  @Action(value = "/down-mov-ot-sequence-list", results = {
    @Result(name = "success", type = "json")})})
public class DownloadMovOtSequence extends ActionSupport {

  private static final long serialVersionUID = 5078264279068585793L;
  private static final Log log = LogFactory.getLog(DownloadMovOtSequence.class);

  private final char moveType = '1';
  private final int minFrameNumber = 20;
  private String dateStr;
  private MoveObjectDao movObjDao = null;

  @SuppressWarnings("unchecked")
  public String execute() {

    Map<Long, String> movObjs = movObjDao.getMoveObjsInfoByDate(dateStr, moveType, minFrameNumber);
    for (Map.Entry<Long, String> entry : movObjs.entrySet()) {
      System.out.println(entry.getKey());
    }
    for (Map.Entry<Long, String> entry : movObjs.entrySet()) {
      String records = entry.getValue();
      records = records.substring(3, records.length() - 3).replace(")\",\"(", "\n");
      records = records.replace("\\\"", "");
      System.out.println(entry.getKey() + ": \n" + records);
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
   * @param dateStr the dateStr to set
   */
  public void setDateStr(String dateStr) {
    this.dateStr = dateStr;
  }

}
