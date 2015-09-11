/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import com.gwac.dao.DataProcessMachineDAO;
import com.gwac.dao.FitsFileCutDAO;
import com.gwac.dao.FitsFileCutRefDAO;
import com.gwac.dao.FitsFileDAO;
import com.gwac.dao.ImageStatusParameterDao;
import com.gwac.dao.OTCatalogDao;
import com.gwac.dao.ObservationSkyDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.OtNumberDao;
import com.gwac.dao.OtObserveRecordDAO;
import com.gwac.dao.ProcessStatusDao;
import com.gwac.dao.UploadFileUnstoreDao;
import com.gwac.model.FitsFile;
import com.gwac.model.FitsFileCut;
import com.gwac.model.FitsFileCutRef;
import com.gwac.model.ImageStatusParameter;
import com.gwac.model.OTCatalog;
import com.gwac.model.ObservationSky;
import com.gwac.model.OtLevel2;
import com.gwac.model.OtObserveRecord;
import com.gwac.model.ProcessStatus;
import com.gwac.model.UploadFileUnstore;
import com.gwac.service.CheckImageStatusThenSendFWHM;
import com.gwac.util.CommonFunction;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 解析一级OT列表文件，计算二级OT，切图，模板切图。
 *
 * @author xy
 */
public class ImageStatusParmServiceImpl implements ImageStatusParmService {

  private static final Log log = LogFactory.getLog(ImageStatusParmServiceImpl.class);
  private UploadFileUnstoreDao ufuDao;
  private FitsFileDAO ffDao;
  private ProcessStatusDao psDao;
  private ImageStatusParameterDao ispDao;
  
  private String rootPath;

  private static boolean running = true;
  private Boolean isBeiJingServer;
  private Boolean isTestServer;

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
    parseImageStatusParm();
    long endTime = System.nanoTime();
    double time1 = 1.0 * (endTime - startTime) / 1e9;

