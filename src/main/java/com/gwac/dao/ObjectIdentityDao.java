/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.ObjectIdentity;
import com.gwac.model.ObjectType;

/**
 *
 * @author xy
 */
public interface ObjectIdentityDao extends BaseHibernateDao<ObjectIdentity> {

  public ObjectIdentity getByName(ObjectType objType, String objName);
}
