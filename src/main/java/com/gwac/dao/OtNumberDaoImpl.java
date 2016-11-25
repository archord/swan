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
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository(value="otNumberDao")
public class OtNumberDaoImpl extends BaseHibernateDaoImpl<OtNumber> implements OtNumberDao {

  @Override
  public int getNumberByDate(String date) {

    int number = 1;
    Session session = getCurrentSession();
    String sql = "select * from ot_number where date='" + date + "'";
    Query q = session.createSQLQuery(sql).addEntity(OtNumber.class);
    List rstList = q.list();
    if (rstList.isEmpty()) {
      OtNumber otn = new OtNumber();
      otn.setDate(date);
      otn.setNumber(number);
      otn.setVarNumber(0);
      otn.setSubNumber(0);
      super.save(otn);
    } else {
      OtNumber otn = (OtNumber) rstList.get(0);
      try {
        number = otn.getNumber()+1;
        otn.setNumber(number);
        super.update(otn);
      } catch (ClassCastException cce) {
        cce.printStackTrace();
      }
    }

    return number;
  }
  
  @Override
  public int getSubNumberByDate(String date) {

    int number = 1;
    Session session = getCurrentSession();
    String sql = "select * from ot_number where date='" + date + "'";
    Query q = session.createSQLQuery(sql).addEntity(OtNumber.class);
    List rstList = q.list();
    if (rstList.isEmpty()) {
      OtNumber otn = new OtNumber();
      otn.setDate(date);
      otn.setNumber(0);
      otn.setVarNumber(0);
      otn.setSubNumber(number);
      super.save(otn);
    } else {
      OtNumber otn = (OtNumber) rstList.get(0);
      try {
        number = otn.getSubNumber()+1;
        otn.setSubNumber(number);
        super.update(otn);
      } catch (ClassCastException cce) {
        cce.printStackTrace();
      }
    }

    return number;
  }
}
