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
  
  private String fupStage2FrameCount;
  private String fupStage2ExposeDuration;
  private String fupStage2Filter;
  private String fupStage2Priority;
  private String fupStage2Telescope;
  
  private String fupStage3ExposeDuration;
  private String fupStage3Priority;
  private String fupStage3FrameCount;
  private String fupStage3Telescope;
  private String fupStage3Filter;
  
  private String fupStage1MagDiff;
  private String fupStage1MinRecordNum;
  private String fupStage2StartTime;
  private String fupStage3StopTime;
  private String fupStage3MagDiff;
  private String fupStage3StartTime;

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

    if (fupStage2FrameCount != null && !fupStage2FrameCount.isEmpty()) {
      wgpdao.updateParameter("fupStage2FrameCount", fupStage2FrameCount);
    }
    if (fupStage2ExposeDuration != null && !fupStage2ExposeDuration.isEmpty()) {
      wgpdao.updateParameter("fupStage2ExposeDuration", fupStage2ExposeDuration);
    }
    if (fupStage2Filter != null && !fupStage2Filter.isEmpty()) {
      wgpdao.updateParameter("fupStage2Filter", fupStage2Filter);
    }
    if (fupStage2Priority != null && !fupStage2Priority.isEmpty()) {
      wgpdao.updateParameter("fupStage2Priority", fupStage2Priority);
    }
    if (fupStage2Telescope != null && !fupStage2Telescope.isEmpty()) {
      wgpdao.updateParameter("fupStage2Telescope", fupStage2Telescope);
    }
    
    if (fupStage3FrameCount != null && !fupStage3FrameCount.isEmpty()) {
      wgpdao.updateParameter("fupStage3FrameCount", fupStage3FrameCount);
    }
    if (fupStage3ExposeDuration != null && !fupStage3ExposeDuration.isEmpty()) {
      wgpdao.updateParameter("fupStage3ExposeDuration", fupStage3ExposeDuration);
    }
    if (fupStage3Filter != null && !fupStage3Filter.isEmpty()) {
      wgpdao.updateParameter("fupStage3Filter", fupStage3Filter);
    }
    if (fupStage3Priority != null && !fupStage3Priority.isEmpty()) {
      wgpdao.updateParameter("fupStage3Priority", fupStage3Priority);
    }
    if (fupStage3Telescope != null && !fupStage3Telescope.isEmpty()) {
      wgpdao.updateParameter("fupStage3Telescope", fupStage3Telescope);
    }

    if (fupStage1MagDiff != null && !fupStage1MagDiff.isEmpty()) {
      wgpdao.updateParameter("fupStage1MagDiff", fupStage1MagDiff);
    }
    if (fupStage1MinRecordNum != null && !fupStage1MinRecordNum.isEmpty()) {
      wgpdao.updateParameter("fupStage1MinRecordNum", fupStage1MinRecordNum);
    }
    if (fupStage2StartTime != null && !fupStage2StartTime.isEmpty()) {
      wgpdao.updateParameter("fupStage2StartTime", fupStage2StartTime);
    }
    if (fupStage3StopTime != null && !fupStage3StopTime.isEmpty()) {
      wgpdao.updateParameter("fupStage3StopTime", fupStage3StopTime);
    }
    if (fupStage3MagDiff != null && !fupStage3MagDiff.isEmpty()) {
      wgpdao.updateParameter("fupStage3MagDiff", fupStage3MagDiff);
    }
    if (fupStage3StartTime != null && !fupStage3StartTime.isEmpty()) {
      wgpdao.updateParameter("fupStage3StartTime", fupStage3StartTime);
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
  
  /**
   * @param fupStage2FrameCount the fupStage2FrameCount to set
   */
  public void setFupStage2FrameCount(String fupStage2FrameCount) {
    this.fupStage2FrameCount = fupStage2FrameCount;
  }

  /**
   * @param fupStage2ExposeDuration the fupStage2ExposeDuration to set
   */
  public void setFupStage2ExposeDuration(String fupStage2ExposeDuration) {
    this.fupStage2ExposeDuration = fupStage2ExposeDuration;
  }

  /**
   * @param fupStage2Filter the fupStage2Filter to set
   */
  public void setFupStage2Filter(String fupStage2Filter) {
    this.fupStage2Filter = fupStage2Filter;
  }

  /**
   * @param fupStage2Priority the fupStage2Priority to set
   */
  public void setFupStage2Priority(String fupStage2Priority) {
    this.fupStage2Priority = fupStage2Priority;
  }

  /**
   * @param fupStage2Telescope the fupStage2Telescope to set
   */
  public void setFupStage2Telescope(String fupStage2Telescope) {
    this.fupStage2Telescope = fupStage2Telescope;
  }

  /**
   * @param fupStage3ExposeDuration the fupStage3ExposeDuration to set
   */
  public void setFupStage3ExposeDuration(String fupStage3ExposeDuration) {
    this.fupStage3ExposeDuration = fupStage3ExposeDuration;
  }

  /**
   * @param fupStage3Priority the fupStage3Priority to set
   */
  public void setFupStage3Priority(String fupStage3Priority) {
    this.fupStage3Priority = fupStage3Priority;
  }

  /**
   * @param fupStage3FrameCount the fupStage3FrameCount to set
   */
  public void setFupStage3FrameCount(String fupStage3FrameCount) {
    this.fupStage3FrameCount = fupStage3FrameCount;
  }

  /**
   * @param fupStage3Telescope the fupStage3Telescope to set
   */
  public void setFupStage3Telescope(String fupStage3Telescope) {
    this.fupStage3Telescope = fupStage3Telescope;
  }

  /**
   * @param fupStage3Filter the fupStage3Filter to set
   */
  public void setFupStage3Filter(String fupStage3Filter) {
    this.fupStage3Filter = fupStage3Filter;
  }

  /**
   * @param fupStage1MagDiff the fupStage1MagDiff to set
   */
  public void setFupStage1MagDiff(String fupStage1MagDiff) {
    this.fupStage1MagDiff = fupStage1MagDiff;
  }

  /**
   * @param fupStage1MinRecordNum the fupStage1MinRecordNum to set
   */
  public void setFupStage1MinRecordNum(String fupStage1MinRecordNum) {
    this.fupStage1MinRecordNum = fupStage1MinRecordNum;
  }

  /**
   * @param fupStage2StartTime the fupStage2StartTime to set
   */
  public void setFupStage2StartTime(String fupStage2StartTime) {
    this.fupStage2StartTime = fupStage2StartTime;
  }

  /**
   * @param fupStage3StopTime the fupStage3StopTime to set
   */
  public void setFupStage3StopTime(String fupStage3StopTime) {
    this.fupStage3StopTime = fupStage3StopTime;
  }

  /**
   * @param fupStage3MagDiff the fupStage3MagDiff to set
   */
  public void setFupStage3MagDiff(String fupStage3MagDiff) {
    this.fupStage3MagDiff = fupStage3MagDiff;
  }

  /**
   * @param fupStage3StartTime the fupStage3StartTime to set
   */
  public void setFupStage3StartTime(String fupStage3StartTime) {
    this.fupStage3StartTime = fupStage3StartTime;
  }

}
