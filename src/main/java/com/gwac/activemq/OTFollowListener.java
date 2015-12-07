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
  private String server30IP;
  private int server30Port;

  @Override
  public void onMessage(Message message) {
    try {
      MapMessage map = (MapMessage) message;
      String followPlan = map.getString("followPlan");
      Short tspId = map.getShort("tspId");
      log.debug("receive followPlan=" + followPlan);
      log.debug("receive tspId=" + tspId);

      if (!isBeiJingServer&&!isTestServer) {
        Socket socket = null;
        DataOutputStream out = null;
        String tIP;
        int tPort;
        if(tspId==1){
          tIP = serverIP;
          tPort = serverPort;
        }else if(tspId==2){
          tIP = server30IP;
          tPort = server30Port;
        }else{
          log.debug("telescope error: tspId=" + tspId);
          return;
        }
        try {
          socket = new Socket(tIP, tPort);
          out = new DataOutputStream(socket.getOutputStream());

          try {
            out.write(followPlan.getBytes());
            out.flush();
            log.debug("send ot2 followPlan to " + tIP + ":" + tPort + ", message:\n" + followPlan);
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
          log.debug("send ot2 followPlan to " + tIP + ":" + tPort + ", message:\n" + followPlan);
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

  /**
   * @param server30IP the server30IP to set
   */
  public void setServer30IP(String server30IP) {
    this.server30IP = server30IP;
  }

  /**
   * @param server30Port the server30Port to set
   */
  public void setServer30Port(int server30Port) {
    this.server30Port = server30Port;
  }

}
