package com.gwac.action;

import com.gwac.dao.CameraDao;
import com.gwac.model.Camera;
import com.opensymphony.xwork2.ActionSupport;
import java.util.*;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;

@Actions({
  @Action(value = "/get-camera-monitor-image-time", results = {
    @Result(name = "success", type = "json")})})
public class GetCameraMonitorImageTime extends ActionSupport {

  private static final long serialVersionUID = 1358264279068585793L;
  private static final Log log = LogFactory.getLog(GetCameraMonitorImageTime.class);
  private List<Camera> cameras;
  @Resource
  private CameraDao CameraDao;

  @SuppressWarnings("unchecked")
  public String execute() {
    cameras =CameraDao.findAll();
    return SUCCESS;
  }

  /**
   * @return the cameras
   */
  public List<Camera> getCameras() {
    return cameras;
  }


}
