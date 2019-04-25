/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import com.gwac.activemq.CrossObjectCheckMessageCreator;
import com.gwac.dao.CrossFileDao;
import com.gwac.dao.CrossObjectDao;
import com.gwac.dao.CrossRecordDao;
import com.gwac.dao.CrossTaskDao;
import com.gwac.dao.OTCatalogDao;
import com.gwac.dao.UploadFileUnstoreDao;
import com.gwac.model.CrossFile;
import com.gwac.model.CrossObject;
import com.gwac.model.CrossRecord;
import com.gwac.model.CrossTask;
import java.util.List;
import javax.annotation.Resource;
import javax.jms.Destination;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

@Service(value = "crossTaskRecordService")
public class CrossTaskRecordServiceImpl implements CrossTaskRecordService {

  private static final Log log = LogFactory.getLog(CrossTaskRecordServiceImpl.class);

  @Resource
  private OTCatalogDao otcDao;
  @Resource
  private CrossTaskDao crossTaskDao;
  @Resource
  private CrossObjectDao crossObjectDao;
  @Resource
  private CrossRecordDao crossRecordDao;
  @Resource
  private CrossFileDao crossFileDao;
  @Resource
  private UploadFileUnstoreDao ufuDao;

  @Value("#{syscfg.gwacDataRootDirectory}")
  private String rootPath;
  @Value("#{syscfg.gwacDataCrossTaskOtStampDirectory}")
  private String cutIDir;
  @Value("#{syscfg.gwacErrorbox}")
  private float errorBox;
  @Value("#{syscfg.gwacErrorbox2}")
  private float errorBox2;
  @Value("#{syscfg.gwacSuccessiveImageNumber}")
  private int successiveImageNumber;
  @Value("#{syscfg.gwacSuccessiveImageNumber2}")
  private int successiveImageNumber2;
  @Value("#{syscfg.gwacFirstNMarkNumber}")
  private int firstNMarkNumber;
  @Value("#{syscfg.gwacOccurImageNumber}")
  private int occurNumber;

  private static boolean running = true;
  @Value("#{syscfg.gwacServerBeijing}")
  private Boolean isBeiJingServer;
  @Value("#{syscfg.gwacServerTest}")
  private Boolean isTestServer;

  @Resource
  private JmsTemplate jmsTemplate;
  @Resource(name = "crossObjectCheckDest")
  private Destination crossObjectCheckDest;

