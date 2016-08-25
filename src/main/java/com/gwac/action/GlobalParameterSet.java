/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.dao.DataProcessMachineDAO;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import javax.servlet.ServletContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.util.ServletContextAware;

/*http://host-url/dpmIsAlive.action?dpm=M01*/
/**
 * @author xy
 */
//@InterceptorRef("jsonValidationWorkflowStack")
//加了这句化，文件传不上来
public class GlobalParameterSet extends ActionSupport implements ServletContextAware{

    private static final Log log = LogFactory.getLog(GlobalParameterSet.class);

    private ServletContext globalContext;
    
    private DataProcessMachineDAO dpmDao;

    protected String dpm = "";
    private String echo = "";

    @Action(value = "dpmIsAlive", results = {
        @Result(location = "manage/result.jsp", name = SUCCESS),
        @Result(location = "manage/result.jsp", name = INPUT),
        @Result(location = "manage/result.jsp", name = ERROR)})
    public String upload() throws Exception {

        String result = SUCCESS;

        if (dpm.isEmpty()) {
            result = ERROR;
            echo += "Error, dpm(name) is empty.\n";
        } else {
            dpmDao.updateLastActiveTime(dpm);
            echo += "success\n";
        }
        
        /* 如果使用struts2的标签，返回结果会有两个空行，这个显示在命令行不好看。
         * 用jsp的out，则不会有两个空行。
         * 在这里将结果信息存储在session中，在jsp页面获得返回信息。
         */
        ActionContext ctx = ActionContext.getContext();
        ctx.getSession().put("echo", getEcho());

        return result;
    }

    public String display() {
        return NONE;
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

    /**
     * @param dpmDao the dpmDao to set
     */
    public void setDpmDao(DataProcessMachineDAO dpmDao) {
        this.dpmDao = dpmDao;
    }

    /**
     * @param dpm the dpm to set
     */
    public void setDpm(String dpm) {
        this.dpm = dpm;
    }

  @Override
  public void setServletContext(ServletContext sc) {
    this.globalContext = sc;
  }

}
