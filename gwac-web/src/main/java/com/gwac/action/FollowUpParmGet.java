package com.gwac.action;

import com.gwac.dao.WebGlobalParameterDao;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

/**
 * 目前已经被GetOtLevel2List取代
 *
 * @author xy
 */
@Action(value = "followUpParmGet", results = {
  @Result(name = "success", type = "json")})
public class FollowUpParmGet extends ActionSupport {

  private static final long serialVersionUID = 1438464279863543517L;
  private static final Log log = LogFactory.getLog(DeletOTFollowUp.class);

  private String AutoFollowUp;
  private String Filter;
  private String Telescope;
  private String FrameCount;
  private String ExposeDuration;
  private String Priority;

  @Resource
  private WebGlobalParameterDao wgpdao;

  @SuppressWarnings("unchecked")
  public String execute() {

    AutoFollowUp = wgpdao.getValueByName("AutoFollowUp");
    Filter = wgpdao.getValueByName("Filter");
    Telescope = wgpdao.getValueByName("Telescope");
    ExposeDuration = wgpdao.getValueByName("ExposeDuration");
    Priority = wgpdao.getValueByName("Priority");
    FrameCount = wgpdao.getValueByName("FrameCount");

    return SUCCESS;
  }

  /**
   * @return the AutoFollowUp
   */
  public String getAutoFollowUp() {
    return AutoFollowUp;
  }

  /**
   * @return the Filter
   */
  public String getFilter() {
    return Filter;
  }

  /**
   * @return the Telescope
   */
  public String getTelescope() {
    return Telescope;
  }

  /**
   * @return the FrameCount
   */
  public String getFrameCount() {
    return FrameCount;
  }

  /**
   * @return the ExposeDuration
   */
  public String getExposeDuration() {
    return ExposeDuration;
  }

  /**
   * @return the Priority
   */
  public String getPriority() {
    return Priority;
  }

}
