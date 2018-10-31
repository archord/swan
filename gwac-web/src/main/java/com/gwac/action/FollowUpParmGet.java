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

    AutoFollowUp = wgpdao.getValueByName("AutoFollowUp");
    Filter = wgpdao.getValueByName("Filter");
    Telescope = wgpdao.getValueByName("Telescope");
    ExposeDuration = wgpdao.getValueByName("ExposeDuration");
    Priority = wgpdao.getValueByName("Priority");
    FrameCount = wgpdao.getValueByName("FrameCount");
    
    fupStage2FrameCount = wgpdao.getValueByName("fupStage2FrameCount");
    fupStage2ExposeDuration = wgpdao.getValueByName("fupStage2ExposeDuration");
    fupStage2Filter = wgpdao.getValueByName("fupStage2Filter");
    fupStage2Priority = wgpdao.getValueByName("fupStage2Priority");
    fupStage2Telescope = wgpdao.getValueByName("fupStage2Telescope");
    
    fupStage3FrameCount = wgpdao.getValueByName("fupStage3FrameCount");
    fupStage3ExposeDuration = wgpdao.getValueByName("fupStage3ExposeDuration");
    fupStage3Filter = wgpdao.getValueByName("fupStage3Filter");
    fupStage3Priority = wgpdao.getValueByName("fupStage3Priority");
    fupStage3Telescope = wgpdao.getValueByName("fupStage3Telescope");
    
    fupStage1MagDiff = wgpdao.getValueByName("fupStage1MagDiff");
    fupStage1MinRecordNum = wgpdao.getValueByName("fupStage1MinRecordNum");
    fupStage2StartTime = wgpdao.getValueByName("fupStage2StartTime");
    fupStage3StopTime = wgpdao.getValueByName("fupStage3StopTime");
    fupStage3MagDiff = wgpdao.getValueByName("fupStage3MagDiff");
    fupStage3StartTime = wgpdao.getValueByName("fupStage3StartTime");

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

  /**
   * @return the fupStage1MagDiff
   */
  public String getFupStage1MagDiff() {
    return fupStage1MagDiff;
  }

  /**
   * @return the fupStage1MinRecordNum
   */
  public String getFupStage1MinRecordNum() {
    return fupStage1MinRecordNum;
  }

  /**
   * @return the fupStage2FrameCount
   */
  public String getFupStage2FrameCount() {
    return fupStage2FrameCount;
  }

  /**
   * @return the fupStage2StartTime
   */
  public String getFupStage2StartTime() {
    return fupStage2StartTime;
  }

  /**
   * @return the fupStage2ExposeDuration
   */
  public String getFupStage2ExposeDuration() {
    return fupStage2ExposeDuration;
  }

  /**
   * @return the fupStage2Filter
   */
  public String getFupStage2Filter() {
    return fupStage2Filter;
  }

  /**
   * @return the fupStage2Priority
   */
  public String getFupStage2Priority() {
    return fupStage2Priority;
  }

  /**
   * @return the fupStage2Telescope
   */
  public String getFupStage2Telescope() {
    return fupStage2Telescope;
  }

  /**
   * @return the fupStage3StopTime
   */
  public String getFupStage3StopTime() {
    return fupStage3StopTime;
  }

  /**
   * @return the fupStage3MagDiff
   */
  public String getFupStage3MagDiff() {
    return fupStage3MagDiff;
  }

  /**
   * @return the fupStage3StartTime
   */
  public String getFupStage3StartTime() {
    return fupStage3StartTime;
  }

  /**
   * @return the fupStage3ExposeDuration
   */
  public String getFupStage3ExposeDuration() {
    return fupStage3ExposeDuration;
  }

  /**
   * @return the fupStage3Priority
   */
  public String getFupStage3Priority() {
    return fupStage3Priority;
  }

  /**
   * @return the fupStage3FrameCount
   */
  public String getFupStage3FrameCount() {
    return fupStage3FrameCount;
  }

  /**
   * @return the fupStage3Telescope
   */
  public String getFupStage3Telescope() {
    return fupStage3Telescope;
  }

  /**
   * @return the fupStage3Filter
   */
  public String getFupStage3Filter() {
    return fupStage3Filter;
  }

}
