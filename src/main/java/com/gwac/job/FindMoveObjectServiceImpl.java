/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import com.gwac.dao.MoveObjectDao;
import com.gwac.dao.MoveObjectRecordDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtObserveRecordDAO;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 数据转移，清空需要频繁查询的表，这些表只存储当天的最新数据，之后将表中数据移动到历史表
 *
 * @author xy
 */
@Service(value = "findMoveObjectService")
public class FindMoveObjectServiceImpl implements BaseService {

  private static final Log log = LogFactory.getLog(FindMoveObjectServiceImpl.class);
  private static boolean running = true;

  @Resource
  private OtLevel2Dao otlv2Dao;
  @Resource
  private OtObserveRecordDAO oorDao;
  @Resource(name = "moveObjectDao")
  private MoveObjectDao moveObjectDao;
  @Resource(name = "moveObjectRecordDao")
  private MoveObjectRecordDao moveObjectRecordDao;
  
  @Value("#{syscfg.gwacServerBeijing}")
  private Boolean isBeiJingServer;
  @Value("#{syscfg.gwacServerTest}")
  private Boolean isTestServer;
  
  int count=1;

//  @Scheduled(cron = "0/1 * *  * * ? ")
  @Override
  public void startJob() {
    int aa= count++;
    log.warn(aa+" start, move object number: " + moveObjectDao.count());
    try {
      Thread.sleep(2*1000);
    } catch (InterruptedException ex) {
      Logger.getLogger(FindMoveObjectServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
    }
    log.warn(aa+" end, move object number: " + moveObjectDao.count());
  }

}
