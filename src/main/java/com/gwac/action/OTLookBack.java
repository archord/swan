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
import com.gwac.dao.OtLevel2Dao;
import com.gwac.model.ApplicationParameters;
import com.gwac.model.FollowUpObservation;
import com.gwac.model.OtLevel2;
import com.gwac.model.OtLevel2FollowParameter;
import com.gwac.model.UserInfo;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Date;
import java.util.Map;
import javax.jms.Destination;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

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
public class OTLookBack extends ActionSupport implements SessionAware, ApplicationAware {

  private static final Log log = LogFactory.getLog(OTLookBack.class);

  private String ot2name;
  private Short flag; //图像相减有目标1，图像相减没有目标2, 0代表没处理或处理报错
  private String echo = "";

  private Map<String, Object> appMap;
  private Map<String, Object> session;

  private OtLevel2Dao ot2Dao;
  private FollowUpObservationDao foDao;
  private JmsTemplate jmsTemplate;
  private Destination otFollowDest;

  @Action(value = "otLookBack", results = {
    @Result(location = "manage/result.jsp", name = SUCCESS),
    @Result(location = "manage/result.jsp", name = INPUT),
    @Result(location = "manage/result.jsp", name = ERROR)})
  public String upload() {

    String result = SUCCESS;
    setEcho("");

    //必须设置望远镜名称
    if (null == ot2name || ot2name.isEmpty()) {
      setEcho(getEcho() + "Error, must set tspname.\n");
    } else {
      ApplicationParameters appParam = (ApplicationParameters) appMap.get("appParam");
      if (flag == 1 && appParam != null && appParam.isAutoFollowUp()) {
        autoFollowUp();
      }
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
        if (tot2.getLookBackResult() == 0) {
          trst = ot2Dao.updateLookBackResult(ot2);
          log.debug((i + 2) + " update, ot2name=" + ot2name + ", flag=" + flag + ", result=" + trst);
        } else {
          log.debug((i + 2) + " update sucess, ot2name=" + ot2name + ", flag=" + flag + ", result=" + trst);
          break;
        }
      }
      echo = "success.\n";
    }

    log.debug(getEcho());
    /* 如果使用struts2的标签，返回结果会有两个空行，这个显示在命令行不好看。
     * 用jsp的out，则不会有两个空行。
     * 在这里将结果信息存储在session中，在jsp页面获得返回信息。
     */
    ActionContext ctx = ActionContext.getContext();
    ctx.getSession().put("echo", getEcho());

    return result;
  }

  public Boolean autoFollowUp() {

    Boolean flag = false;
    UserInfo user = (UserInfo) session.get("userInfo");
    if (user != null) {
      flag = true;
      OtLevel2 ot2 = ot2Dao.getOtLevel2ByName(ot2name, false);

      if ((ot2.getDataProduceMethod() == '1' && ot2.getIsMatch() == 1)
              || (ot2.getDataProduceMethod() == '8' && ot2.getIsMatch() == 2 && ot2.getRc3Match() > 0)) {
        ot2.setFoCount((short) (ot2.getFoCount() + 1));
        ot2Dao.updateFoCount(ot2);

        OtLevel2FollowParameter ot2fp = new OtLevel2FollowParameter();
        ot2fp.setFollowName(String.format("%s_%03d", ot2name, ot2.getFoCount()));
        ot2fp.setDec(ot2.getDec());
        ot2fp.setRa(ot2.getRa());
        ot2fp.setExpTime((short) 20);
        ot2fp.setFilter("R");
        ot2fp.setFrameCount(5);
        ot2fp.setTelescope((short) 1);
        ot2fp.setPriority(20);
        ot2fp.setUserName(user.getLoginName());
        ot2fp.setOtName(ot2name);

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
        fo.setUserId(user.getUiId());
        fo.setTriggerTime(new Date());
        fo.setTriggerType("AUTO"); //MANUAL AUTO
        fo.setTelescopeId(ot2fp.getTelescope());
        foDao.save(fo);

        MessageCreator tmc = new OTFollowMessageCreator(ot2fp);
        jmsTemplate.send(otFollowDest, tmc);
      }
    } else {
      log.debug("user not login, cannot auto follow up, please login!");
    }
    return flag;
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

  @Override
  public void setSession(Map<String, Object> map) {
    this.session = map;
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

  @Override
  public void setApplication(Map<String, Object> map) {
    this.appMap = map;
  }

}
