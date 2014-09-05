/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.FitsFileCut;
import java.util.List;

/**
 *
 * @author xy
 */
public interface FitsFileCutDAO extends BaseHibernateDao<FitsFileCut> {

  public void uploadSuccessCutByName(String fileName);

  public String getUnCuttedStarList(int dpmId);

  public List<FitsFileCut> getCutImageByOtName(String otName);

  public List<FitsFileCut> getCutImageByOtId(long otId);
}
