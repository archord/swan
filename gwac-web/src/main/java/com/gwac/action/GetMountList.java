package com.gwac.action;

import com.gwac.dao.MountDao;
import com.gwac.dao.OtTypeDao;
import com.gwac.model.Mount;
import com.gwac.model.OtType;
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
public class GetMountList extends ActionSupport {

  private static final long serialVersionUID = -3454448234588641394L;
  private static final Log log = LogFactory.getLog(GetMountList.class);

  @Resource
  private MountDao mountDao;
  /**
   * 返回结果
   */
  private List<Mount> mounts;

  @Actions({
    @Action(value = "/get-mount-list-json", results = {
      @Result(name = "json", type = "json")})
  })
  @SuppressWarnings("unchecked")
  public String execute() throws Exception {
        
    mounts=mountDao.getAll();
    return "json";
  }

  /**
   * @return the mounts
   */
  public List<Mount> getMounts() {
    return mounts;
  }


}
