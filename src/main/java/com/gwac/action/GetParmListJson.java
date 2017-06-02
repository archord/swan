package com.gwac.action;

import com.gwac.dao.ImageStatusParameterDao;
import com.gwac.model.ImageStatusParameter;
import com.gwac.util.CommonFunction;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.Result;

@Result(name = "error", location = "/error.jsp")
@ExceptionMapping(exception = "java.lang.Exception", result = "error")
public class GetParmListJson extends ActionSupport {

  private static final long serialVersionUID = -3454448234583441394L;
  private static final Log log = LogFactory.getLog(GetParmListJson.class);

  private String queryParm;
  private ImageStatusParameterDao dao;
  private String parStr;
  private String minDate;

  @Actions({
    @Action(value = "/get-parm-list-json", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {

    System.out.println("queryParm=" + queryParm);
    ArrayList<String> queryParms = new ArrayList();
    queryParms.add("fwhm");
    queryParms.add("obj_num");
    queryParms.add("bg_bright");
    queryParms.add("avg_limit");
    queryParms.add("s2n");
    queryParms.add("xshift");
    queryParms.add("yshift");
    queryParms.add("xrms");
    queryParms.add("yrms");
    queryParms.add("proc_time");
    queryParms.add("temperature_actual");
    queryParms.add("temperature_set");
    if (queryParms.contains(queryParm)) {
      if (queryParm.equals("posshift")) {
        ArrayList<String> plist = new ArrayList();
        plist.add("xshift");
        plist.add("yshift");
        parStr = dao.getJsonByParm(plist);
      } else if (queryParm.equals("posrms")) {
        ArrayList<String> plist = new ArrayList();
        plist.add("xrms");
        plist.add("yrms");
        parStr = dao.getJsonByParm(plist);
      } else {
        ArrayList<String> plist = new ArrayList();
        plist.add(queryParm);
        parStr = dao.getJsonByParm(plist);
      }
      minDate = CommonFunction.getDateTimeString2(dao.getMinDate());
    } else {
      parStr = "cannot find parameter " + queryParm;
      minDate = "";
    }

    return "json";
  }

  /**
   * @param dao the dao to set
   */
  public void setDao(ImageStatusParameterDao dao) {
    this.dao = dao;
  }

  /**
   * @return the parStr
   */
  public String getParStr() {
    return parStr;
  }

  /**
   * @param queryParm the queryParm to set
   */
  public void setQueryParm(String queryParm) {
    this.queryParm = queryParm;
  }

  /**
   * @return the minDate
   */
  public String getMinDate() {
    return minDate;
  }

}
