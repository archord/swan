/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.action;

/**
 *
 * @author xy
 */
import com.gwac.dao.CameraDao;
import com.gwac.dao.MountDao;
import com.gwac.dao.ObservationSkyDao;
import com.gwac.dao.SkyRegionTemplateDao;
import com.gwac.model.Camera;
import com.gwac.model.Mount;
import com.gwac.model.ObservationSky;
import com.gwac.model.SkyRegionTemplate;
import com.gwac.util.CommonFunction;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;

/**
 * from MultipleCommonFileUploadAction
 *
 * @author xy
 */
public class RegTemplateAction extends ActionSupport {

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

  private static final Log log = LogFactory.getLog(RegTemplateAction.class);

  private String tmptName;
  private String groupId;
  private String unitId;
  private String camId;
  private String gridId;
  private String fieldId;
  private Float centerRa;
  private Float centerDec;
  private Float topLeftRa;
  private Float topLeftDec;
  private Float topRightRa;
  private Float topRightDec;
  private Float bottomRightRa;
  private Float bottomRightDec;
  private Float bottomLeftRa;
  private Float bottomLeftDec;
  private String genTime;
  private String storePath;
  private Integer starNum;
  private Float fwhm;

  private List<File> fileUpload = new ArrayList<>();
  private List<String> fileUploadContentType = new ArrayList<>();
  private List<String> fileUploadFileName = new ArrayList<>();

  @Resource
  private SkyRegionTemplateDao srTmptDao;
  @Resource
  private ObservationSkyDao obsSkyDao;
  @Resource
  private CameraDao cameraDao;
  @Resource
  private MountDao mountDao;

  private String echo = "";

