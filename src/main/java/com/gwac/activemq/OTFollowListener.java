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

}
