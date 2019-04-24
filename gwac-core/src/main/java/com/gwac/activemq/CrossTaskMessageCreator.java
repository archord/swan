package com.gwac.activemq;

import com.gwac.model.UploadFileUnstore;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.MessageCreator;

public class CrossTaskMessageCreator implements MessageCreator {

  private static final Log log = LogFactory.getLog(CrossTaskMessageCreator.class);
  private final UploadFileUnstore fileInfo;
  private final String taskName;

  public CrossTaskMessageCreator(UploadFileUnstore fileInfo, String taskName) {
    this.fileInfo = fileInfo;
    this.taskName = taskName;
  }

  @Override
  public Message createMessage(Session session) throws JMSException {

    MapMessage message = session.createMapMessage();
    message.setChar("fileType", fileInfo.getFileType());
    message.setLong("ufuId", fileInfo.getUfuId());
    message.setString("storePath", fileInfo.getStorePath());
    message.setString("fileName", fileInfo.getFileName());
    message.setString("taskName", taskName);
    log.debug("receive message, taskName=" + taskName + ", fileType=" + fileInfo.getFileType() + ",  file=otlist " + fileInfo.getStorePath() + "/" + fileInfo.getFileName());
    return message;
  }

}
