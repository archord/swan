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
@Action(value = "/ccd-pixel-filter-delete", results = {
  @Result(name = "json", type = "json", params = {"root", "msg"})
})
public class CcdPixelFilterDelete  extends ActionSupport{

  private static final long serialVersionUID = -3454448234588641394L;
  private static final Log log = LogFactory.getLog(CcdPixelFilterDelete.class);

  private CcdPixFilterDao cpfDao;
  private long cpfId;
  private Map msg;
  
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {

    msg = new HashMap<>();
    
    try {
      cpfDao.deleteById(cpfId);
      getMsg().put("flag", "1");
    } catch (Exception e) {
      getMsg().put("flag", "0");
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
   * @param cpfId the cpfId to set
   */
  public void setCpfId(long cpfId) {
    this.cpfId = cpfId;
  }

  /**
   * @return the msg
   */
  public Map getMsg() {
    return msg;
  }

}
