package com.gwac.action;

import com.gwac.dao.ImageRecordDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionSupport;
import javax.annotation.Resource;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.ParentPackage;

@ParentPackage("default")
//@InterceptorRef("jsonValidationWorkflowStack")
@Result(name = "error", location = "/error.jsp")
@ExceptionMapping(exception = "java.lang.Exception", result = "error")
public class ImgClassify extends ActionSupport {

  /**
   * @param imgId the imgId to set
   */
  public void setImgId(int imgId) {
    this.imgId = imgId;
  }

  /**
   * @param imgType the imgType to set
   */
  public void setImgType(int imgType) {
    this.imgType = imgType;
  }

  private static final long serialVersionUID = 7968544374444173511L;
  private static final Log log = LogFactory.getLog(ImgClassify.class);
  
  @Resource
  private ImageRecordDao imgDao;

  private int imgId;
  private int imgType;
  private String echo;


  @Action(value = "/img-classify", results = {
    @Result(name = "json", type = "json", params = {"root", "echo"})
  })
  public String userLogout() throws Exception {
    imgDao.updateType(imgId, imgType);
    echo = "success";
    return "json";
  }

  public String getEcho() {
    return echo;
  }


}
