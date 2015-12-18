/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.job;

import com.gwac.dao.FollowUpFitsfileDao;
import com.gwac.dao.FollowUpObjectTypeDao;
import com.gwac.dao.FollowUpObservationDao;
import com.gwac.dao.FollowUpRecordDao;
import com.gwac.dao.OTCatalogDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.UploadFileUnstoreDao;
import com.gwac.model.FollowUpCatalog;
import com.gwac.model.FollowUpFitsfile;
import com.gwac.model.FollowUpObjectType;
import com.gwac.model.FollowUpObservation;
import com.gwac.model.FollowUpRecord;
import com.gwac.model.OtLevel2;
import com.gwac.model.UploadFileUnstore;
import java.io.File;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author xy
 */
public class FollowUpObservationServiceImpl implements ImageStatusParmService {

  private static final Log log = LogFactory.getLog(FollowUpObservationServiceImpl.class);

  private UploadFileUnstoreDao ufuDao;
  private FollowUpObservationDao foDao;
  private FollowUpFitsfileDao fufDao;
  private FollowUpRecordDao frDao;
  private OTCatalogDao otcDao;
  private OtLevel2Dao ot2Dao;
  private FollowUpObjectTypeDao fuotDao;

  private String rootPath;
  private Boolean isBeiJingServer;
  private Boolean isTestServer;

  private static boolean running = true;

  @Override
  public void startJob() {

//    if (isTestServer) {
//      return;
//    }
    if (running == true) {
      log.debug("start job...");
      running = false;
    } else {
      log.warn("job is running, jump this scheduler.");
      return;
    }

    long startTime = System.nanoTime();
    try {//JDBCConnectionException or some other exception
      parseAllDBInfo();
    } catch (Exception ex) {
      log.error("Job error", ex);
    } finally {
      if (running == false) {
        running = true;
      }
    }
    long endTime = System.nanoTime();
    double time1 = 1.0 * (endTime - startTime) / 1e9;
    log.debug("job consume: parse image status parameter " + time1 + ".");
  }

  public void parseAllDBInfo() {
    List<UploadFileUnstore> ufus = ufuDao.getFollowUpFile();
    log.debug("size=" + ufus.size());

    if (!ufus.isEmpty()) {
      for (UploadFileUnstore obj : ufus) {
        parseFollowUpInfo(obj.getUfuId(), obj.getStorePath(), obj.getFileName());
        ufuDao.updateProcessDoneTime(obj.getUfuId());
      }
    }
  }

  public void parseFollowUpInfo(long ufuId, String storePath, String fileName) {

//    String foName = fileName.substring(0, 18);
    String foName = fileName.substring(0, 14) + "_001";
    FollowUpObservation fo = foDao.getByName(foName);
    if (fo == null) {
      return;
    }
    List<FollowUpCatalog> objs = otcDao.getFollowUpCatalog(rootPath + "/" + storePath + "/" + fileName);
    FollowUpFitsfile fuf = new FollowUpFitsfile();
    if (objs.size() > 0) {
      FollowUpCatalog tfr = objs.get(0);
      String ffName = tfr.getFfName();
      String ffPath = storePath.replace("otfollowlist", "otfollowimg");
      fuf.setFfName(ffName);
      fuf.setFfPath(ffPath);
      fuf.setFoId(fo.getFoId());
      File tfile = new File(rootPath + "/" + ffPath + "/" + ffName);
      if (tfile.exists()) {
        fuf.setIsUpload(true);
      } else {
        fuf.setIsUpload(false);
      }
      fufDao.save(fuf);
    }
    for (FollowUpCatalog obj : objs) {
      FollowUpRecord fur = new FollowUpRecord();
      fur.setFoId(fo.getFoId());
      fur.setDateUtc(obj.getDateUt());
      fur.setFilter(obj.getFilter());
      fur.setRa(obj.getRa());
      fur.setDec(obj.getDec());
      fur.setX(obj.getX());
      fur.setY(obj.getY());
      fur.setMagCalUsno(obj.getMagClbtUsno());
      fur.setMagErr(obj.getMagErr());
      fur.setEllipticity(obj.getEllipticity());
      fur.setClassStar(obj.getClassStar());
      fur.setFwhm(obj.getFwhm());
      fur.setFlag(obj.getFlag());
      fur.setB2(obj.getB2());
      fur.setR2(obj.getR2());
      fur.setI(obj.getI());

      FollowUpObjectType fuot = fuotDao.getOtTypeByTypeName(obj.getOtType().trim().toUpperCase());
      if (fuot != null) {
        fur.setFuoTypeId(fuot.getFuoTypeId());
      }else{
        log.error("cannot find follow up object type: "+ obj.getOtType());
      }
      fur.setFrObjId(obj.getObjLabel());
      fur.setFuSerialNumber(obj.getFuSerialNumber());
      fur.setFufId(fuf.getFufId());
      frDao.save(fur);
    }
  }

  /**
   * @param ufuDao the ufuDao to set
   */
  public void setUfuDao(UploadFileUnstoreDao ufuDao) {
    this.ufuDao = ufuDao;
  }

  /**
   * @param foDao the foDao to set
   */
  public void setFoDao(FollowUpObservationDao foDao) {
    this.foDao = foDao;
  }

  /**
   * @param fufDao the fufDao to set
   */
  public void setFufDao(FollowUpFitsfileDao fufDao) {
    this.fufDao = fufDao;
  }

  /**
   * @param frDao the frDao to set
   */
  public void setFrDao(FollowUpRecordDao frDao) {
    this.frDao = frDao;
  }

  /**
   * @param otcDao the otcDao to set
   */
  public void setOtcDao(OTCatalogDao otcDao) {
    this.otcDao = otcDao;
  }

  /**
   * @param rootPath the rootPath to set
   */
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  /**
   * @param isBeiJingServer the isBeiJingServer to set
   */
  public void setIsBeiJingServer(Boolean isBeiJingServer) {
    this.isBeiJingServer = isBeiJingServer;
  }

  /**
   * @param isTestServer the isTestServer to set
   */
  public void setIsTestServer(Boolean isTestServer) {
    this.isTestServer = isTestServer;
  }

  /**
   * @param ot2Dao the ot2Dao to set
   */
  public void setOt2Dao(OtLevel2Dao ot2Dao) {
    this.ot2Dao = ot2Dao;
  }

  /**
   * @param fuotDao the fuotDao to set
   */
  public void setFuotDao(FollowUpObjectTypeDao fuotDao) {
    this.fuotDao = fuotDao;
  }

}
