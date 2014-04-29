package com.gwac.action;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Result;

import com.gwac.model.Telescope;
import com.gwac.service.TelescopeService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;

@Actions({
  @Action(value = "/get-telescope-all-list", results = {
    @Result(location = "manage/tspSelectList.jsp", name = "success"),
    @Result(location = "manage/tspSelectList.jsp", name = "input")})})
public class GetTelescopeAllList extends ActionSupport {

  private static final long serialVersionUID = 5078264279348543593L;
  private static final Log log = LogFactory.getLog(GetTelescopeAllList.class);
  private List<Telescope> gridModel;

  private TelescopeService tspService = null;

  @SuppressWarnings("unchecked")
  public String execute() {

//    setGridModel(dpmDao.findAll(page, rows));
    setGridModel(tspService.findAll());

    return SUCCESS;
  }
  /**
   * @param tspService the tspService to set
   */
  public void setTspService(TelescopeService tspService) {
    this.tspService = tspService;
  }

  /**
   * @return the gridModel
   */
  public List<Telescope> getGridModel() {
    return gridModel;
  }

  /**
   * @param gridModel the gridModel to set
   */
  public void setGridModel(List<Telescope> gridModel) {
    this.gridModel = gridModel;
  }

}
