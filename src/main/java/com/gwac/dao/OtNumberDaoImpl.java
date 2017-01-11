/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.OtNumber;
import java.math.BigInteger;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class OtNumberDaoImpl extends BaseHibernateDaoImpl<OtNumber> implements OtNumberDao {

  private static final Log log = LogFactory.getLog(OtNumberDaoImpl.class);

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
      otn.setJfovSubNumber(0);
      otn.setJfwvNumber(0);
      super.save(otn);
    } else {
      OtNumber otn = (OtNumber) rstList.get(0);
      try {
        number = otn.getNumber() + 1;
        otn.setNumber(number);
        super.update(otn);
      } catch (ClassCastException cce) {
        log.error(cce);
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
      otn.setJfovSubNumber(0);
      otn.setJfwvNumber(0);
      super.save(otn);
    } else {
      OtNumber otn = (OtNumber) rstList.get(0);
      try {
        number = otn.getSubNumber() + 1;
        otn.setSubNumber(number);
        super.update(otn);
      } catch (ClassCastException cce) {
        log.error(cce);
      }
    }

    return number;
  }

  public int getJfovNumberByDate(String date) {

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
      otn.setSubNumber(0);
      otn.setJfovSubNumber(0);
      otn.setJfwvNumber(number);
      super.save(otn);
    } else {
      OtNumber otn = (OtNumber) rstList.get(0);
      try {
        number = otn.getJfwvNumber() + 1;
        otn.setJfwvNumber(number);
        super.update(otn);
      } catch (ClassCastException cce) {
        log.error(cce);
      }
    }

    return number;
  }

  public int getJfovSubNumberByDate(String date) {

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
      otn.setSubNumber(0);
      otn.setJfovSubNumber(number);
      otn.setJfwvNumber(0);
      super.save(otn);
    } else {
      OtNumber otn = (OtNumber) rstList.get(0);
      try {
        number = otn.getJfovSubNumber() + 1;
        otn.setJfovSubNumber(number);
        super.update(otn);
      } catch (ClassCastException cce) {
        log.error(cce);
      }
    }

    return number;
  }
}
