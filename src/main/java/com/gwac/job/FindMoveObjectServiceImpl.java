/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import com.gwac.dao.DataProcessMachineDAO;
import com.gwac.dao.MoveObjectDao;
import com.gwac.dao.MoveObjectRecordDao;
import com.gwac.dao.ObservationSkyDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtObserveRecordDAO;
import com.gwac.linefind.DrawObject;
import com.gwac.linefind.HoughTransform;
import com.gwac.linefind.LineObject;
import com.gwac.model.DataProcessMachine;
import com.gwac.model.ObservationSky;
import com.gwac.model.OtObserveRecord;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 数据转移，清空需要频繁查询的表，这些表只存储当天的最新数据，之后将表中数据移动到历史表
 *
 * @author xy
 */
@Service(value = "findMoveObjectService")
public class FindMoveObjectServiceImpl implements BaseService {

  private static final Log log = LogFactory.getLog(FindMoveObjectServiceImpl.class);
  private static boolean running = true;

  @Resource
  private OtLevel2Dao otlv2Dao;
  @Resource
  private OtObserveRecordDAO oorDao;
  @Resource(name = "moveObjectDao")
  private MoveObjectDao moveObjectDao;
  @Resource(name = "moveObjectRecordDao")
  private MoveObjectRecordDao moveObjectRecordDao;
  @Resource
  private ObservationSkyDao observationSkyDao;
  @Resource
  private DataProcessMachineDAO dpmDao;

  @Value("#{syscfg.gwacServerBeijing}")
  private Boolean isBeiJingServer;
  @Value("#{syscfg.gwacServerTest}")
  private Boolean isTestServer;

  private int imgWidth = 3072;
  private int imgHeight = 3072;
  private int thetaSize = 180;
  private int rhoSize = 100;
  private int thetaRange = 36;
  private int rhoRange = 10;
  private int maxHoughFrameNunmber = 10;
  private int validLineMinPoint = 5;
  private float maxDistance = 100;
  private int minValidPoint = 3;
  private float rhoErrorTimes = (float) 0.2; //0.2

  int count = 1;

//  @Scheduled(cron = "0/1 * *  * * ? ")
  @Override
  public void startJob() {
    int aa = count++;
    log.warn(aa + " start, move object number: ");
    processAllDay();
    log.warn(aa + " end, move object number: ");
  }

  public void processAllDay() {

//    List<String> dateStrs = otlv2Dao.getAllDateStr();
//    List<ObservationSky> skys = observationSkyDao.findAll();
//    List<DataProcessMachine> dpms = dpmDao.findAll();
//    log.debug("total days: " + dateStrs.size());
//    for (String dateStr : dateStrs) {
//      for (DataProcessMachine dpm : dpms) {
//        for (ObservationSky sky : skys) {
//          List<OtObserveRecord> oors = oorDao.getOt1ByDateDpmSkyId(dateStr, dpm.getDpmId(), sky.getSkyId());
//        }
//      }
//    }
    String dateStr = "160928";
    int dpmId = 6;
    int skyId = 11;
    List<OtObserveRecord> oors = oorDao.getOt1ByDateDpmSkyId(dateStr, dpmId, skyId);
    System.out.println("size:" + oors.size());
    processOneDay(oors);

  }

  public void processOneDay(List<OtObserveRecord> oors) {

    HoughTransform ht = new HoughTransform(imgWidth, imgHeight, thetaSize, rhoSize, thetaRange, rhoRange, maxHoughFrameNunmber, minValidPoint, maxDistance, rhoErrorTimes, validLineMinPoint);

    int lastFrameNumber = 0;
    int frameCount = 0;
    int pNum = 0;
    for (OtObserveRecord oor : oors) {
      if (lastFrameNumber != oor.getFfNumber()) {
        lastFrameNumber = oor.getFfNumber();
        ht.endFrame();
      }
      ht.historyAddPoint(oor);
      ht.lineAddPoint(oor);

      pNum++;

    }

    ht.endAllFrame();

    ArrayList<LineObject> mvObjs = ht.getMvObjs();
    ArrayList<LineObject> fastObjs = ht.getFastObjs();
    ArrayList<LineObject> singleFrameObjs = ht.getSingleFrameObjs();

//    ht.saveLine2(outPath);
//    DrawObject dObj = new DrawObject(ht);
//    dObj.drawObjsAll("E:\\160928-5-11.png");
  }

}
