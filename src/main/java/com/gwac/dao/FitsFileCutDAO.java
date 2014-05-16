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

  public List<FitsFileCut> getCutImageByOtName(String otName);
}
