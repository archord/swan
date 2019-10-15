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
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
  private String chatId;

  private String mediaType; //媒体文件类型，分别有图片（image）、语音（voice）、视频（video），普通文件（file）
  private List<File> fileUpload = new ArrayList<>();
  private List<String> fileUploadContentType = new ArrayList<>();
  private List<String> fileUploadFileName = new ArrayList<>();

  private String echo = "";

  @Action(value = "sendTrigger2WChart")
  public String upload() {

    echo = "";
    if ((triggerMsg == null || triggerMsg.isEmpty()) && (fileUpload.isEmpty())) {
      echo = "triggerMsg and file is empty, please check.";
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

      if (chatId == null || chatId.isEmpty()) {
	setChatId("gwac001");
      }
      String trst;
      String url = "https://qyapi.weixin.qq.com/cgi-bin/appchat/send";
      try {
	if ((triggerMsg != null) && (!triggerMsg.isEmpty())) {

	  if (triggerMsg.equals("createChat")) {
	    String urlCreateQun = "https://qyapi.weixin.qq.com/cgi-bin/appchat/create";
	    String msgCreateQun = "{\"name\" : \"" + chatId + "\",\"owner\" : \"XuYang\",\"userlist\" : [\"XuYang\", \"Long\", \"zheng_ya_tong\"],\"chatid\" : \"" + chatId + "\"}";
	    echo = wxCpService.post(urlCreateQun, msgCreateQun);
	    log.info(echo);
	  } else {
	    String msg = "{\"chatid\" : \"" + chatId + "\",\"msgtype\" : \"text\",\"safe\" : \"0\",\"text\" : {\"content\": \"" + triggerMsg + "\"}}";
	    echo = wxCpService.post(url, msg);
	    log.debug(echo);
	  }
	}

	if (fileUpload.size() > 0) {

	  if (mediaType == null || mediaType.isEmpty()) {
	    mediaType = "image";
	  }
	  for (File file : fileUpload) {
	    String mediaId = wxCpService.getMediaService().upload(mediaType, file).getMediaId();
	    if ((mediaId != null) && (!mediaId.isEmpty())) {
	      String msg = "{\"chatid\" : \"" + chatId + "\",\"msgtype\" : \"" + mediaType + "\",\"safe\" : \"0\",\"" + mediaType + "\" : {\"media_id\": \"" + mediaId + "\"}}";
	      echo = wxCpService.post(url, msg);
	      log.debug(echo);
	    } else {
	      echo = "upload failure";
	      log.error(echo);
	    }
	  }
	}

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

  /**
   * @param chatId the chatId to set
   */
  public void setChatId(String chatId) {
    this.chatId = chatId;
  }

  /**
   * @param mediaType the mediaType to set
   */
  public void setMediaType(String mediaType) {
    this.mediaType = mediaType;
  }

  /**
   * @param fileUpload the fileUpload to set
   */
  public void setFileUpload(List<File> fileUpload) {
    this.fileUpload = fileUpload;
  }

  /**
   * @param fileUploadContentType the fileUploadContentType to set
   */
  public void setFileUploadContentType(List<String> fileUploadContentType) {
    this.fileUploadContentType = fileUploadContentType;
  }

  /**
   * @param fileUploadFileName the fileUploadFileName to set
   */
  public void setFileUploadFileName(List<String> fileUploadFileName) {
    this.fileUploadFileName = fileUploadFileName;
  }
}
