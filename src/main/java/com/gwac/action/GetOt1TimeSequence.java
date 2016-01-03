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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.SessionAware;

@Actions({
  @Action(value = "/get-ot1-timesequence-list", results = {
    @Result(name = "success", type = "json")})})
public class GetOt1TimeSequence extends ActionSupport implements SessionAware {

  private static final long serialVersionUID = 5078264279068585793L;
  private static final Log log = LogFactory.getLog(GetOt1TimeSequence.class);

  private Map<String, Object> session;
  private OtObserveRecordDAO oorDao = null;

  private List<Map<String, Object>> ot2TimeSequence;
  private OtObserveRecord firstOor;
  private int maxNumber;

  @SuppressWarnings("unchecked")
  public String execute() {

    maxNumber = 0;
    ot2TimeSequence = new ArrayList();
    List<OtObserveRecord> oors = oorDao.getAllOrderByDate();
    if (oors.size() > 0) {
      firstOor = oors.get(0);

      Calendar cal = Calendar.getInstance();
      Date baseDate = getFirstOor().getDateUt();
      cal.setTime(baseDate);
      double baseDay = cal.getTimeInMillis() / 15000.0; //15S

      for (OtObserveRecord oor : oors) {
        cal.setTime(oor.getDateUt());
        double now = cal.getTimeInMillis() / 15000.0;
        int number = (int) (now - baseDay);
        Map<String, Object> tmap = new HashMap();
        tmap.put("ra", oor.getRaD());
        tmap.put("dec", oor.getDecD());
        tmap.put("number", number);
        ot2TimeSequence.add(tmap);
        if (number > getMaxNumber()) {
          maxNumber = number;
        }
      }

    }
    return SUCCESS;
  }

  @Override
  public void setSession(Map<String, Object> session) {
    this.session = session;
  }

  /**
   * @param oorDao the oorDao to set
   */
  public void setOorDao(OtObserveRecordDAO oorDao) {
    this.oorDao = oorDao;
  }

  /**
   * @return the ot2TimeSequence
   */
  public List<Map<String, Object>> getOt2TimeSequence() {
    return ot2TimeSequence;
  }

  /**
   * @return the firstOor
   */
  public OtObserveRecord getFirstOor() {
    return firstOor;
  }

  /**
   * @return the maxNumber
   */
  public int getMaxNumber() {
    return maxNumber;
  }

}
