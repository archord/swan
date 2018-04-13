package com.gwac.action;

import com.gwac.dao.DataSetDao;
import com.opensymphony.xwork2.ActionSupport;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.Result;

/**
 * 对OT2列表分页显示
 *
 * @author xy
 */
@Result(name = "success", type = "json")
public class GetDataSetList extends ActionSupport {

  private static final long serialVersionUID = 5073694091458591293L;
  private static final Log log = LogFactory.getLog(GetDataSetList.class);

  private Integer dvId;
  private Integer defaultType;
  private String parStr;

  @Resource
  private DataSetDao dao = null;

    @Actions({
    @Action(value = "/get-data-set-list", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {

    parStr = dao.getRecords(dvId, defaultType);

    return "json";
  }

  /**
   * @return the parStr
   */
  public String getParStr() {
    return parStr;
  }

  /**
   * @param dvId the dvId to set
   */
  public void setDvId(Integer dvId) {
    this.dvId = dvId;
  }

  /**
   * @param defaultType the defaultType to set
   */
  public void setDefaultType(Integer defaultType) {
    this.defaultType = defaultType;
  }

}
