package com.gwac.action;

import com.gwac.dao.OtObserveRecordDAO;
import com.gwac.model.OtObserveRecord;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.Result;

@Result(name = "error", location = "/error.jsp")
@ExceptionMapping(exception = "java.lang.Exception", result = "error")
public class GetStarRecordsJson extends ActionSupport {

  private static final long serialVersionUID = -3454448234588641395L;
  private static final Log log = LogFactory.getLog(GetStarRecordsJson.class);

  private int starType;
  private long starId;
  private OtObserveRecordDAO dao;
  private String starRecords;
  private String starRecords2;

  @Actions({
    @Action(value = "/get-star-records-json", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {

    starRecords = dao.getMagCurveByTypeIdStarId(starType, starId, 1);
    starRecords2 = dao.getMagCurveByTypeIdStarId(starType, starId, 8);

    return "json";
  }

  /**
   * @param dao the dao to set
   */
  public void setDao(OtObserveRecordDAO dao) {
    this.dao = dao;
  }

  /**
   * @param starId the starId to set
   */
  public void setStarId(long starId) {
    this.starId = starId;
  }

  /**
   * @return the starRecords
   */
  public String getStarRecords() {
    return starRecords;
  }

  /**
   * @param starType the starType to set
   */
  public void setStarType(int starType) {
    this.starType = starType;
  }

  /**
   * @return the starRecords2
   */
  public String getStarRecords2() {
    return starRecords2;
  }

}
