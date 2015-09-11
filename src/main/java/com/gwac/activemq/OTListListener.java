package com.gwac.activemq;

import com.gwac.service.OtObserveRecordService;
import java.text.DecimalFormat;
import java.util.Date;
import javax.jms.JMSException;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OTListListener implements MessageListener {
  
  private static final Log log = LogFactory.getLog(OTListListener.class);
  private OtObserveRecordService otObserveRecordService;

  @Override
  public void onMessage(Message message) {
    try {
      MapMessage map = (MapMessage) message;
      String storePath = map.getString("storePath");
      String fileName = map.getString("fileName");
      otObserveRecordService.parseLevel1Ot(storePath, fileName);
    } catch (JMSException e) {
      log.error(e);
    }
  }

  /**
   * @param otObserveRecordService the otObserveRecordService to set
   */
  public void setOtObserveRecordService(OtObserveRecordService otObserveRecordService) {
    this.otObserveRecordService = otObserveRecordService;
  }

}
