/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.job;

import com.gwac.activemq.FollowUpObjectCheckMessageCreator;
import com.gwac.dao.FollowUpFitsfileDao;
import com.gwac.dao.FollowUpObjectDao;
import com.gwac.dao.FollowUpObjectTypeDao;
import com.gwac.dao.FollowUpObservationDao;
import com.gwac.dao.FollowUpRecordDao;
import com.gwac.dao.OTCatalogDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.UploadFileUnstoreDao;
import com.gwac.model.FollowUpCatalog;
import com.gwac.model.FollowUpFitsfile;
import com.gwac.model.FollowUpObject;
import com.gwac.model.FollowUpObjectType;
import com.gwac.model.FollowUpObservation;
import com.gwac.model.FollowUpRecord;
import com.gwac.model.OtLevel2;
import com.gwac.model.UploadFileUnstore;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.jms.Destination;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

/**
 *
 * @author xy
 */
@Service(value = "followUpRecordService")
public class FollowUpRecordServiceImpl implements BaseService {

  private static final Log log = LogFactory.getLog(FollowUpRecordServiceImpl.class);
  private static boolean running = true;

  @Resource
  private UploadFileUnstoreDao ufuDao;
  @Resource
  private FollowUpObservationDao foDao;
  @Resource
  private FollowUpFitsfileDao fufDao;
  @Resource
  private FollowUpRecordDao frDao;
  @Resource
  private OTCatalogDao otcDao;
  @Resource
  private OtLevel2Dao ot2Dao;
  @Resource
  private FollowUpObjectTypeDao fuotDao;
  @Resource
  private FollowUpObjectDao fuoDao;

  @Resource
  private JmsTemplate jmsTemplate;
  @Resource(name = "followUpObjCheckDest")
  private Destination followUpObjCheckDest;

  @Value("#{syscfg.gwacFollowupErrorbox}")
  private float followupErrorbox;
  @Value("#{syscfg.gwacDataRootDirectory}")
  private String rootPath;
  @Value("#{syscfg.gwacServerBeijing}")
  private Boolean isBeiJingServer;
  @Value("#{syscfg.gwacServerTest}")
  private Boolean isTestServer;

  @Override
  public void startJob() {

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
    log.debug("job consume: FollowUpObservation " + time1 + ".");
  }

  public void parseAllDBInfo() {
    List<UploadFileUnstore> ufus = ufuDao.getFollowUpFile();
    log.debug("size=" + ufus.size());

    if (!ufus.isEmpty()) {
      for (UploadFileUnstore obj : ufus) {
        parseFollowUpInfo(obj.getUfuId(), obj.getStorePath(), obj.getFileName());
//        ufuDao.updateProcessDoneTime(obj.getUfuId());
      }
    }
  }

  public void parseFollowUpInfo(long ufuId, String storePath, String fileName) {

    String foName, ot2Name;
    if (fileName.charAt(0) == 'F' && fileName.charAt(8) == 'X') {
      foName = fileName.substring(0, 14);
      ot2Name = null;
    } else {
      foName = fileName.substring(0, 18);
      ot2Name = fileName.substring(0, 14);
    }
    parseFollowUpInfo(ufuId, storePath, fileName, foName, ot2Name);
  }

