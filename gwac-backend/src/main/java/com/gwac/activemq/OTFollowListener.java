package com.gwac.activemq;

import com.gwac.dao.FollowUpObservationDao;
import com.gwac.dao.ObjectListAllDao;
import com.gwac.model.FollowUpObservation;
import com.gwac.modelyw.ObjectListAll;
import com.gwac.util.CommonFunction;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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
  @Value("#{syscfg.gwacYunWeiFollowupServerIP}")
  private String yunweiIP;
  @Value("#{syscfg.gwacYunWeiFollowupServerPort}")
  private int yunweiPort;

  @Resource
  private FollowUpObservationDao dao = null;
  @Resource
  private ObjectListAllDao dao2 = null;

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
	String dateStr = CommonFunction.getDateString3(new Date());
	FollowUpObservation fuo = dao.getByName(followName);
	ObjectListAll obj = new ObjectListAll();
	obj.setFollowup_name(fuo.getFoName());
	obj.setObj_name(fuo.getObjName());
	obj.setObjra(fuo.getRa());
	obj.setObjdec(fuo.getDec());
	obj.setDate_beg(dateStr);
	obj.setDate_end(dateStr);
	obj.setFilter_band(fuo.getFilter());
	obj.setExpdur(fuo.getExposeDuration());
	obj.setDelay(0);
	obj.setFrmcnt(fuo.getFrameCount());
	obj.setPriority(fuo.getPriority());
	dao2.save(obj);
	sendInsertNotice();
      }

    } catch (JMSException e) {
      log.error("receive followPlan error!", e);
    }
  }

  void sendInsertNotice() {

    Socket socket = null;
    try {
      socket = new Socket(yunweiIP, yunweiPort);
      DataOutputStream out = new DataOutputStream(socket.getOutputStream());
      String msg = "{\"content\": \"Hello World!\", \"error\": 0, \"type\": \"object_generator\", \"pg_action\": [\"insert\"], \"time\": 0}";
      msg = String.format("%06d%s", msg.length(), msg);
      out.write(msg.getBytes());
      out.flush();
      Thread.sleep(500);
      out.close();
      socket.close();

    } catch (IOException ex) {
      log.error("send ot2 followPlan to YunWei, close socket error.", ex);
    } catch (InterruptedException ex) {
      log.error("send ot2 followPlan to YunWei, sleep error.", ex);
    }
  }

  public void onMessage2(Message message) {
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