  @Action(value = "regTemplateImg")
  public String action1() {

    echo = "";
    if (groupId == null || groupId.isEmpty() || unitId == null || unitId.isEmpty()
            || camId == null || camId.isEmpty() || tmptName == null || tmptName.isEmpty()
            || fileUpload.isEmpty() || null == fieldId || fieldId.isEmpty() || fieldId.equalsIgnoreCase("undefined")
            || null == gridId || gridId.isEmpty() || gridId.equalsIgnoreCase("undefined")) {
      echo = "all parameter cannot be empty.";
      log.warn(echo);
      log.warn("groupId:" + groupId + ", unitId:" + unitId + ", camId:" + camId + ", gridId:" + gridId
              + ", fieldId:" + fieldId + ", tmptName:" + tmptName + ", genTime:" + genTime);
    } else {

      Camera tcamera = cameraDao.getByName(camId);
      Mount tmount = mountDao.getByGroupUnitId(groupId, unitId);
      ObservationSky tsky = obsSkyDao.getByName(fieldId, gridId);

      SkyRegionTemplate obj = new SkyRegionTemplate();
      obj.setBottomLeftDec(bottomLeftDec);
      obj.setBottomLeftRa(bottomLeftRa);
      obj.setBottomRightDec(bottomRightDec);
      obj.setBottomRightRa(bottomRightRa);
      obj.setCamId(tcamera.getCameraId());
      obj.setCenterDec(centerDec);
      obj.setCenterRa(centerRa);
      obj.setMountId(tmount.getMountId());
      obj.setSkyId(tsky.getSkyId());
      obj.setFwhm(fwhm);
      if (genTime != null && !genTime.isEmpty()) {
        genTime = genTime.replace("T", " ");
        try {
          obj.setGenTime(CommonFunction.stringToDate(genTime, "yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
          log.warn("parse datatime error");
        }
      }
      obj.setStarNum(starNum);
      obj.setStorePath(storePath);
      obj.setTmptName(tmptName);
      obj.setTopLeftDec(topLeftDec);
      obj.setTopLeftRa(topLeftRa);
      obj.setTopRightDec(topRightDec);
      obj.setTopRightRa(topRightRa);

      try {
        String rootPath = getText("gwacDataRootDirectory");
        String gwacTemp = getText("gwacTempDirectory");
        if (rootPath.charAt(rootPath.length() - 1) != '/') {
          rootPath += "/";
        }
        int i = 0;
        String tfullPath = rootPath + gwacTemp;
        for (File file : fileUpload) {
          String tfilename = fileUploadFileName.get(i++).trim();
          if (tfilename.isEmpty()) {
            continue;
          }
          tfilename = tfilename.substring(0, tfilename.lastIndexOf('.')) + "_" + CommonFunction.getCurTimeString() + tfilename.substring(tfilename.lastIndexOf('.'));
          log.debug("receive file " + tfilename);
          File destFile = new File(tfullPath, tfilename);
          //如果存在，必须删除，否则FileUtils.moveFile报错FileExistsException
          try {
            if (destFile.exists()) {
              log.warn(destFile + " already exist, delete it.");
              FileUtils.forceDelete(destFile);
            }
            FileUtils.moveFile(file, destFile);

            obj.setStorePath(tfilename);
            srTmptDao.save(obj);
            echo = "regist template success!";
            log.debug(obj.toString());
          } catch (IOException ex) {
            log.error("delete or move file errror ", ex);
            echo = "regist template failure!";
          }
          break;
        }
      } catch (Exception ex) {
        log.error("delete or move file errror ", ex);
        echo = "upload failure";
      }
    }

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
   * @param tmptName the tmptName to set
   */
  public void setTmptName(String tmptName) {
    this.tmptName = tmptName;
  }

  /**
   * @param groupId the groupId to set
   */
  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  /**
   * @param unitId the unitId to set
   */
  public void setUnitId(String unitId) {
    this.unitId = unitId;
  }

  /**
   * @param camId the camId to set
   */
  public void setCamId(String camId) {
    this.camId = camId;
  }

  /**
   * @param gridId the gridId to set
   */
  public void setGridId(String gridId) {
    this.gridId = gridId;
  }

  /**
   * @param fieldId the fieldId to set
   */
  public void setFieldId(String fieldId) {
    this.fieldId = fieldId;
  }

  /**
   * @param centerRa the centerRa to set
   */
  public void setCenterRa(Float centerRa) {
    this.centerRa = centerRa;
  }

  /**
   * @param centerDec the centerDec to set
   */
  public void setCenterDec(Float centerDec) {
    this.centerDec = centerDec;
  }

  /**
   * @param topLeftRa the topLeftRa to set
   */
  public void setTopLeftRa(Float topLeftRa) {
    this.topLeftRa = topLeftRa;
  }

  /**
   * @param topLeftDec the topLeftDec to set
   */
  public void setTopLeftDec(Float topLeftDec) {
    this.topLeftDec = topLeftDec;
  }

  /**
   * @param topRightRa the topRightRa to set
   */
  public void setTopRightRa(Float topRightRa) {
    this.topRightRa = topRightRa;
  }

  /**
   * @param topRightDec the topRightDec to set
   */
  public void setTopRightDec(Float topRightDec) {
    this.topRightDec = topRightDec;
  }

  /**
   * @param bottomRightRa the bottomRightRa to set
   */
  public void setBottomRightRa(Float bottomRightRa) {
    this.bottomRightRa = bottomRightRa;
  }

  /**
   * @param bottomRightDec the bottomRightDec to set
   */
  public void setBottomRightDec(Float bottomRightDec) {
    this.bottomRightDec = bottomRightDec;
  }

  /**
   * @param bottomLeftRa the bottomLeftRa to set
   */
  public void setBottomLeftRa(Float bottomLeftRa) {
    this.bottomLeftRa = bottomLeftRa;
  }

  /**
   * @param bottomLeftDec the bottomLeftDec to set
   */
  public void setBottomLeftDec(Float bottomLeftDec) {
    this.bottomLeftDec = bottomLeftDec;
  }

  /**
   * @param genTime the genTime to set
   */
  public void setGenTime(String genTime) {
    this.genTime = genTime;
  }

  /**
   * @param storePath the storePath to set
   */
  public void setStorePath(String storePath) {
    this.storePath = storePath;
  }

  /**
   * @param starNum the starNum to set
   */
  public void setStarNum(Integer starNum) {
    this.starNum = starNum;
  }

  /**
   * @param fwhm the fwhm to set
   */
  public void setFwhm(Float fwhm) {
    this.fwhm = fwhm;
  }

  /**
   * @param srTmptDao the srTmptDao to set
   */
  public void setSrTmptDao(SkyRegionTemplateDao srTmptDao) {
    this.srTmptDao = srTmptDao;
  }
}
