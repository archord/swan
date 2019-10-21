/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.activemq.CrossTaskMessageCreator;
import com.gwac.dao.CrossFileDao;
import com.gwac.dao.UploadFileUnstoreDao;
import com.gwac.model.UploadFileUnstore;
import com.gwac.util.CommonFunction;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.jms.Destination;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
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
public class CrossTaskUpload extends ActionSupport implements ApplicationAware {

  private static final Log log = LogFactory.getLog(CrossTaskUpload.class);
  private static final Set<String> typeSet = new HashSet(Arrays.asList(new String[]{"crossOTList", "crossOTStamp"}));

  @Resource
  private CrossFileDao crossFileDao;
  @Resource
  private UploadFileUnstoreDao ufuDao;
  @Resource
  private JmsTemplate jmsTemplate;
  @Resource(name = "crossTaskDest")
  private Destination crossTaskDest;

  private Map<String, Object> appmap;

  private String taskName;
  private String fileType;
  private String sendTime; //yyyyMMddHHmmssSSS
  private Date sendTimeObj;
  private List<File> fileUpload = new ArrayList<>();
  private List<String> fileUploadContentType = new ArrayList<>();
  private List<String> fileUploadFileName = new ArrayList<>();
  private String echo = "";
  private String dateStr = null;

  @Action(value = "crossTaskUpload", results = {
    @Result(location = "manage/result.jsp", name = SUCCESS)
    ,
    @Result(location = "manage/result.jsp", name = INPUT)
    ,
    @Result(location = "manage/result.jsp", name = ERROR)})
  public String upload() {

    long startTime = System.nanoTime();
    long endTime = 0;

    boolean flag = true;
    String result = SUCCESS;
    echo = "";

    log.debug("sendTime=" + sendTime + ", fileType=" + fileType + ": " + fileUpload.size() + " files.");

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
	initDateStr();
	dateStr = dateStr.substring(2);
	try {
	  if (sendTime != null && !sendTime.isEmpty()) {
	    String fmtStr = "yyyyMMddHHmmss";
	    if (sendTime.length() > fmtStr.length()) {
	      sendTime = sendTime.substring(0, fmtStr.length());
	    }
	    SimpleDateFormat sdf = new SimpleDateFormat(fmtStr);
	    sendTimeObj = sdf.parse(sendTime);
	  }
	} catch (ParseException ex) {
	  sendTimeObj = null;
	  log.error("parse sendTime: " + sendTime + " error", ex);
	}
	String rootPath = getText("gwacDataRootDirectory");
	if (rootPath.charAt(rootPath.length() - 1) != '/') {
	  rootPath += "/";
	}

	if (typeSet.contains(fileType)) {
	  String destPath = rootPath + dateStr + "/" + taskName + "/";
	  File destDir = new File(destPath);
	  if (!destDir.exists()) {
	    destDir.mkdirs();
	    log.debug("create dir " + destDir);
	  }
	  char tfileType = '0';
	  String tpath = "";
	  switch (fileType) {
	    case "crossOTList":
	      tfileType = 'z';
	      tpath = destPath + getText("gwacDataCrossTaskOtListDirectory");
	      break;
	    case "crossOTStamp":
	      tfileType = 'y';
	      tpath = destPath + getText("gwacDataCrossTaskOtStampDirectory");
	      break;
	  }
	  storeFile(fileUpload, fileUploadFileName, tpath, rootPath, tfileType);
	  echo += "success upload " + fileUpload.size() + " files.";
	} else {
	  echo += "unrecognize fileType:" + fileType;
	  for (String fname : fileUploadFileName) {
	    echo += ", fname:" + fname;
	  }
	  log.warn(echo);
	}
      } catch (Exception ex) {
	log.error("delete or move file errror ", ex);
      }
    } else {
      result = ERROR;
    }

    endTime = System.nanoTime();
    double time1 = 1.0 * (endTime - startTime) / 1e9;
    log.debug("fileType=" + fileType + ": " + fileUpload.size() + " files, total time: " + time1 + "s, ");

    log.debug(echo);
    sendResultMsg(echo);
    return null;
  }

  public void storeFile(List<File> files, List<String> fnames, String path, String rootPath, char fileType) {

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
      obj.setFileType(fileType);   //otlist:1, starlist:2, origimage:3, cutimage:4, 9种监控图（共108幅）:5, varlist:6
      obj.setUploadDate(new Date());
      obj.setUploadSuccess(Boolean.TRUE);
      if (sendTimeObj != null) {
	obj.setSendTime(sendTimeObj);
      }
      ufuDao.save(obj);
      if ('z' == fileType) {
	MessageCreator tmc = new CrossTaskMessageCreator(obj, taskName, dateStr);
	jmsTemplate.send(crossTaskDest, tmc);
      }
    }
  }

  public void initDateStr() {
    dateStr = (String) appmap.get("datestr");
    if (null == dateStr) {
      dateStr = CommonFunction.getUniqueDateStr();
      dateStr = dateStr.substring(2);
      appmap.put("datestr", dateStr);
    }
  }

  public void sendResultMsg(String msg) {

    HttpServletResponse response = ServletActionContext.getResponse();
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out;
    try {
      out = response.getWriter();
      out.print(msg);
    } catch (IOException ex) {
      log.error("response error: ", ex);
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

  /**
   * @param taskName the taskName to set
   */
  public void setTaskName(String taskName) {
    this.taskName = taskName;
  }

}
