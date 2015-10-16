package com.gwac.activemq;

import com.gwac.model.UploadFileUnstore;
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
  private OtObserveRecordService otVarObserveRecordService;
  private OtObserveRecordService otSubObserveRecordService;

  @Override
  public void onMessage(Message message) {
    try {
      MapMessage map = (MapMessage) message;
      char fileType = map.getChar("fileType");
      long ufuId = map.getLong("ufuId");
      String storePath = map.getString("storePath");
      String fileName = map.getString("fileName");
      log.debug("receive message, fileType=" + fileType + ",  file=otlist " + storePath + "/" + fileName);
      if (fileType == '1') {
        otObserveRecordService.parseLevel1Ot(ufuId, storePath, fileName);
      } else if (fileType == '6') {
        otVarObserveRecordService.parseLevel1Ot(ufuId, storePath, fileName);
      } else if (fileType == '8') {
        otSubObserveRecordService.parseLevel1Ot(ufuId, storePath, fileName);
      } else {
        log.error("wrong fileType");
      }
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

  /**
   * @param otVarObserveRecordService the otVarObserveRecordService to set
   */
  public void setOtVarObserveRecordService(OtObserveRecordService otVarObserveRecordService) {
    this.otVarObserveRecordService = otVarObserveRecordService;
  }

  /**
   * @return the otSubObserveRecordService
   */
  public OtObserveRecordService getOtSubObserveRecordService() {
    return otSubObserveRecordService;
  }

  /**
   * @param otSubObserveRecordService the otSubObserveRecordService to set
   */
  public void setOtSubObserveRecordService(OtObserveRecordService otSubObserveRecordService) {
    this.otSubObserveRecordService = otSubObserveRecordService;
  }

}
