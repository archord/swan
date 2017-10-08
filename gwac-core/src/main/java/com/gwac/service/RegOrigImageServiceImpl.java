/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.service;

import com.gwac.dao.CameraDao;
import com.gwac.dao.FileNumberDao;
import com.gwac.dao.FitsFile2DAO;
import com.gwac.dao.MountDao;
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
import com.gwac.model.Camera;
import com.gwac.model.Mount;

/**
 *
 * @author xy
 */
@Service(value = "regOrigImageServiceImpl")
public final class RegOrigImageServiceImpl implements RegOrigImageService {

  private static final Log log = LogFactory.getLog(RegOrigImageServiceImpl.class);

  @Resource
  private ObservationSkyDao obsSkyDao;
  @Resource
  private CameraDao cameraDao;
  @Resource
  private FitsFile2DAO ff2Dao;
  @Resource
  private FileNumberDao fnumDao;
  @Resource
  private MountDao mountDao;

  public RegOrigImageServiceImpl() {
  }

  @Override
  public void updateSystemStatus(String groupId, String unitId, String camId,
          String gridId, String fieldId, String imgName, String imgPath, String genTime, String dateStr) {

    boolean isExist = ff2Dao.exist(imgName);
    Camera tcamera = cameraDao.getByName(camId);
    Mount tmount = mountDao.getByGroupUnitId(groupId, unitId);
    if (isExist) {
      log.warn(imgName + " already exist.");
    } else if (tcamera == null) {
      log.error("can not find cameraId: " + camId);
    } else if (tmount == null) {
      log.error("can not find groupId: " + groupId + ", unitId: " + unitId);
    } else {
//        camId = imgName.substring(0, 1) + camId;
      ObservationSky tsky = obsSkyDao.getByName(fieldId, gridId);

      String tDateStr = genTime;
      String tDateFormate = "yyyy-MM-dd HH:mm:ss.SSS";
      if (genTime.length() > tDateFormate.length()) {
        tDateStr = genTime.substring(0, tDateFormate.length());
      }
      tDateStr = tDateStr.replace('T', ' ');
      Date ffDate = CommonFunction.stringToDate(tDateStr, tDateFormate);

      FileNumber fnum = new FileNumber();
      fnum.setCamId(tcamera.getCameraId());
      fnum.setSkyId(tsky.getSkyId());
      fnum.setDateStr(dateStr);
      int ffNumber = fnumDao.getNextNumber(fnum);

      FitsFile2 ff2 = new FitsFile2();
      ff2.setCamId(tcamera.getCameraId());
      ff2.setFfNumber(ffNumber);
      ff2.setMountId(tmount.getMountId());
      ff2.setSkyId(tsky.getSkyId());
      ff2.setGenTime(ffDate);
      ff2.setImgName(imgName);
      ff2.setImgPath(imgPath);
      ff2Dao.save(ff2);
      
      log.debug("register " + imgName + " success.");
    }
  }

}
