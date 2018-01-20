package com.gwac.action;

import com.gwac.dao.WebGlobalParameterDao;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.ApplicationAware;

/**
 * 目前已经被GetOtLevel2List取代
 *
 * @author xy
 */
@Action(value = "followUpParmSet", results = {
  @Result(name = "success", type = "json")})
public class FollowUpParmSet extends ActionSupport implements ApplicationAware {

  private static final long serialVersionUID = 1438464279863543517L;
  private static final Log log = LogFactory.getLog(DeletOTFollowUp.class);

  private Map<String, Object> appMap;

  private String autoFollowUp;
  private String filter;
  private String telescope;
  private String frameCount;
  private String exposeDuration;
  private String priority;
  private String result;

  @Resource
  private WebGlobalParameterDao wgpdao;

  @SuppressWarnings("unchecked")
  public String execute() {

    Map appParams = (Map) appMap.get("appParams");
    if (appParams == null) {
      appParams = new HashMap();
    }

    if (autoFollowUp != null && !autoFollowUp.isEmpty()) {
      wgpdao.updateParameter("AutoFollowUp", autoFollowUp);
      appParams.put("AutoFollowUp", autoFollowUp);
    }

    if (filter != null && !filter.isEmpty()) {
      wgpdao.updateParameter("Filter", filter);
    }

    if (telescope != null && !telescope.isEmpty()) {
      wgpdao.updateParameter("Telescope", telescope);
    }

    if (exposeDuration != null && !exposeDuration.isEmpty()) {
      wgpdao.updateParameter("ExposeDuration", exposeDuration);
    }

    if (priority != null && !priority.isEmpty()) {
      wgpdao.updateParameter("Priority", priority);
    }

    if (frameCount != null && !frameCount.isEmpty()) {
      wgpdao.updateParameter("FrameCount", frameCount);
    }
    appMap.put("appParams", appParams);

    result = "success!";
    return SUCCESS;
  }

  /**
   * @return the result
   */
  public String getResult() {
    return result;
  }

  /**
   * @param autoFollowUp the autoFollowUp to set
   */
  public void setAutoFollowUp(String autoFollowUp) {
    this.autoFollowUp = autoFollowUp;
  }

  /**
   * @param filter the filter to set
   */
  public void setFilter(String filter) {
    this.filter = filter;
  }

  /**
   * @param telescope the telescope to set
   */
  public void setTelescope(String telescope) {
    this.telescope = telescope;
  }

  /**
   * @param frameCount the frameCount to set
   */
  public void setFrameCount(String frameCount) {
    this.frameCount = frameCount;
  }

  /**
   * @param exposeDuration the exposeDuration to set
   */
  public void setExposeDuration(String exposeDuration) {
    this.exposeDuration = exposeDuration;
  }

  /**
   * @param priority the priority to set
   */
  public void setPriority(String priority) {
    this.priority = priority;
  }

  @Override
  public void setApplication(Map<String, Object> map) {
    this.appMap = map;
  }
}
