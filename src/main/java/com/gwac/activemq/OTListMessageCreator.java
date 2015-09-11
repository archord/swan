package com.gwac.activemq;

import com.gwac.model.UploadFileUnstore;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.MessageCreator;

public class OTListMessageCreator implements MessageCreator {
  
  private static final Log log = LogFactory.getLog(OTListMessageCreator.class);
  private final UploadFileUnstore otList;
  
  public OTListMessageCreator(UploadFileUnstore otList) {
    this.otList = otList;
  }
  
  @Override
  public Message createMessage(Session session) throws JMSException {
    
    MapMessage message = session.createMapMessage();
    message.setString("storePath", otList.getStorePath());
    message.setString("fileName", otList.getFileName());
    log.debug("send otList message: " + otList.getStorePath() + "/" + otList.getFileName());
    return message;
  }
  
}
