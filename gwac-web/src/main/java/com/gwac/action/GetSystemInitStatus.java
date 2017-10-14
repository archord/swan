package com.gwac.action;

import com.gwac.dao.CameraDao;
import com.gwac.dao.MountDao;
import com.gwac.dao.MultimediaResourceDao;
import com.gwac.model.MultimediaResource;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.Result;

@Result(name = "error", location = "/error.jsp")
@ExceptionMapping(exception = "java.lang.Exception", result = "error")
public class GetSystemInitStatus extends ActionSupport {

  private static final long serialVersionUID = 1024448236588641394L;
  private static final Log log = LogFactory.getLog(GetSystemInitStatus.class);

  @Resource
  private MountDao mountDao;
  @Resource
  private CameraDao cameraDao;
  /**
   * 返回结果
   */
  private String mountStatus;
  private String ccdStatus;

  @Actions({
    @Action(value = "/get-system-init-status", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {
    mountStatus = mountDao.getMountsStatus();
    ccdStatus = cameraDao.getCamersStatus();
    return "json";
  }

  /**
   * @return the mountStatus
   */
  public String getMountStatus() {
    return mountStatus;
  }

  /**
   * @return the ccdStatus
   */
  public String getCcdStatus() {
    return ccdStatus;
  }

}
