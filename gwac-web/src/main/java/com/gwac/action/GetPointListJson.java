package com.gwac.action;

import com.gwac.dao.CameraDao;
import com.gwac.dao.ImageStatusParameterDao;
import com.gwac.dao.ObservationPlanDao;
import com.gwac.model.Camera;
import com.gwac.model.ObservationPlan;
import com.gwac.util.CommonFunction;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.Result;

@Result(name = "error", location = "/error.jsp")
@ExceptionMapping(exception = "java.lang.Exception", result = "error")
public class GetPointListJson extends ActionSupport {

  private static final long serialVersionUID = -3454448234583441394L;
  private static final Log log = LogFactory.getLog(GetPointListJson.class);

  private String obsDate;
  private String unitId;
  private String fieldId;

  @Resource
  private ImageStatusParameterDao ispDao;
  @Resource
  private CameraDao camDao;
  @Resource
  private ObservationPlanDao obsPlanDao = null;

  private String parStr;

  @Actions({
    @Action(value = "/get-point-list-json", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {

    String cameraName = unitId.substring(1) + "5";
    Camera tcam = camDao.getByName(cameraName);
    ObservationPlan obsPlan = obsPlanDao.getLatestObservationPlanByFieldId(unitId, fieldId);
    parStr = ispDao.getPointJsonByParm(obsDate, tcam.getCameraId(), obsPlan.getRa(), obsPlan.getDec());

    return "json";
  }

  /**
   * @return the parStr
   */
  public String getParStr() {
    return parStr;
  }

  /**
   * @param obsDate the obsDate to set
   */
  public void setObsDate(String obsDate) {
    this.obsDate = obsDate;
  }

  /**
   * @param unitId the unitId to set
   */
  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  /**
   * @param fieldId the fieldId to set
   */
  public void setFieldId(String fieldId) {
    this.fieldId = fieldId;
  }

}
