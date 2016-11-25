package com.gwac.activemq;

import com.gwac.model.UploadFileUnstore;
import com.gwac.service.OtObserveRecordService;
import java.text.DecimalFormat;
import java.util.Date;
import javax.annotation.Resource;
import javax.jms.JMSException;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service(value = "oTListListener")
public class OTListListener implements MessageListener {

  private static final Log log = LogFactory.getLog(OTListListener.class);
  @Resource(name = "otObserveRecordService")
  private OtObserveRecordService otObserveRecordService;
  @Resource(name = "otVarObserveRecordService")
  private OtObserveRecordService otVarObserveRecordService;
  @Resource(name = "otSubObserveRecordService")
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
      
      long startTime = System.nanoTime();
      if (fileType == '1') {
        otObserveRecordService.parseLevel1Ot(ufuId, storePath, fileName);
      } else if (fileType == '6') {
        otVarObserveRecordService.parseLevel1Ot(ufuId, storePath, fileName);
      } else if (fileType == '8') {
        otSubObserveRecordService.parseLevel1Ot(ufuId, storePath, fileName);
      } else {
        log.error("wrong fileType");
      }
      
      long endTime = System.nanoTime();
      log.debug("process file "+fileName+" consume " + 1.0 * (endTime - startTime) / 1e9 + " seconds.");
      
    } catch (JMSException e) {
      log.error(e);
    }
  }

}
