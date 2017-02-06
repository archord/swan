package com.gwac.activemq;

import com.gwac.model.OtLevel2;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.MessageCreator;

public class OTCheckMessageCreator implements MessageCreator {

  private static final Log log = LogFactory.getLog(OTCheckMessageCreator.class);
  private final OtLevel2 ot2;
  private final Boolean autoFollowUp;

  public OTCheckMessageCreator(OtLevel2 ot2, Boolean autoFollowUp) {
    this.ot2 = ot2;
    this.autoFollowUp = autoFollowUp;
  }

  @Override
  public Message createMessage(Session session) throws JMSException {

    MapMessage message = session.createMapMessage();
    message.setLong("ot2Id", ot2.getOtId());
    message.setBoolean("autoFollowUp", autoFollowUp);
    log.debug("send ot2 check message, ot2Id=" + ot2.getOtId() + ", ot2name=" + ot2.getName() + ", autoFollowUp=" + autoFollowUp);
    return message;
  }

}
