/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */

package com.gwac.dao;

import com.gwac.model.SystemStatusMonitor;
import java.util.List;

/**
 *
 * @author xy
 */
public interface SystemStatusMonitorDao  extends BaseHibernateDao<SystemStatusMonitor>{
  public List<SystemStatusMonitor> getAllStatus();
  public void updateCameraStatus(String id, String status);
  public void updateObservationPlan(String id, Long opSn);
  public void updateImgRegister(String id, String imgName);
  public void updateImgParmFile(String id, String imgParmName);
  public void updateOt1List(String id, String ot1ListName);
  public void updateImgCut(String id, String imgCutFileName);
  public void updateImgCutRef(String id, String imgCutFileName);
  public void updateImgCutRequest(String id, String imgCutFileName);
  public void updateImgCutRefRequest(String id, String imgCutFileName);
  public void updateOt2LookBack(String id, String ot2Name);
  public void updateOt1ListSub(String id, String ot1ListName);
  public void updateImgCutSub(String id, String imgCutFileName);
  public void updateOt2LookBackSub(String id, String ot2Name);
  public void updateThumbnail(String id, String thumbnailName);
  public void updateLog(String id, String logContent);
  public void updateImgParm(String id, long imgParmId);
  public void updateProcessMachine(String id, int status, float storage, float cpu);
  public void updateCaptureMachine(String id, int status, float storage, float cpu);
  
}
