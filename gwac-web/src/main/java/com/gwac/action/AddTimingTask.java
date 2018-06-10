/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.dao.DataProcessMachineDAO;
import com.gwac.dao.TimingTaskDao;
import com.gwac.model.DataProcessMachine;
import com.gwac.model.TimingTask;
import com.gwac.util.CommonFunction;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class AddTimingTask extends ActionSupport {

  /**
   * @param ttId the ttId to set
   */
  public void setTtId(long ttId) {
    this.ttId = ttId;
  }

  /**
   * @param actionType the actionType to set
   */
  public void setActionType(String actionType) {
    this.actionType = actionType;
  }

  private static final Log log = LogFactory.getLog(AddTimingTask.class);

  @Resource
  private TimingTaskDao dao;

  private long ttId;
  private String actionType;
  private String ttName;
  private String ttCommand;
  private String dpmName;
  private String planStartTime;
  private String planEndTime;
  private String comments;
  private char type;
  private String executePath;
  private String planStartDate;
  private String planEndDate;
  private List<File> ttFileName = new ArrayList<>();
  private List<String> ttFileNameContentType = new ArrayList<>();
  private List<String> ttFileNameFileName = new ArrayList<>();
  private String echo = "";

  @Action(value = "addTimingTask", results = {
    @Result(location = "manage/result.jsp", name = SUCCESS)
    ,
    @Result(location = "manage/result.jsp", name = INPUT)
    ,
    @Result(location = "manage/result.jsp", name = ERROR)})
  public String upload() {

    long startTime = System.nanoTime();
    long endTime = 0;

    boolean flag = true;
    echo = "";

    if (actionType.equals("deleteTTForm")) {
      if (ttId > -1) {
        dao.deleteById(ttId);
      }
      echo = "delete success!";
    } else {

      //必须传输数据文件
      //Error, must transform data file
      if (ttName.isEmpty() || ttCommand.isEmpty()) {
        echo = echo + "Error, must set ttName and ttCommand.\n";
        flag = false;
      } else {

        TimingTask tt = new TimingTask();
        tt.setTtName(ttName);
        tt.setTtCommand(ttCommand);
        tt.setStatus('1');
        tt.setComments(comments);
        tt.setType(type);
        tt.setExecutePath(executePath);
        tt.setDpmName(dpmName);
        if (!planStartTime.isEmpty()) {
          Date tdate = CommonFunction.stringToDate(planStartTime, "HH:mm:ss");
          tt.setPlanStartTime(tdate);
        }
        if (!planEndTime.isEmpty()) {
          Date tdate = CommonFunction.stringToDate(planEndTime, "HH:mm:ss");
          tt.setPlanEndTime(tdate);
        }
        if (!planStartDate.isEmpty()) {
          Date tdate = CommonFunction.stringToDate(planStartDate, "yyyy-MM-dd");
          tt.setPlanStartDate(tdate);
        }
        if (!planEndDate.isEmpty()) {
          Date tdate = CommonFunction.stringToDate(planEndDate, "yyyy-MM-dd");
          tt.setPlanEndDate(tdate);
        }
        if (ttFileName.size() > 0) {
          tt.setTtFileName(ttFileNameFileName.get(0));
        }
        //log.debug(tt.toString());
        if (actionType.equals("updateTTForm")) {
          if (ttId > -1) {
            if (ttFileName.isEmpty()) {
              TimingTask tt2 = dao.getById(ttId);
              tt2.setComments(tt.getComments());
              tt2.setDpmName(tt.getDpmName());
              tt2.setExecutePath(tt.getExecutePath());
              tt2.setPlanEndDate(tt.getPlanEndDate());
              tt2.setPlanEndTime(tt.getPlanEndTime());
              tt2.setPlanStartDate(tt.getPlanStartTime());
              tt2.setPlanStartTime(tt.getPlanStartTime());
              tt2.setRealEndTime(tt.getRealEndTime());
              tt2.setTtCommand(tt.getTtCommand());
              tt2.setTtName(tt.getTtName());
              tt2.setType(tt.getType());
              dao.update(tt2);
            } else {
              tt.setTtId(ttId);
              dao.update(tt);
            }
          } else {
            echo = "update failure, ttId=" + ttId;
          }
        } else {
          dao.save(tt);
        }

        //计算数据文件大小
        long fileTotalSize = 0;
        for (File file : ttFileName) {
          fileTotalSize += file.length();
        }
        int i = 0;
        if (fileTotalSize * 1.0 / 1048576 > 10.0) {
          echo = echo + "total file size is " + fileTotalSize * 1.0 / 1048576 + " beyond 10MB, total file " + ttFileName.size();
          for (File file : ttFileName) {
            log.warn(ttFileNameFileName.get(i++).trim() + ": " + file.length() * 1.0 / 1048576 + "MB");
          }
          flag = false;
        }
        log.debug("fileTotalSize:" + fileTotalSize * 1.0 / 1048576 + "MB");

        if (flag) {
          try {
            String rootPath = getText("gwacDataRootDirectory");
            String scriptDir = getText("gwacBatchScript");
            if (rootPath.charAt(rootPath.length() - 1) != '/') {
              rootPath += "/";
            }
            String storePath = rootPath + scriptDir;
            i = 0;
            for (File file : ttFileName) {
              String tfilename = ttFileNameFileName.get(i++).trim();
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

                echo = "add task success";
              } catch (IOException ex) {
                log.error("delete or move file errror ", ex);
                echo = "add task failure";
              }
            }
            endTime = System.nanoTime();
          } catch (Exception ex) {
            log.error("delete or move file errror ", ex);
            echo = "add task failure";
          }
        }
      }

      double time1 = 1.0 * (endTime - startTime) / 1e9;
      log.debug("upload " + ttFileName.size() + " files, total time: " + time1 + "s, ");
    }
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
   * @param ttName the ttName to set
   */
  public void setTtName(String ttName) {
    this.ttName = ttName;
  }

  /**
   * @param ttCommand the ttCommand to set
   */
  public void setTtCommand(String ttCommand) {
    this.ttCommand = ttCommand;
  }

  /**
   * @param dpmName the dpmName to set
   */
  public void setDpmName(String dpmName) {
    this.dpmName = dpmName;
  }

  /**
   * @param planStartTime the planStartTime to set
   */
  public void setPlanStartTime(String planStartTime) {
    this.planStartTime = planStartTime;
  }

  /**
   * @param planEndTime the planEndTime to set
   */
  public void setPlanEndTime(String planEndTime) {
    this.planEndTime = planEndTime;
  }

  /**
   * @param comments the comments to set
   */
  public void setComments(String comments) {
    this.comments = comments;
  }

  /**
   * @param type the type to set
   */
  public void setType(char type) {
    this.type = type;
  }

  /**
   * @param executePath the executePath to set
   */
  public void setExecutePath(String executePath) {
    this.executePath = executePath;
  }

  /**
   * @param planStartDate the planStartDate to set
   */
  public void setPlanStartDate(String planStartDate) {
    this.planStartDate = planStartDate;
  }

  /**
   * @param planEndDate the planEndDate to set
   */
  public void setPlanEndDate(String planEndDate) {
    this.planEndDate = planEndDate;
  }

  /**
   * @param ttFileName the ttFileName to set
   */
  public void setTtFileName(List<File> ttFileName) {
    this.ttFileName = ttFileName;
  }

  /**
   * @param ttFileNameContentType the ttFileNameContentType to set
   */
  public void setTtFileNameContentType(List<String> ttFileNameContentType) {
    this.ttFileNameContentType = ttFileNameContentType;
  }

  /**
   * @param ttFileNameFileName the ttFileNameFileName to set
   */
  public void setTtFileNameFileName(List<String> ttFileNameFileName) {
    this.ttFileNameFileName = ttFileNameFileName;
  }

}
