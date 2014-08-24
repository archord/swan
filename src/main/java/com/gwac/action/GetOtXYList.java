package com.gwac.action;

import com.gwac.dao.OtLevel2Dao;
import com.gwac.model.OtLevel2;
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
  private List<OtLevel2> gridModel;
  private OtLevel2Dao obDao = null;

  @SuppressWarnings("unchecked")
  public String execute() {

    gridModel = obDao.findAll();

    return SUCCESS;
  }

  /**
   * @return an collection that contains the actual data
   */
  public List<OtLevel2> getGridModel() {
    return gridModel;
  }

  /**
   * @param gridModel an collection that contains the actual data
   */
  public void setGridModel(List<OtLevel2> gridModel) {
    this.gridModel = gridModel;
  }

  /**
   * @param obDao the obDao to set
   */
  public void setObDao(OtLevel2Dao obDao) {
    this.obDao = obDao;
  }

  public void setSession(Map<String, Object> session) {
    this.session = session;
  }
}
