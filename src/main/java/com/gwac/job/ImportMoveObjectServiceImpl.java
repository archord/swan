/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import com.gwac.dao.MoveObjectDao;
import com.gwac.dao.MoveObjectRecordDao;
import com.gwac.dao.OtObserveRecordDAO;
import com.gwac.model.MoveObject;
import com.gwac.model.MoveObjectRecord;
import com.gwac.model.OtObserveRecord;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.gwac.util.CommonFunction;

/**
 * 数据转移，清空需要频繁查询的表，这些表只存储当天的最新数据，之后将表中数据移动到历史表
 *
 * @author xy
 */
@Service(value = "importMoveObjectService")
public class ImportMoveObjectServiceImpl implements BaseService {

  private static final Log log = LogFactory.getLog(ImportMoveObjectServiceImpl.class);
  private static boolean running = true;

  @Resource
  private OtObserveRecordDAO oorDao;
  @Resource(name = "moveObjectDao")
  private MoveObjectDao moveObjectDao;
  @Resource(name = "moveObjectRecordDao")
  private MoveObjectRecordDao moveObjectRecordDao;

  @Value("#{syscfg.gwacServerBeijing}")
  private Boolean isBeiJingServer;
  @Value("#{syscfg.gwacServerTest}")
  private Boolean isTestServer;

//  @Scheduled(cron = "0/1 * *  * * ? ")
  @Override
  public void startJob() {

//    if (isBeiJingServer || isTestServer) {
//      return;
//    }
    long startTime = System.nanoTime();
    getAll();
    long endTime = System.nanoTime();
    log.debug("job consume " + 1.0 * (endTime - startTime) / 1e9 + " seconds.");
  }

  void getAll() {

    String dirPath1 = "/home/xy/Downloads/myresource/linefinder";

    log.debug("read object from " + dirPath1);
    File dir1 = new File(dirPath1);
    File[] flist1 = dir1.listFiles();
    log.debug("total read " + flist1.length + " object files.");

    int i = 0;
    for (File tfile : flist1) {
      List<OtObserveRecord> oors = getOT1(tfile);
      if (oors.size() <= 150) {
        continue;
      }
      OtObserveRecord oor1 = oors.get(0);
      OtObserveRecord oor2 = oors.get(1);
      double dis = CommonFunction.getGreatCircleDistance(oor1.getRaD(), oor1.getDecD(), oor2.getRaD(), oor2.getDecD()) * 3600; // 1 second
      if (dis > 6 * 30.0) {
        log.debug(tfile.getName() + ", speed is " + dis + ", exceed " + 6 * 30.0);
        continue;
      }
      for (OtObserveRecord oor : oors) {
        oorDao.save(oor);
      }
      OtObserveRecord toor1 = oors.get(0);
      OtObserveRecord toor2 = oors.get(oors.size() - 1);
      MoveObject mo = new MoveObject();
      mo.setAvgFramePointNumber((float) 1);
      mo.setDateStr("161228");
      mo.setDpmId(12);
      mo.setFirstFrameNum(toor1.getFfNumber());
      mo.setFirstFrameTime(toor1.getDateUt());
      mo.setFramePointMaxNumber(1);
      mo.setFramePointMultiNumber(0);
      mo.setLastFrameNum(toor2.getFfNumber());
      mo.setLastFrameTime(toor2.getDateUt());
      if (dis < 3 * 30.0) {
        mo.setMovType('1');
      } else {
        mo.setMovType('2');
      }
      mo.setPointNumber(oors.size());
      mo.setSkyId(0);
      mo.setTotalFrameNumber(oors.size());
      moveObjectDao.save(mo);

      for (OtObserveRecord oor : oors) {
        MoveObjectRecord mor = new MoveObjectRecord();
        mor.setOorId(oor.getOorId());
        mor.setMovId(mo.getMovId());
        moveObjectRecordDao.save(mor);
      }
      log.debug(tfile.getName() + " save " + oors.size() + " records!");
      i++;
//      break;
    }
    log.debug("total save " + i + " objs.");
  }

  public List<OtObserveRecord> getOT1(File file) {

    List<OtObserveRecord> ot1list = new ArrayList();

    BufferedReader reader = null;
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
      reader = new BufferedReader(new FileReader(file));
      String tempString = null;
      int tline = 0;
      int tIdx = 1;
      int tlen = "2016-12-28 15:54:17".length();
      //2318.9	381.724	170.284	4.01298	2015/12/18 18:25:28	10.7607	1777	34	1626.55	2273.18
      while ((tempString = reader.readLine()) != null) {
        if (tempString.isEmpty()) {
          continue;
        }
        tline++;
        if (tline % 15 != 1) {
          continue;
        }
        String[] tstr = tempString.split(" +");
        float x = 0;
        float y = 0;  //1
        float ra = Float.parseFloat(tstr[3]); //2
        float dec = Float.parseFloat(tstr[4]);
        String dateStr = tstr[1] + " " + tstr[2];
        float mag = 0;
        int number = tIdx++;
        Date tdate = null;
        if (tstr[4].trim().length() == tlen) {
          tdate = sdf.parse(dateStr);
        } else {
          tdate = sdf2.parse(dateStr);
        }

        OtObserveRecord ot1 = new OtObserveRecord();
        ot1.setOtId((long) 0);
        ot1.setX(x);
        ot1.setY(y);
        ot1.setRaD(ra);
        ot1.setDecD(dec);
        ot1.setMagAper(mag);
        ot1.setFfNumber(number);
        ot1.setXTemp(x);
        ot1.setYTemp(y);
        ot1.setDateUt(tdate);
        ot1.setDateStr("161228");
        ot1.setDataProduceMethod('1');
        ot1.setDpmId(12);
        ot1.setFfId((long)0);

        ot1list.add(ot1);
        tline++;
      }
      reader.close();

    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException ex) {
      ex.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    }
    return ot1list;
  }

}
