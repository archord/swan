/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.opensymphony.xwork2.ActionSupport;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;

/*http://host-url/dpmIsAlive.action?dpm=M01*/
/**
 * @author xy
 */
//@InterceptorRef("jsonValidationWorkflowStack")
//加了这句化，文件传不上来
public class WXMessage extends ActionSupport {

  private static final Log log = LogFactory.getLog(WXMessage.class);

  private String msg_signature;
  private String timestamp;
  private String nonce;
  private String echostr;

  private String echo = "";

  @Action(value = "wxvalidation")
  public String upload() throws Exception {

    String sCorpID = "";
    String sToken = "";
    String sEncodingAESKey = "";
    
    log.debug("msg_signature="+msg_signature);
    log.debug("timestamp="+timestamp);
    log.debug("nonce="+nonce);
    log.debug("echostr="+echostr);

    WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
    String sEchoStr = ""; //需要返回的明文
    try {
      sEchoStr = wxcpt.VerifyURL(msg_signature, timestamp, nonce, echostr);
      log.debug("verifyurl echostr: " + sEchoStr);
    } catch (Exception e) {
      log.error("response error: ", e);
    }

    sendResultMsg(sEchoStr);

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
   * @param msg_signature the msg_signature to set
   */
  public void setMsg_signature(String msg_signature) {
    this.msg_signature = msg_signature;
  }

  /**
   * @param timestamp the timestamp to set
   */
  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * @param nonce the nonce to set
   */
  public void setNonce(String nonce) {
    this.nonce = nonce;
  }

  /**
   * @param echostr the echostr to set
   */
  public void setEchostr(String echostr) {
    this.echostr = echostr;
  }
}
