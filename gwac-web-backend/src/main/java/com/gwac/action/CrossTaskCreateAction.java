/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.dao.CameraDao;
import com.gwac.dao.CrossTaskDao;
import com.gwac.model.Camera;
import com.gwac.model.CrossTask;
import com.gwac.util.CommonFunction;
import com.opensymphony.xwork2.ActionSupport;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.interceptor.ApplicationAware;

/**
 * from MultipleCommonFileUploadAction
 *
 * @author xy
 */
public class CrossTaskCreateAction extends ActionSupport implements ApplicationAware {

  private static final Log log = LogFactory.getLog(CrossTaskCreateAction.class);

  private String teleName;
  private String taskName;
  private String crossMethod;
  private String dateStr;
  private Float mergedR;
  private Float mergedMag;
  private Float cvsR;
  private Float cvsMag;
  private Float rc3R;
  private Float rc3MinMag;
  private Float rc3MaxMag;
  private Float minorPlanetR;
  private Float minorPlanetMag;
  private Float ot2HisR;
  private Float ot2HisMag;
  private Float usnoR1;
  private Float usnoMag1;
  private Float usnoR2;
  private Float usnoMag2;

  @Resource
  private CrossTaskDao crossTaskDao;
  @Resource
  private CameraDao cameraDao;
  private Map<String, Object> appMap = null;

  private String echo = "";

  @Action(value = "crossTaskCreate")
  public void upload() {

    echo = "";

    if (taskName == null || taskName.isEmpty() || crossMethod == null || crossMethod.isEmpty()) {
      echo = "all parameter cannot be empty.";
      log.warn(echo);
      log.warn("taskName:" + taskName + ", crossMethod:" + crossMethod);
    } else {
//      System.out.println(mergedR);
//      System.out.println(mergedMag);
//      System.out.println(cvsR);
//      System.out.println(cvsMag);
//      System.out.println(rc3R);
//      System.out.println(rc3MinMag);
//      System.out.println(rc3MaxMag);
//      System.out.println(minorPlanetR);
//      System.out.println(minorPlanetMag);
//      System.out.println(ot2HisR);
//      System.out.println(ot2HisMag);
//      System.out.println(usnoR1);
//      System.out.println(usnoMag1);
//      System.out.println(usnoR2);
//      System.out.println(usnoMag2);
      if(dateStr==null || dateStr.isEmpty()){
	initDateStr();
      }
      CrossTask ct = new CrossTask();
      ct.setCreateTime(new Date());
      ct.setCrossMethod(Integer.parseInt(crossMethod));
      ct.setCtName(taskName);
      ct.setDateStr(dateStr);
      ct.setFfCount(0);
      ct.setObjCount(0);
      ct.setMergedR(mergedR);
      ct.setMergedMag(mergedMag);
      ct.setCvsR(cvsR);
      ct.setCvsMag(cvsMag);
      ct.setRc3R(rc3R);
      ct.setRc3MinMag(rc3MinMag);
      ct.setRc3MaxMag(rc3MaxMag);
      ct.setMinorPlanetR(minorPlanetR);
      ct.setMinorPlanetMag(minorPlanetMag);
      ct.setOt2HisR(ot2HisR);
      ct.setOt2HisMag(ot2HisMag);
      ct.setUsnoR1(usnoR1);
      ct.setUsnoMag1(usnoMag1);
      ct.setUsnoR2(usnoR2);
      ct.setUsnoMag2(usnoMag2);
      if(teleName!=null&&teleName.trim().length()>0){
	Camera tcam = cameraDao.getByName(teleName);
	ct.setTelescopeId(tcam.getCameraId());
      }else{
	ct.setTelescopeId(0);
      }
      if(crossTaskDao.exist(ct)){
	echo = taskName + " already exist, please select another name.";
      }else{
	crossTaskDao.save(ct);
	echo = "create CrossTask success.";
      }
      log.debug(echo);
    }

    sendResultMsg(echo);
  }

  public void sendResultMsg(String msg) {

    HttpServletResponse response = ServletActionContext.getResponse();
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out;
    try {
      out = response.getWriter();
      out.print(msg);
    } catch (IOException ex) {
      log.error("response error: ", ex);
    }
  }

  public void initDateStr() {
    dateStr = (String) appMap.get("datestr");
    if (null == dateStr) {
      setDateStr(CommonFunction.getUniqueDateStr());
      appMap.put("datestr", dateStr);
    }
    dateStr = dateStr.substring(2);
  }

  /**
   * @param mergedR the mergedR to set
   */
  public void setMergedR(Float mergedR) {
    this.mergedR = mergedR;
  }

  /**
   * @param mergedMag the mergedMag to set
   */
  public void setMergedMag(Float mergedMag) {
    this.mergedMag = mergedMag;
  }

  /**
   * @param cvsR the cvsR to set
   */
  public void setCvsR(Float cvsR) {
    this.cvsR = cvsR;
  }

  /**
   * @param cvsMag the cvsMag to set
   */
  public void setCvsMag(Float cvsMag) {
    this.cvsMag = cvsMag;
  }

  /**
   * @param rc3R the rc3R to set
   */
  public void setRc3R(Float rc3R) {
    this.rc3R = rc3R;
  }

  /**
   * @param rc3Mag the rc3Mag to set
   */
  public void setRc3MinMag(Float rc3MinMag) {
    this.rc3MinMag = rc3MinMag;
  }
  
  public void setRc3MaxMag(Float rc3MaxMag) {
    this.rc3MaxMag = rc3MaxMag;
  }

  /**
   * @param minorPlanetR the minorPlanetR to set
   */
  public void setMinorPlanetR(Float minorPlanetR) {
    this.minorPlanetR = minorPlanetR;
  }

  /**
   * @param minorPlanetMag the minorPlanetMag to set
   */
  public void setMinorPlanetMag(Float minorPlanetMag) {
    this.minorPlanetMag = minorPlanetMag;
  }

  /**
   * @param ot2HisR the ot2HisR to set
   */
  public void setOt2HisR(Float ot2HisR) {
    this.ot2HisR = ot2HisR;
  }

  /**
   * @param ot2HisMag the ot2HisMag to set
   */
  public void setOt2HisMag(Float ot2HisMag) {
    this.ot2HisMag = ot2HisMag;
  }

  /**
   * @param usnoR1 the usnoR1 to set
   */
  public void setUsnoR1(Float usnoR1) {
    this.usnoR1 = usnoR1;
  }

  /**
   * @param usnnoMag1 the usnnoMag1 to set
   */
  public void setUsnoMag1(Float usnnoMag1) {
    this.usnoMag1 = usnnoMag1;
  }

  /**
   * @param usnoR2 the usnoR2 to set
   */
  public void setUsnoR2(Float usnoR2) {
    this.usnoR2 = usnoR2;
  }

  /**
   * @param usnnoMag2 the usnnoMag2 to set
   */
  public void setUsnoMag2(Float usnnoMag2) {
    this.usnoMag2 = usnnoMag2;
  }

  /**
   * @return the echo
   */
  public String getEcho() {
    return echo;
  }


  @Override
  public void setApplication(Map<String, Object> map) {
    this.appMap = map;
  }

  /**
   * @param taskName the taskName to set
   */
  public void setTaskName(String taskName) {
    this.taskName = taskName;
  }

  /**
   * @param crossMethod the crossMethod to set
   */
  public void setCrossMethod(String crossMethod) {
    this.crossMethod = crossMethod;
  }

  /**
   * @param dateStr the dateStr to set
   */
  public void setDateStr(String dateStr) {
    this.dateStr = dateStr;
  }

}
