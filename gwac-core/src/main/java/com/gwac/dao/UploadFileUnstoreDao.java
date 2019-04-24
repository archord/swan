/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.UploadFileUnstore;
import java.util.List;

/**
 *
 * @author xy
 */
public interface UploadFileUnstoreDao extends BaseHibernateDao<UploadFileUnstore> {

  public List<UploadFileUnstore> getCrossTaskFile();

  public List<UploadFileUnstore> getOTLevel1File();

  public List<UploadFileUnstore> getImgStatusFile();

  public List<UploadFileUnstore> getVarStarListFile();

  public List<UploadFileUnstore> getSubOTLevel1File();

  public List<UploadFileUnstore> getFollowUpFile();

  public void updateProcessDoneTime(long ufuId);

  public void removeAll();
}
