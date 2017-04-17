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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 数据转移，清空需要频繁查询的表，这些表只存储当天的最新数据，之后将表中数据移动到历史表
 *
 * @author xy
 */
public class DataBackupServiceImpl implements DataBackupService {

  private static final Log log = LogFactory.getLog(DataBackupServiceImpl.class);

  private OtLevel2Dao otlv2Dao;
  private FitsFileCutDAO ffcDao;
  private OtObserveRecordDAO oorDao;
  private ConfigFileDao cfDao;
  private ImageStatusParameterDao ispDao;
  private DataProcessMachineDAO dpmDao;
  private UploadFileUnstoreDao ufuDao;
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

  /**
   * @param otlv2Dao the otlv2Dao to set
   */
  public void setOtlv2Dao(OtLevel2Dao otlv2Dao) {
    this.otlv2Dao = otlv2Dao;
  }

  /**
   * @param ffcDao the ffcDao to set
   */
  public void setFfcDao(FitsFileCutDAO ffcDao) {
    this.ffcDao = ffcDao;
  }

  /**
   * @param oorDao the oorDao to set
   */
  public void setOorDao(OtObserveRecordDAO oorDao) {
    this.oorDao = oorDao;
  }

  /**
   * @return the cfDao
   */
  public ConfigFileDao getCfDao() {
    return cfDao;
  }

  /**
   * @param cfDao the cfDao to set
   */
  public void setCfDao(ConfigFileDao cfDao) {
    this.cfDao = cfDao;
  }

  /**
   * @return the ispDao
   */
  public ImageStatusParameterDao getIspDao() {
    return ispDao;
  }

  /**
   * @param ispDao the ispDao to set
   */
  public void setIspDao(ImageStatusParameterDao ispDao) {
    this.ispDao = ispDao;
  }

  /**
   * @param dpmDao the dpmDao to set
   */
  public void setDpmDao(DataProcessMachineDAO dpmDao) {
    this.dpmDao = dpmDao;
  }

  /**
   * @param ufuDao the ufuDao to set
   */
  public void setUfuDao(UploadFileUnstoreDao ufuDao) {
    this.ufuDao = ufuDao;
  }

  /**
   * @param cpfDao the cpfDao to set
   */
  public void setCpfDao(CcdPixFilterDao cpfDao) {
    this.cpfDao = cpfDao;
  }

}
