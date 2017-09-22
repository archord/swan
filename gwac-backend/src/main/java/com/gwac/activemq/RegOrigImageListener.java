package com.gwac.activemq;

import com.gwac.model.UploadFileUnstore;
import com.gwac.service.OtObserveRecordService;
import com.gwac.service.RegOrigImageService;
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

@Service(value = "regOrigImageListener")
public class RegOrigImageListener implements MessageListener {

  private static final Log log = LogFactory.getLog(RegOrigImageListener.class);
  @Resource(name = "regOrigImageServiceImpl")
  private RegOrigImageService regOrigImageService;

  @Override
  public void onMessage(Message message) {
    try {
      MapMessage map = (MapMessage) message;
      String groupId = map.getString("groupId");
      String camId = map.getString("camId");
      String gridId = map.getString("gridId");
      String fieldId = map.getString("fieldId");
      String unitId = map.getString("unitId");
      String imgName = map.getString("imgName");
      String imgPath = map.getString("imgPath");
      String genTime = map.getString("genTime");
      String dateStr = map.getString("dateStr");
      log.debug("receive reg orig image message, groupId=" + groupId + ",camId=" + camId
              + ",gridId=" + gridId + ",fieldId=" + fieldId + ",unitId=" + unitId
              + ",imgName=" + imgName + ",imgPath=" + imgPath + ",genTime=" + genTime
              + ",dateStr=" + dateStr);

      long startTime = System.nanoTime();
      regOrigImageService.updateSystemStatus(groupId, unitId, camId, gridId, fieldId, imgName, imgPath, genTime, dateStr);
      long endTime = System.nanoTime();
      log.debug("reg image " + imgName + " consume " + 1.0 * (endTime - startTime) / 1e9 + " seconds.");

    } catch (JMSException e) {
      log.error(e);
    }
  }

}
