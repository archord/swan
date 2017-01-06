/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.activemq.OTListMessageCreator;
import com.gwac.dao.UploadFileUnstoreDao;
import com.gwac.model.UploadFileRecord;
import com.gwac.model.UploadFileUnstore;
import com.gwac.util.CommonFunction;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Destination;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.ApplicationAware;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * from MultipleCommonFileUploadAction
 *
 * @author xy
 */
public class CommonFileUpload extends ActionSupport implements ApplicationAware {

  private static final Log log = LogFactory.getLog(CommonFileUpload.class);
  
  private UploadFileUnstoreDao ufuDao;
  private JmsTemplate jmsTemplate;
  private Destination otlistDest;

  private Map<String, Object> appmap;

  private String fileType;
  private String sendTime; //yyyyMMddHHmmssSSS
  private Date sendTimeObj;
  private List<File> fileUpload = new ArrayList<File>();
  private List<String> fileUploadContentType = new ArrayList<String>();
  private List<String> fileUploadFileName = new ArrayList<String>();
  private String echo = "";

  @Action(value = "commonFileUpload", results = {
    @Result(location = "manage/result.jsp", name = SUCCESS),
    @Result(location = "manage/result.jsp", name = INPUT),
    @Result(location = "manage/result.jsp", name = ERROR)})
  public String upload() {

    long startTime = System.nanoTime();
    long endTime = 0;

    boolean flag = true;
    String result = SUCCESS;
    echo = "";

    log.debug("fileType=" + fileType + ": " + fileUpload.size() + " files.");

    //必须设置传输机器名称
    if (null == fileType || fileType.isEmpty()) {
      echo = echo + "Error, must set file type(fileType).\n";
      flag = false;
    }

    //必须传输数据文件
    //Error, must transform data file
    if (fileUpload.isEmpty()) {
      echo = echo + "Error, must upload data file(fileUpload).\n";
      flag = false;
    }

    if (fileUpload.size() != fileUploadFileName.size()) {
      echo = echo + "Error，please check upload command and retry!\n";
      flag = false;
    }

    //计算数据文件大小
    long fileTotalSize = 0;
    for (File file : fileUpload) {
      fileTotalSize += file.length();
    }
    int i = 0;
    if (fileTotalSize * 1.0 / 1048576 > 100.0) {
      echo = echo + "total file size is " + fileTotalSize * 1.0 / 1048576 + " beyond 100MB, total file " + fileUpload.size();
      for (File file : fileUpload) {
        log.warn(fileUploadFileName.get(i++).trim() + ": " + file.length() * 1.0 / 1048576 + "MB");
      }
      flag = false;
    }
    log.debug("fileTotalSize:" + fileTotalSize * 1.0 / 1048576 + "MB");

    if (flag) {
      try {
        if (sendTime != null && !sendTime.isEmpty()) {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
          sendTimeObj = sdf.parse(sendTime);
        }
      } catch (ParseException ex) {
        sendTimeObj = null;
        log.error("parse sendTime error", ex);
      }
      try {
        String dateStr = (String) appmap.get("datestr");
        if (null == dateStr) {
          dateStr = CommonFunction.getUniqueDateStr();
          appmap.put("datestr", dateStr);
          log.debug("has not dateStr:" + dateStr);
        } else {
          log.debug("has dateStr:" + dateStr);
        }
        //G002_Mon_objt_161219T11523152
        String tfName = fileUploadFileName.get(0);
        String dpmName = tfName.substring(0, tfName.indexOf("_"));

        String rootPath = getText("gwac.data.root.directory");
        if (rootPath.charAt(rootPath.length() - 1) != '/') {
          rootPath += "/";
        }
        String destPath = rootPath + dateStr + "/" + dpmName + "/";

        File destDir = new File(destPath);
        if (!destDir.exists()) {
          destDir.mkdirs();
          log.debug("create dir " + destDir);
        }

        if ("crsot1".equals(fileType)) {
          String tpath = destPath + getText("gwac.data.otlist.directory") + "/";
          storeOt1List(fileUpload, fileUploadFileName, tpath, rootPath);
        } else if ("ot2im".equals(fileType) || "ot2imr".equals(fileType)) {
          String tpath = destPath + getText("gwac.data.cutimages.directory") + "/";
          storeOT2Image(fileUpload, fileUploadFileName, tpath, rootPath);
        } else if ("imqty".equals(fileType)) {
          String tpath = destPath + getText("gwac.data.imgstatus.directory") + "/";
          storeFile(fileUpload, fileUploadFileName, tpath, rootPath);
        } else if ("impre".equals(fileType)) {
          String thead = getText("gwac.data.thumbnail.directory");
          String tpath = rootPath + thead + "/" + dateStr + "/" + dpmName + "/";
          storeFile(fileUpload, fileUploadFileName, tpath, rootPath);
        } else if ("magclb".equals(fileType)) {
          String tpath = destPath + getText("gwac.data.magcalibration.directory") + "/";
          storeFile(fileUpload, fileUploadFileName, tpath, rootPath);
        } else {
          log.warn("unrecognize fileType:" + fileType);
        }
        echo += "success upload " + fileUpload.size() + " files.";
        endTime = System.nanoTime();
      } catch (Exception ex) {
        log.error("delete or move file errror ", ex);
      }
    } else {
      result = ERROR;
    }

    log.debug(echo);
    ActionContext ctx = ActionContext.getContext();
    ctx.getSession().put("echo", echo);

    double time1 = 1.0 * (endTime - startTime) / 1e9;
    log.debug("fileType=" + fileType + ": " + fileUpload.size() + " files, total time: " + time1 + "s, ");

    return result;
  }

