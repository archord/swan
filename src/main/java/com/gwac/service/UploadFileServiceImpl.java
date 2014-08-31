/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

import com.gwac.dao.UploadFileRecordDao;
import com.gwac.dao.UploadFileUnstoreDao;
import com.gwac.model.UploadFileRecord;
import com.gwac.model.UploadFileUnstore;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author xy
 */
public class UploadFileServiceImpl implements UploadFileService {

  private static final Log log = LogFactory.getLog(UploadFileServiceImpl.class);
  //单次传输配置文件信息
  private String storePath;
  private String configPath;
  private String configFile;
  private String[] otList;
  private String[] starList;
  private String[] origImage;
  private String[] cutImages;
  //系统配置文件信息
  private String rootDir;
  private String otLDir;
  private String starLDir;
  private String orgIDir;
  private String cutIDir;
  private String cfgDir;
  private UploadFileRecordDao ufrDao;
  private UploadFileUnstoreDao ufuDao;

  public UploadFileServiceImpl() {
  }

  public UploadFileServiceImpl(String configPath, String cfile) {
    this.configFile = cfile;
    this.configPath = configPath;
  }

  public void storeOTList() {
  }

  public int parseConfigFile() {
    InputStream input = null;
    int fNum = 0;
    try {
      input = new FileInputStream(getConfigPath() + configFile);
      Properties cfile = new Properties();
      cfile.load(input);

      String tmpStr = cfile.getProperty("otlist");
      otList = (tmpStr == null || tmpStr.isEmpty()) ? null : tmpStr.split(",");
      if (otList != null) {
        fNum += otList.length;
      }

      tmpStr = cfile.getProperty("starlist");
      starList = (tmpStr == null || tmpStr.isEmpty()) ? null : tmpStr.split(",");
      if (starList != null) {
        fNum += starList.length;
      }

      tmpStr = cfile.getProperty("origimage");
      origImage = (tmpStr == null || tmpStr.isEmpty()) ? null : tmpStr.split(",");
      if (origImage != null) {
        fNum += origImage.length;
      }

      tmpStr = cfile.getProperty("cutimages");
      cutImages = (tmpStr == null || tmpStr.isEmpty()) ? null : tmpStr.split(",");
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
    //存储OT列表
    fileNum += storeOTList(path);
    //存储星表
    fileNum += storeStarList(path);
    //存储原始图像
    fileNum += storeOrigImage(path);
    //存储切图文件
    fileNum += storeCutImage(path);
    return fileNum;
  }

  public int storeOTList(String path) {

    int fileNum = 0;
    File tfile1 = null;
    File tfile2 = null;
    try {
      if (otList != null) {
        for (String tStr : otList) {
          if (!tStr.isEmpty()) {
            String tpath = path + otLDir;
            tStr = tStr.trim();
            tfile1 = new File(path, tStr);
            tfile2 = new File(tpath + "/", tStr);

            UploadFileUnstore obj = new UploadFileUnstore();
            obj.setStorePath(tpath.substring(rootDir.length() + 1));
            obj.setFileName(tStr);
            obj.setFileType('1');   //otlist:1, starlist:2, origimage:3, cutimage:4
            obj.setUploadDate(new Date());

            UploadFileRecord obj2 = new UploadFileRecord();
            obj2.setStorePath(tpath.substring(rootDir.length() + 1));
            obj2.setFileName(tStr);
            obj2.setFileType('1');   //otlist:1, starlist:2, origimage:3, cutimage:4
            obj2.setUploadDate(new Date());

            //如果存在，必须删除，否则FileUtils.moveFile报错FileExistsException
            if (tfile1.exists()) {
              if (tfile2.exists()) {
                FileUtils.forceDelete(tfile2);
              }
              //FileUtils.moveFileToDirectory(tfile1, tfile2, true);
              FileUtils.moveFile(tfile1, tfile2);
              obj.setUploadSuccess(Boolean.TRUE);
              obj2.setUploadSuccess(Boolean.TRUE);
              fileNum++;
            } else {
              obj.setUploadSuccess(Boolean.FALSE);
              obj2.setUploadSuccess(Boolean.FALSE);
              log.warn("File " + tfile1.getAbsolutePath() + " does not exist!");
            }
            ufuDao.save(obj);
            ufrDao.save(obj2);
          }
        }
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return fileNum;
  }

  public int storeStarList(String path) {

    int fileNum = 0;
    File tfile1 = null;
    File tfile2 = null;
    try {
      if (starList != null) {
        for (String tStr : starList) {
          if (!tStr.isEmpty()) {
            String tpath = path + starLDir;
            tStr = tStr.trim();
            tfile1 = new File(path, tStr);
            tfile2 = new File(tpath + "/", tStr);

            UploadFileUnstore obj = new UploadFileUnstore();
            obj.setStorePath(tpath.substring(rootDir.length() + 1));
            obj.setFileName(tStr);
            obj.setFileType('2');   //otlist:1, starlist:2, origimage:3, cutimage:4
            obj.setUploadDate(new Date());

            UploadFileRecord obj2 = new UploadFileRecord();
            obj2.setStorePath(tpath.substring(rootDir.length() + 1));
            obj2.setFileName(tStr);
            obj2.setFileType('2');   //otlist:1, starlist:2, origimage:3, cutimage:4
            obj2.setUploadDate(new Date());

            if (tfile1.exists()) {
              if (tfile2.exists()) {
                FileUtils.forceDelete(tfile2);
              }
              //FileUtils.moveFileToDirectory(tfile1, tfile2, true);
              FileUtils.moveFile(tfile1, tfile2);
              obj.setUploadSuccess(Boolean.TRUE);
              obj2.setUploadSuccess(Boolean.TRUE);
              fileNum++;
            } else {
              obj.setUploadSuccess(Boolean.FALSE);
              obj2.setUploadSuccess(Boolean.FALSE);
              log.warn("File " + tfile1.getAbsolutePath() + " does not exist!");
            }
            ufuDao.save(obj);
            ufrDao.save(obj2);
          }
        }
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return fileNum;
  }

  public int storeOrigImage(String path) {

    int fileNum = 0;
    File tfile1 = null;
    File tfile2 = null;
    try {
      if (origImage != null) {
        for (String tStr : origImage) {
          if (!tStr.isEmpty()) {
            String tpath = path + orgIDir;
            tStr = tStr.trim();
            tfile1 = new File(path, tStr);
            tfile2 = new File(tpath + "/", tStr);

            UploadFileRecord obj = new UploadFileRecord();
            obj.setStorePath(tpath.substring(rootDir.length() + 1));
            obj.setFileName(tStr);
            obj.setFileType('3');   //otlist:1, starlist:2, origimage:3, cutimage:4
            obj.setUploadDate(new Date());

            if (tfile1.exists()) {
              if (tfile2.exists()) {
                FileUtils.forceDelete(tfile2);
              }
              //FileUtils.moveFileToDirectory(tfile1, tfile2, true);
              FileUtils.moveFile(tfile1, tfile2);
              obj.setUploadSuccess(Boolean.TRUE);
              fileNum++;
            } else {
              log.warn("File " + tfile1.getAbsolutePath() + " does not exist!");
              obj.setUploadSuccess(Boolean.FALSE);
            }
            ufrDao.save(obj);
          }
        }
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return fileNum;
  }

  public int storeCutImage(String path) {

    int fileNum = 0;
    File tfile1 = null;
    File tfile2 = null;
    try {
      if (cutImages != null) {
        for (String tStr : cutImages) {
          if (!tStr.isEmpty()) {
            String tpath = path + cutIDir;
            tStr = tStr.trim();
            tfile1 = new File(path, tStr);
            tfile2 = new File(tpath + "/", tStr);

            UploadFileRecord obj = new UploadFileRecord();
            obj.setStorePath(tpath.substring(rootDir.length() + 1));
            obj.setFileName(tStr);
            obj.setFileType('4');   //otlist:1, starlist:2, origimage:3, cutimage:4
            obj.setUploadDate(new Date());

            if (tfile1.exists()) {
              if (tfile2.exists()) {
                FileUtils.forceDelete(tfile2);
              }
              //FileUtils.moveFileToDirectory(tfile1, tfile2, true);
              FileUtils.moveFile(tfile1, tfile2);
              obj.setUploadSuccess(Boolean.TRUE);
              fileNum++;
            } else {
              obj.setUploadSuccess(Boolean.FALSE);
              log.warn("File " + tfile1.getAbsolutePath() + " does not exist!");
            }
            ufrDao.save(obj);
          }
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

  /**
   * @param ufrDao the ufrDao to set
   */
  public void setUfrDao(UploadFileRecordDao ufrDao) {
    this.ufrDao = ufrDao;
  }

  /**
   * @param ufuDao the ufuDao to set
   */
  public void setUfuDao(UploadFileUnstoreDao ufuDao) {
    this.ufuDao = ufuDao;
  }

  /**
   * @return the cfgDir
   */
  public String getCfgDir() {
    return cfgDir;
  }

  /**
   * @param cfgDir the cfgDir to set
   */
  public void setCfgDir(String cfgDir) {
    this.cfgDir = cfgDir;
  }

  /**
   * @return the configPath
   */
  public String getConfigPath() {
    return configPath;
  }

  /**
   * @param configPath the configPath to set
   */
  public void setConfigPath(String configPath) {
    this.configPath = configPath;
  }

  /**
   * @return the rootDir
   */
  public String getRootDir() {
    return rootDir;
  }

  /**
   * @param rootDir the rootDir to set
   */
  public void setRootDir(String rootDir) {
    this.rootDir = rootDir;
  }
}
