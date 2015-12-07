package com.gwac.activemq;

import com.gwac.model.OtLevel2FollowParameter;
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
  private final OtLevel2FollowParameter ot2fp;

  public OTFollowMessageCreator(OtLevel2FollowParameter ot2fp) {
    this.ot2fp = ot2fp;
  }

  @Override
  public Message createMessage(Session session) throws JMSException {

    String followPlan = ot2fp.getTriggerMsg();
    Short tspId = ot2fp.getTelescope();
    MapMessage message = session.createMapMessage();
    message.setString("followPlan", followPlan);
    message.setShort("tspId", tspId);
    log.debug("send to tspId " + tspId + ", followPlan message: " + followPlan);
    return message;
  }

}
