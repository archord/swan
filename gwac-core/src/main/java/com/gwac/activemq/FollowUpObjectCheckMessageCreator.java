package com.gwac.activemq;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.MessageCreator;

public class FollowUpObjectCheckMessageCreator implements MessageCreator {
  
  private static final Log log = LogFactory.getLog(FollowUpObjectCheckMessageCreator.class);
  private final long fupObsId;
  
  public FollowUpObjectCheckMessageCreator(long fupObsId) {
    this.fupObsId = fupObsId;
  }
  
  @Override
  public Message createMessage(Session session) throws JMSException {
    
    MapMessage message = session.createMapMessage();
    message.setLong("fupObsId", fupObsId);
    log.debug("send fileInfo message: fupObsId=" + fupObsId );
    return message;
  }
  
}