    if (running == false) {
      running = true;
      log.debug("job is done.");
    }
    log.debug("job consume: parse image status parameter " + time1 + ".");
  }

  /**
   * 解析图像状态参数文件
   */
  public void parseImageStatusParm() {

    List<UploadFileUnstore> ufus = ufuDao.getImgStatusFile();
//    log.debug("size=" + ufus.size());
    if (ufus != null) {
      List<ImageStatusParameter> isps = new ArrayList<ImageStatusParameter>();
      InputStream input = null;
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      DateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmss");
      for (UploadFileUnstore ufu : ufus) {

        File tfile = new File(rootPath + "/" + ufu.getStorePath(), ufu.getFileName());
        if (tfile.exists()) {
          try {
            input = new FileInputStream(tfile);
            Properties cfile = new Properties();
            cfile.load(input);

            ImageStatusParameter isp = new ImageStatusParameter();
            String tStr = cfile.getProperty("DateObsUT");
            String tStr2 = cfile.getProperty("TimeObsUT");
            tStr = tStr.trim();
            tStr2 = tStr2.trim();
            if (tStr != null && tStr2 != null && !tStr.isEmpty() && !tStr2.isEmpty()) {
              isp.setTimeObsUt(df.parse(tStr + " " + tStr2));
              tStr = cfile.getProperty("Obj_Num");
              if (tStr != null) {
                tStr = tStr.trim();
                if (!tStr.isEmpty() && !tStr.equalsIgnoreCase("nan")) {
                  isp.setObjNum(Integer.parseInt(tStr.trim()));
                }
              }
              tStr = cfile.getProperty("bgbright");
              if (tStr != null) {
                tStr = tStr.trim();
                if (!tStr.isEmpty() && !tStr.equalsIgnoreCase("nan")) {
                  isp.setBgBright(Float.parseFloat(tStr));
                }
              }
              tStr = cfile.getProperty("Fwhm");
              if (tStr != null) {
                tStr = tStr.trim();
                if (!tStr.isEmpty() && !tStr.equalsIgnoreCase("nan")) {
                  isp.setFwhm(Float.parseFloat(tStr));
                }
              }
              tStr = cfile.getProperty("AverDeltaMag");
              if (tStr != null) {
                tStr = tStr.trim();
                if (!tStr.isEmpty() && !tStr.equalsIgnoreCase("nan")) {
                  isp.setS2n(Float.parseFloat(tStr));
                }
              }
              tStr = cfile.getProperty("AverLimit");
              if (tStr != null) {
                tStr = tStr.trim();
                if (!tStr.isEmpty() && !tStr.equalsIgnoreCase("nan")) {
                  isp.setAvgLimit(Float.parseFloat(tStr));
                }
              }
              tStr = cfile.getProperty("Extinc");
              if (tStr != null) {
                tStr = tStr.trim();
                if (!tStr.isEmpty() && !tStr.equalsIgnoreCase("nan")) {
                  isp.setExtinc(Float.parseFloat(tStr));
                }
              }
              tStr = cfile.getProperty("xshift");
              if (tStr != null) {
                tStr = tStr.trim();
                if (!tStr.isEmpty() && !tStr.equalsIgnoreCase("nan")) {
                  isp.setXshift(Float.parseFloat(tStr));
                }
              }
              tStr = cfile.getProperty("yshift");
              if (tStr != null) {
                tStr = tStr.trim();
                if (!tStr.isEmpty() && !tStr.equalsIgnoreCase("nan")) {
                  isp.setYshift(Float.parseFloat(tStr));
                }
              }
              tStr = cfile.getProperty("xrms");
              if (tStr != null) {
                tStr = tStr.trim();
                if (!tStr.isEmpty() && !tStr.equalsIgnoreCase("nan")) {
                  isp.setXrms(Float.parseFloat(tStr));
                }
              }
              tStr = cfile.getProperty("yrms");
              if (tStr != null) {
                tStr = tStr.trim();
                if (!tStr.isEmpty() && !tStr.equalsIgnoreCase("nan")) {
                  isp.setYrms(Float.parseFloat(tStr));
                }
              }
              tStr = cfile.getProperty("OC1");
              if (tStr != null) {
                tStr = tStr.trim();
                if (!tStr.isEmpty() && !tStr.equalsIgnoreCase("nan")) {
                  isp.setOt1Num(Integer.parseInt(tStr));
                }
              }
              tStr = cfile.getProperty("VC1");
              if (tStr != null) {
                tStr = tStr.trim();
                if (!tStr.isEmpty() && !tStr.equalsIgnoreCase("nan")) {
                  isp.setVar1Num(Integer.parseInt(tStr));
                }
              }
              tStr = cfile.getProperty("Image");
              if (tStr != null) {
                tStr = tStr.trim();
                if (!tStr.isEmpty() && !tStr.equalsIgnoreCase("nan")) {
                  tStr2 = cfile.getProperty("DirData");
                  FitsFile ff = new FitsFile();
                  ff.setFileName(tStr);
                  ff.setStorePath(tStr2);
                  ffDao.save(ff);
                  isp.setFfId(ff.getFfId());

                  int dpmId = Integer.parseInt(tStr.substring(3, 5));  //应该在数据库中通过dpmName查询
                  int number = Integer.parseInt(tStr.substring(22, 26));
                  isp.setDpmId(dpmId);
                  isp.setPrcNum(number);
                }
              }
              tStr = cfile.getProperty("ra_mount");
              if (tStr != null) {
                tStr = tStr.trim();
                if (!tStr.isEmpty() && !tStr.equalsIgnoreCase("nan")) {
                  if (tStr.contains(":")) {
                    isp.setMountRa(CommonFunction.dmsToDegree(tStr));
                  } else {
                    isp.setMountRa(Float.parseFloat(tStr));
                  }
                }
              }
              tStr = cfile.getProperty("dec_mount");
              if (tStr != null) {
                tStr = tStr.trim();
                if (!tStr.isEmpty() && !tStr.equalsIgnoreCase("nan")) {
                  if (tStr.contains(":")) {
                    isp.setMountDec(CommonFunction.dmsToDegree(tStr));
                  } else {
                    isp.setMountDec(Float.parseFloat(tStr));
                  }
                }
              }
              tStr = cfile.getProperty("State");
              if (tStr != null) {
                tStr = tStr.trim();
                if (!tStr.isEmpty() && !tStr.equalsIgnoreCase("nan")) {
                  ProcessStatus ps = new ProcessStatus();
                  ps.setPsName(tStr);
                  psDao.save(ps);
                  isp.setProcStageId(ps.getPsId());
                }
              }
              tStr = cfile.getProperty("TimeProcess");
              if (tStr != null) {
                tStr = tStr.trim();
                if (!tStr.isEmpty() && !tStr.equalsIgnoreCase("nan")) {
                  isp.setProcTime(Float.parseFloat(tStr));
                }
              }
              tStr = cfile.getProperty("ellipticity");
              if (tStr != null) {
                tStr = tStr.trim();
                if (!tStr.isEmpty() && !tStr.equalsIgnoreCase("nan")) {
                  isp.setAvgEllipticity(Float.parseFloat(tStr));
                }
              }
              tStr = cfile.getProperty("tempset");
              if (tStr != null) {
                tStr = tStr.trim();
                if (!tStr.isEmpty() && !tStr.equalsIgnoreCase("nan")) {
                  isp.setTemperatureSet(Float.parseFloat(tStr));
                }
              }
              tStr = cfile.getProperty("tempact");
              if (tStr != null) {
                tStr = tStr.trim();
                if (!tStr.isEmpty() && !tStr.equalsIgnoreCase("nan")) {
                  isp.setTemperatureActual(Float.parseFloat(tStr));
                }
              }
              tStr = cfile.getProperty("exptime");
              if (tStr != null) {
                tStr = tStr.trim();
                if (!tStr.isEmpty() && !tStr.equalsIgnoreCase("nan")) {
                  isp.setExposureTime(Float.parseFloat(tStr));
                }
              }
              tStr = cfile.getProperty("ra_imgCenter");
              if (tStr != null) {
                tStr = tStr.trim();
                if (!tStr.isEmpty() && !tStr.equalsIgnoreCase("nan")) {
                  isp.setImgCenterRa(Float.parseFloat(tStr));
                }
              }
              tStr = cfile.getProperty("dec_imgCenter");
              if (tStr != null) {
                tStr = tStr.trim();
                if (!tStr.isEmpty() && !tStr.equalsIgnoreCase("nan")) {
                  isp.setImgCenterDec(Float.parseFloat(tStr));
                }
              }
              tStr = cfile.getProperty("TimeProcessEnd");
              if (tStr != null) {
                tStr = tStr.trim();
                if (!tStr.isEmpty() && !tStr.equalsIgnoreCase("nan")) {
                  isp.setProcEndTime(df2.parse(tStr));
                }
              }
              isps.add(isp);
              ispDao.save(isp);
              
//              CheckImageStatusThenSendFWHM cis = new CheckImageStatusThenSendFWHM(isp);
//              cis.chechStatus();
            }
          } catch (NumberFormatException ex) {
            log.error(ufu.getFileName());
            log.error(ex);
          } catch (FileNotFoundException ex) {
            log.error(ufu.getFileName());
            log.error(ex);
          } catch (IOException ex) {
            log.error(ufu.getFileName());
            log.error(ex);
          } catch (ParseException ex) {
            log.error(ufu.getFileName());
            log.error(ex);
          } finally {
            if (input != null) {
              try {
                input.close();
              } catch (IOException ex) {
                log.error(ex);
              }
            }
          }
        }
      }
//      if (!isps.isEmpty()) {
//        ispDao.save(isps);
//      }
    }
  }

  /**
   * @param ufuDao the ufuDao to set
   */
  public void setUfuDao(UploadFileUnstoreDao ufuDao) {
    this.ufuDao = ufuDao;
  }

  /**
   * @param ffDao the ffDao to set
   */
  public void setFfDao(FitsFileDAO ffDao) {
    this.ffDao = ffDao;
  }

  /**
   * @param psDao the psDao to set
   */
  public void setPsDao(ProcessStatusDao psDao) {
    this.psDao = psDao;
  }

  /**
   * @param ispDao the ispDao to set
   */
  public void setIspDao(ImageStatusParameterDao ispDao) {
    this.ispDao = ispDao;
  }

}
