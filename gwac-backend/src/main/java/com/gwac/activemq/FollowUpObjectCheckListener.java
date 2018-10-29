package com.gwac.activemq;

import com.gwac.job.FollowUpObjectCheckServiceImpl;
import javax.annotation.Resource;
import javax.jms.JMSException;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service(value = "followUpObjCheckListener")
public class FollowUpObjectCheckListener implements MessageListener {

  private static final Log log = LogFactory.getLog(FollowUpObjectCheckListener.class);

  @Resource(name = "fupObjCheckService")
  private FollowUpObjectCheckServiceImpl service;

  @Override
  public void onMessage(Message message) {
    try {
      MapMessage map = (MapMessage) message;
      long fupObsId = map.getLong("fupObsId");
      log.debug("receive message, fupObsId=" + fupObsId);
      
      long startTime = System.nanoTime();
      service.checkObjects(fupObsId);
      
      long endTime = System.nanoTime();
      log.debug("check followUpObservation Id="+fupObsId+" all Objects, consume " + 1.0 * (endTime - startTime) / 1e9 + " seconds.");
      
    } catch (JMSException e) {
      log.error(e);
    }
  }

}
