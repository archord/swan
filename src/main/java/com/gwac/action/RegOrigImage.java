/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

/**
 * from MultipleCommonFileUploadAction
 *
 * @author xy
 */
public class RegOrigImage extends ActionSupport {

  private static final Log log = LogFactory.getLog(RegOrigImage.class);

  private String groupId;
  private String unitId;
  private String camId;
  private String gridId;
  private String fieldId;
  private String imgName;
  private String imgPath;
  private String genTime; //yyyyMMddHHmmssSSS

  private String echo = "";

  @Action(value = "regOrigImg", results = {
    @Result(location = "manage/result.jsp", name = SUCCESS),
    @Result(location = "manage/result.jsp", name = INPUT),
    @Result(location = "manage/result.jsp", name = ERROR)})
  public String upload() {

    String result = SUCCESS;
    echo = "";

    log.debug("groupId:" + groupId);
    log.debug("unitId:" + unitId);
    log.debug("camId:" + camId);
    log.debug("gridId:" + gridId);
    log.debug("fieldId:" + fieldId);
    log.debug("imgName:" + imgName);
    log.debug("imgPath:" + imgPath);
    log.debug("genTime:" + genTime);

    echo = "register image success!";
    log.debug(echo);
    ActionContext ctx = ActionContext.getContext();
    ctx.getSession().put("echo", echo);

    return result;
  }

  public String display() {
    return NONE;
  }

  /**
   * @return the echo
   */
  public String getEcho() {
    return echo;
  }

  /**
   * @param groupId the groupId to set
   */
  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  /**
   * @param unitId the unitId to set
   */
  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  /**
   * @param camId the camId to set
   */
  public void setCamId(String camId) {
    this.camId = camId;
  }

  /**
   * @param gridId the gridId to set
   */
  public void setGridId(String gridId) {
    this.gridId = gridId;
  }

  /**
   * @param fieldId the fieldId to set
   */
  public void setFieldId(String fieldId) {
    this.fieldId = fieldId;
  }

  /**
   * @param imgName the imgName to set
   */
  public void setImgName(String imgName) {
    this.imgName = imgName;
  }

  /**
   * @param imgPath the imgPath to set
   */
  public void setImgPath(String imgPath) {
    this.imgPath = imgPath;
  }

  /**
   * @param genTime the genTime to set
   */
  public void setGenTime(String genTime) {
    this.genTime = genTime;
  }

}
