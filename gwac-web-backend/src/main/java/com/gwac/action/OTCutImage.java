package com.gwac.action;

import com.gwac.dao.OtLevel2Dao;
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
@Action(value = "oTCutImage", results = {
  @Result(name = "success", type = "json")})
public class OTCutImage extends ActionSupport{

  private static final long serialVersionUID = 1437264279538543553L;
  private static final Log log = LogFactory.getLog(OTCutImage.class);

  private String result = "";
  private String otName;
  private String cutImageRequest;

  @Resource
  private OtLevel2Dao ot2Dao;

  @SuppressWarnings("unchecked")
  public String execute() {

    if(otName.isEmpty() || cutImageRequest.isEmpty()){
      result = "otId or cutImageRequest cannot be empty.";
    }else{
      result = "success.";
      ot2Dao.updateCutImageRequest(otName, cutImageRequest);
    }
    return SUCCESS;
  }

  /**
   * @return the result
   */
  public String getResult() {
    return result;
  }

  /**
   * @param cutImageRequest the cutImageRequest to set
   */
  public void setCutImageRequest(String cutImageRequest) {
    this.cutImageRequest = cutImageRequest;
  }

  /**
   * @param otName the otName to set
   */
  public void setOtName(String otName) {
    this.otName = otName;
  }

}
