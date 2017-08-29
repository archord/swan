/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gwac.dao;

import com.gwac.model.OtLevel2;
import com.gwac.model2.Rc3;
import java.util.List;

/**
 *
 * @author xy
 */
public interface Rc3Dao extends BaseHibernateDao<Rc3> {
  
  public List<Rc3> queryByOt2(OtLevel2 ot2, float searchRadius, float minMag, float maxMag);
}
