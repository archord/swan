/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.SystemStatusMonitorDao;
import com.gwac.model.OtLevel2;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.io.IOException;
import java.io.PrintWriter;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

/*parameter：currentDirectory, configFile, [fileUpload], [fileUpload].*/
/* curl command example: */
/* curl http://localhost/otLookBack.action -F ot2name=M151017_C00020 -F flag=1 */
/**
 * @author xy
 */
//@InterceptorRef("jsonValidationWorkflowStack")
//加了这句化，文件传不上来
//@ParentPackage(value="struts-default")
//@Controller()
//@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OTLookBack extends ActionSupport {

  private static final Log log = LogFactory.getLog(OTLookBack.class);

  private String ot2name;
  private Short flag; //图像相减有目标1，图像相减没有目标2, 0代表没处理或处理报错
  private String echo = "";

  @Resource
  private OtLevel2Dao ot2Dao;
  @Resource
  private SystemStatusMonitorDao ssmDao;

  @Action(value = "otLookBack", results = {
    @Result(location = "manage/result.jsp", name = SUCCESS),
    @Result(location = "manage/result.jsp", name = INPUT),
    @Result(location = "manage/result.jsp", name = ERROR)})
  public String upload() {

    String result = SUCCESS;
    setEcho("");

    //必须设置望远镜名称
    if (null == ot2name || ot2name.isEmpty()) {
      setEcho(getEcho() + "Error, must set ot2name.\n");
    } else {
      OtLevel2 ot2 = new OtLevel2();
      ot2.setName(ot2name.trim());
      ot2.setLookBackResult(flag);
      int trst = ot2Dao.updateLookBackResult(ot2);
      log.debug("1 update, ot2name=" + ot2name + ", flag=" + flag + ", result=" + trst);
      for (int i = 0; i < 5; i++) {
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          log.error("sleep error", e);
        }
        OtLevel2 tot2 = ot2Dao.getOtLevel2ByName(ot2name, false);
        if (tot2 == null) {
          break;
        }
        if (tot2.getLookBackResult() == 0) {
          trst = ot2Dao.updateLookBackResult(ot2);
          log.debug((i + 2) + " update, ot2name=" + ot2name + ", flag=" + flag + ", result=" + trst);
        } else {
          log.debug((i + 2) + " update sucess, ot2name=" + ot2name + ", flag=" + flag + ", result=" + trst);
          break;
        }
      }
      echo = "lookback success.\n";

      String ip = ServletActionContext.getRequest().getRemoteAddr();
      String unitId = ip.substring(ip.lastIndexOf('.')+1);
      if(unitId.length()<3){
        unitId = "0" + unitId;
      }
      ssmDao.updateOt2LookBack(unitId, ot2name);
    }

    log.debug(getEcho());
    sendResultMsg(echo);

    return null;
  }

  public void sendResultMsg(String msg) {

    HttpServletResponse response = ServletActionContext.getResponse();
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out;
    try {
      out = response.getWriter();
      out.print(msg);
    } catch (IOException ex) {
      log.error("response error: ", ex);
    }
  }

  /**
   * @param ot2name the ot2name to set
   */
  public void setOt2name(String ot2name) {
    this.ot2name = ot2name;
  }

  /**
   * @param flag the flag to set
   */
  public void setFlag(Short flag) {
    this.flag = flag;
  }

  /**
   * @return the echo
   */
  public String getEcho() {
    return echo;
  }

  /**
   * @param echo the echo to set
   */
  public void setEcho(String echo) {
    this.echo = echo;
  }

}
