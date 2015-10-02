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
  private final UploadFileUnstore fileInfo;
  
  public OTListMessageCreator(UploadFileUnstore fileInfo) {
    this.fileInfo = fileInfo;
  }
  
  @Override
  public Message createMessage(Session session) throws JMSException {
    
    MapMessage message = session.createMapMessage();
    message.setChar("fileType", fileInfo.getFileType());
    message.setLong("ufuId", fileInfo.getUfuId());
    message.setString("storePath", fileInfo.getStorePath());
    message.setString("fileName", fileInfo.getFileName());
    log.debug("send fileInfo message: " + fileInfo.getStorePath() + "/" + fileInfo.getFileName());
    return message;
  }
  
}
