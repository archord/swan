/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import com.gwac.dao.SystemLogDao;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 数据转移，清空需要频繁查询的表，这些表只存储当天的最新数据，之后将表中数据移动到历史表
 *
 * @author xy
 */
@Service(value = "dataBackupService")
public class DataBackupServiceImpl implements BaseService {

  private static final Log log = LogFactory.getLog(DataBackupServiceImpl.class);
  private static boolean running = true;

  @Value("#{syscfg.gwacServerBeijing}")
  private Boolean isBeiJingServer;
  @Value("#{syscfg.gwacServerTest}")
  private Boolean isTestServer;

  @Resource
  private SystemLogDao sysLogDao;

  @Override
  public void startJob() {

//    if (isBeiJingServer || isTestServer) {
//      return;
//    }
    if (isTestServer) {
      return;
    }

    if (running == true) {
      log.debug("start job...");
      running = false;
    } else {
      log.warn("job is running, jump this scheduler.");
      return;
    }

    long startTime = System.nanoTime();
    try {
      int day = 7;
      int day2 = 1;
      /**
      code  数量
      108	1873833
      400	1449034
      2089	73079
      314	14528
      111	9955
      2110	9954
      2091	9726
      163	8354
      164	4060
      167	3612
      2002	3553
      * **/
      String codes = "2012,400,108,2089,314";
      sysLogDao.removeOldRecord(day);
      sysLogDao.removeOldRecord(day2, codes);
    } catch (Exception ex) {
      log.error("Job error", ex);
    } finally {
      if (running == false) {
        running = true;
      }
    }
    long endTime = System.nanoTime();
    log.debug("job consume " + 1.0 * (endTime - startTime) / 1e9 + " seconds.");
  }

}
