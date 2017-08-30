/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

/**
 * @author xy parameter： curl command example: curl
 * http://localhost/dataSyncRecv.action -F filePaths=160802/M09/cutimages -F
 * fileUpload=@M160802_C00681_1080.fit -F filePaths=160802/M09/cutimages -F
 * files=@M160802_C00681_1080.jpg
 */
public class DataSyncRecvAction extends ActionSupport {

  private static final Log log = LogFactory.getLog(DataSyncRecvAction.class);
  private List<File> files = new ArrayList<>();
  private List<String> filesContentType = new ArrayList<>();
  private List<String> filesFileName = new ArrayList<>();
  private List<String> filePaths = new ArrayList<>();
  private String echo = "";

  @Action(value = "dataSyncRecv", results = {
    @Result(location = "manage/result.jsp", name = SUCCESS),
    @Result(location = "manage/result.jsp", name = INPUT),
    @Result(location = "manage/result.jsp", name = ERROR)})
  public String upload() {

    long startTime = System.nanoTime();

    boolean flag = true;
    String result = SUCCESS;
    echo = "";

    //必须传输数据文件
    //Error, must transform data file
    if (filePaths.isEmpty() || files.isEmpty()) {
      setEcho(echo + "Error, must upload data file(files).\n");
      flag = false;
    }

    if (filePaths.size() != files.size()) {
      log.debug("filePaths: " + filePaths.size());
      for (String aa : filePaths) {
        log.debug(aa);
      }
      log.debug("files: " + files.size());
      log.debug("filesFileName: " + filesFileName.size());
      for (String aa : filesFileName) {
        log.debug(aa);
      }
//      setEcho(echo + "Error，please check upload command and retry!\n");
//      flag = false;
    }

    long fileTotalSize = 0;
    for (File file : files) {
      fileTotalSize += file.length();
    }
    if (fileTotalSize * 1.0 / 1048576 > 100.0) {
      String msg = "total file size is " + fileTotalSize * 1.0 / 1048576 + ", beyond 100MB, abort.";
      setEcho(echo + msg);

      int j = 0;
      for (File file : files) {
        log.debug(filesFileName.get(j++).trim() + ": " + file.length() * 1.0 / 1048576 + "MB");
      }
      flag = false;
    }

    if (flag) {

      String rootPath = getText("gwacDataRootDirectory");
      if (rootPath.charAt(rootPath.length() - 1) != '/') {
        rootPath += "/";
      }

      int successNum = 0;
      for (int i = 0; i < filesFileName.size(); i++) {
        String tpath = filePaths.get(i);
        String tfile = filesFileName.get(i);
        if (tpath.isEmpty() || tfile.isEmpty()) {
          continue;
        }
        String destPath = rootPath + tpath;

        try {
          File destDir = new File(destPath);
          if (!destDir.exists()) {
            destDir.mkdirs();
            log.debug("create dir " + destDir);
          }

          File destFile = new File(destPath, tfile);
          log.debug("receive file " + destFile);
          if (destFile.exists()) {
            log.warn(destFile + " already exist, delete it.");
            FileUtils.forceDelete(destFile);
          }
          FileUtils.moveFile(files.get(i), destFile);
          successNum++;
        } catch (IOException ex) {
          log.error("delete or move file " + tfile + " errror ", ex);
        }
      }

      setEcho(echo + "Success, total upload " + successNum + " files.\n");
    } else {
      result = ERROR;
    }

    log.debug(echo);

    long endTime = System.nanoTime();
    double time1 = 1.0 * (endTime - startTime) / 1e9;
    log.debug("total time: " + time1 + "s.");

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
   * @param echo the echo to set
   */
  public void setEcho(String echo) {
    this.echo = echo;
  }

  /**
   * @param files the files to set
   */
  public void setFiles(List<File> files) {
    this.files = files;
  }

  /**
   * @param filesContentType the filesContentType to set
   */
  public void setFilesContentType(List<String> filesContentType) {
    this.filesContentType = filesContentType;
  }

  /**
   * @param filesFileName the filesFileName to set
   */
  public void setFilesFileName(List<String> filesFileName) {
    this.filesFileName = filesFileName;
  }

  /**
   * @param filePaths the filePaths to set
   */
  public void setFilePaths(List<String> filePaths) {
    this.filePaths = filePaths;
  }
}
