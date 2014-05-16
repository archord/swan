/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.ConfigFile;
import java.math.BigInteger;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class ConfigFileDaoImpl extends BaseHibernateDaoImpl<ConfigFile> implements ConfigFileDao {

  public Boolean exist(ConfigFile obj) {
    Boolean flag = false;
    Session session = getCurrentSession();
    String sql = "select cf_id from config_file where file_name='" + obj.getFileName() + "' ";
    Query q = session.createSQLQuery(sql);
    if (!q.list().isEmpty()) {
      BigInteger cfId = (BigInteger) q.list().get(0);
      obj.setCfId(cfId.longValue());
      flag = true;
    }
    return flag;
  }

}
