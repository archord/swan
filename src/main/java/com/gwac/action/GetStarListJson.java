package com.gwac.action;

import com.gwac.dao.OtLevel2MatchDao;
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
public class GetStarListJson extends ActionSupport{

  private static final long serialVersionUID = -3454448234588641394L;
  private static final Log log = LogFactory.getLog(GetStarListJson.class);

  private int starType;
  @Resource
  private OtLevel2MatchDao dao;
  private List<Long> objs;

  @Actions({
    @Action(value = "/get-star-list-json", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {
        
    objs=dao.getIdsByStarType(starType);
    
    return "json";
  }

  /**
   * @return the objs
   */
  public List<Long> getObjs() {
    return objs;
  }

  /**
   * @param starType the starType to set
   */
  public void setStarType(int starType) {
    this.starType = starType;
  }

}
