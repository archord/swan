package com.gwac.action;

import com.gwac.dao.FollowUpObservationDao;
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
@Action(value = "get-followup-observation", results = {
  @Result(name = "success", type = "json")})
public class GetFollowUpObservation extends ActionSupport {

  private static final long serialVersionUID = 5073694279068591293L;
  private static final Log log = LogFactory.getLog(GetFollowUpObservation.class);

  private int foId;
  private String dataStr;

  @Resource
  private FollowUpObservationDao dao = null;

  @SuppressWarnings("unchecked")
//  @Transactional(readOnly=true)
  public String execute() {
    System.out.println("foId="+foId);
    dataStr = dao.getById(foId);

    return SUCCESS;
  }

  /**
   * @param foId the foId to set
   */
  public void setFoId(int foId) {
    this.foId = foId;
  }

  /**
   * @return the dataStr
   */
  public String getDataStr() {
    return dataStr;
  }

}