  public void parseFollowUpInfo(long ufuId, String storePath, String fileName, String foName, String ot2Name) {

    long ot2Id = 0;
    if (ot2Name != null && !ot2Name.trim().isEmpty()) {
      OtLevel2 ot2 = ot2Dao.getOtLevel2ByName(ot2Name.trim(), false);
      if (ot2 != null) {
        ot2Id = ot2.getOtId();
      }else{
        log.warn("can not find ot2:" + ot2Name);
      }
    }

    FollowUpObservation fo = foDao.getByName(foName);
    if (fo == null) {
      log.error("can not find FollowUpObservation:" + foName);
      return;
    }
    List<FollowUpCatalog> objs = otcDao.getFollowUpCatalog(rootPath + "/" + storePath + "/" + fileName);

    if (objs.size() > 0) { //解析并存储FollUpFits文件

      FollowUpCatalog tfr = objs.get(0);
      FollowUpFitsfile fuf = fufDao.getByName(tfr.getFfName());
      if (fuf == null) {
        /**
         * fuf = new FollowUpFitsfile(); fuf.setFfName(tfr.getFfName());
         * fuf.setFfPath(storePath); fuf.setFoId(fo.getFoId()); File tfile = new
         * File(rootPath + "/" + storePath + "/" + tfr.getFfName()); if
         * (tfile.exists()) { fuf.setIsUpload(true); } else {
         * fuf.setIsUpload(false); } fufDao.save(fuf); *
         */
        log.error("hibernate cache problem, can not find FollowUpFitsfile " + tfr.getFfName());
        return;
      }

      short checkId = 1;
      short catasId = 2;
      short miniotId = 3;
      short newotId = 4;
      FollowUpObjectType tfuot = fuotDao.getOtTypeByTypeName("CHECK");
      if (tfuot != null) {
        checkId = tfuot.getFuoTypeId();
      }
      tfuot = fuotDao.getOtTypeByTypeName("MINIOT");
      if (tfuot != null) {
        miniotId = tfuot.getFuoTypeId();
      }
      tfuot = fuotDao.getOtTypeByTypeName("CATAS");
      if (tfuot != null) {
        catasId = tfuot.getFuoTypeId();
      }
      tfuot = fuotDao.getOtTypeByTypeName("NEWOT");
      if (tfuot != null) {
        newotId = tfuot.getFuoTypeId();
      }

      List<FollowUpCatalog> checkObjs = new ArrayList<>();
      List<FollowUpCatalog> miniotObjs = new ArrayList<>();
      List<FollowUpCatalog> catasObjs = new ArrayList<>();
      List<FollowUpCatalog> newotObjs = new ArrayList<>();
      for (FollowUpCatalog obj : objs) {
        if (obj.getOtType().trim().equalsIgnoreCase("CHECK")) {
          checkObjs.add(obj);
        } else if (obj.getOtType().trim().equalsIgnoreCase("MINIOT")) {
          miniotObjs.add(obj);
        } else if (obj.getOtType().trim().equalsIgnoreCase("CATAS")) {
          catasObjs.add(obj);
        } else if (obj.getOtType().trim().equalsIgnoreCase("NEWOT")) {
          newotObjs.add(obj);
        }
      }

      for (FollowUpCatalog obj : checkObjs) {
        saveFollowUpCatalog(obj, ot2Id, fo.getFoId(), fuf.getFufId(), checkId);
      }

      if (miniotObjs.size() > 0) {
        foDao.updateProcessResult(foName, '1'); //MINIOT:1, CATAS:2, NEWOT:3
        for (FollowUpCatalog obj : miniotObjs) {
          saveFollowUpCatalog(obj, ot2Id, fo.getFoId(), fuf.getFufId(), miniotId);
        }
      } else if (catasObjs.size() > 0) {
        foDao.updateProcessResult(foName, '2');
        for (FollowUpCatalog obj : catasObjs) {
          saveFollowUpCatalog(obj, ot2Id, fo.getFoId(), fuf.getFufId(), catasId);
        }
      } else if (newotObjs.size() > 0) {
        foDao.updateProcessResult(foName, '3');
        for (FollowUpCatalog obj : newotObjs) {
          saveFollowUpCatalog(obj, ot2Id, fo.getFoId(), fuf.getFufId(), newotId);
        }
      }
      MessageCreator tmc = new FollowUpObjectCheckMessageCreator(fo.getFoId());
      jmsTemplate.send(followUpObjCheckDest, tmc);
    }
  }

  public void saveFollowUpCatalog(FollowUpCatalog obj, long ot2Id, long foId, long fufId, short fuotId) {

    FollowUpObject fuo = new FollowUpObject();
    fuo.setOtId(ot2Id);
    fuo.setFoId(foId);
    fuo.setFuoTypeId(fuotId);
    fuo.setStartTimeUtc(obj.getDateUt());
    fuo.setLastRa(obj.getRa());
    fuo.setLastDec(obj.getDec());
    fuo.setLastX(obj.getX());
    fuo.setLastY(obj.getY());
    fuo.setFoundSerialNumber(obj.getFuSerialNumber());
    fuo.setRecordTotal(1);
    fuo.setFoundMag(obj.getMagClbtUsno());
    fuo.setB2(obj.getB2());
    fuo.setR2(obj.getR2());
    fuo.setI(obj.getI());
    fuo.setLastMag(obj.getMagClbtUsno());
    fuo.setLastTimeUtc(obj.getDateUt());
    fuo.setLastSerialNumber(obj.getFuSerialNumber());

    List<FollowUpObject> fuos = fuoDao.exist(fuo, followupErrorbox);
    if (fuos.size() > 0) {
      FollowUpObject tfuo = fuos.get(0);
      fuo.setFuoId(tfuo.getFuoId());

      tfuo.setLastRa(fuo.getLastRa());
      tfuo.setLastDec(fuo.getLastDec());
      tfuo.setLastX(fuo.getLastX());
      tfuo.setLastY(fuo.getLastY());
      tfuo.setRecordTotal(tfuo.getRecordTotal() + 1);
      tfuo.setLastMag(fuo.getLastMag());
      tfuo.setLastTimeUtc(fuo.getLastTimeUtc());
      tfuo.setLastSerialNumber(fuo.getLastSerialNumber());
      if (fuo.getFoundSerialNumber() < tfuo.getFoundSerialNumber()) {
        tfuo.setStartTimeUtc(fuo.getStartTimeUtc());
        tfuo.setFoundSerialNumber(fuo.getFoundSerialNumber());
      }
      fuoDao.update(tfuo);
    } else {
      int fuoNum = fuoDao.countTypeNumberByOtId(fuo);
      String fuoName = String.format("%s%02d", obj.getOtType(), fuoNum + 1);
      fuo.setFuoName(fuoName);
      fuoDao.save(fuo);
    }

    FollowUpRecord fur = new FollowUpRecord();
    fur.setFuoId(fuo.getFuoId());
    fur.setFoId(foId);
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
    fur.setFuoTypeId(fuotId);
    fur.setFrObjId(obj.getObjLabel());
    fur.setFuSerialNumber(obj.getFuSerialNumber());
    fur.setFufId(fufId);
    fur.setDistance(obj.getDistance());
    
    frDao.save(fur);
  }

}
