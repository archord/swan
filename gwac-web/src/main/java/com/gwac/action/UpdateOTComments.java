package com.gwac.action;

import com.gwac.dao.OtLevel2Dao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionSupport;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.ParentPackage;

@ParentPackage("default")
//@InterceptorRef("jsonValidationWorkflowStack")
@Result(name = "error", location = "/error.jsp")
@ExceptionMapping(exception = "java.lang.Exception", result = "error")
public class UpdateOTComments extends ActionSupport {

  private static final long serialVersionUID = 7968568324444173511L;
  private static final Log log = LogFactory.getLog(UpdateOTComments.class);

  @Resource
  private OtLevel2Dao ot2Dao;

  private int otId;
  private String comments;
  private String echo;
  private Map msg;
  private Boolean queryHis;

  @Action(value = "/update-ot2-comments", results = {
    @Result(name = "json", type = "json", params = {"root", "msg"})
  })
  public String userLogout() throws Exception {

    List<Integer> tlist = ot2Dao.hisOrCurExist(otId);
    if (!tlist.isEmpty()) {
      msg = new HashMap<>();
      msg.put("flag", "1");
      Integer his = tlist.get(0);
      queryHis = his == 1;
      if (queryHis) {
	ot2Dao.updateCommentsHis(otId, comments);
      } else {
	ot2Dao.updateComments(otId, comments);
      }
    } else {
      msg = new HashMap<>();
      msg.put("flag", "0");
    }
    return "json";
  }

  public String getEcho() {
    return echo;
  }

  /**
   * @return the msg
   */
  public Map getMsg() {
    return msg;
  }
  /**
   * @param otId the otId to set
   */
  public void setOtId(int otId) {
    this.otId = otId;
  }

  /**
   * @param comments the comments to set
   */
  public void setComments(String comments) {
    this.comments = comments;
  }

}
