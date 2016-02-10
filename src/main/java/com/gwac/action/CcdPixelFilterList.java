/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.action;

import com.gwac.dao.CcdPixFilterDao;
import com.gwac.model.CcdPixFilter;
import com.opensymphony.xwork2.ActionSupport;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

/**
 *
 * @author xy
 */
@Action(value = "/ccd-pixel-filter-list", results = {
  @Result(name = "json", type = "json")
})
public class CcdPixelFilterList  extends ActionSupport{

  private static final long serialVersionUID = -3454448234588641394L;
  private static final Log log = LogFactory.getLog(CcdPixelFilterList.class);

  private CcdPixFilterDao cpfDao;
  private List<CcdPixFilter> gridModel;

  @SuppressWarnings("unchecked")
  public String execute() throws Exception {

    try {
      gridModel = cpfDao.findAll();
    } catch (Exception e) {
      log.error("add ccd filter error:", e);
    }

    return "json";
  }

  /**
   * @param cpfDao the cpfDao to set
   */
  public void setCpfDao(CcdPixFilterDao cpfDao) {
    this.cpfDao = cpfDao;
  }

  /**
   * @return the gridModel
   */
  public List<CcdPixFilter> getGridModel() {
    return gridModel;
  }

}
