/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

import com.gwac.dao.OtObserveRecordDAO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author xy
 */
public class StoreStarInfoImpl {

  private static final Log log = LogFactory.getLog(StoreStarInfoImpl.class);
  //单次传输配置文件信息
  private String storePath;
  private String configFile;
  private String otList;
  private String starList;
  private String origImage;
  private String[] cutImages;
  private String tCutImage;
  //系统配置文件信息
  private String otLDir;
  private String starLDir;
  private String orgIDir;
  private String cutIDir;
  private OtObserveRecordDAO otORDao;

  public StoreStarInfoImpl() {
  }

  public StoreStarInfoImpl(String path, String cfile) {
    this.configFile = cfile;
    this.storePath = path;
  }

  public void storeOTList() {
  }

  public int parseConfigFile() {
    InputStream input = null;
    int fNum = 0;
    try {
      input = new FileInputStream(storePath + configFile);
      Properties cfile = new Properties();
      cfile.load(input);
      otList = cfile.getProperty("otlist");
      if (otList != null) {
        fNum++;
      }

      starList = cfile.getProperty("starlist");
      if (starList != null) {
        fNum++;
      }

      origImage = cfile.getProperty("origimage");
      if (origImage != null) {
        fNum++;
      }

      tCutImage = cfile.getProperty("cutimages");
      cutImages = (tCutImage != null) ? tCutImage.split(",") : null;
      if (cutImages != null) {
        fNum += cutImages.length;
      }

    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      if (input != null) {
        try {
          input.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return fNum;
  }

  public int checkAndMoveDataFile(String path) {

    int fileNum = 0;

    File tfile1 = null;
    File tfile2 = null;
    try {
      //存储OT列表
      tfile1 = new File(path, otList);
      tfile2 = new File(path + otLDir + "/", otList);
      if (tfile1.exists()) {
        if (tfile2.exists()) {
          FileUtils.forceDelete(tfile2);
        }
        FileUtils.moveFile(tfile1, tfile2);
        fileNum++;
      } else {
        log.warn("File " + tfile1.getAbsolutePath() + " does not exist!");
      }
      //存储星表
      tfile1 = new File(path, starList);
      tfile2 = new File(path + starLDir + "/", starList);
      if (tfile1.exists()) {
        if (tfile2.exists()) {
          FileUtils.forceDelete(tfile2);
        }
        FileUtils.moveFile(tfile1, tfile2);
        fileNum++;
      } else {
        log.warn("File " + tfile1.getAbsolutePath() + " does not exist!");
      }
      //存储原始图像
      tfile1 = new File(path, origImage);
      tfile2 = new File(path + orgIDir + "/", origImage);
      if (tfile1.exists()) {
        if (tfile2.exists()) {
          FileUtils.forceDelete(tfile2);
        }
        FileUtils.moveFile(tfile1, tfile2);
        fileNum++;
      } else {
        log.warn("File " + tfile1.getAbsolutePath() + " does not exist!");
      }
      //存储切图文件
      for (String tci : cutImages) {
        tfile1 = new File(path, tci);
        tfile2 = new File(path + cutIDir + "/", tci);
        if (tfile1.exists()) {
          if (tfile2.exists()) {
            FileUtils.forceDelete(tfile2);
          }
          //FileUtils.moveFileToDirectory(tfile1, tfile2, true);
          FileUtils.moveFile(tfile1, tfile2);
          fileNum++;
        } else {
          log.warn("File " + tfile1.getAbsolutePath() + " does not exist!");
        }
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    return fileNum;
  }

  /**
   * @param otLDir the otLDir to set
   */
  public void setOtLDir(String otLDir) {
    this.otLDir = otLDir;
  }

  /**
   * @param starLDir the starLDir to set
   */
  public void setStarLDir(String starLDir) {
    this.starLDir = starLDir;
  }

  /**
   * @param orgIDir the orgIDir to set
   */
  public void setOrgIDir(String orgIDir) {
    this.orgIDir = orgIDir;
  }

  /**
   * @param cutIDir the cutIDir to set
   */
  public void setCutIDir(String cutIDir) {
    this.cutIDir = cutIDir;
  }

  /**
   * @return the otORDao
   */
  public OtObserveRecordDAO getOtORDao() {
    return otORDao;
  }

  /**
   * @param otORDao the otORDao to set
   */
  public void setOtORDao(OtObserveRecordDAO otORDao) {
    this.otORDao = otORDao;
  }

  /**
   * @param storePath the storePath to set
   */
  public void setStorePath(String storePath) {
    this.storePath = storePath;
  }

  /**
   * @param configFile the configFile to set
   */
  public void setConfigFile(String configFile) {
    this.configFile = configFile;
  }
}
