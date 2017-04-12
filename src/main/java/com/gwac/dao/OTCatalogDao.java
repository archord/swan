/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.FollowUpCatalog;
import com.gwac.model4.OTCatalog;
import java.util.List;

/**
 *
 * @author xy
 */
public interface OTCatalogDao {

  public List<OTCatalog> getOT1Catalog(String path);

  public List<OTCatalog> getOT1CutCatalog(String path);

  public List<OTCatalog> getOT1VarCatalog(String path);
  
  public List<FollowUpCatalog> getFollowUpCatalog(String path);
}
