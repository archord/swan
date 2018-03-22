/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import com.gwac.dao.CameraDao;
import com.gwac.dao.FitsFile2DAO;
import com.gwac.dao.ImageStatusParameterDao;
import com.gwac.dao.SystemStatusMonitorDao;
import com.gwac.dao.UploadFileUnstoreDao;
import com.gwac.model.Camera;
import com.gwac.model.FitsFile2Show;
import com.gwac.model.ImageStatusParameter;
import com.gwac.model.UploadFileUnstore;
import com.gwac.util.CommonFunction;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 解析一级OT列表文件，计算二级OT，切图，模板切图。
 *
 * @author xy
 */
@Service(value = "imageStatusParmService")
public class ImageStatusParmServiceImpl implements BaseService {

  private static final Log log = LogFactory.getLog(ImageStatusParmServiceImpl.class);
  private static boolean running = true;

  @Resource
  private UploadFileUnstoreDao ufuDao;
  @Resource
  private FitsFile2DAO ff2Dao;
  @Resource
  private CameraDao cameraDao;
  @Resource
  private ImageStatusParameterDao ispDao;
  @Resource
  private SystemStatusMonitorDao ssmDao;
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
      Map<ImageStatusParameter, FitsFile2Show> isps = new HashMap<>();
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      DateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmss");

