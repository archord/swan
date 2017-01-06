/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

import com.gwac.activemq.OTListMessageCreator;
import com.gwac.dao.DataProcessMachineDAO;
import com.gwac.dao.FitsFileCutDAO;
import com.gwac.dao.FitsFileCutRefDAO;
import com.gwac.dao.ObservationSkyDao;
import com.gwac.dao.UploadFileRecordDao;
import com.gwac.dao.UploadFileUnstoreDao;
import com.gwac.model.DataProcessMachine;
import com.gwac.model.FitsFileCut;
import com.gwac.model.FitsFileCutRef;
import com.gwac.model.ObservationSky;
import com.gwac.model.UploadFileRecord;
import com.gwac.model.UploadFileUnstore;
import com.gwac.util.CommonFunction;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;
import javax.jms.Destination;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 *
 * @author xy
 */
public class CommonFileUploadServiceImpl implements UploadFileService {

  private static final Log log = LogFactory.getLog(CommonFileUploadServiceImpl.class);
  //单次传输配置文件信息
  private Date sendTime = null;
  private String storePath;
  private String configPath;
  private String configFile;
  private String[] otList;
  private String[] starList;
  private String[] origImage;
  private String[] cutImages;
  private String[] varList;
  private String[] imgStatus;
  private String[] otListSub;
  private String[] cutImagesSub;

  //系统配置文件信息
  private String rootDir;
  private String otLDir;
  private String starLDir;
  private String orgIDir;
  private String cutIDir;
  private String cfgDir;
  private String varLDir;
  private String imgSDir;
  private String otListSubDir;
  private String cutImagesSubDir;
  private String thumbnailDir;
  private String magClbDir;

  private UploadFileRecordDao ufrDao;
  private UploadFileUnstoreDao ufuDao;
  private DataProcessMachineDAO dpmDao;
  private FitsFileCutDAO ffcDao;
  private FitsFileCutRefDAO ffcrDao;
  private ObservationSkyDao skyDao;

  private JmsTemplate jmsTemplate;
  private Destination otlistDest;

  private Boolean isBeiJingServer;
  private Boolean isTestServer;

  public void saveFile(String dateStr, String fileType, List<File> fileUpload, List<String> names, String sendTime) {

    Date sendTimeObj = null;
    try {
      if (sendTime != null && !sendTime.isEmpty()) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        sendTimeObj = sdf.parse(sendTime);
      }
    } catch (ParseException ex) {
      log.error("parse sendTime error", ex);
    }

    String rootPath = rootDir;
    if (rootPath.charAt(rootPath.length() - 1) != '/') {
      rootPath += "/";
    }
    //G002_Mon_objt_161219T11523152
    String tfName = names.get(0);
    String dpmName = tfName.substring(0, tfName.indexOf("_"));
    String subPath = dateStr + "/" + dpmName + "/";
    String destPath = rootPath + subPath;

    File destDir = new File(destPath);
    if (!destDir.exists()) {
      destDir.mkdirs();
      log.debug("create dir " + destDir);
    }

