/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gwac.dao;

import com.gwac.model.OtType;
import java.util.List;

/**
 *
 * @author xy
 */
public interface OtTypeDao extends BaseHibernateDao<OtType> {
  
  public OtType getOtTypeByTypeName(String typeName);
  
  public List<OtType> getOtTypes();
}
