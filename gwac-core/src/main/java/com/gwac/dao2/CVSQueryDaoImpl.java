/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao2;

import com.gwac.model.OtLevel2;
import com.gwac.model2.Cvs;
import com.gwac.util.SearchBoxSphere;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository(value="cvsQueryDao")
public class CVSQueryDaoImpl extends MysqlHibernateDaoImpl<Cvs> implements CVSQueryDao {
  
  @Override
  public List<Cvs> queryByOt2(OtLevel2 ot2, float searchRadius, float mag) {

    SearchBoxSphere sbs = new SearchBoxSphere(ot2.getRa(), ot2.getDec(), searchRadius);
    int tflag = sbs.calSearchBox();
    if (tflag != 0) {
      Session session = getCurrentSession();
      String sql = "select * from cvs where mag < " + mag + " and ";
      if (tflag == 1) {
        sql += "RAdeg between " + sbs.getMinRa() + " and " + sbs.getMaxRa() + " and ";
        sql += "DEdeg between " + sbs.getMinDec() + " and " + sbs.getMaxDec() + " ";
      } else {
        sql += "(RAdeg > " + sbs.getMinRa() + " or RAdeg <" + sbs.getMaxRa() + ") and ";
        sql += "DEdeg between " + sbs.getMinDec() + " and " + sbs.getMaxDec() + " ";
      }

      Query q = session.createSQLQuery(sql).addEntity(Cvs.class);
      return q.list();
    }
    return new ArrayList();
  }

}
