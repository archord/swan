/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gwac.dao;

import com.gwac.model.ConfigFile;

/**
 *
 * @author xy
 */
public interface ConfigFileDao extends BaseHibernateDao<ConfigFile>{
  public Boolean exist(ConfigFile obj);
}
