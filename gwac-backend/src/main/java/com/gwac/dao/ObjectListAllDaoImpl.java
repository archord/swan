/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.modelyw.ObjectListAll;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository(value="objectListAllDao")
public class ObjectListAllDaoImpl extends BaseHibernateYunweiDaoImpl<ObjectListAll> implements ObjectListAllDao {

  private static final Log log = LogFactory.getLog(ObjectListAllDaoImpl.class);

}
