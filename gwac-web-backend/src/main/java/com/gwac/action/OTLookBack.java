/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.activemq.OTFollowMessageCreator;
import com.gwac.dao.FollowUpObservationDao;
import com.gwac.dao.Ot2StreamNodeTimeDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.SystemStatusMonitorDao;
import com.gwac.dao.UserInfoDAO;
import com.gwac.dao.WebGlobalParameterDao;
import com.gwac.model.FollowUpObservation;
import com.gwac.model.OtLevel2;
import com.gwac.model4.OtLevel2FollowParameter;
import com.gwac.model.UserInfo;
import com.opensymphony.xwork2.ActionSupport;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;
import javax.annotation.Resource;
import javax.jms.Destination;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/*parameter：currentDirectory, configFile, [fileUpload], [fileUpload].*/
 /* curl command example: */
 /* curl http://localhost/otLookBack.action -F ot2name=M151017_C00020 -F flag=1 */
/**
 * @author xy
 */
public class OTLookBack extends ActionSupport {

  private static final Log log = LogFactory.getLog(OTLookBack.class);

  private String ot2name;
  private Short flag; //图像相减有目标1，图像相减没有目标2, 0代表没处理或处理报错
  private String echo = "";

  private Map<String, Object> appMap;

  @Resource
  private OtLevel2Dao ot2Dao;
  @Resource
  private FollowUpObservationDao foDao;
  @Resource
  private UserInfoDAO userDao;
  @Resource
  private JmsTemplate jmsTemplate;
  @Resource
  private Destination otFollowDest;
  @Resource
  private SystemStatusMonitorDao ssmDao;
  @Resource
  private WebGlobalParameterDao webGlobalParameterDao;
  @Resource
  private Ot2StreamNodeTimeDao ot2StreamNodeTimeDao;

  @Action(value = "otLookBack")
  public void upload() {

    setEcho("");

    //必须设置望远镜名称
    if (null == ot2name || ot2name.isEmpty()) {
      setEcho(getEcho() + "Error, must set ot2name.\n");
    } else {
      String parmValue = webGlobalParameterDao.getValueByName("AutoFollowUp");
      if (flag == 1 && parmValue.equalsIgnoreCase("true")) {
        autoFollowUp();
      }

      OtLevel2 ot2 = new OtLevel2();
      ot2.setName(ot2name.trim());
      ot2.setLookBackResult(flag);
      int trst = ot2Dao.updateLookBackResult(ot2);
      log.debug("1 update, ot2name=" + ot2name + ", flag=" + flag + ", updatedRowNumber=" + trst + ", AutoFollowUp=" + parmValue);
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
          log.debug((i + 2) + " update, ot2name=" + ot2name + ", flag=" + flag + ", updatedRowNumber=" + trst);
        } else {
          log.debug((i + 2) + " update sucess, ot2name=" + ot2name + ", flag=" + flag + ", updatedRowNumber=" + trst);
          break;
        }
      }
      echo = "lookback success.\n";

      String ip = ServletActionContext.getRequest().getRemoteAddr();
      String unitId = ip.substring(ip.lastIndexOf('.') + 1);
      if (unitId.length() < 3) {
        unitId = "0" + unitId;
      }
      ssmDao.updateOt2LookBack(unitId, ot2name);
      ot2StreamNodeTimeDao.updateLookBackTime(ot2name);
    }

    log.debug(getEcho());
    sendResultMsg(echo);
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

  public void autoFollowUp() {

    log.debug("start auto follow up, ot2name=" + ot2name);
    short isMatch = ot2Dao.getIsMatchByName(ot2name);
    if (isMatch == -1) {
      return;
    }
    if (isMatch == 0) {
      log.warn("query isMatch 1, ot2name=" + ot2name + ", isMatch=" + isMatch);
      for (int i = 0; i < 20; i++) {
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          log.error("sleep error", e);
        }
        isMatch = ot2Dao.getIsMatchByName(ot2name);
        if (isMatch > 0) {
          log.warn("query isMatch "+(i+2)+", ot2name=" + ot2name + ", isMatch=" + isMatch);
          break;
        }else{
          log.warn("query isMatch "+(i+2)+", ot2name=" + ot2name + ", isMatch=" + isMatch);
        }
      }
    }

    OtLevel2 ot2 = ot2Dao.getOtLevel2ByName(ot2name, false);
    log.debug("isMatch1="+isMatch+", isMatch2=" + ot2.getIsMatch());