    try {
      if ("crsot1".equals(fileType)) {
        String tpath = destPath + otLDir + "/";
        storeOt1List(fileUpload, names, tpath, sendTimeObj);

      } else if ("ot2im".equals(fileType) || "ot2imr".equals(fileType)) {
        storeOT2CutImage(fileUpload, names);

      } else if ("imqty".equals(fileType)) {
        String tpath = destPath + imgSDir + "/";
        storeImgQualityFile(fileUpload, names, tpath);

      } else if ("impre".equals(fileType)) {
        String tpath = rootPath + thumbnailDir + "/" + dateStr + "/" + dpmName + "/";
        storeFile(fileUpload, names, tpath);

      } else if ("magclb".equals(fileType)) {
        String tpath = destPath + magClbDir + "/";
        storeFile(fileUpload, names, tpath);

      } else {
        log.warn("unrecognize fileType:" + fileType);
      }
    } catch (Exception ex) {
      log.error("delete or move file errror ", ex);
    }
  }

  public void storeOT2CutImage(List<File> files, List<String> fnames) {

    int i = 0;
    for (File file : files) {
      String tfilename = fnames.get(i++).trim();
      if (tfilename.isEmpty()) {
        continue;
      }
      String path = "";
      if (tfilename.endsWith("jpg") && tfilename.contains("ref")) {
        List<FitsFileCutRef> ffcs = ffcrDao.getByName(tfilename);
        if (!ffcs.isEmpty()) {
          path = ffcs.get(0).getStorePath();
        }
        try {
          String dateStr = tfilename.substring(tfilename.indexOf("ref_") + 4, tfilename.indexOf(".jpg"));
          if (dateStr != null && dateStr.length() == 15) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
            Date genDate = sdf.parse(dateStr.replace('T', ' '));

            FitsFileCutRef ffcr = new FitsFileCutRef();
            ffcr.setFileName(tfilename.substring(0, tfilename.indexOf(".jpg")));
            ffcr.setGenerateTime(genDate);
            ffcr.setSuccessCut(Boolean.TRUE);
            ffcrDao.updateByName(ffcr);
          } else {
            log.error("ot2 ref cut file date error, date=" + dateStr);
          }
        } catch (ParseException ex) {
          log.error("parse ref cut image date error.");
        }
      } else {
        List<FitsFileCut> ffcs = ffcDao.getByName(tfilename);
        if (!ffcs.isEmpty()) {
          path = ffcs.get(0).getStorePath();
        }
      }
      if (!path.isEmpty()) {
        log.debug("receive file " + tfilename);
        File destFile = new File(path, tfilename);
        //如果存在，必须删除，否则FileUtils.moveFile报错FileExistsException
        try {
          if (destFile.exists()) {
            log.warn(destFile + " already exist, delete it.");
            FileUtils.forceDelete(destFile);
          }
          FileUtils.moveFile(file, destFile);
        } catch (IOException ex) {
          log.error("delete or move file errror ", ex);
        }
      }
    }
  }

  public void storeImgQualityFile(List<File> files, List<String> fnames, String path) {

    File tDir = new File(path);
    if (!tDir.exists()) {
      tDir.mkdirs();
    }

    int i = 0;
    for (File file : files) {
      String tfilename = fnames.get(i++).trim();
      if (tfilename.isEmpty()) {
        continue;
      }
      log.debug("receive file " + tfilename);
      File destFile = new File(path, tfilename);
      //如果存在，必须删除，否则FileUtils.moveFile报错FileExistsException
      try {
        if (destFile.exists()) {
          log.warn(destFile + " already exist, delete it.");
          FileUtils.forceDelete(destFile);
        }
        FileUtils.moveFile(file, destFile);
      } catch (IOException ex) {
        log.error("delete or move file errror ", ex);
      }
      UploadFileUnstore obj = new UploadFileUnstore();
      obj.setStorePath(path.substring(rootDir.length() + 1));
      obj.setFileName(tfilename);
      obj.setFileType('7');   //otlist:1, starlist:2, origimage:3, cutimage:4, 9种监控图（共108幅）:5, varlist:6, imgstatus:7
      obj.setUploadDate(new Date());
      obj.setUploadSuccess(Boolean.TRUE);
      ufuDao.save(obj);
    }
  }

  public void storeOt1List(List<File> files, List<String> names, String path, Date sendTime) {

    File tDir = new File(path);
    if (!tDir.exists()) {
      tDir.mkdirs();
    }

    int i = 0;
    for (File file : files) {
      String tfilename = names.get(i++).trim();
      if (tfilename.isEmpty()) {
        continue;
      }
      log.debug("receive file " + tfilename);
      File destFile = new File(path, tfilename);
      try {
        if (destFile.exists()) {
          log.warn(destFile + " already exist, delete it.");
          FileUtils.forceDelete(destFile);
        }
        //如果存在，必须删除，否则FileUtils.moveFile报错FileExistsException
        FileUtils.moveFile(file, destFile);
      } catch (IOException ex) {
        log.error("delete or move file errror ", ex);
      }

      UploadFileUnstore obj = new UploadFileUnstore();
      obj.setStorePath(path.substring(rootDir.length() + 1));
      obj.setFileName(tfilename);
      obj.setFileType('1');   //otlist:1, starlist:2, origimage:3, cutimage:4, 9种监控图（共108幅）:5, varlist:6
      obj.setUploadDate(new Date());
      obj.setSendTime(sendTime);
      obj.setUploadSuccess(Boolean.TRUE);
      ufuDao.save(obj);

      MessageCreator tmc = new OTListMessageCreator(obj);
      jmsTemplate.send(otlistDest, tmc);
    }
  }

  public void storeFile(List<File> files, List<String> fnames, String path) {

    File tDir = new File(path);
    if (!tDir.exists()) {
      tDir.mkdirs();
    }

    int i = 0;
    for (File file : files) {
      String tfilename = fnames.get(i++).trim();
      if (tfilename.isEmpty()) {
        continue;
      }
      log.debug("receive file " + tfilename);
      File destFile = new File(path, tfilename);
      //如果存在，必须删除，否则FileUtils.moveFile报错FileExistsException
      try {
        if (destFile.exists()) {
          log.warn(destFile + " already exist, delete it.");
          FileUtils.forceDelete(destFile);
        }
        FileUtils.moveFile(file, destFile);
      } catch (IOException ex) {
        log.error("delete or move file errror ", ex);
      }
    }
  }
}
