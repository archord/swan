/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.model.OtLevel2;
import com.gwac.service.OtNameRequestService;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

/**
 * OT名称请求服务，传递OT对应的标示字符串、模板X、模板Y三个字段，查询OT的名称，
 * 如果OT存在，则返回OT名；
 * 如果不存在，则新建OT，并取名，返回新OT名称。
 */
//@InterceptorRef("jsonValidationWorkflowStack")
//加了这句化，文件传不上来
public class GetOtName extends ActionSupport {

  private static final Log log = LogFactory.getLog(UserRegister.class);
  @Resource
  private OtNameRequestService onrService;
  private File requestList;
  private List<String> fileUploadContentType = new ArrayList<String>();
  private List<String> fileUploadFileName = new ArrayList<String>();
  private String echo = "";

  @Action(value = "getOtName", results = {
    @Result(location = "manage/result.jsp", name = SUCCESS),
    @Result(location = "manage/result.jsp", name = INPUT),
    @Result(location = "manage/result.jsp", name = ERROR)})
  public String getOt() throws Exception {

    boolean flag = true;
    String result = SUCCESS;
    echo = "";

    //必须传输参数配置文件
    //Must transform parameter config file
    if (null == getRequestList()) {
      setEcho(getEcho() + "Must upload config file(configFile).\n");
      flag = false;
    }

    if (flag) {
      List<OtLevel2> objs = onrService.getOtNames(requestList);
      for(OtLevel2 obj:objs){
      }

    } else {
      result = ERROR;
    }

    log.info(echo);
    /* 如果使用struts2的标签，返回结果会有两个空行，这个显示在命令行不好看。
     * 用jsp的out，则不会有两个空行。
     * 在这里将结果信息存储在session中，在jsp页面获得返回信息。
     */
    ActionContext ctx = ActionContext.getContext();
    ctx.getSession().put("echo", getEcho());

    return result;
  }

  public String display() {
    return NONE;
  }

  public List<String> getFileUploadContentType() {
    return fileUploadContentType;
  }

  public void setFileUploadContentType(List<String> fileUploadContentType) {
    this.fileUploadContentType = fileUploadContentType;
  }

  public List<String> getFileUploadFileName() {
    return fileUploadFileName;
  }

  public void setFileUploadFileName(List<String> fileUploadFileName) {
    this.fileUploadFileName = fileUploadFileName;
  }

  /**
   * @return the echo
   */
  public String getEcho() {
    return echo;
  }

  /**
   * @param echo the echo to set
   */
  public void setEcho(String echo) {
    this.echo = echo;
  }

  /**
   * @return the requestList
   */
  public File getRequestList() {
    return requestList;
  }

  /**
   * @param requestList the requestList to set
   */
  public void setRequestList(File requestList) {
    this.requestList = requestList;
  }

}