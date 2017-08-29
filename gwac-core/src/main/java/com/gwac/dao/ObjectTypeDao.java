/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.ObjectType;

/**
 *
 * @author xy
 */
public interface ObjectTypeDao extends BaseHibernateDao<ObjectType> {

  public ObjectType getByName(String name);
}
