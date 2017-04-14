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
import com.gwac.dao.FileNumberDao;
import com.gwac.dao.FitsFile2DAO;
import com.gwac.dao.ObjectIdentityDao;
import com.gwac.dao.ObjectTypeDao;
import com.gwac.dao.ObservationSkyDao;
import com.gwac.model.FileNumber;
import com.gwac.model.FitsFile2;
import com.gwac.model.ObjectIdentity;
import com.gwac.model.ObjectType;
import com.gwac.model.ObservationSky;
import com.gwac.util.CommonFunction;
import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Date;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.ApplicationAware;

/**
 * from MultipleCommonFileUploadAction
 *
 * @author xy
 */
public class RegOrigImage extends ActionSupport implements ApplicationAware {

  private static final Log log = LogFactory.getLog(RegOrigImage.class);

  private String groupId;
  private String unitId;
  private String camId;
  private String gridId;
  private String fieldId;
  private String imgName;
  private String imgPath;
  private String genTime; //yyyyMMddHHmmssSSS

  private ObservationSkyDao obsSkyDao; //fieldId
  private DataProcessMachineDAO dpmDao;
  private ObjectIdentityDao objIdtyDao;
  private ObjectTypeDao objTypeDao;
  private FitsFile2DAO ff2Dao;
  private FileNumberDao fnumDao;

  private Map<String, Object> appMap = null;
  private String dateStr = null;
  private ObjectType groupType = null;
  private ObjectType unitType = null;
  private ObjectType ffovCameraType = null;
  private ObjectType jfovCameraType = null;
  private ObjectType computerType = null;
  private ObjectType gridType = null;

  private String echo = "";

  @Action(value = "regOrigImg", results = {
    @Result(location = "manage/result.jsp", name = SUCCESS),
    @Result(location = "manage/result.jsp", name = INPUT),
    @Result(location = "manage/result.jsp", name = ERROR)})
  public String upload() {

    String result = SUCCESS;
    echo = "";
    log.debug("groupId:" + groupId);
    log.debug("unitId:" + unitId);
    log.debug("camId:" + camId);
    log.debug("gridId:" + gridId);
    log.debug("fieldId:" + fieldId);
    log.debug("imgName:" + imgName);
    log.debug("imgPath:" + imgPath);
    log.debug("genTime:" + genTime);

    // || genTime.length() != "yyyy-MM-ddTHH:mm:ss.SSSSSS".length()
    if (groupId == null || groupId.isEmpty() || unitId == null || unitId.isEmpty()
            || camId == null || camId.isEmpty() || gridId == null || gridId.isEmpty()
            || fieldId == null || fieldId.isEmpty() || imgName == null || imgName.isEmpty()
            || imgPath == null || imgPath.isEmpty() || genTime == null || genTime.isEmpty()) {
//      echo = "all parameter cannot be empty, and genTime must formated as 'yyyy-MM-ddTHH:mm:ss.SSSSSS'.";
      echo = "all parameter cannot be empty.";
      log.warn(echo);
    } else {

      boolean isExist = ff2Dao.exist(imgName);
      if (isExist) {
        echo = imgName + " already exist.";
      } else {
        initObjType();
//        camId = imgName.substring(0, 1) + camId;
        dpmDao.getDpmByName(camId);

//        int gridId = 1;
        ObjectIdentity group = objIdtyDao.getByName(groupType, groupId);
        ObjectIdentity unit = objIdtyDao.getByName(unitType, unitId);
        ObjectIdentity camera;
        if (Integer.parseInt(camId) % 5 == 0) {
          camera = objIdtyDao.getByName(ffovCameraType, camId);
        } else {
          camera = objIdtyDao.getByName(jfovCameraType, camId);
        }
        ObjectIdentity grid = objIdtyDao.getByName(gridType, gridId);
        ObservationSky obsSky = obsSkyDao.getByName(fieldId, grid.getObjId());

        String tDateStr = genTime;
        String tDateFormate = "yyyy-MM-dd HH:mm:ss.SSS";
        if (genTime.length() > tDateFormate.length()) {
          tDateStr = genTime.substring(0, tDateFormate.length());
        }
        tDateStr = tDateStr.replace('T', ' ');
        Date ffDate = CommonFunction.stringToDate(tDateStr, tDateFormate);

        FileNumber fnum = new FileNumber();
        fnum.setCamId(camera.getObjId());
        fnum.setDateStr(dateStr);
        fnum.setFieldId((int) obsSky.getSkyId());
        fnum.setGridId(grid.getObjId());
        int ffNumber = fnumDao.getNextNumber(fnum);

        FitsFile2 ff2 = new FitsFile2();
        ff2.setCamId(camera.getObjId());
        ff2.setFfNumber(ffNumber);
        ff2.setFieldId((int) obsSky.getSkyId());
        ff2.setGenTime(ffDate);
        ff2.setGridId(grid.getObjId());
        ff2.setGroupId(group.getObjId());
        ff2.setImgName(imgName);
        ff2.setImgPath(imgPath);
        ff2.setUnitId(unit.getObjId());
        ff2Dao.save(ff2);
        echo = "register image success!";
      }
      log.debug(echo);
    }
    ActionContext ctx = ActionContext.getContext();
    ctx.getSession().put("echo", echo);
    return result;
  }