  /**
   * 解析一级OT列表文件，得出二级OT，切图文件名称，二级OT模板切图名称
   *
   * @param ufuId
   * @param storePath
   * @param fileName
   */
  @Override
  public void parseLevel1Ot(long ufuId, String storePath, String fileName, String taskName) {

    if (storePath != null && fileName != null && taskName != null) {

      CrossTask ct = crossTaskDao.getByName(taskName);
      if (ct == null) {
	return;
      }

      CrossFile cf = new CrossFile();
      cf.setCtId(ct.getCtId());
      cf.setFileName(fileName);
      crossFileDao.save(cf);
      if (cf.getCfId() == 0) {
	return;
      }
      ct.setFfCount(ct.getFfCount() + 1);
      crossTaskDao.update(ct);
      int number = ct.getFfCount();

      List<CrossRecord> otcs = otcDao.getCrossRecord(rootPath + "/" + storePath + "/" + fileName);
      log.debug(fileName + ", otlv1 size:" + otcs.size());

      for (CrossRecord otc : otcs) {

	otc.setFfNumber(number);
	otc.setCtId(ct.getCtId());
	otc.setCfId(cf.getCfId());
	String stampStorePath = ct.getDateStr() + "/" + taskName + "/" + cutIDir;
	otc.setStampPath(stampStorePath);

	CrossObject otLv2 = new CrossObject();
	otLv2.setCtId(ct.getCtId());
	otLv2.setFoundTimeUtc(otc.getDateUtc());
	otLv2.setX(otc.getX());
	otLv2.setY(otc.getY());
	otLv2.setXtemp(otc.getXTemp());
	otLv2.setYtemp(otc.getYTemp());
	otLv2.setRa(otc.getRa());
	otLv2.setDec(otc.getDec());
	otLv2.setMag(otc.getMag());
	otLv2.setFirstFfNumber(number);
	otLv2.setLastFfNumber(number);
	otLv2.setTotal(0);
	otLv2.setDateStr(ct.getDateStr());

	//当前这条记录是与最近5幅之内的OT匹配，还是与当晚所有OT匹配，这里选择与当晚所有OT匹配
	//existInLatestN与最近5幅比较
	CrossObject tlv2 = crossObjectDao.exist(otLv2, errorBox);
	if (tlv2 != null) {
	  if (tlv2.getFirstFfNumber() > number) {
	    tlv2.setFirstFfNumber(number);
	    tlv2.setFoundTimeUtc(otLv2.getFoundTimeUtc());
	  } else {
	    tlv2.setLastFfNumber(otLv2.getLastFfNumber());
	    tlv2.setXtemp(otLv2.getXtemp());
	    tlv2.setYtemp(otLv2.getYtemp());
	    tlv2.setRa(otLv2.getRa());
	    tlv2.setDec(otLv2.getDec());
	    tlv2.setMag(otLv2.getMag());
	  }
	  tlv2.setTotal(tlv2.getTotal() + 1);
	  crossObjectDao.updateSomeRealTimeInfo(tlv2);

	  otc.setCoId(tlv2.getCoId());
	  crossRecordDao.save(otc);
	} else {

	  otc.setCoId((long) 0);
	  crossRecordDao.save(otc);
	  if (ct.getCrossMethod() == 1) {

	    CrossObject tOtLv2 = new CrossObject();
	    tOtLv2.setCtId(ct.getCtId());
	    tOtLv2.setFoundTimeUtc(otc.getDateUtc());
	    tOtLv2.setX(otc.getX());
	    tOtLv2.setY(otc.getY());
	    tOtLv2.setXtemp(otc.getXTemp());
	    tOtLv2.setYtemp(otc.getYTemp());
	    tOtLv2.setRa(otc.getRa());
	    tOtLv2.setDec(otc.getDec());
	    tOtLv2.setMag(otc.getMag());
	    tOtLv2.setFirstFfNumber(number);
	    tOtLv2.setLastFfNumber(number);
	    tOtLv2.setTotal(0);
	    tOtLv2.setDateStr(ct.getDateStr());
	    tOtLv2.setOtType((short) 0);
	    tOtLv2.setIsMatch((short) 0);
	    tOtLv2.setCvsMatch(false);
	    tOtLv2.setRc3Match(false);
	    tOtLv2.setMinorPlanetMatch(false);
	    tOtLv2.setHisMatch(false);
	    tOtLv2.setOtherMatch(false);
	    tOtLv2.setUsnoMatch(false);
	    tOtLv2.setBadMatch(false);
	    tOtLv2.setLookBackResult((short) 0);
	    tOtLv2.setFollowUpResult((short) 0);
	    tOtLv2.setFoCount((short) 0);
	    tOtLv2.setLookBackCnn((float) -1);
	    tOtLv2.setProbability((float) 0);
	    crossObjectDao.save(tOtLv2);

	    otc.setCoId(tOtLv2.getCoId());
	    crossRecordDao.update(otc);

	    MessageCreator tmc = new CrossObjectCheckMessageCreator(tOtLv2);
	    jmsTemplate.send(crossObjectCheckDest, tmc);
	  } else if (ct.getCrossMethod() == 2) {
	    List<CrossRecord> oors = crossRecordDao.matchLatestN(otc, errorBox, successiveImageNumber);
	    if (oors.size() >= occurNumber) {
	      CrossRecord oor1 = oors.get(0);

	      CrossObject tOtLv2 = new CrossObject();
	      tOtLv2.setCtId(ct.getCtId());
	      tOtLv2.setFoundTimeUtc(oor1.getDateUtc());
	      tOtLv2.setX(oor1.getX());
	      tOtLv2.setY(oor1.getY());
	      tOtLv2.setXtemp(oor1.getXTemp());
	      tOtLv2.setYtemp(oor1.getYTemp());
	      tOtLv2.setRa(oor1.getRa());
	      tOtLv2.setDec(oor1.getDec());
	      tOtLv2.setMag(oor1.getMag());
	      tOtLv2.setFirstFfNumber(oor1.getFfNumber());
	      tOtLv2.setLastFfNumber(number);
	      tOtLv2.setTotal(2);
	      tOtLv2.setDateStr(ct.getDateStr());
	      tOtLv2.setOtType((short) 0);
	      tOtLv2.setIsMatch((short) 0);
	      tOtLv2.setCvsMatch(false);
	      tOtLv2.setRc3Match(false);
	      tOtLv2.setMinorPlanetMatch(false);
	      tOtLv2.setHisMatch(false);
	      tOtLv2.setOtherMatch(false);
	      tOtLv2.setUsnoMatch(false);
	      tOtLv2.setBadMatch(false);
	      tOtLv2.setLookBackResult((short) 0);
	      tOtLv2.setFollowUpResult((short) 0);
	      tOtLv2.setFoCount((short) 0);
	      tOtLv2.setLookBackCnn((float) -1);
	      tOtLv2.setProbability((float) 0);
	      crossObjectDao.save(tOtLv2);

	      MessageCreator tmc = new CrossObjectCheckMessageCreator(tOtLv2);
	      jmsTemplate.send(crossObjectCheckDest, tmc);

	      for (CrossRecord tOor : oors) {
		if (tOor.getCoId() != 0) {
		  continue;
		}
		tOor.setCoId(tOtLv2.getCoId());
		crossRecordDao.update(tOor);
	      }
	    }
	  } else {
	    log.warn("unknown CrossMethod:" + ct.getCrossMethod());
	  }
	}
      }
      ufuDao.updateProcessDoneTime(ufuId);
    }
  }

}
