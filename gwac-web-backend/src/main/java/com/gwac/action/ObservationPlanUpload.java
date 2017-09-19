/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.activemq.OTListMessageCreator;
import com.gwac.dao.ObservationPlanDao;
import com.gwac.model.FitsFileCut;
import com.gwac.model.FitsFileCutRef;
import com.gwac.model.ObservationPlan;
import com.gwac.model.UploadFileUnstore;
import com.gwac.util.CommonFunction;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.jms.core.MessageCreator;

/**
 * from MultipleCommonFileUploadAction
 *
 * @author xy
 */
public class ObservationPlanUpload extends ActionSupport {

  private static final Log log = LogFactory.getLog(ObservationPlanUpload.class);

  @Resource
  private ObservationPlanDao obsPlanDao;

  private ObservationPlan obsPlan;
  private String echo = "";

  @Action(value = "observationPlanUpload", results = {
    @Result(location = "manage/result.jsp", name = SUCCESS),
    @Result(location = "manage/result.jsp", name = INPUT),
    @Result(location = "manage/result.jsp", name = ERROR)})
  public String upload() {

    boolean flag = true;
    String result = SUCCESS;
    echo = "";

    log.debug("observationPlan:" + obsPlan.toString());

    //必须设置传输机器名称
    if (null == obsPlan || !obsPlan.isValid()) {
      echo = echo + "Error, observationPlan is inValid.\n";
      flag = false;
      result = ERROR;
    } else {
      obsPlanDao.save(obsPlan);
    }
    log.debug(echo);
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

}
