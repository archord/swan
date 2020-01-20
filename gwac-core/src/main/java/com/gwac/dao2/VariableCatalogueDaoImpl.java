/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao2;

import com.gwac.model2.VariableCatalogue;
import com.gwac.util.SearchBoxSphere;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository(value="variableCatalogueDao")
public class VariableCatalogueDaoImpl extends MysqlHibernateDaoImpl<VariableCatalogue> implements VariableCatalogueDao {
  
  @Override
  public List<VariableCatalogue> queryByOt2(float ra, float dec, float searchRadius) {

    SearchBoxSphere sbs = new SearchBoxSphere(ra, dec, searchRadius);
    int tflag = sbs.calSearchBox();
    if (tflag != 0) {
      Session session = getCurrentSession();
      String sql = "select * from variable_catalogue where ";
      if (tflag == 1) {
        sql += "radeg between " + sbs.getMinRa() + " and " + sbs.getMaxRa() + " and ";
        sql += "decdeg between " + sbs.getMinDec() + " and " + sbs.getMaxDec() + " ";
      } else {
        sql += "(radeg > " + sbs.getMinRa() + " or radeg <" + sbs.getMaxRa() + ") and ";
        sql += "decdeg between " + sbs.getMinDec() + " and " + sbs.getMaxDec() + " ";
      }

      Query q = session.createSQLQuery(sql).addEntity(VariableCatalogue.class);
      return q.list();
    }
    return new ArrayList();
  }

}
