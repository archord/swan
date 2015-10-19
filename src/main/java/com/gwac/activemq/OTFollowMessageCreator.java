package com.gwac.activemq;

import com.gwac.model.UploadFileUnstore;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.MessageCreator;

public class OTFollowMessageCreator implements MessageCreator {
  
  private static final Log log = LogFactory.getLog(OTFollowMessageCreator.class);
  private final String followPlan;
  
  public OTFollowMessageCreator(String followPlan) {
    this.followPlan = followPlan;
  }
  
  @Override
  public Message createMessage(Session session) throws JMSException {
    
    MapMessage message = session.createMapMessage();
    message.setString("followPlan", followPlan);
    log.debug("send followPlan message: " + followPlan);
    return message;
  }
  
}
