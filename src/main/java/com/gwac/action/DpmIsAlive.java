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
import com.gwac.dao.SyncFileDao;
import com.gwac.model.SyncFile;
import com.gwac.service.SendMessage;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

/*http://host-url/dpmIsAlive.action?dpm=M01*/
/**
 * @author xy
 */
//@InterceptorRef("jsonValidationWorkflowStack")
//加了这句化，文件传不上来
public class DpmIsAlive extends ActionSupport {

    private static final Log log = LogFactory.getLog(DpmIsAlive.class);

    @Resource
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
     * @param dpm the dpm to set
     */
    public void setDpm(String dpm) {
        this.dpm = dpm;
    }

}
