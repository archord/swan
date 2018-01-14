package com.gwac.activemq;

import com.gwac.dao.FollowUpObservationDao;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.annotation.Resource;
import javax.jms.JMSException;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service(value = "oTFollowListener")
public class OTFollowListener implements MessageListener {

  private static final Log log = LogFactory.getLog(OTFollowListener.class);

  @Value("#{syscfg.gwacServerBeijing}")
  private Boolean isBeiJingServer;
  @Value("#{syscfg.gwacServerTest}")
  private Boolean isTestServer;
  @Value("#{syscfg.gwacFollowServerIp}")
  private String serverIP;
  @Value("#{syscfg.gwacFollowServerPort}")
  private int serverPort;
  @Value("#{syscfg.gwacFollow30ServerIp}")
  private String server30IP;
  @Value("#{syscfg.gwacFollow30ServerPort}")
  private int server30Port;
  
  @Resource
  private FollowUpObservationDao dao = null;

  @Override
  public void onMessage(Message message) {
    try {
      
      MapMessage map = (MapMessage) message;
      String followName = map.getString("followName");
      String followPlan = map.getString("followPlan");
      Short tspId = map.getShort("tspId");
      log.debug("receive followName=" + followName);
      log.debug("receive followPlan=" + followPlan);
      log.debug("receive tspId=" + tspId);
      
      char executeStatus = '1';
      dao.updateExecuteStatus(followName, executeStatus);

      if (!isBeiJingServer && !isTestServer) {
        Socket socket = null;
        DataOutputStream out = null;
        String tIP;
        int tPort;
        if (tspId == 1) {
          tIP = serverIP;
          tPort = serverPort;
        } else if (tspId == 2) {
          tIP = server30IP;
          tPort = server30Port;
        } else {
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
          log.error("send ot2 followPlan to " + tIP + ":" + tPort + ", message:\n" + followPlan);
          log.error("send ot2 followPlan, cannot connect to server.", ex);
        }
      }

    } catch (JMSException e) {
      log.error("receive followPlan error!", e);
    }
  }

}
