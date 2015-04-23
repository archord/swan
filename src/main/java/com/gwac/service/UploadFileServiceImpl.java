/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

import com.gwac.dao.DataProcessMachineDAO;
import com.gwac.dao.FitsFileCutDAO;
import com.gwac.dao.FitsFileCutRefDAO;
import com.gwac.dao.UploadFileRecordDao;
import com.gwac.dao.UploadFileUnstoreDao;
import com.gwac.model.DataProcessMachine;
import com.gwac.model.FitsFileCutRef;
import com.gwac.model.UploadFileRecord;
import com.gwac.model.UploadFileUnstore;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Pattern;
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
  private String[] varList;
  //系统配置文件信息
  private String rootDir;
  private String otLDir;
  private String starLDir;
  private String orgIDir;
  private String cutIDir;
  private String cfgDir;
  private String varLDir;
  private UploadFileRecordDao ufrDao;
  private UploadFileUnstoreDao ufuDao;
  private DataProcessMachineDAO dpmDao;
  private FitsFileCutDAO ffcDao;
  private FitsFileCutRefDAO ffcrDao;

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
      File tfile = new File(configPath, configFile);
      if (!tfile.exists()) {
        log.error("file not exist: " + tfile);
        return 0;
      }
      input = new FileInputStream(tfile);
      Properties cfile = new Properties();
      cfile.load(input);

      String dateStr = cfile.getProperty("date").trim();
      String dpmName = cfile.getProperty("dpmname").trim();
      String curProcNumber = cfile.getProperty("curprocnumber").trim();
      String dfInfo = cfile.getProperty("dfinfo").trim();
