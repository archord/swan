package com.gwac.action;

import com.gwac.dao.FollowUpObjectDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.model.FollowUpObject;
import com.gwac.model.OtLevel2;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.Result;

@Result(name = "error", location = "/error.jsp")
@ExceptionMapping(exception = "java.lang.Exception", result = "error")
public class GetOtFollowupObjectList extends ActionSupport {

  private static final long serialVersionUID = -3454448234482641394L;
  private static final Log log = LogFactory.getLog(GetOtFollowupObjectList.class);

  private String otName;
  private Boolean queryHis;

  private FollowUpObjectDao fuoDao;
  private OtLevel2Dao obDao;

  private OtLevel2 ot2;
  private List<FollowUpObject> objs;

  @Actions({
    @Action(value = "/get-ot-followup-object-list", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {

    List<Integer> tlist = obDao.hisOrCurExist(otName);
    if (!tlist.isEmpty()) {
      Integer his = tlist.get(0);
      queryHis = his == 1;
      ot2 = obDao.getOtLevel2ByName(otName, queryHis);
      objs = fuoDao.getByOtId(ot2.getOtId(), queryHis);
    } else {
      objs = new ArrayList();
    }

    return "json";
  }

  /**
   * @param otName the otName to set
   */
  public void setOtName(String otName) {
    this.otName = otName;
  }

  /**
   * @param queryHis the queryHis to set
   */
  public void setQueryHis(Boolean queryHis) {
    this.queryHis = queryHis;
  }

  /**
   * @param fuoDao the fuoDao to set
   */
  public void setFuoDao(FollowUpObjectDao fuoDao) {
    this.fuoDao = fuoDao;
  }

  /**
   * @param obDao the obDao to set
   */
  public void setObDao(OtLevel2Dao obDao) {
    this.obDao = obDao;
  }

  /**
   * @return the ot2
   */
  public OtLevel2 getOt2() {
    return ot2;
  }

  /**
   * @return the objs
   */
  public List<FollowUpObject> getObjs() {
    return objs;
  }

}
