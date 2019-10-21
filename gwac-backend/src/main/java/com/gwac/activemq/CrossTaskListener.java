package com.gwac.activemq;

import com.gwac.job.CrossTaskRecordService;
import javax.annotation.Resource;
import javax.jms.JMSException;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service(value = "crossTaskListener")
public class CrossTaskListener implements MessageListener {

  private static final Log log = LogFactory.getLog(CrossTaskListener.class);
  @Resource(name = "crossTaskRecordService")
  private CrossTaskRecordService crossTaskRecordService;

  @Override
  public void onMessage(Message message) {
    try {
      MapMessage map = (MapMessage) message;
      char fileType = map.getChar("fileType");
      long ufuId = map.getLong("ufuId");
      String storePath = map.getString("storePath");
      String fileName = map.getString("fileName");
      String taskName = map.getString("taskName");
      String dateStr = map.getString("dateStr");
      log.debug("receive message, taskName=" + taskName + ", fileType=" + fileType + ",  file=otlist " + storePath + "/" + fileName);

      long startTime = System.nanoTime();
      if (fileType == 'z') {
        crossTaskRecordService.parseLevel1Ot(ufuId, storePath, fileName, taskName, dateStr);
      } else {
        log.error("wrong fileType");
      }

      long endTime = System.nanoTime();
      log.debug("process file " + fileName + " consume " + 1.0 * (endTime - startTime) / 1e9 + " seconds.");

    } catch (JMSException e) {
      log.error(e);
    }
  }

}
