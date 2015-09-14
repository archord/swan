/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.job;

import com.gwac.dao.FitsFileDAO;
import com.gwac.dao.ImageStatusParameterDao;
import com.gwac.dao.ProcessStatusDao;
import com.gwac.dao.UploadFileUnstoreDao;
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
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

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
    log.debug("size=" + ufus.size());
    if (ufus != null) {

      List<ImageStatusParameter> isps = new ArrayList<ImageStatusParameter>();
      InputStream input = null;
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      DateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmss");
      for (UploadFileUnstore ufu : ufus) {
        log.debug("ufuId=" + ufu.getUfuId());
        File tfile = new File(rootPath + "/" + ufu.getStorePath(), ufu.getFileName());
        log.debug(tfile.getAbsolutePath());
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
        } else {
          log.error("file not exists!");
        }
      }

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
            out.write(tmsg.getBytes());
            out.flush();
            log.debug("send message to " + ip + " : " + tmsg);
            try {
              Thread.sleep(100);
            } catch (InterruptedException ex) {
              log.error("sleep error!", ex);
            }
          } else {
            log.debug("image status do not meet send status.");
          }
        }
      } catch (IOException ex) {
        log.error("send message error", ex);
      } finally {
        try {
          if (out != null) {
            out.close();
          }
          if (socket != null || !socket.isClosed()) {
            socket.close();
          }
        } catch (IOException ex) {
          log.error("close socket error", ex);
        }
      }
    }
  }

  public Boolean chechStatus(ImageStatusParameter isp) {
    
    ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
    try {
      log.debug(ow.writeValueAsString(isp));
    } catch (IOException ex) {
      Logger.getLogger(ImageStatusParmServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
    }
    Boolean flag = false;
    if (isp != null) {
      Boolean s1 = isp.getXrms() < 0.13 && isp.getYrms() < 0.13
              && isp.getAvgEllipticity() > 0 && isp.getAvgEllipticity() < 0.26
              && isp.getObjNum() > 5000 && isp.getObjNum() < 30000
              && isp.getBgBright() < 10000 && isp.getBgBright() > 1000
              && isp.getS2n() < 0.5 && isp.getAvgLimit() > 12
              && isp.getFwhm() < 10 && isp.getFwhm() > 1;
      Boolean s2 = (isp.getXshift() + 99) < 0.00001
              && isp.getObjNum() > 5000 && isp.getObjNum() < 30000
              && isp.getBgBright() < 10000 && isp.getBgBright() > 1000
              && isp.getFwhm() < 10 && isp.getFwhm() > 1;  //&& isp.getAvgEllipticity() > 0 && isp.getAvgEllipticity() < 0.26
      flag = s1 || s2;
    }else{
      log.error("image status is null");
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

}
