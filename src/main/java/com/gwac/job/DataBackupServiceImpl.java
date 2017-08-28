/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import com.gwac.dao.CcdPixFilterDao;
import com.gwac.dao.ConfigFileDao;
import com.gwac.dao.DataProcessMachineDAO;
import com.gwac.dao.FitsFileCutDAO;
import com.gwac.dao.ImageStatusParameterDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtObserveRecordDAO;
import com.gwac.dao.SystemLogDao;
import com.gwac.dao.UploadFileUnstoreDao;
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
  private OtLevel2Dao otlv2Dao;
  @Resource
  private FitsFileCutDAO ffcDao;
  @Resource
  private OtObserveRecordDAO oorDao;
  @Resource
  private ConfigFileDao cfDao;
  @Resource
  private ImageStatusParameterDao ispDao;
  @Resource
  private DataProcessMachineDAO dpmDao;
  @Resource
  private UploadFileUnstoreDao ufuDao;
  @Resource
  private CcdPixFilterDao cpfDao;
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
    try {//JDBCConnectionException or some other exception
      otlv2Dao.moveDataToHisTable();
      ffcDao.moveDataToHisTable();
      oorDao.moveDataToHisTable();
      cfDao.moveDataToHisTable();
      ispDao.moveDataToHisTable();
      dpmDao.everyDayInit();
      ufuDao.removeAll();
      cpfDao.removeAll();
      int day = 7;
      int day2 = 1;
      String codes = "2012,400";
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
