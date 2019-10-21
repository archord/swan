package com.gwac.activemq;

import com.gwac.service2.Ot2CheckService;
import javax.annotation.Resource;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service(value = "crossObjectCheckListener")
public class CrossObjectCheckListener implements MessageListener {

  private static final Log log = LogFactory.getLog(CrossObjectCheckListener.class);
  @Resource(name="crossObjectCheckService")
  private Ot2CheckService crossObjectCheckService;

  @Override
  public void onMessage(Message message) {
    try {
      MapMessage map = (MapMessage) message;
      long objId = map.getLong("objId");
      String type = map.getString("type"); //CrossObject
      log.debug("receive check message, objId=" + objId );
      crossObjectCheckService.searchOT2(objId);
    } catch (Exception e) {
      log.error(e);
    }
  }

}
