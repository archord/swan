package com.gwac.action;

import com.gwac.activemq.OTFollowMessageCreator;
import com.gwac.dao.FollowUpObservationDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtNumberDao;
import com.gwac.model.FollowUpObservation;
import com.gwac.model.OtLevel2;
import com.gwac.model4.OtLevel2FollowParameter;
import com.gwac.model.UserInfo;
import com.gwac.util.CommonFunction;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.util.*;
import javax.annotation.Resource;
import javax.jms.Destination;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * 目前已经被GetOtLevel2List取代
 *
 * @author xy
 */
@Action(value = "otFollowUp", results = {
  @Result(name = "success", type = "json")})
public class OTFollowUp extends ActionSupport implements SessionAware {

  private static final long serialVersionUID = 1437264279538543593L;
  private static final Log log = LogFactory.getLog(OTFollowUp.class);

  private Map<String, Object> session;

  private String result = "";
  private OtLevel2FollowParameter ot2fp;
  private Integer foId;

  @Resource
  private FollowUpObservationDao foDao;
  @Resource
  private OtLevel2Dao ot2Dao;
  @Resource
  private OtNumberDao otnDao;

  @Resource
  private JmsTemplate jmsTemplate;
  @Resource(name = "otFollowDest")
  private Destination otFollowDest;

  @SuppressWarnings("unchecked")
  public String execute() {

    if (ot2fp != null) {
      log.debug(ot2fp.toString());
      UserInfo tuser = (UserInfo) session.get("userInfo");
      if (null != tuser) {
        ot2fp.setUserName(tuser.getLoginName());
      }

      OtLevel2 ot2 = null;
      if (!ot2fp.getOtName().trim().isEmpty()) {
        ot2 = ot2Dao.getOtLevel2ByName(ot2fp.getOtName().trim(), true);
        if (null != ot2 && ot2fp.getFollowName().trim().isEmpty()) {
          ot2.setFoCount((short) (ot2.getFoCount() + 1));
          ot2Dao.updateFoCount(ot2);
          ot2fp.setFollowName(String.format("%s_%03d", ot2fp.getOtName(), ot2.getFoCount()));
          if (Math.abs(ot2fp.getRa() + 99) < 0.00001 && Math.abs(ot2fp.getDec() + 99) < 0.00001) {
            ot2fp.setRa(ot2.getRa());
            ot2fp.setDec(ot2.getDec());
          }
          foId = null; //更改后随名称后，自动由更新改为增加
        }
      }

      if (ot2fp.getFollowName().trim().isEmpty()) {
        String dateStr = CommonFunction.getUniqueDateStr().substring(2);
        int followNum = otnDao.getFollowupNumberByDate(dateStr);
        String tName = String.format("F%s_X%05d", dateStr, followNum);
        ot2fp.setFollowName(tName);
        ot2fp.setOtName("null"); //用于发送后随计划时填充字段
        foId = null;  //更改后随名称后，自动由更新改为增加
      }

      FollowUpObservation fo = new FollowUpObservation();
      fo.setBackImageCount(0);
      fo.setRa(ot2fp.getRa());
      fo.setDec(ot2fp.getDec());
      fo.setEpoch(ot2fp.getEpoch());
      fo.setExposeDuration((short) ot2fp.getExpTime());
      fo.setFilter(ot2fp.getFilter());
      fo.setFoName(ot2fp.getFollowName().trim());
      fo.setFoObjCount((short) 0);
      fo.setFrameCount((short) ot2fp.getFrameCount());
      fo.setImageType(ot2fp.getImageType());
      if (ot2 != null) {
        fo.setOtId(ot2.getOtId());
      }
      fo.setPriority((short) ot2fp.getPriority());
      if (null != tuser) {
        fo.setUserId(tuser.getUiId());
      }
      fo.setTriggerTime(new Date());
      fo.setTriggerType(ot2fp.getTriggerType()); //0:AUTO; 1:MANUAL, 2:PLANNING
      fo.setTelescopeId(ot2fp.getTelescope());
      fo.setProcessResult('0');
      if (ot2fp.getComment() != null && !ot2fp.getComment().isEmpty()) {
        fo.setComment(ot2fp.getComment());
      }

      if (ot2fp.getBegineTime() != null && !ot2fp.getBegineTime().isEmpty()) {
        Date tdate = CommonFunction.stringToDate(ot2fp.getBegineTime(), "yyyy-MM-dd HH:mm:ss");
        fo.setBeginTime(tdate);
      } else {
        fo.setBeginTime(fo.getTriggerTime());
        ot2fp.setBegineTime("-1");
      }
      if (ot2fp.getEndTime() != null && !ot2fp.getEndTime().isEmpty()) {
        Date tdate = CommonFunction.stringToDate(ot2fp.getEndTime(), "yyyy-MM-dd HH:mm:ss");
        fo.setEndTime(tdate);
      }else{
        ot2fp.setEndTime("-1");
      }

      long tsecond = (fo.getBeginTime().getTime() - new Date().getTime()) / 1000;
      if (tsecond < 60) {
        try {
          MessageCreator tmc = new OTFollowMessageCreator(ot2fp);
          jmsTemplate.send(otFollowDest, tmc);
          log.debug(ot2fp.getTriggerMsg());
          setResult("success!");
        } catch (Exception e) {
          setResult("send followup message error!");
          log.error("send followup message error:", e);
        }
        fo.setExecuteStatus('1');
      } else {
        fo.setExecuteStatus('0');
      }

      if (foId != null) {
        fo.setFoId(foId);
        foDao.update(fo);
      } else {
        foDao.save(fo);
      }
    } else {
      setResult("parameter error!");
    }

    return SUCCESS;
  }

  @Override
  public void setSession(Map<String, Object> map) {
    this.session = map;
  }

  /**
   * @return the ot2fp
   */
  public OtLevel2FollowParameter getOt2fp() {
    return ot2fp;
  }

  /**
   * @return the result
   */
  public String getResult() {
    return result;
  }

  /**
   * @param result the result to set
   */
  public void setResult(String result) {
    this.result = result;
  }

  /**
   * @param ot2fp the ot2fp to set
   */
  public void setOt2fp(OtLevel2FollowParameter ot2fp) {
    this.ot2fp = ot2fp;
  }

  /**
   * @param foId the foId to set
   */
  public void setFoId(Integer foId) {
    this.foId = foId;
  }

}
