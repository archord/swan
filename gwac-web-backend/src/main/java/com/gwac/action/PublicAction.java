/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.action;

import com.gwac.service.PublicService;
import com.opensymphony.xwork2.ActionSupport;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;

/**
 *
 * @author xy
 */
public class PublicAction extends ActionSupport {

  private static final long serialVersionUID = 1435264279738343593L;
  private static final Log log = LogFactory.getLog(PublicAction.class);

  @Resource
  private PublicService publicService;
  
  @Actions({
    @Action(value = "/public-service", results = {
      @Result(name = "json", type = "json")})
  })
  @Override
  public String execute() {
    publicService.executeService();
    return "json";
  }


}