      for (UploadFileUnstore ufu : ufus) {

        File tfile = new File(rootPath + "/" + ufu.getStorePath(), ufu.getFileName());
        if (tfile.exists()) {
          log.debug("start parse file, ufuId=" + ufu.getUfuId() + ", " + tfile.getAbsolutePath());

          try (InputStream input = new FileInputStream(tfile)) {

            String tStr;
            String tStr2;
            FitsFile2Show ff2 = null;
            int ccdId = -1;
            int prcNum = -1;
            Date imageTime = null;
            Integer subSecond = 0;

            Properties cfile = new Properties();
            cfile.load(input);

            String dateObsUT = cfile.getProperty("DateObsUT");
            //10:53:49.522836
            String timeObsUT = cfile.getProperty("TimeObsUT");
            String imageName = cfile.getProperty("Image");

            if (StringUtils.isNotBlank(dateObsUT) && StringUtils.isNotBlank(timeObsUT)) {
              try {
                int tSecIdx = timeObsUT.indexOf('.');
                if (tSecIdx > -1) {
                  String tSubSecond = timeObsUT.substring(tSecIdx + 1);
                  if (tSubSecond.length() > 0) {
                    subSecond = Integer.parseInt(tSubSecond);
                  }
                  timeObsUT = timeObsUT.substring(0, tSecIdx);
                }
                imageTime = df.parse(dateObsUT.trim() + " " + timeObsUT.trim());
              } catch (ParseException ex) {
                log.error("parse image status file: " + ufu.getFileName()
                        + ", error imageTime: " + dateObsUT.trim() + " " + timeObsUT.trim(), ex);
              } catch (NumberFormatException e) {
                log.error("error imageTime: ", e);
              }
            }

            if (StringUtils.isNotBlank(imageName)) {
              String tname = imageName.substring(0, imageName.indexOf('.')) + ".fit";
              ff2 = ff2Dao.getShowByName(tname);
              if (ff2 != null) {
                ccdId = ff2.getCamId();
                prcNum = ff2.getFfNumber();
              } else {
                log.error("cannot find image :" + tname);
              }
            }

            //时间，机器编号，图像编号，任何一个不正常，该条记录没有意义
            if (imageTime != null && ccdId != -1 && prcNum != -1) {
              ImageStatusParameter isp = new ImageStatusParameter();
              isp.setTimeSubSecond(subSecond);
              isp.setTimeObsUt(imageTime);
              isp.setDpmId(ccdId);
              isp.setPrcNum(prcNum);

              tStr = cfile.getProperty("Obj_Num");
//              if(tStr!=null&&!tStr.isEmpty())
              try {
                isp.setObjNum(Integer.parseInt(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setObjNum(-99);
                log.warn("Obj_Num=" + tStr, e);
              } catch (Exception e) {
                isp.setObjNum(-99);
                log.error("Obj_Num: " + tStr, e);
              }

              tStr = cfile.getProperty("bgbright");
              try {
                isp.setBgBright(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setBgBright(new Float(-99));
                log.warn("bgbright=" + tStr, e);
              } catch (Exception e) {
                isp.setBgBright(new Float(-99));
                log.error("bgbright: " + tStr, e);
              }

              tStr = cfile.getProperty("Fwhm");
              try {
                isp.setFwhm(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setFwhm(new Float(-99));
                log.warn("Fwhm=" + tStr, e);
              } catch (Exception e) {
                isp.setFwhm(new Float(-99));
                log.error("Fwhm: " + tStr, e);
              }

              tStr = cfile.getProperty("AverDeltaMag");
              try {
                isp.setS2n(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setS2n(new Float(-99));
                log.warn("AverDeltaMag=" + tStr, e);
              } catch (Exception e) {
                isp.setS2n(new Float(-99));
                log.error("AverDeltaMag: " + tStr, e);
              }

              tStr = cfile.getProperty("AverLimit");
              try {
                isp.setAvgLimit(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setAvgLimit(new Float(-99));
                log.warn("AverLimit=" + tStr, e);
              } catch (Exception e) {
                isp.setAvgLimit(new Float(-99));
                log.error("AverLimit: " + tStr, e);
              }

              tStr = cfile.getProperty("Extinc");
              try {
                isp.setExtinc(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setExtinc(new Float(-99));
                log.warn("Extinc=" + tStr, e);
              } catch (Exception e) {
                isp.setExtinc(new Float(-99));
                log.error("Extinc: " + tStr, e);
              }

              tStr = cfile.getProperty("xshift");
              try {
                isp.setXshift(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setXshift(new Float(-99));
                log.warn("xshift=" + tStr, e);
              } catch (Exception e) {
                isp.setXshift(new Float(-99));
                log.error("xshift: " + tStr, e);
              }

              tStr = cfile.getProperty("yshift");
              try {
                isp.setYshift(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setYshift(new Float(-99));
                log.warn("yshift=" + tStr, e);
              } catch (Exception e) {
                isp.setYshift(new Float(-99));
                log.error("yshift: " + tStr, e);
              }

              tStr = cfile.getProperty("xrms");
              try {
                isp.setXrms(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setXrms(new Float(-99));
                log.warn("xrms=" + tStr, e);
              } catch (Exception e) {
                isp.setXrms(new Float(-99));
                log.error("xrms: " + tStr, e);
              }

              tStr = cfile.getProperty("yrms");
              try {
                isp.setYrms(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setYrms(new Float(-99));
                log.warn("yrms=" + tStr, e);
              } catch (Exception e) {
                isp.setYrms(new Float(-99));
                log.error("yrms: " + tStr, e);
              }

              tStr = cfile.getProperty("OC1");
              try {
                isp.setOt1Num(Integer.parseInt(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setOt1Num(-99);
                log.warn("OC1=" + tStr, e);
              } catch (Exception e) {
                isp.setOt1Num(-99);
                log.error("OC1: " + tStr, e);
              }

              tStr = cfile.getProperty("VC1");
              try {
                isp.setVar1Num(Integer.parseInt(tStr));
              } catch (NumberFormatException e) {
                isp.setVar1Num(-99);
                log.warn("VC1=" + tStr, e);
              } catch (Exception e) {
                isp.setVar1Num(-99);
                log.error("VC1: " + tStr, e);
              }

              isp.setFfId(ff2.getFfId());

              tStr = cfile.getProperty("TimeProcess");
              try {
                isp.setProcTime(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setProcTime(new Float(-99));
                log.warn("TimeProcess=" + tStr, e);
              } catch (Exception e) {
                isp.setProcTime(new Float(-99));
                log.error("TimeProcess: " + tStr, e);
              }

              tStr = cfile.getProperty("ellipticity");
              try {
                isp.setAvgEllipticity(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setAvgEllipticity(new Float(-99));
                log.warn("ellipticity=" + tStr, e);
              } catch (Exception e) {
                isp.setAvgEllipticity(new Float(-99));
                log.error("ellipticity: " + tStr, e);
              }

              tStr = cfile.getProperty("tempset");
              try {
                isp.setTemperatureSet(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setTemperatureSet(new Float(-99));
                log.warn("tempset=" + tStr, e);
              } catch (Exception e) {
                isp.setTemperatureSet(new Float(-99));
                log.error("tempset: " + tStr, e);
              }

              tStr = cfile.getProperty("tempact");
              try {
                isp.setTemperatureActual(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setTemperatureActual(new Float(-99));
                log.warn("tempact=" + tStr, e);
              } catch (Exception e) {
                isp.setTemperatureActual(new Float(-99));
                log.error("tempact: " + tStr, e);
              }

              tStr = cfile.getProperty("exptime");
              try {
                isp.setExposureTime(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setExposureTime(new Float(-99));
                log.warn("exptime=" + tStr, e);
              } catch (Exception e) {
                isp.setExposureTime(new Float(-99));
                log.error("exptime: " + tStr, e);
              }

              tStr = cfile.getProperty("ra_mount");
              try {
                isp.setMountRa(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setMountRa(CommonFunction.dmsToDegree(tStr));
                log.warn("ra_mount=" + tStr, e);
              } catch (Exception e) {
                isp.setMountRa(CommonFunction.dmsToDegree(tStr));
                log.error("ra_mount: " + tStr, e);
              }

              tStr = cfile.getProperty("dec_mount");
              try {
                isp.setMountDec(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setMountDec(CommonFunction.dmsToDegree(tStr));
                log.warn("dec_mount=" + tStr, e);
              } catch (Exception e) {
                isp.setMountDec(CommonFunction.dmsToDegree(tStr));
                log.error("dec_mount: " + tStr, e);
              }

              tStr = cfile.getProperty("ra_imgCenter");
              try {
                isp.setImgCenterRa(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setImgCenterRa(new Float(-99));
                log.warn("ra_imgCenter=" + tStr, e);
              } catch (Exception e) {
                isp.setImgCenterRa(new Float(-99));
                log.error("ra_imgCenter: " + tStr, e);
              }

              tStr = cfile.getProperty("dec_imgCenter");
              try {
                isp.setImgCenterDec(Float.parseFloat(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setImgCenterDec(new Float(-99));
                log.warn("dec_imgCenter=" + tStr, e);
              } catch (Exception e) {
                isp.setImgCenterDec(new Float(-99));
                log.error("dec_imgCenter: " + tStr, e);
              }

              tStr = cfile.getProperty("TimeProcessEnd");
              if (StringUtils.isNotBlank(tStr)) {
                try {
                  isp.setProcEndTime(df2.parse(tStr));
                } catch (ParseException ex) {
                  log.error("parse image status file: " + ufu.getFileName() + ", error procEndTiime: " + tStr, ex);
                } catch (Exception e) {
                  log.error("procEndTiime: " + tStr, e);
                }
              }

              tStr = cfile.getProperty("AstroFlag");
              try {
                isp.setAstroFlag(Integer.parseInt(tStr.trim()));
              } catch (NumberFormatException e) {
                isp.setAstroFlag(new Integer(-99));
                log.warn("AstroFlag=" + tStr, e);
              } catch (Exception e) {
                isp.setAstroFlag(new Integer(-99));
                log.error("AstroFlag: " + tStr, e);
              }

              tStr = cfile.getProperty("template_path");
              isp.setTemplatePath(tStr);

              isp.setSendSuccess(false);
              isps.put(isp, ff2);
              ispDao.save(isp);
              String unitId = imageName.substring(1, imageName.indexOf("_"));
              ssmDao.updateImgParm(unitId, isp);
            } else {
              //imageTime != null && ccdId != -1 && prcNum
              log.error("imageTime != null && ccdId != -1 && prcNum");
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
        String ip = "172.28.1.11"; //190.168.1.32
        int port = 4011;
        Socket socket = null;
        DataOutputStream out = null;

        try {
          socket = new Socket(ip, port);
          out = new DataOutputStream(socket.getOutputStream());
          Iterator<Map.Entry<ImageStatusParameter, FitsFile2Show>> entries = isps.entrySet().iterator();
          while (entries.hasNext()) {
            Map.Entry<ImageStatusParameter, FitsFile2Show> entry = entries.next();
            ImageStatusParameter tisp = entry.getKey();
            FitsFile2Show tff2 = entry.getValue();
            sendFocus(tisp, tff2, out);
            sendGuide(tisp, tff2, out);
            if (tisp.getSendSuccess()) {
              ispDao.update(tisp);
            }
          }
          try {
            out.close();
            socket.close();
          } catch (IOException ex) {
            log.error("send focus or guide, close socket error.", ex);
          }
        } catch (IOException ex) {
          log.error("send focus or guide, cannot connect to server.", ex);
        }
      }
    }
  }

  public void sendFocus(ImageStatusParameter isp, FitsFile2Show ff2, DataOutputStream out) {
    if (chechFocusStatus(isp)) {
      String tmsg = getFWHMMessage(isp, ff2);
      try {
        out.write(tmsg.getBytes());
        out.flush();
        log.debug("start send fwhm, ccdId:" + isp.getDpmId() + ", number: " + isp.getPrcNum() + ", message: " + tmsg);
      } catch (IOException ex) {
        log.error("send fwhm, send message error.", ex);
      }
      isp.setSendSuccess(true);
      try {
        Thread.sleep(100);
      } catch (InterruptedException ex) {
        log.error("send fwhm, delay error.", ex);
      }
    } else {
      log.debug("send fwhm, image status do not meet send status.");
    }
  }

  public void sendGuide(ImageStatusParameter isp, FitsFile2Show ff2, DataOutputStream out) {
    if (chechGuideStatus(isp)) {
      String tmsg = getGuideMessage(isp, ff2);
      try {
        out.write(tmsg.getBytes());
        out.flush();
        log.debug("start send guide, ccdId:" + isp.getDpmId() + ", number: " + isp.getPrcNum() + ", message: " + tmsg);
      } catch (IOException ex) {
        log.error("send guide, send message error.", ex);
      }
      isp.setSendSuccess(true);
      try {
        Thread.sleep(100);
      } catch (InterruptedException ex) {
        log.error("send fwhm, delay error.", ex);
      }
    } else {
      log.debug("send fwhm, image status do not meet send status.");
    }
  }

  public Boolean chechGuideStatus(ImageStatusParameter isp) {

    Boolean flag = false;
    if (isp.getAstroFlag() <= 1 && isp.getAstroFlag() >= -2) {
      if (isp.getMountDec() != null && isp.getMountRa() != null
              && isp.getImgCenterDec() != null && isp.getImgCenterRa() != null
              && isp.getMountDec() >= -90 && isp.getMountDec() <= 90
              && isp.getMountRa() >= 0 && isp.getMountRa() <= 360
              && isp.getImgCenterDec() >= -90 && isp.getImgCenterDec() <= 90
              && isp.getImgCenterRa() >= 0 && isp.getImgCenterRa() <= 360) {
        Camera tcam = cameraDao.getById(isp.getDpmId());
        if (tcam.getCameraType().equalsIgnoreCase("FFoV")) {
          flag = true;
        }
      }
    }
    return flag;
  }

  public Boolean chechFocusStatus(ImageStatusParameter isp) {

    Boolean flag = false;
    if (isp.getAstroFlag() <= 1 && isp.getAstroFlag() >= -2) {
      if (isp.getXrms() != null && isp.getYrms() != null && isp.getAvgEllipticity() != null
              && isp.getObjNum() != null && isp.getBgBright() != null && isp.getS2n() != null
              && isp.getAvgLimit() != null && isp.getFwhm() != null && isp.getXshift() != null) {

        if (isp.getFwhm() < 5 && isp.getFwhm() > 0) {
          flag = true;
        }

//        Boolean s1 = Math.abs(isp.getXshift() + 99) > 0.00001
//                && isp.getFwhm() < 10 && isp.getFwhm() > 1
//                && isp.getXrms() > 0 && isp.getXrms() < 0.13
//                && isp.getYrms() > 0 && isp.getYrms() < 0.13
//                && isp.getAvgEllipticity() > 0 && isp.getAvgEllipticity() < 0.26
//                && isp.getObjNum() > 5000 && isp.getObjNum() < 30000
//                && isp.getBgBright() < 10000 && isp.getBgBright() > 1000
//                && isp.getS2n() < 0.5
//                && isp.getAvgLimit() > 11;
//        Boolean s2 = (isp.getXshift() + 99) < 0.00001
//                && isp.getFwhm() < 10 && isp.getFwhm() > 1
//                && isp.getAvgEllipticity() > 0 && isp.getAvgEllipticity() < 0.26
//                && isp.getObjNum() > 5000 && isp.getObjNum() < 30000
//                && isp.getBgBright() < 10000 && isp.getBgBright() > 1000;
//        flag = s1 || s2;
      }
    }
    return flag;
  }

  public String getGuideMessage(ImageStatusParameter isp, FitsFile2Show ff2) {

    StringBuilder sb = new StringBuilder();
    sb.append("guide Group_ID=");
    sb.append(ff2.getGroupName());
    sb.append(", Unit_ID=");
    sb.append(ff2.getUnitName());
    sb.append(", ra=");
    sb.append(isp.getImgCenterRa());
    sb.append(", dec=");
    sb.append(isp.getImgCenterDec());
    sb.append(", objra=");
    sb.append(isp.getMountRa());
    sb.append(", objdec=");
    sb.append(isp.getMountDec());
    sb.append("\n");
    return sb.toString();
  }

  public String getFWHMMessage(ImageStatusParameter isp, FitsFile2Show ff2) {

    StringBuilder sb = new StringBuilder();
    sb.append("fwhm Group_ID=");
    sb.append(ff2.getGroupName());
    sb.append(", Unit_ID=");
    sb.append(ff2.getUnitName());
    sb.append(", Camera_ID=");
    sb.append(ff2.getCamName());
    sb.append(", Value=");
    sb.append(isp.getFwhm());
    sb.append("\n");
    return sb.toString();
  }

}
