package com.gwac.activemq;

import com.gwac.model.CrossObject;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.MessageCreator;

public class CrossObjectCheckMessageCreator implements MessageCreator {

  private static final Log log = LogFactory.getLog(CrossObjectCheckMessageCreator.class);
  private final CrossObject ot2;

  public CrossObjectCheckMessageCreator(CrossObject ot2) {
    this.ot2 = ot2;
  }

  @Override
  public Message createMessage(Session session) throws JMSException {

    MapMessage message = session.createMapMessage();
    message.setLong("objId", ot2.getCoId());
    message.setString("type", "CrossObject");
    log.debug("send CrossObject check message, objId=" + ot2.getCoId());
    return message;
  }

}
