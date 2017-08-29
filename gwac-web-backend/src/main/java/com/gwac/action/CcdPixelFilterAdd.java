/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.action;

import com.gwac.dao.CcdPixFilterDao;
import com.gwac.model.CcdPixFilter;
import com.opensymphony.xwork2.ActionSupport;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

/**
 *
 * @author xy
 */
@Action(value = "/ccd-pixel-filter-add", results = {
  @Result(name = "json", type = "json", params = {"root", "msg"})
})
public class CcdPixelFilterAdd extends ActionSupport {

  private static final long serialVersionUID = -3454448234588641394L;
  private static final Log log = LogFactory.getLog(CcdPixelFilterAdd.class);

  @Resource
  private CcdPixFilterDao cpfDao;
  private CcdPixFilter ccdFilter;

  private Map msg;

  @SuppressWarnings("unchecked")
  public String execute() throws Exception {

    msg = new HashMap<>();
    
    try {
      cpfDao.save(ccdFilter);
      msg.put("flag", "1");
    } catch (Exception e) {
      msg.put("flag", "0");
      log.error("add ccd filter error:", e);
    }

    return "json";
  }

  /**
   * @param ccdFilter the ccdFilter to set
   */
  public void setCcdFilter(CcdPixFilter ccdFilter) {
    this.ccdFilter = ccdFilter;
  }

  /**
   * @return the msg
   */
  public Map getMsg() {
    return msg;
  }

  /**
   * @return the ccdFilter
   */
  public CcdPixFilter getCcdFilter() {
    return ccdFilter;
  }
}