//    if ((ot2.getDataProduceMethod() == '1' && ot2.getIsMatch() == 1)
//            || (ot2.getDataProduceMethod() == '8' && ot2.getIsMatch() == 2 && ot2.getRc3Match() > 0)) {
    if ((ot2.getDataProduceMethod() == '1' && isMatch == 1)) {
      ot2StreamNodeTimeDao.updateLookUpTime(ot2.getOtId());

      ot2.setFoCount((short) (ot2.getFoCount() + 1));
      ot2Dao.updateFoCount(ot2);

      String filter = webGlobalParameterDao.getValueByName("Filter");
      String frameCount = webGlobalParameterDao.getValueByName("FrameCount");
      String exposeDuration = webGlobalParameterDao.getValueByName("ExposeDuration");
      String telescope = webGlobalParameterDao.getValueByName("Telescope");
      String priority = webGlobalParameterDao.getValueByName("Priority");

      OtLevel2FollowParameter ot2fp = new OtLevel2FollowParameter();
      ot2fp.setFollowName(String.format("%s_%03d", ot2name, ot2.getFoCount()));
      ot2fp.setDec(ot2.getDec());
      ot2fp.setRa(ot2.getRa());
      ot2fp.setExpTime(Short.parseShort(exposeDuration));
      ot2fp.setFilter(filter);
      ot2fp.setFrameCount(Integer.parseInt(frameCount));
      ot2fp.setTelescope(Short.parseShort(telescope));
      ot2fp.setPriority(Integer.parseInt(priority));
      ot2fp.setOtName(ot2name);
      ot2fp.setUserName("gwac");

      UserInfo user = userDao.getUserByLoginName("gwac");
      FollowUpObservation fo = new FollowUpObservation();
      fo.setBackImageCount(0);
      fo.setDec(ot2fp.getDec());
      fo.setEpoch(ot2fp.getEpoch());
      fo.setExposeDuration((short) ot2fp.getExpTime());
      fo.setFilter(ot2fp.getFilter());
      fo.setFoName(ot2fp.getFollowName());
      fo.setFoObjCount((short) 0);
      fo.setFrameCount((short) ot2fp.getFrameCount());
      fo.setImageType(ot2fp.getImageType());
      fo.setOtId(ot2.getOtId());
      fo.setPriority((short) ot2fp.getPriority());
      fo.setRa(ot2fp.getRa());
      if (user != null) {
        fo.setUserId(user.getUiId());
      }
      fo.setTriggerTime(new Date());
      fo.setTriggerType('0'); //0:AUTO; 1:MANUAL, 2:PLANNING
      fo.setTelescopeId(ot2fp.getTelescope());
      fo.setBeginTime(fo.getTriggerTime());
      fo.setExecuteStatus('1');
      fo.setProcessResult('0');
      fo.setObjName(ot2fp.getOtName().trim());
      foDao.save(fo);

      MessageCreator tmc = new OTFollowMessageCreator(ot2fp);
      jmsTemplate.send(otFollowDest, tmc);
      log.debug(ot2fp.getTriggerMsg());
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

  /**
   * @param ot2Dao the ot2Dao to set
   */
  public void setOt2Dao(OtLevel2Dao ot2Dao) {
    this.ot2Dao = ot2Dao;
  }

  /**
   * @param foDao the foDao to set
   */
  public void setFoDao(FollowUpObservationDao foDao) {
    this.foDao = foDao;
  }

  /**
   * @param jmsTemplate the jmsTemplate to set
   */
  public void setJmsTemplate(JmsTemplate jmsTemplate) {
    this.jmsTemplate = jmsTemplate;
  }

  /**
   * @param otFollowDest the otFollowDest to set
   */
  public void setOtFollowDest(Destination otFollowDest) {
    this.otFollowDest = otFollowDest;
  }

  /**
   * @param userDao the userDao to set
   */
  public void setUserDao(UserInfoDAO userDao) {
    this.userDao = userDao;
  }

}
