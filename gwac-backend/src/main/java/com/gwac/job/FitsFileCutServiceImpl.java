/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import com.gwac.dao.DataProcessMachineDAO;
import com.gwac.dao.FitsFile2DAO;
import com.gwac.dao.FitsFileCutDAO;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtObserveRecordDAO;
import com.gwac.model.FitsFile2;
import com.gwac.model.FitsFileCut;
import com.gwac.model.OtLevel2;
import com.gwac.model.OtObserveRecord;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 根据OtObserveRecordService产生的有观测记录的切图序列，计算序列中缺失的切图文件。
 *
 * @author xy
 */
@Service(value = "fitsFileCutService")
public class FitsFileCutServiceImpl implements BaseService {

  private static final Log log = LogFactory.getLog(FitsFileCutServiceImpl.class);
  private static boolean running = true;
  
  @Resource
  private FitsFile2DAO ff2Dao;
  @Resource
  private FitsFileCutDAO ffcDao;
  @Resource
  private OtLevel2Dao otlv2Dao;
  @Resource
  private DataProcessMachineDAO dpmDao;
  @Resource
  private OtObserveRecordDAO oorDao;

  @Value("#{syscfg.gwacSuccessiveImageNumber}")
  private int successiveImageNumber;
  @Value("#{syscfg.gwacImageHeadTailCutNumber}")
  private int headTailCutNumber;
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

      for (int i = otlv2.getLastFfNumber() + 1; i <= otlv2.getLastFfNumber() + 2; i++) {
        String ffName = String.format("%s_%04d.fit", otlv2.getIdentify(), i);
        FitsFile2 tff = ff2Dao.getByName(ffName);
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
        ffc.setPriority((short) (otlv2.getLastFfNumber() - i));
        ffcDao.save(ffc);
      }

      otlv2.setCuttedFfNumber(otlv2.getLastFfNumber() + 2);
      otlv2Dao.updateCuttedFfNumber(otlv2);
    }
  }

  /**
   * 1，OT2在生成时，已切图编号为0（cuttedFfNumber=0）
   */
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
        FitsFile2 tff = ff2Dao.getByName(ffName);
        if (tff == null) {
          log.warn("can't find orig fits file " + ffName + ", is the sky region name correct?");
          continue;
        }
        while (oorIdx < oors.size() && oors.get(oorIdx).getFfNumber() <= i) {
          oorIdx++;
        }

        int tIdx = oorIdx;
        if (i >= otlv2.getFirstFfNumber() && oorIdx > 0) {
          tIdx = oorIdx - 1;
        }
        //防止指针越界
        if (tIdx < 0) {
          tIdx = 0;
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
        if (toor.getFfNumber() == i || i < otlv2.getFirstFfNumber() || i > otlv2.getLastFfNumber()) {
          if (i == otlv2.getLastFfNumber()) {
            ffc.setPriority((short) 0);
          } else {
            ffc.setPriority((short) (i - otlv2.getFirstFfNumber()));
          }
        } else {
          ffc.setPriority(Short.MAX_VALUE);
        }

        ffcDao.save(ffc);

        if (toor.getFfNumber() == i) {
          toor.setFfcId(ffc.getFfcId());
          oorDao.updateFfcId(toor);
        }
      }

      otlv2.setCuttedFfNumber(otlv2.getLastFfNumber());
      otlv2Dao.updateCuttedFfNumber(otlv2);
    }
  }

}
