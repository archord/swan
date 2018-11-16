package com.gwac.activemq;

import com.gwac.job.FollowUpRecordServiceImpl;
import javax.annotation.Resource;
import javax.jms.JMSException;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service(value = "followUpCatalogListener")
public class FollowUpCatalogListener implements MessageListener {

  private static final Log log = LogFactory.getLog(FollowUpCatalogListener.class);

  @Resource(name = "followUpRecordService")
  private FollowUpRecordServiceImpl followUpRecordService;

  @Override
  public void onMessage(Message message) {
    try {
      MapMessage map = (MapMessage) message;
      char fileType = map.getChar("fileType");
      long ufuId = map.getLong("ufuId");
      String storePath = map.getString("storePath");
      String fileName = map.getString("fileName");
      String followUpName = map.getString("followUpName");
      String otName = map.getString("otName");
      log.debug("receive message, followUpName=" + followUpName + ", ufuId=" + ufuId + ",  file=otlist " + storePath + "/" + fileName+ " , otName=" + otName);
      
      long startTime = System.nanoTime();
      followUpRecordService.parseFollowUpInfo(ufuId, storePath, fileName, followUpName, otName);
      
      long endTime = System.nanoTime();
      log.debug("process file "+fileName+" consume " + 1.0 * (endTime - startTime) / 1e9 + " seconds.");
      
    } catch (JMSException e) {
      log.error(e);
    }
  }

}