  public void initObjType() {
    dateStr = (String) appMap.get("datestr");
    if (null == dateStr) {
      dateStr = CommonFunction.getUniqueDateStr();
      appMap.put("datestr", dateStr);
    }
    groupType = (ObjectType) appMap.get("group");
    if (groupType == null) {
      groupType = objTypeDao.getByName("group");
      appMap.put("group", groupType);
    }
    unitType = (ObjectType) appMap.get("unit");
    if (unitType == null) {
      unitType = objTypeDao.getByName("unit");
      appMap.put("unit", unitType);
    }
    ffovCameraType = (ObjectType) appMap.get("FFoV");
    if (ffovCameraType == null) {
      ffovCameraType = objTypeDao.getByName("FFoV");
      appMap.put("FFoV", ffovCameraType);
    }
    jfovCameraType = (ObjectType) appMap.get("JFoV");
    if (jfovCameraType == null) {
      jfovCameraType = objTypeDao.getByName("JFoV");
      appMap.put("JFoV", jfovCameraType);
    }
    computerType = (ObjectType) appMap.get("computer");
    if (computerType == null) {
      computerType = objTypeDao.getByName("computer");
      appMap.put("computer", computerType);
    }
    gridType = (ObjectType) appMap.get("grid");
    if (gridType == null) {
      gridType = objTypeDao.getByName("grid");
      appMap.put("grid", gridType);
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
   * @param imgName the imgName to set
   */
  public void setImgName(String imgName) {
    this.imgName = imgName;
  }

  /**
   * @param imgPath the imgPath to set
   */
  public void setImgPath(String imgPath) {
    this.imgPath = imgPath;
  }

  /**
   * @param genTime the genTime to set
   */
  public void setGenTime(String genTime) {
    this.genTime = genTime;
  }

  @Override
  public void setApplication(Map<String, Object> map) {
    this.appMap = map;
  }

  /**
   * @param obsSkyDao the obsSkyDao to set
   */
  public void setObsSkyDao(ObservationSkyDao obsSkyDao) {
    this.obsSkyDao = obsSkyDao;
  }

  /**
   * @param dpmDao the dpmDao to set
   */
  public void setDpmDao(DataProcessMachineDAO dpmDao) {
    this.dpmDao = dpmDao;
  }

  /**
   * @param objIdtyDao the objIdtyDao to set
   */
  public void setObjIdtyDao(ObjectIdentityDao objIdtyDao) {
    this.objIdtyDao = objIdtyDao;
  }

  /**
   * @param objTypeDao the objTypeDao to set
   */
  public void setObjTypeDao(ObjectTypeDao objTypeDao) {
    this.objTypeDao = objTypeDao;
  }

  /**
   * @param ff2Dao the ff2Dao to set
   */
  public void setFf2Dao(FitsFile2DAO ff2Dao) {
    this.ff2Dao = ff2Dao;
  }

  /**
   * @param fnumDao the fnumDao to set
   */
  public void setFnumDao(FileNumberDao fnumDao) {
    this.fnumDao = fnumDao;
  }

}
