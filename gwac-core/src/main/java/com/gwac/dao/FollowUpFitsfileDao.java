/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.FollowUpFitsfile;
import com.gwac.model.Telescope;
import java.util.List;

/**
 *
 * @author xy
 */
public interface FollowUpFitsfileDao extends BaseHibernateDao<FollowUpFitsfile> {
  
  public List<FollowUpFitsfile> getByFoName(String foName);
  
  public List<FollowUpFitsfile> getByOtId(long otId);

  public FollowUpFitsfile getByName(String ffName);
  
  public void updateIsUpload(String ffName);
}
