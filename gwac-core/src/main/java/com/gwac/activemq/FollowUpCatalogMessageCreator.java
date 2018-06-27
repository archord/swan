package com.gwac.activemq;

import com.gwac.model.UploadFileUnstore;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.MessageCreator;

public class FollowUpCatalogMessageCreator implements MessageCreator {
  
  private static final Log log = LogFactory.getLog(FollowUpCatalogMessageCreator.class);
  private final UploadFileUnstore fileInfo;
  private final String followUpName;
  private final String otName;
  
  public FollowUpCatalogMessageCreator(UploadFileUnstore fileInfo, String followUpName, String otName) {
    this.fileInfo = fileInfo;
    this.followUpName = followUpName;
    this.otName = otName;
  }
  
  @Override
  public Message createMessage(Session session) throws JMSException {
    
    MapMessage message = session.createMapMessage();
    message.setChar("fileType", fileInfo.getFileType());
    message.setLong("ufuId", fileInfo.getUfuId());
    message.setString("storePath", fileInfo.getStorePath());
    message.setString("fileName", fileInfo.getFileName());
    message.setString("followUpName", followUpName);
    message.setString("otName", otName);
    log.debug("send fileInfo message: " + followUpName + ", " + fileInfo.getStorePath() + "/" + fileInfo.getFileName());
    return message;
  }
  
}
