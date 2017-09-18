package com.gwac.action;

import com.gwac.dao.CoordinateShowDao;
import com.gwac.model.CoordinateShow;
import com.opensymphony.xwork2.ActionSupport;
import java.util.*;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;

@Actions({
  @Action(value = "/get-coor-list", results = {
    @Result(name = "success", type = "json")})})
public class GetCoorlList extends ActionSupport {

  private static final long serialVersionUID = 5078264279068585793L;
  private static final Log log = LogFactory.getLog(GetCoorlList.class);

  private List<CoordinateShow> objs;
  @Resource
  private CoordinateShowDao csDao;

  private String querySql;

  @SuppressWarnings("unchecked")
  public String execute() {

    log.debug(querySql);
    try {
      if (querySql != null && !querySql.isEmpty()) {
        querySql = querySql.toLowerCase();
        if (querySql.contains("select") && !querySql.contains("delete") && !querySql.contains("drop") && !querySql.contains("update") && !querySql.contains("alter")) {
          objs = csDao.getCoordinateShow(querySql);
        }
      } else {
        objs = new ArrayList();
      }
    } catch (Exception e) {
      objs = new ArrayList();
      log.error("query error:", e);
    }
    log.debug(getObjs().size());
    return SUCCESS;
  }

  /**
   * @return the objs
   */
  public List<CoordinateShow> getObjs() {
    return objs;
  }

  /**
   * @param querySql the querySql to set
   */
  public void setQuerySql(String querySql) {
    this.querySql = querySql;
  }

}
