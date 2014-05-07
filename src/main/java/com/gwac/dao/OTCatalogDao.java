/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.OTCatalog;
import java.util.List;

/**
 *
 * @author xy
 */
public interface OTCatalogDao {
  public List<OTCatalog> getOTCatalog(String path);
}
