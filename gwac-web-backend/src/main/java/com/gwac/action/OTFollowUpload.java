/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.dao.FollowUpFitsfileDao;
import com.gwac.dao.Ot2StreamNodeTimeDao;
import com.gwac.dao.OtLevel2Dao;
import com.gwac.dao.UploadFileRecordDao;
import com.gwac.dao.UploadFileUnstoreDao;
import com.gwac.model.OtLevel2;
import com.gwac.model.UploadFileRecord;
import com.gwac.model.UploadFileUnstore;
import com.gwac.util.CommonFunction;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.ApplicationAware;

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
public class OTFollowUpload extends ActionSupport implements ApplicationAware {

  private static final Log log = LogFactory.getLog(OTFollowUpload.class);

  private Map<String, Object> appmap;

  private String tspname;
  private String ot2name;
  private String followname;
  private File objlist;
  private File fitsname;
  private String objlistFileName;
  private String fitsnameFileName;

  private String rootPath;
  private String destPath;

  @Resource
  private OtLevel2Dao ot2Dao;
  @Resource
  private UploadFileRecordDao ufrDao;
  @Resource
  private UploadFileUnstoreDao ufuDao;
  @Resource
  private FollowUpFitsfileDao fufDao;
  @Resource
  private Ot2StreamNodeTimeDao ot2StreamNodeTimeDao;

  private String echo = "";

  @Action(value = "otFollowUpload", results = {
    @Result(location = "manage/result.jsp", name = SUCCESS),
    @Result(location = "manage/result.jsp", name = INPUT),
    @Result(location = "manage/result.jsp", name = ERROR)})
  public String upload() {

    boolean flag = true;
    String result = SUCCESS;
    echo = "";

    log.debug("tspname=" + tspname + ", ot2name=" + ot2name + ", followname=" + followname);

    //必须设置望远镜名称
    if (null == tspname || tspname.isEmpty()) {
      setEcho(echo + "Error, must set tspname.\n");
      flag = false;
    }

    //必须设置后随名称
    if (null == followname || followname.isEmpty()) {
      setEcho(echo + "Error, must set followname.\n");
      flag = false;
    }

    //必须传输数据文件
    //Error, must transform data file
    if (null == objlist) {
      setEcho(echo + "Error, must upload Object file(objlist).\n");
      flag = false;
    }

    if (null == objlistFileName || objlistFileName.trim().isEmpty()) {
      setEcho(echo + "objlistFileName is empty!\n");
      flag = false;
    }

    if (flag) {

      String dateStr = (String) appmap.get("datestr");
      if (null == dateStr) {
        dateStr = CommonFunction.getUniqueDateStr();
        appmap.put("datestr", dateStr);
        log.debug("has not dateStr:" + dateStr);
      } else {
        log.debug("has dateStr:" + dateStr);
      }

      rootPath = getText("gwacDataRootDirectory");
      destPath = rootPath;
      if (destPath.charAt(destPath.length() - 1) != '/') {
        destPath += "/" + dateStr + "/";
      } else {
        destPath += dateStr + "/";
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
    
    if(ot2name!=null&&!ot2name.isEmpty()&&!"null".equals(ot2name)){
      ot2StreamNodeTimeDao.updateLookUpResultTime(ot2name);
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

  public void receiveFollowObjectList() {

    String otFollowListPath = destPath + getText("gwacDataOtfollowlistDirectory");
    File otFollowListDir = new File(otFollowListPath);
    if (!otFollowListDir.exists()) {
      otFollowListDir.mkdir();
      log.debug("create dir " + otFollowListDir);
    }

    String finalName = objlistFileName.replaceFirst(ot2name, followname);
    UploadFileUnstore obj = new UploadFileUnstore();
    obj.setStorePath(otFollowListPath.substring(rootPath.length() + 1));
    obj.setFileName(finalName);
    obj.setFileType('9');   //otlist:1, starlist:2, origimage:3, cutimage:4, 9种监控图（共108幅）:5, varlist:6, imgstatus:7, otlistSub:8, followObjectList:9, otfollowimg:A
    obj.setUploadDate(new Date());

    UploadFileRecord obj2 = new UploadFileRecord();
    obj2.setStorePath(otFollowListPath.substring(rootPath.length() + 1));
    obj2.setFileName(finalName);
    obj2.setFileType('9');   //otlist:1, starlist:2, origimage:3, cutimage:4, 9种监控图（共108幅）:5, varlist:6, imgstatus:7, otlistSub:8, followObjectList:9, otfollowimg:A
    obj2.setUploadDate(new Date());

    try {
      if (objlist.exists()) {
        File otFollowListFile = new File(otFollowListPath + "/", finalName);
        log.debug("receive otfollowlist file " + otFollowListFile);
        obj.setUploadSuccess(Boolean.TRUE);
        obj2.setUploadSuccess(Boolean.TRUE);
        if (otFollowListFile.exists()) {
          log.warn(otFollowListFile + " already exist, delete it.");
          FileUtils.forceDelete(otFollowListFile);
          FileUtils.moveFile(objlist, otFollowListFile);
        } else {
          FileUtils.moveFile(objlist, otFollowListFile);
          ufuDao.save(obj);
        }
      } else {
        obj.setUploadSuccess(Boolean.FALSE);
        obj2.setUploadSuccess(Boolean.FALSE);
      }
      ufrDao.save(obj2);
    } catch (IOException ex) {
      log.error("receive otfollowlist " + objlistFileName + " error!", ex);
    }
  }

  public void receiveOTFollowImg() {

    if (null != getFitsname() && null != getFitsnameFileName() && !fitsnameFileName.trim().isEmpty()) {
      String fitsNamePath = destPath + getText("gwacDataOtfollowimgDirectory");
      File fitsNameDir = new File(fitsNamePath);
      if (!fitsNameDir.exists()) {
        fitsNameDir.mkdir();
        log.debug("create dir " + fitsNameDir);
      }

      String finalName = fitsnameFileName.replaceFirst(ot2name, followname);
      UploadFileRecord obj2 = new UploadFileRecord();
      obj2.setStorePath(fitsNamePath.substring(rootPath.length() + 1));
      obj2.setFileName(finalName);
      obj2.setFileType('A');   //otlist:1, starlist:2, origimage:3, cutimage:4, 9种监控图（共108幅）:5, varlist:6, imgstatus:7, otlistSub:8, followObjectList:9, otfollowimg:A
      obj2.setUploadDate(new Date());

      try {
        if (fitsname.exists()) {
          File fitsNameFile = new File(fitsNamePath + "/", finalName);
          log.debug("receive otfollowimg file " + fitsNameFile);
          if (fitsNameFile.exists()) {
            log.warn(fitsNameFile + " already exist, delete it.");
            FileUtils.forceDelete(fitsNameFile);
          }
          FileUtils.moveFile(getFitsname(), fitsNameFile);
          obj2.setUploadSuccess(true);
          fufDao.updateIsUpload(finalName);
        } else {
          obj2.setUploadSuccess(false);
        }
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
   * @return the followname
   */
  public String getFollowname() {
    return followname;
  }

  /**
   * @param followname the followname to set
   */
  public void setFollowname(String followname) {
    this.followname = followname;
  }

  @Override
  public void setApplication(Map<String, Object> map) {
    this.appmap = map;
  }
}
