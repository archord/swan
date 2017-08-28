/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.service;

/**
 *
 * @author xy
 */
public interface RegOrigImageService {

  public void updateSystemStatus(String groupId, String unitId, String camId,
          String gridId, String fieldId, String imgName, String imgPath, String genTime, String dateStr);
}
