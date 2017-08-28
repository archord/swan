/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.ImageStatusParameter;
import com.gwac.model.UploadFileUnstore;
import java.util.Date;
import java.util.List;

/**
 *
 * @author xy
 */
public interface ImageStatusParameterDao extends BaseHibernateDao<ImageStatusParameter> {

  public void moveDataToHisTable();

  public List<ImageStatusParameter> getImgStatusParmByDpmId(String dpmName);
  
  public List<ImageStatusParameter> getImgStatusParmByDate(String dateStr);
  
  public List<ImageStatusParameter> getCurAllParm();
  
  public List<ImageStatusParameter> getLatestParmOfAllDpm();
  
  public ImageStatusParameter getPreviousStatus(ImageStatusParameter isp);
  
  public String getCurAllParmJson();
  
  public String getJsonByParm(List<String> parmName);
  
  public Date getMinDate();
  
}
