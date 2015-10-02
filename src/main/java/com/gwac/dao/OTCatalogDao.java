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

  public List<OTCatalog> getOT1Catalog(String path);

  public List<OTCatalog> getOT2Catalog(String path);

  public List<OTCatalog> getOT1VarCatalog(String path);
}
