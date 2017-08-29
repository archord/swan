/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.service;

import com.gwac.dao.CcdPixFilterDao;
import com.gwac.dao.ConfigFileDao;
import com.gwac.dao.DataProcessMachineDAO;
import com.gwac.dao.FitsFileCutDAO;
import com.gwac.dao.ImageStatusParameterDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtObserveRecordDAO;
import com.gwac.dao.UploadFileUnstoreDao;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * 数据转移，清空需要频繁查询的表，这些表只存储当天的最新数据，之后将表中数据移动到历史表
 *
 * @author xy
 */
@Service
public class DataBackupServiceImpl implements DataBackupService {

  private static final Log log = LogFactory.getLog(DataBackupServiceImpl.class);

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

  @Override
  public void backupData() {
    try {
      otlv2Dao.moveDataToHisTable();
      ffcDao.moveDataToHisTable();
      oorDao.moveDataToHisTable();
      cfDao.moveDataToHisTable();
      ispDao.moveDataToHisTable();
      dpmDao.everyDayInit();
      ufuDao.removeAll();
      cpfDao.removeAll();
    } catch (Exception ex) {
      log.error("backup error", ex);
    }
  }
  
  @Override
  public void deleteData() {
    try {
      /**for test*/
      otlv2Dao.deleteAll("ot_level2_match");
      otlv2Dao.deleteAll("fits_file_cut_ref");
      otlv2Dao.deleteAll("object_identity");
      otlv2Dao.deleteAll("ot_tmpl_wrong");
      otlv2Dao.deleteAll("move_object");
      otlv2Dao.deleteAll("move_object_record");
      otlv2Dao.deleteAll("observation_sky");
      
      otlv2Dao.deleteAll("ot_level2");
      oorDao.deleteAll("ot_observe_record");
      otlv2Dao.deleteAll("fits_file2");
      otlv2Dao.deleteAll("fits_file_cut");
      
      cfDao.deleteAll("config_file");
      otlv2Dao.deleteAll("system_log");
      ispDao.deleteAll("image_status_parameter");
      dpmDao.everyDayInit();
      ufuDao.removeAll();
      cpfDao.removeAll();
    } catch (Exception ex) {
      log.error("delete error", ex);
    }
  }

}
