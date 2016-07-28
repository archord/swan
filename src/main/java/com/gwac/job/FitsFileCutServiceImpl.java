/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import com.gwac.dao.DataProcessMachineDAO;
import com.gwac.dao.FitsFileCutDAO;
import com.gwac.dao.FitsFileDAO;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtObserveRecordDAO;
import com.gwac.model.DataProcessMachine;
import com.gwac.model.FitsFile;
import com.gwac.model.FitsFileCut;
import com.gwac.model.OtLevel2;
import com.gwac.model.OtObserveRecord;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 根据OtObserveRecordService产生的有观测记录的切图序列，计算序列中缺失的切图文件。
 *
 * @author xy
 */
public class FitsFileCutServiceImpl implements FitsFileCutService {

  private static final Log log = LogFactory.getLog(FitsFileCutServiceImpl.class);

  private static boolean running = true;
  private Boolean isBeiJingServer;
  private Boolean isTestServer;

  private FitsFileDAO ffDao;
  private FitsFileCutDAO ffcDao;
  private OtLevel2Dao otlv2Dao;
  private DataProcessMachineDAO dpmDao;
  private OtObserveRecordDAO oorDao;

  private int successiveImageNumber;
  private int headTailCutNumber;

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
      addMissedCutImages();
      addMissedCutImagesForTrueOT2();
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

  /**
   * 对真OT2，当超过5帧没有出现时，预先切图到出现的最后一帧的后两帧 每天8点，定时将所有Ot2标示为切图完成
   */
  public void addMissedCutImagesForTrueOT2() {

    List<OtLevel2> otlv2s = otlv2Dao.getUnCutRecord(successiveImageNumber);

    for (OtLevel2 otlv2 : otlv2s) {
      List<FitsFileCut> tffcs = ffcDao.getFirstCutFile(otlv2);
      List<OtObserveRecord> oors = oorDao.getLastRecord(otlv2);

      if (tffcs.isEmpty() || oors.isEmpty()) {
        continue;
      }
      FitsFileCut firstFfc = tffcs.get(0);
      OtObserveRecord lastRecord = oors.get(0);
      
      for (int i = otlv2.getLastFfNumber() + 1; i <= otlv2.getLastFfNumber()+2; i++) {
        String ffName = String.format("%s_%04d.fit", otlv2.getIdentify(), i);
        FitsFile tff = ffDao.getByName(ffName);
        if (tff == null) {
          log.warn("can't find orig fits file " + ffName + ", is the sky region name correct?");
          continue;
        }

        FitsFileCut ffc = new FitsFileCut();
        ffc.setFfId(tff.getFfId());
        ffc.setStorePath(firstFfc.getStorePath());
        ffc.setFileName(String.format("%s_%04d", otlv2.getName(), i));
        ffc.setOtId(otlv2.getOtId());
        ffc.setNumber(i);
        ffc.setDpmId(otlv2.getDpmId().shortValue());
        ffc.setImgX(lastRecord.getX());
        ffc.setImgY(lastRecord.getY());
        ffc.setRequestCut(false);
        ffc.setSuccessCut(false);
        ffc.setIsMissed(true);
        ffc.setPriority(Short.MAX_VALUE);
        ffcDao.save(ffc);
      }
      
      otlv2.setCuttedFfNumber(otlv2.getLastFfNumber()+2);
      otlv2Dao.updateCuttedFfNumber(otlv2);
    }
  }

  public void addMissedCutImages() {

    List<OtLevel2> otlv2s = otlv2Dao.getMissedFFCLv2OT();
//    log.debug(otlv2s.size() + " otlv2s wait to be cut.");
    for (OtLevel2 otlv2 : otlv2s) {
//      log.debug("otlv2(id=" + otlv2.getOtId() + ") add it's uncutted image to DB.");
      int cuttedFfNumber = otlv2.getCuttedFfNumber();

      /**
       * cuttedFfNumber == 0代表ot2还没开始计算切图
       */
      if (cuttedFfNumber == 0) {
        cuttedFfNumber = otlv2.getFirstFfNumber() > 2 ? otlv2.getFirstFfNumber() - (headTailCutNumber + 1) : 1;
      }

      List<FitsFileCut> tffcs = ffcDao.getFirstCutFile(otlv2);
      List<OtObserveRecord> oors = oorDao.getUnCutRecord(otlv2.getOtId(), cuttedFfNumber);

      if (tffcs.isEmpty()) {
        continue;
      }
      FitsFileCut firstFfc = tffcs.get(0);

      int oorIdx = 0;
      for (int i = cuttedFfNumber + 1; i <= otlv2.getLastFfNumber(); i++) {
        String ffName = String.format("%s_%04d.fit", otlv2.getIdentify(), i);
        FitsFile tff = ffDao.getByName(ffName);
        if (tff == null) {
          log.warn("can't find orig fits file " + ffName + ", is the sky region name correct?");
          continue;
        }
        while (oors.get(oorIdx).getFfNumber() <= i) {
          oorIdx++;
        }

        int tIdx = oorIdx;
        if (i >= otlv2.getFirstFfNumber()) {
          tIdx = oorIdx - 1;
        }

        OtObserveRecord toor = oors.get(tIdx);
        FitsFileCut ffc = new FitsFileCut();
        ffc.setFfId(tff.getFfId());
        ffc.setStorePath(firstFfc.getStorePath());
        ffc.setFileName(String.format("%s_%04d", otlv2.getName(), i));
        ffc.setOtId(otlv2.getOtId());
        ffc.setNumber(i);
        ffc.setDpmId(otlv2.getDpmId().shortValue());
        ffc.setImgX(toor.getX());
        ffc.setImgY(toor.getY());
        ffc.setRequestCut(false);
        ffc.setSuccessCut(false);
        ffc.setIsMissed(true);
        ffc.setPriority(Short.MAX_VALUE);
        ffcDao.save(ffc);
        
        if(toor.getFfNumber()==i){
          toor.setFfcId(ffc.getFfcId());
          oorDao.updateFfcId(toor);
        }
      }

      otlv2.setCuttedFfNumber(otlv2.getLastFfNumber());
      otlv2Dao.updateCuttedFfNumber(otlv2);
    }
  }

  /**
   * @param ffDao the ffDao to set
   */
  public void setFfDao(FitsFileDAO ffDao) {
    this.ffDao = ffDao;
  }

  /**
   * @param ffcDao the ffcDao to set
   */
  public void setFfcDao(FitsFileCutDAO ffcDao) {
    this.ffcDao = ffcDao;
  }

  /**
   * @param otlv2Dao the otlv2Dao to set
   */
  public void setOtlv2Dao(OtLevel2Dao otlv2Dao) {
    this.otlv2Dao = otlv2Dao;
  }

  /**
   * @param dpmDao the dpmDao to set
   */
  public void setDpmDao(DataProcessMachineDAO dpmDao) {
    this.dpmDao = dpmDao;
  }

  /**
   * @param successiveImageNumber the successiveImageNumber to set
   */
  public void setSuccessiveImageNumber(int successiveImageNumber) {
    this.successiveImageNumber = successiveImageNumber;
  }

  /**
   * @param headTailCutNumber the headTailCutNumber to set
   */
  public void setHeadTailCutNumber(int headTailCutNumber) {
    this.headTailCutNumber = headTailCutNumber;
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
   * @param oorDao the oorDao to set
   */
  public void setOorDao(OtObserveRecordDAO oorDao) {
    this.oorDao = oorDao;
  }
}
