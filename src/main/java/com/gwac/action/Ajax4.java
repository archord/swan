
package com.gwac.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionSupport;
import java.util.Map;
import org.apache.struts2.interceptor.ApplicationAware;

public class Ajax4 extends ActionSupport implements ApplicationAware {

  private static final long serialVersionUID = -3405146826130407758L;
  Map<String, Object> appmap;

  @Action(value = "/ajax4", results = {
    @Result(location = "ajax4.jsp", name = "success")})
  public String execute() throws Exception {
    System.out.println("datestr="+appmap.get("datestr"));
    return SUCCESS;
  }

  @Override
  public void setApplication(Map<String, Object> map) {
    this.appmap = map;
  }
}