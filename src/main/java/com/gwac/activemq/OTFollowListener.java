package com.gwac.activemq;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.jms.JMSException;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OTFollowListener implements MessageListener {

  private static final Log log = LogFactory.getLog(OTFollowListener.class);
  private Boolean isBeiJingServer;
  private Boolean isTestServer;
  private String serverIP;
  private int serverPort;

  @Override
  public void onMessage(Message message) {
    try {
      MapMessage map = (MapMessage) message;
      String followPlan = map.getString("followPlan");
      log.debug("receive followPlan=" + followPlan);

      if (!isBeiJingServer&&!isTestServer) {
        Socket socket = null;
        DataOutputStream out = null;
        try {
          socket = new Socket(serverIP, serverPort);
          out = new DataOutputStream(socket.getOutputStream());

          try {
            out.write(followPlan.getBytes());
            out.flush();
            log.debug("send ot2 followPlan to " + serverIP + ":" + serverPort + ", message:\n" + followPlan);
          } catch (IOException ex) {
            log.error("send ot2 followPlan error.", ex);
          }
          try {
            out.close();
            socket.close();
          } catch (IOException ex) {
            log.error("send ot2 followPlan, close socket error.", ex);
          }
        } catch (IOException ex) {
          log.debug("send ot2 followPlan to " + serverIP + ":" + serverPort + ", message:\n" + followPlan);
          log.error("send ot2 followPlan, cannot connect to server.", ex);
        }
      }

    } catch (JMSException e) {
      log.error("receive followPlan error!", e);
    }
  }

  /**
   * @param isBeiJingServer the isBeiJingServer to set
   */
  public void setIsBeiJingServer(Boolean isBeiJingServer) {
    this.isBeiJingServer = isBeiJingServer;
  }

  /**
   * @param isTestServer the isTestServer to set
   */
  public void setIsTestServer(Boolean isTestServer) {
    this.isTestServer = isTestServer;
  }

  /**
   * @param serverIP the serverIP to set
   */
  public void setServerIP(String serverIP) {
    this.serverIP = serverIP;
  }

  /**
   * @param serverPort the serverPort to set
   */
  public void setServerPort(int serverPort) {
    this.serverPort = serverPort;
  }

}
