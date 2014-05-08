/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.OtNumber;
import java.math.BigInteger;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class OtNumberDaoImpl extends BaseHibernateDaoImpl<OtNumber> implements OtNumberDao {

  @Override
  public int getNumberByDate(String date) {

    int number = 1;
    Session session = getCurrentSession();
    String sql = "select otn_id, number from ot_number where date='" + date + "'";
    Query q = session.createSQLQuery(sql);
    List rstList = q.list();
    if (rstList.isEmpty()) {
      OtNumber otn = new OtNumber();
      otn.setDate(date);
      otn.setNumber(number);
      super.save(otn);
    } else {
      Object[] row = (Object[]) rstList.get(0);
      try {
        BigInteger otnId = (BigInteger) row[0];
        number = (Integer) row[1] + 1;
        OtNumber otn = new OtNumber();
        otn.setOtnId(otnId.longValue());
        otn.setDate(date);
        otn.setNumber(number);
        super.update(otn);
      } catch (ClassCastException cce) {
        cce.printStackTrace();
      }
    }

    return number;
  }
}
