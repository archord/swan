/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.dao.SkyRegionTemplateDao;
import com.gwac.model.SkyRegionTemplate;
import com.gwac.util.CommonFunction;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.text.ParseException;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

/**
 * from MultipleCommonFileUploadAction
 *
 * @author xy
 */
public class RegTemplateAction extends ActionSupport {

  /**
   * @param tmptName the tmptName to set
   */
  public void setTmptName(String tmptName) {
    this.tmptName = tmptName;
  }

  /**
   * @param groupId the groupId to set
   */
  public void setGroupId(Integer groupId) {
    this.groupId = groupId;
  }

  /**
   * @param unitId the unitId to set
   */
  public void setUnitId(Integer unitId) {
    this.unitId = unitId;
  }

  /**
   * @param camId the camId to set
   */
  public void setCamId(Integer camId) {
    this.camId = camId;
  }

  /**
   * @param gridId the gridId to set
   */
  public void setGridId(Integer gridId) {
    this.gridId = gridId;
  }

  /**
   * @param fieldId the fieldId to set
   */
  public void setFieldId(Integer fieldId) {
    this.fieldId = fieldId;
  }

  /**
   * @param centerRa the centerRa to set
   */
  public void setCenterRa(Float centerRa) {
    this.centerRa = centerRa;
  }

  /**
   * @param centerDec the centerDec to set
   */
  public void setCenterDec(Float centerDec) {
    this.centerDec = centerDec;
  }

  /**
   * @param topLeftRa the topLeftRa to set
   */
  public void setTopLeftRa(Float topLeftRa) {
    this.topLeftRa = topLeftRa;
  }

  /**
   * @param topLeftDec the topLeftDec to set
   */
  public void setTopLeftDec(Float topLeftDec) {
    this.topLeftDec = topLeftDec;
  }

  /**
   * @param topRightRa the topRightRa to set
   */
  public void setTopRightRa(Float topRightRa) {
    this.topRightRa = topRightRa;
  }

  /**
   * @param topRightDec the topRightDec to set
   */
  public void setTopRightDec(Float topRightDec) {
    this.topRightDec = topRightDec;
  }

  /**
   * @param bottomRightRa the bottomRightRa to set
   */
  public void setBottomRightRa(Float bottomRightRa) {
    this.bottomRightRa = bottomRightRa;
  }

  /**
   * @param bottomRightDec the bottomRightDec to set
   */
  public void setBottomRightDec(Float bottomRightDec) {
    this.bottomRightDec = bottomRightDec;
  }

  /**
   * @param bottomLeftRa the bottomLeftRa to set
   */
  public void setBottomLeftRa(Float bottomLeftRa) {
    this.bottomLeftRa = bottomLeftRa;
  }

  /**
   * @param bottomLeftDec the bottomLeftDec to set
   */
  public void setBottomLeftDec(Float bottomLeftDec) {
    this.bottomLeftDec = bottomLeftDec;
  }

  /**
   * @param genTime the genTime to set
   */
  public void setGenTime(String genTime) {
    this.genTime = genTime;
  }

  /**
   * @param storePath the storePath to set
   */
  public void setStorePath(String storePath) {
    this.storePath = storePath;
  }

  /**
   * @param starNum the starNum to set
   */
  public void setStarNum(Integer starNum) {
    this.starNum = starNum;
  }

  /**
   * @param fwhm the fwhm to set
   */
  public void setFwhm(Float fwhm) {
    this.fwhm = fwhm;
  }

  /**
   * @param srTmptDao the srTmptDao to set
   */
  public void setSrTmptDao(SkyRegionTemplateDao srTmptDao) {
    this.srTmptDao = srTmptDao;
  }

  private static final Log log = LogFactory.getLog(RegTemplateAction.class);

  private String tmptName;
  private Integer groupId;
  private Integer unitId;
  private Integer camId;
  private Integer gridId;
  private Integer fieldId;
  private Float centerRa;
  private Float centerDec;
  private Float topLeftRa;
  private Float topLeftDec;
  private Float topRightRa;
  private Float topRightDec;
  private Float bottomRightRa;
  private Float bottomRightDec;
  private Float bottomLeftRa;
  private Float bottomLeftDec;
  private String genTime;
  private String storePath;
  private Integer starNum;
  private Float fwhm;
  private SkyRegionTemplateDao srTmptDao;

  private String echo = "";

  @Action(value = "regTemplateImg", results = {
    @Result(location = "manage/result.jsp", name = SUCCESS)
    ,
    @Result(location = "manage/result.jsp", name = INPUT)
    ,
    @Result(location = "manage/result.jsp", name = ERROR)})
  public String action1() {

    String result = SUCCESS;
    echo = "";
    if (tmptName != null) {
      SkyRegionTemplate obj = new SkyRegionTemplate();
      obj.setBottomLeftDec(bottomLeftDec);
      obj.setBottomLeftRa(bottomLeftRa);
      obj.setBottomRightDec(bottomRightDec);
      obj.setBottomRightRa(bottomRightRa);
      obj.setCamId(camId);
      obj.setCenterDec(centerDec);
      obj.setCenterRa(centerRa);
      obj.setFieldId(fieldId);
      obj.setFwhm(fwhm);
      if (genTime != null && !genTime.isEmpty()) {
        genTime = genTime.replace("T", " ");
        try {
          obj.setGenTime(CommonFunction.stringToDate(genTime, "yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
          log.warn("parse datatime error");
        }
      }
      obj.setGridId(gridId);
      obj.setGroupId(groupId);
      obj.setStarNum(starNum);
      obj.setStorePath(storePath);
      obj.setTmptName(tmptName);
      obj.setTopLeftDec(topLeftDec);
      obj.setTopLeftRa(topLeftRa);
      obj.setTopRightDec(topRightDec);
      obj.setTopRightRa(topRightRa);
      obj.setUnitId(unitId);
      srTmptDao.save(obj);
      echo = "regist template success!";
    } else {
      echo = "regist template failure!";
      log.error("tmplate is null");
    }

    ActionContext ctx = ActionContext.getContext();
    ctx.getSession().put("echo", echo);
    return result;
  }

}
