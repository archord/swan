/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.dao.ManualUploadFileDao;
import com.gwac.model.ManualUploadFile;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

/**
 * from MultipleCommonFileUploadAction
 *
 * @author xy
 */
public class ManualFileUpload extends ActionSupport {

  private static final Log log = LogFactory.getLog(ManualFileUpload.class);
  private static final Set<String> typeSet = new HashSet(Arrays.asList(new String[]{"crsot1", "imqty", "subot1", "impre", "magclb", "subot2im"}));

  @Resource
  private ManualUploadFileDao mufDao;

  private String comments;
  private List<File> fileUpload = new ArrayList<>();
  private List<String> fileUploadContentType = new ArrayList<>();
  private List<String> fileUploadFileName = new ArrayList<>();
  private String echo = "";

  @Action(value = "manualFileUpload", results = {
    @Result(location = "manage/result.jsp", name = SUCCESS),
    @Result(location = "manage/result.jsp", name = INPUT),
    @Result(location = "manage/result.jsp", name = ERROR)})
  public String upload() {

    long startTime = System.nanoTime();
    long endTime = 0;

    boolean flag = true;
    String result = SUCCESS;
    echo = "";

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
    if (fileTotalSize * 1.0 / 1048576 > 2000.0) {
      echo = echo + "total file size is " + fileTotalSize * 1.0 / 1048576 + " beyond 2000MB, total file " + fileUpload.size();
      for (File file : fileUpload) {
        log.warn(fileUploadFileName.get(i++).trim() + ": " + file.length() * 1.0 / 1048576 + "MB");
      }
      flag = false;
    }
    log.debug("fileTotalSize:" + fileTotalSize * 1.0 / 1048576 + "MB");

    if (flag) {
      try {
        String rootPath = getText("gwacDataRootDirectory");
        String gwacManualUpload = getText("gwacManualUpload");
        if (rootPath.charAt(rootPath.length() - 1) != '/') {
          rootPath += "/";
        }
        String storePath = rootPath + gwacManualUpload;
        i = 0;
        for (File file : fileUpload) {
          String tfilename = fileUploadFileName.get(i++).trim();
          if (tfilename.isEmpty()) {
            continue;
          }
          log.debug("receive file " + tfilename);
          File destFile = new File(storePath, tfilename);
          //如果存在，必须删除，否则FileUtils.moveFile报错FileExistsException
          try {
            if (destFile.exists()) {
              log.warn(destFile + " already exist, delete it.");
              FileUtils.forceDelete(destFile);
            }
            FileUtils.moveFile(file, destFile);
            
            ManualUploadFile muf = new ManualUploadFile();
            muf.setName(tfilename);
            muf.setComments(comments);
            muf.setUiId(0);
            muf.setStatus(1);
            muf.setTime(new Date());
            mufDao.save(muf);
            echo = "upload success";
          } catch (IOException ex) {
            log.error("delete or move file errror ", ex);
            echo = "upload failure";
          }
        }
        endTime = System.nanoTime();
      } catch (Exception ex) {
        log.error("delete or move file errror ", ex);
        echo = "upload failure";
      }
    } else {
      result = ERROR;
    }

    double time1 = 1.0 * (endTime - startTime) / 1e9;
    log.debug("upload " + fileUpload.size() + " files, total time: " + time1 + "s, ");

    log.debug(echo);
    sendResultMsg(echo);
    return null;
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

  /**
   * @return the echo
   */
  public String getEcho() {
    return echo;
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
   * @param comments the comments to set
   */
  public void setComments(String comments) {
    this.comments = comments;
  }


}
