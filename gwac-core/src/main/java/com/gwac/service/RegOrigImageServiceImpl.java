/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

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
import java.util.Date;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

/**
 *
 * @author xy
 */
@Service(value = "regOrigImageServiceImpl")
public final class RegOrigImageServiceImpl implements RegOrigImageService{

  private static final Log log = LogFactory.getLog(RegOrigImageServiceImpl.class);

  private ObjectType groupType = null;
  private ObjectType unitType = null;
  private ObjectType ffovCameraType = null;
  private ObjectType jfovCameraType = null;
  private ObjectType computerType = null;
  private ObjectType gridType = null;

  @Resource
  private ObservationSkyDao obsSkyDao;
  @Resource
  private DataProcessMachineDAO dpmDao;
  @Resource
  private ObjectIdentityDao objIdtyDao;
  @Resource
  private FitsFile2DAO ff2Dao;
  @Resource
  private FileNumberDao fnumDao;
  @Resource
  private ObjectTypeDao objTypeDao;
  
  public RegOrigImageServiceImpl(){
  }

  @Override
  public void updateSystemStatus(String groupId, String unitId, String camId, 
          String gridId, String fieldId, String imgName, String imgPath, String genTime, String dateStr) {
    
    initObjType();

    boolean isExist = ff2Dao.exist(imgName);
//    if (isExist) {
//      log.warn(imgName + " already exist.");
//    } else 
    {
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
      log.debug("register " + imgName + " success.");
    }
  }

  public void initObjType() {
    if (groupType == null) {
      groupType = objTypeDao.getByName("group");
    }
    if (unitType == null) {
      unitType = objTypeDao.getByName("unit");
    }
    if (ffovCameraType == null) {
      ffovCameraType = objTypeDao.getByName("FFoV");
    }
    if (jfovCameraType == null) {
      jfovCameraType = objTypeDao.getByName("JFoV");
    }
    if (computerType == null) {
      computerType = objTypeDao.getByName("computer");
    }
    if (gridType == null) {
      gridType = objTypeDao.getByName("grid");
    }
  }

}
