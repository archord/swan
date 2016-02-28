/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import com.gwac.dao.DataProcessMachineDAO;
import com.gwac.dao.FitsFileDAO;
import com.gwac.dao.ImageStatusParameterDao;
import com.gwac.dao.ProcessStatusDao;
import com.gwac.dao.UploadFileUnstoreDao;
import com.gwac.model.DataProcessMachine;
import com.gwac.model.FitsFile;
import com.gwac.model.ImageStatusParameter;
import com.gwac.model.ProcessStatus;
import com.gwac.model.UploadFileUnstore;
import com.gwac.util.CommonFunction;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
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
  private DataProcessMachineDAO dpmDao;

  private String rootPath;

  private static boolean running = true;
  private Boolean isBeiJingServer;
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
      parseImageStatusParm();
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

  /**
   * 解析图像状态参数文件 StringUtils.isNumericSpace("-12") return false
   * StringUtils.isNumericSpace("12.2") return false
   */
  public void parseImageStatusParm() {

    List<UploadFileUnstore> ufus = ufuDao.getImgStatusFile();
    log.debug("size=" + ufus.size());

    if (!ufus.isEmpty()) {
      List<ImageStatusParameter> isps = new ArrayList<>();
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      DateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmss");

      for (UploadFileUnstore ufu : ufus) {

        File tfile = new File(rootPath + "/" + ufu.getStorePath(), ufu.getFileName());
        if (tfile.exists()) {
          log.debug("start parse file, ufuId=" + ufu.getUfuId() + ", " + tfile.getAbsolutePath());

          try (InputStream input = new FileInputStream(tfile)) {

            String tStr;
            String tStr2;
            int dpmId = -1;
            int prcNum = -1;
            Date imageTime = null;

            Properties cfile = new Properties();
            cfile.load(input);

            String dateObsUT = cfile.getProperty("DateObsUT");
            String timeObsUT = cfile.getProperty("TimeObsUT");
            String imageName = cfile.getProperty("Image");

            if (StringUtils.isNotBlank(dateObsUT) && StringUtils.isNotBlank(timeObsUT)) {
              try {
                imageTime = df.parse(dateObsUT.trim() + " " + timeObsUT.trim());
              } catch (ParseException ex) {
                log.error("parse image status file: " + ufu.getFileName()
                        + ", error imageTime: " + dateObsUT.trim() + " " + timeObsUT.trim(), ex);
              } catch (Exception e) {
                log.error("unknow error: ", e);
              }
            }

            if (StringUtils.isNotBlank(imageName)) {
              String ccdType = imageName.substring(0, 1); //"M"
              String dpmName = ccdType + imageName.substring(3, 5);  //应该在数据库中通过dpmName查询
              String numberStr = imageName.substring(22, 26);
              DataProcessMachine dpm = dpmDao.getDpmByName(dpmName);
              if (dpm != null) {
                dpmId = dpm.getDpmId();
              }
              if (StringUtils.isNumeric(numberStr)) {
                prcNum = Integer.parseInt(numberStr);
              }
            }

            //时间，机器编号，图像编号，任何一个不正常，该条记录没有意义
            if (imageTime != null && dpmId != -1 && prcNum != -1) {
              ImageStatusParameter isp = new ImageStatusParameter();

              isp.setTimeObsUt(imageTime);
              isp.setDpmId(dpmId);
              isp.setPrcNum(prcNum);

              tStr = cfile.getProperty("Obj_Num");
              try {
                isp.setObjNum(Integer.parseInt(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setObjNum(-99);
                log.warn("Obj_Num=" + tStr, e);
              } catch (Exception e) {
                isp.setObjNum(-99);
                log.error("unknow error: " + tStr, e);
              }

              tStr = cfile.getProperty("bgbright");
              try {
                isp.setBgBright(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setBgBright(new Float(-99));
                log.warn("bgbright=" + tStr, e);
              } catch (Exception e) {
                isp.setBgBright(new Float(-99));
                log.error("unknow error: " + tStr, e);
              }

              tStr = cfile.getProperty("Fwhm");
              try {
                isp.setFwhm(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setFwhm(new Float(-99));
                log.warn("Fwhm=" + tStr, e);
              } catch (Exception e) {
                isp.setFwhm(new Float(-99));
                log.error("unknow error: " + tStr, e);
              }

              tStr = cfile.getProperty("AverDeltaMag");
              try {
                isp.setS2n(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setS2n(new Float(-99));
                log.warn("AverDeltaMag=" + tStr, e);
              } catch (Exception e) {
                isp.setS2n(new Float(-99));
                log.error("unknow error: " + tStr, e);
              }

              tStr = cfile.getProperty("AverLimit");
              try {
                isp.setAvgLimit(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setAvgLimit(new Float(-99));
                log.warn("AverLimit=" + tStr, e);
              } catch (Exception e) {
                isp.setAvgLimit(new Float(-99));
                log.error("unknow error: " + tStr, e);
              }

              tStr = cfile.getProperty("Extinc");
              try {
                isp.setExtinc(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setExtinc(new Float(-99));
                log.warn("Extinc=" + tStr, e);
              } catch (Exception e) {
                isp.setExtinc(new Float(-99));
                log.error("unknow error: " + tStr, e);
              }

              tStr = cfile.getProperty("xshift");
              try {
                isp.setXshift(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setXshift(new Float(-99));
                log.warn("xshift=" + tStr, e);
              } catch (Exception e) {
                isp.setXshift(new Float(-99));
                log.error("unknow error: " + tStr, e);
              }

              tStr = cfile.getProperty("yshift");
              try {
                isp.setYshift(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setYshift(new Float(-99));
                log.warn("yshift=" + tStr, e);
              } catch (Exception e) {
                isp.setYshift(new Float(-99));
                log.error("unknow error: " + tStr, e);
              }

              tStr = cfile.getProperty("xrms");
              try {
                isp.setXrms(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setXrms(new Float(-99));
                log.warn("xrms=" + tStr, e);
              } catch (Exception e) {
                isp.setXrms(new Float(-99));
                log.error("unknow error: " + tStr, e);
              }

              tStr = cfile.getProperty("yrms");
              try {
                isp.setYrms(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setYrms(new Float(-99));
                log.warn("yrms=" + tStr, e);
              } catch (Exception e) {
                isp.setYrms(new Float(-99));
                log.error("unknow error: " + tStr, e);
              }

              tStr = cfile.getProperty("OC1");
              try {
                isp.setOt1Num(Integer.parseInt(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setOt1Num(-99);
                log.warn("OC1=" + tStr, e);
              } catch (Exception e) {
                isp.setOt1Num(-99);
                log.error("unknow error: " + tStr, e);
              }

              tStr = cfile.getProperty("VC1");
              try {
                isp.setVar1Num(Integer.parseInt(tStr));
              } catch (NumberFormatException e) {
                isp.setVar1Num(-99);
                log.warn("VC1=" + tStr, e);
              } catch (Exception e) {
                isp.setVar1Num(-99);
                log.error("unknow error: " + tStr, e);
              }

              tStr2 = cfile.getProperty("DirData");
              FitsFile ff = new FitsFile();
              ff.setFileName(imageName);
              ff.setStorePath(tStr2);
              ffDao.save(ff);
              isp.setFfId(ff.getFfId());

              tStr = cfile.getProperty("ra_mount");
              try {
                isp.setMountRa(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setMountRa(CommonFunction.dmsToDegree(tStr));
                log.warn("ra_mount=" + tStr, e);
              } catch (Exception e) {
                isp.setMountRa(CommonFunction.dmsToDegree(tStr));
                log.error("unknow error: " + tStr, e);
              }

              tStr = cfile.getProperty("dec_mount");
              try {
                isp.setMountDec(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setMountDec(CommonFunction.dmsToDegree(tStr));
                log.warn("dec_mount=" + tStr, e);
              } catch (Exception e) {
                isp.setMountDec(CommonFunction.dmsToDegree(tStr));
                log.error("unknow error: " + tStr, e);
              }

              tStr = cfile.getProperty("State");
              if (StringUtils.isNotBlank(tStr) && !StringUtils.equalsIgnoreCase(tStr, "nan")) {
                ProcessStatus ps = new ProcessStatus();
                ps.setPsName(tStr);
                psDao.save(ps);
                isp.setProcStageId(ps.getPsId());
              }

              tStr = cfile.getProperty("TimeProcess");
              try {
                isp.setProcTime(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setProcTime(new Float(-99));
                log.warn("TimeProcess=" + tStr, e);
              } catch (Exception e) {
                isp.setProcTime(new Float(-99));
                log.error("unknow error: " + tStr, e);
              }

              tStr = cfile.getProperty("ellipticity");
              try {
                isp.setAvgEllipticity(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setAvgEllipticity(new Float(-99));
                log.warn("ellipticity=" + tStr, e);
              } catch (Exception e) {
                isp.setAvgEllipticity(new Float(-99));
                log.error("unknow error: " + tStr, e);
              }

              tStr = cfile.getProperty("tempset");
              try {
                isp.setTemperatureSet(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setTemperatureSet(new Float(-99));
                log.warn("tempset=" + tStr, e);
              } catch (Exception e) {
                isp.setTemperatureSet(new Float(-99));
                log.error("unknow error: " + tStr, e);
              }

              tStr = cfile.getProperty("tempact");
              try {
                isp.setTemperatureActual(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setTemperatureActual(new Float(-99));
                log.warn("tempact=" + tStr, e);
              } catch (Exception e) {
                isp.setTemperatureActual(new Float(-99));
                log.error("unknow error: " + tStr, e);
              }

              tStr = cfile.getProperty("exptime");
              try {
                isp.setExposureTime(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setExposureTime(new Float(-99));
                log.warn("exptime=" + tStr, e);
              } catch (Exception e) {
                isp.setExposureTime(new Float(-99));
                log.error("unknow error: " + tStr, e);
              }

              tStr = cfile.getProperty("CC_RA");
              try {
                isp.setImgCenterRa(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setImgCenterRa(new Float(-99));
                log.warn("ra_imgCenter=" + tStr, e);
              } catch (Exception e) {
                isp.setImgCenterRa(new Float(-99));
                log.error("unknow error: " + tStr, e);
              }

              tStr = cfile.getProperty("CC_DEC");
              try {
                isp.setImgCenterDec(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setImgCenterDec(new Float(-99));
                log.warn("dec_imgCenter=" + tStr, e);
              } catch (Exception e) {
                isp.setImgCenterDec(new Float(-99));
                log.error("unknow error: " + tStr, e);
              }

              tStr = cfile.getProperty("TimeProcessEnd");
              if (StringUtils.isNotBlank(tStr)) {
                try {
                  isp.setProcEndTime(df2.parse(tStr));
                } catch (ParseException ex) {
                  log.error("parse image status file: " + ufu.getFileName() + ", error procEndTiime: " + tStr, ex);
                } catch (Exception e) {
                  log.error("unknow error: " + tStr, e);
                }
              }
              isp.setSendSuccess(false);
              isps.add(isp);
              ispDao.save(isp);
            }

            try {
              input.close();
            } catch (IOException ex) {
              log.error("close property file InputStream error.", ex);
            }
          } catch (IOException ex) {
            log.error("parse image status file: " + ufu.getFileName(), ex);
          }
        } else {
          log.error("ufuId=" + ufu.getUfuId() + ", " + tfile.getAbsolutePath() + " not exists!");
        }
      }

      if (!isBeiJingServer && isps.size() > 0) {
        String ip = "190.168.1.32"; //190.168.1.32
        int port = 18851;
        Socket socket = null;
        DataOutputStream out = null;

        try {
          socket = new Socket(ip, port);
          out = new DataOutputStream(socket.getOutputStream());
          for (ImageStatusParameter isp : isps) {
            if (chechStatus(isp)) {
              String tmsg = getFWHMMessage(isp);
              try {
                out.write(tmsg.getBytes());
                out.flush();
                log.debug("send fwhm, dpmId:" + isp.getDpmId() + ", number: " + isp.getPrcNum() + ", message: " + tmsg);
              } catch (IOException ex) {
                log.error("send fwhm, send message error.", ex);
              }
              isp.setSendSuccess(true);
              ispDao.update(isp);

              try {
                Thread.sleep(100);
              } catch (InterruptedException ex) {
                log.error("send fwhm, delay error.", ex);
              }
            } else {
              log.debug("send fwhm, image status do not meet send status.");
            }
          }

          try {
            out.close();
            socket.close();
          } catch (IOException ex) {
            log.error("send fwhm, close socket error.", ex);
          }
        } catch (IOException ex) {
          log.error("send fwhm, cannot connect to server.", ex);
        }
      }

    }
  }

  public Boolean chechStatus(ImageStatusParameter isp) {
    log.debug("psId=" + isp.getProcStageId());
    Boolean flag = false;
    ProcessStatus ps = psDao.getByPsId((short) isp.getProcStageId());
    if (ps != null && !StringUtils.equalsIgnoreCase(ps.getPsName(), "TempMaking")
            && !StringUtils.equalsIgnoreCase(ps.getPsName(), "BadComImage")) {

      ImageStatusParameter previoueIsp = ispDao.getPreviousStatus(isp);
      if (previoueIsp != null && previoueIsp.getXshift() != null && previoueIsp.getYshift() != null
              && isp.getXrms() != null && isp.getYrms() != null && isp.getAvgEllipticity() != null
              && isp.getObjNum() != null && isp.getBgBright() != null && isp.getS2n() != null
              && isp.getAvgLimit() != null && isp.getFwhm() != null && isp.getXshift() != null) {

        float tXshift = previoueIsp.getXshift() - isp.getXshift();
        float tYshift = previoueIsp.getYshift() - isp.getYshift();

        Boolean s1 = Math.abs(isp.getXshift() + 99) > 0.00001
                && isp.getFwhm() < 5 && isp.getFwhm() > 1
                && isp.getXrms() > 0 && isp.getXrms() < 0.13
                && isp.getYrms() > 0 && isp.getYrms() < 0.13
                && isp.getAvgEllipticity() > 0 && isp.getAvgEllipticity() < 0.3
                && isp.getObjNum() > 5000 && isp.getObjNum() < 30000
                && isp.getBgBright() < 10000 && isp.getBgBright() > 1000
                && isp.getS2n() < 0.5
                && isp.getAvgLimit() > 12
                && (tXshift * tXshift + tYshift * tYshift) < 0.5;
        Boolean s2 = (isp.getXshift() + 99) < 0.00001
                && isp.getFwhm() < 5 && isp.getFwhm() > 1
                && isp.getAvgEllipticity() > 0 && isp.getAvgEllipticity() < 0.3
                && isp.getObjNum() > 5000 && isp.getObjNum() < 30000
                && isp.getBgBright() < 10000 && isp.getBgBright() > 1000;
        flag = s1 || s2;
      }
    } else {
      log.error("image status is null or has null porperties!");
    }
    return flag;
  }

  public String getFWHMMessage(ImageStatusParameter isp) {
    int dpmId = isp.getDpmId();
    String msg = "d#fwhm" + (int) Math.ceil(dpmId / 2.0);
    if (dpmId % 2 == 0) {
      msg += "N";
    } else {
      msg += "S";
    }
    msg += String.format("%04.0f", isp.getFwhm() * 100);
    msg += "%";
    return msg;
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
   * @param dpmDao the dpmDao to set
   */
  public void setDpmDao(DataProcessMachineDAO dpmDao) {
    this.dpmDao = dpmDao;
  }

}
