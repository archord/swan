/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.SystemLog;
import java.util.List;

/**
 *
 * @author xy
 */
public interface SystemLogDao extends BaseHibernateDao<SystemLog> {
  
  public String getLogCodes();
  
  public String getMsgIPs();
  
  public void removeOldRecord(int day);

  public String findRecord(int start, int length, String dateStart, String dateEnd, String logCode, String msgIp);
  
  public void removeOldRecord(int day, String codes);
}
