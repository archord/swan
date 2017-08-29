package com.gwac.activemq;

import com.gwac.model.OtLevel2;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.MessageCreator;

public class RegOrigImageMessageCreator implements MessageCreator {

  private static Log log = LogFactory.getLog(RegOrigImageMessageCreator.class);
  
  private final String groupId;
  private final String unitId;
  private final String camId;
  private final String gridId;
  private final String fieldId;
  private final String imgName;
  private final String imgPath;
  private final String genTime;
  private final String dateStr;

  public RegOrigImageMessageCreator(String groupId, String unitId, String camId, 
          String gridId, String fieldId, String imgName, String imgPath, String genTime, String dateStr) {
    this.groupId = groupId;
    this.unitId = unitId;
    this.camId = camId;
    this.gridId = gridId;
    this.fieldId = fieldId;
    this.imgName = imgName;
    this.imgPath = imgPath;
    this.genTime = genTime;
    this.dateStr = dateStr;
  }

  @Override
  public Message createMessage(Session session) throws JMSException {

    MapMessage message = session.createMapMessage();
    message.setString("groupId", groupId);
    message.setString("camId", camId);
    message.setString("gridId", gridId);
    message.setString("fieldId", fieldId);
    message.setString("unitId", unitId);
    message.setString("imgName", imgName);
    message.setString("imgPath", imgPath);
    message.setString("genTime", genTime);
    message.setString("dateStr", dateStr);
    log.debug("send reg orig image message, groupId=" + groupId + ",camId=" + camId  
            + ",gridId=" + gridId  + ",fieldId=" + fieldId + ",unitId=" + unitId 
            + ",imgName=" + imgName + ",imgPath=" + imgPath + ",genTime=" + genTime
            + ",dateStr=" + dateStr);
    return message;
  }

}
