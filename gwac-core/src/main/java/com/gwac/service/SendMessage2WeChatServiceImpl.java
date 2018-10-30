/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

import com.gwac.dao.WebGlobalParameterDao;
import javax.annotation.Resource;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.config.WxCpInMemoryConfigStorage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author xy
 */
@Service(value = "sendMsg2WeChat")
public class SendMessage2WeChatServiceImpl implements SendMessageService{
  
  private static final Log log = LogFactory.getLog(SendMessage2WeChatServiceImpl.class);

  @Resource
  private WebGlobalParameterDao wgpdao;
  
  @Override
  public void send(String msg, String destId) {
    
    String wtag = "weixin1";
    String corpId = wgpdao.getValueByName(wtag, "corpId");
    String corpSecret = wgpdao.getValueByName(wtag, "corpSecret");
    String token = wgpdao.getValueByName(wtag, "token");
    String aesKey = wgpdao.getValueByName(wtag, "aesKey");
    Integer agentId = Integer.parseInt(wgpdao.getValueByName(wtag, "agentId"));

    log.debug(msg);

    WxCpInMemoryConfigStorage config = new WxCpInMemoryConfigStorage();
    config.setCorpId(corpId);      // 设置微信企业号的appid
    config.setCorpSecret(corpSecret);  // 设置微信企业号的app corpSecret
    config.setAgentId(agentId);     // 设置微信企业号应用ID
    config.setToken(token);       // 设置微信企业号应用的token
    config.setAesKey(aesKey);      // 设置微信企业号应用的EncodingAESKey

    WxCpServiceImpl wxCpService = new WxCpServiceImpl();
    wxCpService.setWxCpConfigStorage(config);

    String url = "https://qyapi.weixin.qq.com/cgi-bin/appchat/send";
    String sendData = "{\"chatid\" : \"" + destId + "\",\"msgtype\" : \"text\",\"safe\" : \"0\",\"text\" : {\"content\": \"" + msg + "\"}}";

    try {
      String echo = wxCpService.post(url, sendData);
      log.debug(echo);
    } catch (WxErrorException ex) {
      log.error("send trigger to wechart error:", ex);
    }
  }
  
}
