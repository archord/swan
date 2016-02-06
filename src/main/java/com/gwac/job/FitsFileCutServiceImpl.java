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
import com.gwac.model.DataProcessMachine;
import com.gwac.model.FitsFile;
import com.gwac.model.FitsFileCut;
import com.gwac.model.OtLevel2;
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

  public void addMissedCutImages() {

    List<OtLevel2> otlv2s = otlv2Dao.getMissedFFCLv2OT();
//    log.debug(otlv2s.size() + " otlv2s wait to be cut.");
    for (OtLevel2 otlv2 : otlv2s) {
//      log.debug("otlv2(id=" + otlv2.getOtId() + ") add it's uncutted image to DB.");
      int cuttedFfNumber = otlv2.getCuttedFfNumber();
      List<FitsFileCut> ffcs = ffcDao.getUnCutImageByOtId(otlv2.getOtId(), cuttedFfNumber);
      if (ffcs.isEmpty()) {
        log.warn("otlv2 " + otlv2.getOtId() + " is not cut done, but uncuted ffcs is empty.");
        continue;
      }
      otlv2.setCuttedFfNumber(ffcs.get(ffcs.size() - 1).getNumber());
//      otlv2Dao.update(otlv2);
      otlv2Dao.updateCuttedFfNumber(otlv2);

      //add head missed image
      FitsFileCut headFFC = ffcs.get(0);
      int headNum = headFFC.getNumber();

      if (headNum == otlv2.getFirstFfNumber()) {
        int tNum = headNum - headTailCutNumber;
        if (tNum < 1) {
          tNum = 1;  //number start from 1
        }
        for (int i = tNum; i < headNum; i++) {
          String ffName = String.format("%s_%04d.fit", otlv2.getIdentify(), i);
          FitsFile tff = ffDao.getByName(ffName);
          if (tff == null) {
            log.warn("can't find orig fits file " + ffName);
            continue;
          }
          FitsFileCut ffc = new FitsFileCut();
          ffc.setFfId(tff.getFfId());
          ffc.setStorePath(headFFC.getStorePath());
          ffc.setFileName(String.format("%s_%04d", otlv2.getName(), i));
          ffc.setOtId(otlv2.getOtId());
          ffc.setNumber(i);
          ffc.setDpmId(headFFC.getDpmId());
          ffc.setImgX(headFFC.getImgX());
          ffc.setImgY(headFFC.getImgY());
          ffc.setRequestCut(false);
          ffc.setSuccessCut(false);
          ffc.setIsMissed(true);
          ffc.setPriority(Short.MAX_VALUE);
          ffcDao.save(ffc);
        }
      }

//      log.info("add center missed image");
      //add center missed image
      for (int i = 0; i < ffcs.size() - 1; i++) {

        FitsFileCut curFFC = ffcs.get(i);
        int firstNum = curFFC.getNumber();
        int secondNum = ffcs.get(i + 1).getNumber();

        for (int j = firstNum + 1; j < secondNum; j++) {
//          log.info("add number " + j);
          String ffName = String.format("%s_%04d.fit", otlv2.getIdentify(), j);
          FitsFile tff = ffDao.getByName(ffName);
          if (tff == null) {
            log.warn("add missed cut fits file, can't find orig fits file " + ffName);
            continue;
          }
          FitsFileCut ffc = new FitsFileCut();
          ffc.setFfId(tff.getFfId());
          ffc.setStorePath(curFFC.getStorePath());
          ffc.setFileName(String.format("%s_%04d", otlv2.getName(), j));
          ffc.setOtId(otlv2.getOtId());
          ffc.setNumber(j);
          ffc.setDpmId(curFFC.getDpmId());
          ffc.setImgX(curFFC.getImgX());
          ffc.setImgY(curFFC.getImgY());
          ffc.setRequestCut(false);
          ffc.setSuccessCut(false);
          ffc.setIsMissed(true);
          ffc.setPriority((short) (j - otlv2.getFirstFfNumber()));
          ffcDao.save(ffc);
        }
      }

//      log.info("add tail missed image");
      //add tail missed image
      DataProcessMachine dpm = dpmDao.getDpmById(otlv2.getDpmId());
      int curProcessNumber = dpm.getCurProcessNumber();
      FitsFileCut lastFFC = ffcs.get(ffcs.size() - 1);
      int lastNumber = lastFFC.getNumber();
//      int lastNumber = otlv2.getLastFfNumber();
//      log.info("curProcessNumber " + curProcessNumber);

      //如果超过5(successiveImageNumber)帧没有再出现新的图像，则标示该OT不会再出新的观测序列
      if (curProcessNumber - lastNumber >= successiveImageNumber) {
        int tNum = lastNumber + headTailCutNumber;
        for (int i = lastNumber + 1; i <= tNum; i++) {
//          log.info("add number " + i);
          String ffName = String.format("%s_%04d.fit", otlv2.getIdentify(), i);
          FitsFile tff = ffDao.getByName(ffName);
          if (tff == null) {
            log.warn("add missed cut fits file, can't find orig fits file " + ffName);
            continue;
          }
          FitsFileCut ffc = new FitsFileCut();
          ffc.setFfId(tff.getFfId());
          ffc.setStorePath(lastFFC.getStorePath());
          ffc.setFileName(String.format("%s_%04d", otlv2.getName(), i));
          ffc.setOtId(otlv2.getOtId());
          ffc.setNumber(i);
          ffc.setDpmId(lastFFC.getDpmId());
          ffc.setImgX(lastFFC.getImgX());
          ffc.setImgY(lastFFC.getImgY());
          ffc.setRequestCut(false);
          ffc.setSuccessCut(false);
          ffc.setIsMissed(true);
          ffc.setPriority(Short.MAX_VALUE);
          ffcDao.save(ffc);
        }
        otlv2Dao.updateAllFileCuttedById(otlv2.getOtId());
      }
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
}