//      log.debug("dpmName=" + dpmName + ".");
//      log.debug("curProcNumber=" + curProcNumber + ".");
//      log.debug("dfInfo=" + dfInfo + ".");
      if (dpmName != null && curProcNumber != null && dfInfo != null
              && !dpmName.isEmpty() && !curProcNumber.isEmpty() && !dfInfo.isEmpty()) {
        dpmName = dpmName.toUpperCase();
        Pattern p = Pattern.compile("[ ]+");
        String[] strs = p.split(dfInfo);
        float totalSize = 0;
        float leftSize = 0;
        float percent = 0;
        if (strs[2].contains("T")) {
          totalSize = Float.parseFloat(strs[2].replace('T', ' '));
        } else if (strs[2].contains("G")) {
          totalSize = Float.parseFloat(strs[2].replace('G', ' ')) / (float) 1024.0;
        } else if (strs[2].contains("M")) {
          totalSize = Float.parseFloat(strs[2].replace('M', ' ')) / (float) (1024.0 * 1024.0);
        }
        if (strs[3].contains("T")) {
          leftSize = Float.parseFloat(strs[3].replace('T', ' '));
        } else if (strs[3].contains("G")) {
          leftSize = Float.parseFloat(strs[3].replace('G', ' ')) / (float) 1024.0;
        } else if (strs[3].contains("M")) {
          leftSize = Float.parseFloat(strs[3].replace('M', ' ')) / (float) (1024.0 * 1024.0);
        }
        if (strs[5].contains("%")) {
          percent = Float.parseFloat(strs[5].replace('%', ' '));
        }
//        log.debug(totalSize);
//        log.debug(leftSize);
//        log.debug(percent);

        DataProcessMachine dpm = dpmDao.getDpmByName(dpmName);
        dpm.setCurProcessNumber(Integer.parseInt(curProcNumber));
        dpm.setTotalStorageSize(totalSize);
        dpm.setUsedStorageSize(leftSize);
        dpmDao.update(dpm);
      }

      String tmpStr = cfile.getProperty("otlist");
      otList = (tmpStr == null || tmpStr.isEmpty()) ? null : tmpStr.trim().split(",");
      if (otList != null) {
        fNum += otList.length;
      }

      tmpStr = cfile.getProperty("varilist");
      varList = (tmpStr == null || tmpStr.isEmpty()) ? null : tmpStr.trim().split(",");
      if (varList != null) {
        fNum += varList.length;
      }

      tmpStr = cfile.getProperty("starlist");
      starList = (tmpStr == null || tmpStr.isEmpty()) ? null : tmpStr.trim().split(",");
      if (starList != null) {
        fNum += starList.length;
      }

      tmpStr = cfile.getProperty("origimage");
      origImage = (tmpStr == null || tmpStr.isEmpty()) ? null : tmpStr.trim().split(",");
      if (origImage != null) {
        fNum += origImage.length;
      }

      tmpStr = cfile.getProperty("cutimages");
      cutImages = (tmpStr == null || tmpStr.isEmpty()) ? null : tmpStr.trim().split(",");
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
    //存储变星列表
    fileNum += storeVarList(path);
    return fileNum;
  }

  public int storeOTList(String path) {

    int fileNum = 0;
    File tfile1 = null;
    File tfile2 = null;
    try {

      String tpath = path + otLDir;
      File dir = new File(tpath);
      if (!dir.exists()) {
        dir.mkdir();
      }

      if (otList != null) {
        for (String tStr : otList) {
          tStr = tStr.trim();
          if (!tStr.isEmpty()) {
            log.debug("receive file " + tStr);
            tfile1 = new File(path, tStr);
            tfile2 = new File(tpath + "/", tStr);

            UploadFileUnstore obj = new UploadFileUnstore();
            obj.setStorePath(tpath.substring(rootDir.length() + 1));
            obj.setFileName(tStr);
            obj.setFileType('1');   //otlist:1, starlist:2, origimage:3, cutimage:4, 9种监控图（共108幅）:5, varlist:6
            obj.setUploadDate(new Date());

            UploadFileRecord obj2 = new UploadFileRecord();
            obj2.setStorePath(tpath.substring(rootDir.length() + 1));
            obj2.setFileName(tStr);
            obj2.setFileType('1');   //otlist:1, starlist:2, origimage:3, cutimage:4, 9种监控图（共108幅）:5, varlist:6
            obj2.setUploadDate(new Date());

            //如果存在，必须删除，否则FileUtils.moveFile报错FileExistsException
            if (tfile2.exists()) {
              if (tfile1.exists()) {
                log.warn(tfile2 + " already exist, delete it.");
                FileUtils.forceDelete(tfile2);
                //FileUtils.moveFileToDirectory(tfile1, tfile2, true);
                FileUtils.moveFile(tfile1, tfile2);
                fileNum++;
              }
            } else {
              if (tfile1.exists()) {
                FileUtils.moveFile(tfile1, tfile2);
                fileNum++;
                obj.setUploadSuccess(Boolean.TRUE);
                obj2.setUploadSuccess(Boolean.TRUE);
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
      }
    } catch (IOException ex) {
      log.info("move file errror:");
      log.error(ex);
    }
    return fileNum;
  }

  public int storeVarList(String path) {

    int fileNum = 0;
    File tfile1 = null;
    File tfile2 = null;
    try {

      String tpath = path + varLDir;
      File dir = new File(tpath);
      if (!dir.exists()) {
        dir.mkdir();
      }

      if (varList != null) {
        for (String tStr : varList) {
          tStr = tStr.trim();
          if (!tStr.isEmpty()) {
            log.debug("receive varlist file " + tStr);
            tfile1 = new File(path, tStr);
            tfile2 = new File(tpath + "/", tStr);

            UploadFileUnstore obj = new UploadFileUnstore();
            obj.setStorePath(tpath.substring(rootDir.length() + 1));
            obj.setFileName(tStr);
            obj.setFileType('6');   //otlist:1, starlist:2, origimage:3, cutimage:4, 9种监控图（共108幅）:5, varlist:6
            obj.setUploadDate(new Date());

            UploadFileRecord obj2 = new UploadFileRecord();
            obj2.setStorePath(tpath.substring(rootDir.length() + 1));
            obj2.setFileName(tStr);
            obj2.setFileType('6');   //otlist:1, starlist:2, origimage:3, cutimage:4, 9种监控图（共108幅）:5, varlist:6
            obj2.setUploadDate(new Date());

            //如果存在，必须删除，否则FileUtils.moveFile报错FileExistsException
            if (tfile2.exists()) {
              if (tfile1.exists()) {
                log.warn(tfile2 + " already exist, delete it.");
                FileUtils.forceDelete(tfile2);
                //FileUtils.moveFileToDirectory(tfile1, tfile2, true);
                FileUtils.moveFile(tfile1, tfile2);
                fileNum++;
              }
            } else {
              if (tfile1.exists()) {
                FileUtils.moveFile(tfile1, tfile2);
                fileNum++;
                obj.setUploadSuccess(Boolean.TRUE);
                obj2.setUploadSuccess(Boolean.TRUE);
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
      }
    } catch (IOException ex) {
      log.info("move file errror:");
      log.error(ex);
    }
    return fileNum;
  }

  public int storeStarList(String path) {

    int fileNum = 0;
    File tfile1 = null;
    File tfile2 = null;
    try {

      String tpath = path + starLDir;
      File dir = new File(tpath);
      if (!dir.exists()) {
        dir.mkdir();
      }

      if (starList != null) {
        for (String tStr : starList) {
          tStr = tStr.trim();
          if (!tStr.isEmpty()) {
            log.debug("receive file " + tStr);
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

            if (tfile2.exists()) {
              if (tfile1.exists()) {
                log.warn(tfile2 + " already exist, delete it.");
                FileUtils.forceDelete(tfile2);
                //FileUtils.moveFileToDirectory(tfile1, tfile2, true);
                FileUtils.moveFile(tfile1, tfile2);
                fileNum++;
              }
            } else {
              if (tfile1.exists()) {
                FileUtils.moveFile(tfile1, tfile2);
                fileNum++;
                obj.setUploadSuccess(Boolean.TRUE);
                obj2.setUploadSuccess(Boolean.TRUE);
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
      }
    } catch (IOException ex) {
      log.info("move file errror:");
      log.error(ex);
    }
    return fileNum;
  }

  public int storeOrigImage(String path) {

    int fileNum = 0;
    File tfile1 = null;
    File tfile2 = null;
    try {

      String tpath = path + orgIDir;
      File dir = new File(tpath);
      if (!dir.exists()) {
        dir.mkdir();
      }

      if (origImage != null) {
        for (String tStr : origImage) {
          tStr = tStr.trim();
          if (!tStr.isEmpty()) {
            log.debug("receive file " + tStr);
            tfile1 = new File(path, tStr);
            tfile2 = new File(tpath + "/", tStr);

            UploadFileRecord obj = new UploadFileRecord();
            obj.setStorePath(tpath.substring(rootDir.length() + 1));
            obj.setFileName(tStr);
            obj.setFileType('3');   //otlist:1, starlist:2, origimage:3, cutimage:4
            obj.setUploadDate(new Date());

            if (tfile2.exists()) {
              if (tfile1.exists()) {
                log.warn(tfile2 + " already exist, delete it.");
                FileUtils.forceDelete(tfile2);
                //FileUtils.moveFileToDirectory(tfile1, tfile2, true);
                FileUtils.moveFile(tfile1, tfile2);
                fileNum++;
              }
            } else {
              if (tfile1.exists()) {
                FileUtils.moveFile(tfile1, tfile2);
                fileNum++;
                obj.setUploadSuccess(Boolean.TRUE);
              } else {
                obj.setUploadSuccess(Boolean.FALSE);
                log.warn("File " + tfile1.getAbsolutePath() + " does not exist!");
              }
              ufrDao.save(obj);
            }
          }
        }
      }
    } catch (IOException ex) {
      log.info("move file errror:");
      log.error(ex);
    }
    return fileNum;
  }

  public int storeCutImage(String path) {

    int fileNum = 0;
    File tfile1 = null;
    File tfile2 = null;
    try {

      String tpath = path + cutIDir;
      File dir = new File(tpath);
      if (!dir.exists()) {
        dir.mkdir();
      }

      if (cutImages != null) {
        for (String tStr : cutImages) {
          tStr = tStr.trim();
          if (!tStr.isEmpty()) {
            log.debug("receive file " + tStr);
            tfile1 = new File(path, tStr);
            tfile2 = new File(tpath + "/", tStr);

            UploadFileRecord obj = new UploadFileRecord();
            obj.setStorePath(tpath.substring(rootDir.length() + 1));
            obj.setFileName(tStr);
            obj.setFileType('4');   //otlist:1, starlist:2, origimage:3, cutimage:4
            obj.setUploadDate(new Date());

            if (tfile2.exists()) {
              if (tfile1.exists()) {
                log.warn(tfile2 + " already exist, delete it.");
                FileUtils.forceDelete(tfile2);
                //FileUtils.moveFileToDirectory(tfile1, tfile2, true);
                FileUtils.moveFile(tfile1, tfile2);
                fileNum++;
              }
            } else {
              if (tfile1.exists()) {
                FileUtils.moveFile(tfile1, tfile2);
                fileNum++;
                obj.setUploadSuccess(Boolean.TRUE);
                if (tStr.indexOf(".jpg") > 0) {
                  ffcDao.uploadSuccessCutByName(tStr.substring(0, tStr.indexOf('.')));
                }
              } else {
                obj.setUploadSuccess(Boolean.FALSE);
                log.warn("File " + tfile1.getAbsolutePath() + " does not exist!");
              }
              ufrDao.save(obj);
            }

            /**
             * 解析模板截图时间
             */
            if (tfile2.exists() && tStr.endsWith("jpg") && tStr.contains("ref")) {
              try {
                String dateStr = tStr.substring(tStr.indexOf("ref_") + 4, tStr.indexOf(".jpg"));
                if (dateStr != null && dateStr.length() == 15) {
                  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
                  Date genDate = sdf.parse(dateStr.replace('T', ' '));

                  FitsFileCutRef ffcr = new FitsFileCutRef();
                  ffcr.setFileName(tStr.substring(0, tStr.indexOf(".jpg")));
                  ffcr.setGenerateTime(genDate);
                  ffcr.setSuccessCut(Boolean.TRUE);
                  ffcrDao.updateByName(ffcr);
                }
              } catch (ParseException ex) {
                log.error("parse ref cut image date error.");
              }
            }
          }
        }
      }
    } catch (IOException ex) {
      log.info("move file errror:");
      log.error(ex);
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

  /**
   * @return the dpmDao
   */
  public DataProcessMachineDAO getDpmDao() {
    return dpmDao;
  }

  /**
   * @param dpmDao the dpmDao to set
   */
  public void setDpmDao(DataProcessMachineDAO dpmDao) {
    this.dpmDao = dpmDao;
  }

  /**
   * @param ffcDao the ffcDao to set
   */
  public void setFfcDao(FitsFileCutDAO ffcDao) {
    this.ffcDao = ffcDao;
  }

  /**
   * @return the ffcrDao
   */
  public FitsFileCutRefDAO getFfcrDao() {
    return ffcrDao;
  }

  /**
   * @param ffcrDao the ffcrDao to set
   */
  public void setFfcrDao(FitsFileCutRefDAO ffcrDao) {
    this.ffcrDao = ffcrDao;
  }

  /**
   * @param varLDir the varLDir to set
   */
  public void setVarLDir(String varLDir) {
    this.varLDir = varLDir;
  }
}
