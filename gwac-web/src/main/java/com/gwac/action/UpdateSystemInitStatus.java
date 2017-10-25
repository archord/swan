package com.gwac.action;

import com.gwac.dao.CameraDao;
import com.gwac.dao.MountDao;
import com.gwac.dao.MultimediaResourceDao;
import com.gwac.dao.SystemStatusMonitorDao;
import com.gwac.model.MultimediaResource;
import com.opensymphony.xwork2.ActionSupport;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.Result;

@Result(name = "error", location = "/error.jsp")
@ExceptionMapping(exception = "java.lang.Exception", result = "error")
public class UpdateSystemInitStatus extends ActionSupport {

  private static final long serialVersionUID = 1024448236588641394L;
  private static final Log log = LogFactory.getLog(UpdateSystemInitStatus.class);

  @Resource
  private MountDao mountDao;
  @Resource
  private CameraDao cameraDao;
  @Resource
  private SystemStatusMonitorDao ssmDao;
  /**
   * 返回结果
   */
  private List<String> mounts = new ArrayList();
  private List<String> ccds = new ArrayList();
  private String status;

  @Actions({
    @Action(value = "/update-system-init-status", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public void doUpdate() throws Exception {
    if(!mounts.isEmpty()){
      StringBuilder sb = new StringBuilder();
      for(String tstr:mounts){
        sb.append("'");
        sb.append(tstr);
        sb.append( "',");
      }
      String tstr = sb.toString();
      mountDao.updateStatus(tstr.substring(0,tstr.length()-1), status);
    }
    if(!ccds.isEmpty()){
      StringBuilder sb = new StringBuilder();
      for(String tstr:ccds){
        sb.append("'");
        sb.append(tstr);
        sb.append( "',");
      }
      String tstr = sb.toString();
      cameraDao.updateStatus(tstr.substring(0,tstr.length()-1), status);
      ssmDao.updateCameraStatus(tstr.substring(0,tstr.length()-1), status);
    }
    sendResultMsg("{rst:\"success\"");
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
  
  /**
   * @param status the status to set
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * @param mounts the mounts to set
   */
  public void setMounts(List<String> mounts) {
    this.mounts = mounts;
  }

  /**
   * @param ccds the ccds to set
   */
  public void setCcds(List<String> ccds) {
    this.ccds = ccds;
  }


}
