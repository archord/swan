/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.OtBase;

/**
 *
 * @author xy
 */
public interface OtBaseDao extends BaseHibernateDao<OtBase> {

  public Boolean exist(OtBase obj);

  public OtBase getOtBaseByName(String otName);
}