  public void storeOT2Image(List<File> files, List<String> fnames, String path, String rootPath) {

    int i = 0;
    for (File file : files) {
      String tfilename = fnames.get(i++).trim();
      if (tfilename.isEmpty()) {
        continue;
      }
      log.debug("receive file " + tfilename + " to " + path);
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

  public void storeOt1List(List<File> files, List<String> fnames, String path, String rootPath) {

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
      obj.setStorePath(path.substring(rootPath.length()));
      obj.setFileName(tfilename);
      obj.setFileType('1');   //otlist:1, starlist:2, origimage:3, cutimage:4, 9种监控图（共108幅）:5, varlist:6
      obj.setUploadDate(new Date());
      if (sendTimeObj != null) {
        obj.setSendTime(sendTimeObj);
      }
      ufuDao.save(obj);
      MessageCreator tmc = new OTListMessageCreator(obj);
      jmsTemplate.send(otlistDest, tmc);
    }
  }

  public void storeFile(List<File> files, List<String> fnames, String path, String rootPath) {

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

  public String display() {
    return NONE;
  }

  /**
   * @return the echo
   */
  public String getEcho() {
    return echo;
  }

  /**
   * @param fileType the fileType to set
   */
  public void setFileType(String fileType) {
    this.fileType = fileType;
  }

  @Override
  public void setApplication(Map<String, Object> map) {
    this.appmap = map;
  }

  /**
   * @param fileUpload the fileUpload to set
   */
  public void setFileUpload(List<File> fileUpload) {
    this.fileUpload = fileUpload;
  }

  /**
   * @param fileUploadContentType the fileUploadContentType to set
   */
  public void setFileUploadContentType(List<String> fileUploadContentType) {
    this.fileUploadContentType = fileUploadContentType;
  }

  /**
   * @param fileUploadFileName the fileUploadFileName to set
   */
  public void setFileUploadFileName(List<String> fileUploadFileName) {
    this.fileUploadFileName = fileUploadFileName;
  }

  /**
   * @param sendTime the sendTime to set
   */
  public void setSendTime(String sendTime) {
    this.sendTime = sendTime;
  }

}
