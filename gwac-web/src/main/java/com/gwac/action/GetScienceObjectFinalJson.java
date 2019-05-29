package com.gwac.action;

import com.gwac.dao.ScienceObjectFinalDao;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

/**
 * 对OT2列表分页显示
 *
 * @author xy
 */
@Action(value = "get-sciobjfinal-detail", results = {
  @Result(name = "success", type = "json")})
public class GetScienceObjectFinalJson extends ActionSupport {

  private static final long serialVersionUID = 5073694279068591293L;
  private static final Log log = LogFactory.getLog(GetScienceObjectFinalJson.class);

  private long sofId;
  private String dataStr;

  @Resource
  private ScienceObjectFinalDao dao = null;

  @SuppressWarnings("unchecked")
//  @Transactional(readOnly=true)
  public String execute() {
    dataStr = dao.getById(sofId);

    return SUCCESS;
  }

  /**
   * @return the dataStr
   */
  public String getDataStr() {
    return dataStr;
  }

  /**
   * @param sofId the sofId to set
   */
  public void setSofId(int sofId) {
    this.sofId = sofId;
  }

}
