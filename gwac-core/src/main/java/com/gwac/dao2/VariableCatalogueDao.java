/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao2;

import com.gwac.dao.BaseHibernateDao;
import com.gwac.model2.VariableCatalogue;
import java.util.List;

/**
 *
 * @author xy
 */
public interface VariableCatalogueDao extends BaseHibernateDao<VariableCatalogue> {
  
  public List<VariableCatalogue> queryByOt2(float ra, float dec, float searchRadius);

}
