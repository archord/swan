/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gwac.dao;

import com.gwac.model.OtLevel2;
import com.gwac.model2.Rc3;
import com.gwac.util.SearchBoxSphere;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class Rc3DaoImpl extends MysqlHibernateDaoImpl<Rc3> implements Rc3Dao {
  
  @Override
  public List<Rc3> queryByOt2(OtLevel2 ot2, float searchRadius, float minMag, float maxMag) {

    SearchBoxSphere sbs = new SearchBoxSphere(ot2.getRa(), ot2.getDec(), searchRadius);
    int tflag = sbs.calSearchBox();
    if (tflag != 0) {
      Session session = getCurrentSession();
      String sql = "select * from rc3 where mVmag!=0 and mVmag < " + maxMag + " and mVmag > " + minMag + " and ";
      if (tflag == 1) {
        sql += "RAdeg between " + sbs.getMinRa() + " and " + sbs.getMaxRa() + " and ";
        sql += "DEdeg between " + sbs.getMinDec() + " and " + sbs.getMaxDec() + " ";
      } else {
        sql += "(RAdeg > " + sbs.getMinRa() + " or RAdeg <" + sbs.getMaxRa() + ") and ";
        sql += "DEdeg between " + sbs.getMinDec() + " and " + sbs.getMaxDec() + " ";
      }

      Query q = session.createSQLQuery(sql).addEntity(Rc3.class);
      return q.list();
    }
    return new ArrayList();
  }
}
