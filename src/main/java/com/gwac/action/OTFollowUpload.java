/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.dao.ConfigFileDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.UploadFileRecordDao;
import com.gwac.dao.UploadFileUnstoreDao;
import com.gwac.model.ConfigFile;
import com.gwac.model.OtLevel2;
import com.gwac.model.UploadFileRecord;
import com.gwac.model.UploadFileUnstore;
import com.gwac.service.UploadFileServiceImpl;
import com.gwac.util.CommonFunction;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

/*parameter：currentDirectory, configFile, [fileUpload], [fileUpload].*/
/* curl command example: */
/* curl http://localhost/otFollowUpload.action */
/* -F tspname=F01 */
/* -F ot2name=M151017_C00020 */
/* -F objlist=@M2_04_150827_1_060060_0160.fit.skyOT */
/* -F fitsname=@M2_04_150827_1_060060_0160.fit */
/**
 * @author xy
 */
//@InterceptorRef("jsonValidationWorkflowStack")
//加了这句化，文件传不上来
//@ParentPackage(value="struts-default")
//@Controller()
//@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class OTFollowUpload extends ActionSupport {

  private static final Log log = LogFactory.getLog(OTFollowUpload.class);

  private String tspname;
  private String ot2name;
  private File objlist;
  private File fitsname;
  private String objlistFileName;
  private String fitsnameFileName;

  private String rootPath;
  private String destPath;

  private OtLevel2Dao ot2Dao;
  private UploadFileRecordDao ufrDao;
  private UploadFileUnstoreDao ufuDao;

  private String echo = "";

  @Action(value = "otFollowUpload", results = {
    @Result(location = "manage/result.jsp", name = SUCCESS),
    @Result(location = "manage/result.jsp", name = INPUT),
    @Result(location = "manage/result.jsp", name = ERROR)})
  public String upload() {

    boolean flag = true;
    String result = SUCCESS;
    echo = "";

    //必须设置望远镜名称
    if (null == getTspname()) {
      setEcho(echo + "Error, must set tspname.\n");
      flag = false;
    }

    //必须设置后随OT的名称
    if (null == getOt2name()) {
      setEcho(echo + "Error, must set ot2name.\n");
      flag = false;
    }

    //必须传输数据文件
    //Error, must transform data file
    if (null == getObjlist()) {
      setEcho(echo + "Error, must upload Object file(objlist).\n");
      flag = false;
    }

    if (null == getObjlistFileName() || getObjlistFileName().trim().isEmpty()) {
      setEcho(echo + "objlistFileName is empty!\n");
      flag = false;
    }

    if (flag) {
      OtLevel2 ot2 = ot2Dao.getOtLevel2ByName(getOt2name(), false);
      String idStr = ot2.getIdentify();

      rootPath = getText("gwac.data.root.directory");
      destPath = rootPath;
      if (destPath.charAt(destPath.length() - 1) != '/') {
        destPath += "/" + CommonFunction.getStorePath(idStr) + "/";
      } else {
        destPath += CommonFunction.getStorePath(idStr) + "/";
      }

      File destDir = new File(destPath);
      if (!destDir.exists()) {
        destDir.mkdirs();
        log.debug("create dir " + destDir);
      }

      receiveFollowObjectList();
      receiveOTFollowImg();

      setEcho(echo + "Success!\n");
    } else {
      result = ERROR;
    }

    log.debug(echo);
    /* 如果使用struts2的标签，返回结果会有两个空行，这个显示在命令行不好看。
     * 用jsp的out，则不会有两个空行。
     * 在这里将结果信息存储在session中，在jsp页面获得返回信息。
     */
    ActionContext ctx = ActionContext.getContext();
    ctx.getSession().put("echo", echo);

    return result;
  }

  public void receiveFollowObjectList() {

    String otFollowListPath = destPath + getText("gwac.data.otfollowlist.directory") + "/";
    File otFollowListDir = new File(otFollowListPath);
    if (!otFollowListDir.exists()) {
      otFollowListDir.mkdir();
      log.debug("create dir " + otFollowListDir);
    }

    try {
      File otFollowListFile = new File(otFollowListPath, getObjlistFileName());
      log.debug("receive otfollowlist file " + otFollowListFile);
      if (otFollowListFile.exists()) {
        log.warn(otFollowListFile + " already exist, delete it.");
        return;
//        FileUtils.forceDelete(otFollowListFile);
      }
      FileUtils.moveFile(getObjlist(), otFollowListFile);
    } catch (IOException ex) {
      log.error("receive otfollowlist " + getObjlistFileName() + " error!", ex);
    }

    UploadFileUnstore obj = new UploadFileUnstore();
    obj.setStorePath(otFollowListPath.substring(rootPath.length() + 1));
    obj.setFileName(getObjlistFileName());
    obj.setFileType('9');   //otlist:1, starlist:2, origimage:3, cutimage:4, 9种监控图（共108幅）:5, varlist:6, imgstatus:7, otlistSub:8, followObjectList:9, otfollowimg:A
    obj.setUploadDate(new Date());
    ufuDao.save(obj);

    UploadFileRecord obj2 = new UploadFileRecord();
    obj2.setStorePath(otFollowListPath.substring(rootPath.length() + 1));
    obj2.setFileName(getObjlistFileName());
    obj2.setFileType('9');   //otlist:1, starlist:2, origimage:3, cutimage:4, 9种监控图（共108幅）:5, varlist:6, imgstatus:7, otlistSub:8, followObjectList:9, otfollowimg:A
    obj2.setUploadDate(new Date());
    ufrDao.save(obj2);
  }

  public void receiveOTFollowImg() {

    if (null != getFitsname() && null != getFitsnameFileName() && !fitsnameFileName.trim().isEmpty()) {
      String fitsNamePath = destPath + getText("gwac.data.otfollowimg.directory") + "/";
      File fitsNameDir = new File(fitsNamePath);
      if (!fitsNameDir.exists()) {
        fitsNameDir.mkdir();
        log.debug("create dir " + fitsNameDir);
      }

      try {
        File fitsNameFile = new File(fitsNamePath, fitsnameFileName);
        log.debug("receive otfollowimg file " + fitsNameFile);
        if (fitsNameFile.exists()) {
          log.warn(fitsNameFile + " already exist, delete it.");
          return;
//          FileUtils.forceDelete(fitsNameFile);
        }
        FileUtils.moveFile(getFitsname(), fitsNameFile);

        UploadFileRecord obj2 = new UploadFileRecord();
        obj2.setStorePath(fitsNamePath.substring(rootPath.length() + 1));
        obj2.setFileName(fitsnameFileName);
        obj2.setFileType('A');   //otlist:1, starlist:2, origimage:3, cutimage:4, 9种监控图（共108幅）:5, varlist:6, imgstatus:7, otlistSub:8, followObjectList:9, otfollowimg:A
        obj2.setUploadDate(new Date());
        ufrDao.save(obj2);
      } catch (IOException ex) {
        log.error("receive otfollowimg " + fitsnameFileName + " error!", ex);
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
   * @param echo the echo to set
   */
  public void setEcho(String echo) {
    this.echo = echo;
  }

  /**
   * @param ot2Dao the ot2Dao to set
   */
  public void setOt2Dao(OtLevel2Dao ot2Dao) {
    this.ot2Dao = ot2Dao;
  }

  /**
   * @return the tspname
   */
  public String getTspname() {
    return tspname;
  }

  /**
   * @param tspname the tspname to set
   */
  public void setTspname(String tspname) {
    this.tspname = tspname;
  }

  /**
   * @return the ot2name
   */
  public String getOt2name() {
    return ot2name;
  }

  /**
   * @param ot2name the ot2name to set
   */
  public void setOt2name(String ot2name) {
    this.ot2name = ot2name;
  }

  /**
   * @return the objlist
   */
  public File getObjlist() {
    return objlist;
  }

  /**
   * @param objlist the objlist to set
   */
  public void setObjlist(File objlist) {
    this.objlist = objlist;
  }

  /**
   * @return the fitsname
   */
  public File getFitsname() {
    return fitsname;
  }

  /**
   * @param fitsname the fitsname to set
   */
  public void setFitsname(File fitsname) {
    this.fitsname = fitsname;
  }

  /**
   * @return the objlistFileName
   */
  public String getObjlistFileName() {
    return objlistFileName;
  }

  /**
   * @param objlistFileName the objlistFileName to set
   */
  public void setObjlistFileName(String objlistFileName) {
    this.objlistFileName = objlistFileName;
  }

  /**
   * @return the fitsnameFileName
   */
  public String getFitsnameFileName() {
    return fitsnameFileName;
  }

  /**
   * @param fitsnameFileName the fitsnameFileName to set
   */
  public void setFitsnameFileName(String fitsnameFileName) {
    this.fitsnameFileName = fitsnameFileName;
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
}
