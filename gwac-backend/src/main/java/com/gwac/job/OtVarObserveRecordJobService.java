/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.job;

import com.gwac.dao.UploadFileUnstoreDao;
import com.gwac.model.UploadFileUnstore;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author xy
 */
public class OtVarObserveRecordJobService {

  private static final Log log = LogFactory.getLog(OtObserveRecordServiceImpl.class);
  private UploadFileUnstoreDao ufuDao;
  private OtObserveRecordService otVarObserveRecordService;

  public void storeOTCatalog() {
    List<UploadFileUnstore> ufus = ufuDao.getVarStarListFile();
    log.debug("ufu var number:" + ufus.size());
    for (UploadFileUnstore ufu : ufus) {
      otVarObserveRecordService.parseLevel1Ot(ufu.getUfuId(), ufu.getStorePath(), ufu.getFileName());
    }
  }

  /**
   * @param ufuDao the ufuDao to set
   */
  public void setUfuDao(UploadFileUnstoreDao ufuDao) {
    this.ufuDao = ufuDao;
  }

  /**
   * @param otVarObserveRecordService the otVarObserveRecordService to set
   */
  public void setOtVarObserveRecordService(OtObserveRecordService otVarObserveRecordService) {
    this.otVarObserveRecordService = otVarObserveRecordService;
  }

}
