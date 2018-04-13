package com.gwac.action;

import com.gwac.dao.ImageRecordDao;
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
public class GetImageList extends ActionSupport {

  private static final long serialVersionUID = 5073694091458591293L;
  private static final Log log = LogFactory.getLog(GetImageList.class);

  private Integer dsId;
  private String parStr;

  @Resource
  private ImageRecordDao dao = null;

    @Actions({
    @Action(value = "/get-image-list", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {

    parStr = dao.getRecords(dsId);

    return "json";
  }

  /**
   * @return the parStr
   */
  public String getParStr() {
    return parStr;
  }

  /**
   * @param dsId the dsId to set
   */
  public void setDsId(Integer dsId) {
    this.dsId = dsId;
  }


}
