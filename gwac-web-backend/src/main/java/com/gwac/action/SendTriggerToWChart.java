/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.dao.WebGlobalParameterDao;
import com.opensymphony.xwork2.ActionSupport;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.config.WxCpInMemoryConfigStorage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;

/**
 * from MultipleCommonFileUploadAction
 *
 * @author xy
 */
public class SendTriggerToWChart extends ActionSupport {

  private static final Log log = LogFactory.getLog(SendTriggerToWChart.class);

  @Resource
  private WebGlobalParameterDao wgpDao;

  private String triggerMsg;

  private String echo = "";

  @Action(value = "sendTrigger2WChart")
  public String upload() {

    echo = "";

    if (triggerMsg == null || triggerMsg.isEmpty()) {
      echo = "triggerMsg is empty, please check.";
    } else {

      String wtag = "weixin1";
      String corpId = wgpDao.getValueByName(wtag, "corpId");
      String corpSecret = wgpDao.getValueByName(wtag, "corpSecret");
      String token = wgpDao.getValueByName(wtag, "token");
      String aesKey = wgpDao.getValueByName(wtag, "aesKey");
      Integer agentId = Integer.parseInt(wgpDao.getValueByName(wtag, "agentId"));

      String ip = ServletActionContext.getRequest().getRemoteAddr();
      Date tdate = new Date();
      log.debug(triggerMsg);

      WxCpInMemoryConfigStorage config = new WxCpInMemoryConfigStorage();
      config.setCorpId(corpId);      // 设置微信企业号的appid
      config.setCorpSecret(corpSecret);  // 设置微信企业号的app corpSecret
      config.setAgentId(agentId);     // 设置微信企业号应用ID
      config.setToken(token);       // 设置微信企业号应用的token
      config.setAesKey(aesKey);      // 设置微信企业号应用的EncodingAESKey

      WxCpServiceImpl wxCpService = new WxCpServiceImpl();
      wxCpService.setWxCpConfigStorage(config);

      String url = "https://qyapi.weixin.qq.com/cgi-bin/appchat/send";
      String msg = "{\"chatid\" : \"gwac001\",\"msgtype\" : \"text\",\"safe\" : \"0\",\"text\" : {\"content\": \"" + triggerMsg + "\"}}";

      String trst;
      try {
        echo = wxCpService.post(url, msg);
        log.debug(echo);
      } catch (WxErrorException ex) {
        log.error("send trigger to wechart error:", ex);
      }
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

  /**
   * @return the echo
   */
  public String getEcho() {
    return echo;
  }

  /**
   * @param triggerMsg the triggerMsg to set
   */
  public void setTriggerMsg(String triggerMsg) {
    this.triggerMsg = triggerMsg;
  }
}
