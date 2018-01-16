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
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository(value = "otnDao")
public class OtNumberDaoImpl extends BaseHibernateDaoImpl<OtNumber> implements OtNumberDao {

  private static final Log log = LogFactory.getLog(OtNumberDaoImpl.class);

  @Override
  public int getNumberByDate(String date) {

    int number = 1;
    Session session = getCurrentSession();
    String sql = "with updated_rows as (update ot_number set number=number+1 where date='" + date + "' returning *) select number from updated_rows ";
    Query q = session.createSQLQuery(sql);
    List rstList = q.list();
    if (rstList.isEmpty()) {
      OtNumber otn = new OtNumber();
      otn.setDate(date);
      otn.setNumber(number);
      super.save(otn);
    } else {
      number = (Integer) rstList.get(0);
    }

    return number;
  }

  @Override
  public int getSubNumberByDate(String date) {

    int number = 1;
    Session session = getCurrentSession();
    String sql = "with updated_rows as (update ot_number set sub_number=sub_number+1 where date='" + date + "' returning *) select sub_number from updated_rows ";
    Query q = session.createSQLQuery(sql);
    List rstList = q.list();
    if (rstList.isEmpty()) {
      OtNumber otn = new OtNumber();
      otn.setDate(date);
      otn.setSubNumber(number);
      super.save(otn);
    } else {
      number = (Integer) rstList.get(0);
    }

    return number;
  }

  @Override
  public int getJfovNumberByDate(String date) {

    int number = 1;
    Session session = getCurrentSession();
    String sql = "with updated_rows as (update ot_number set jfov_number=jfov_number+1 where date='" + date + "' returning *) select jfov_number from updated_rows ";
    Query q = session.createSQLQuery(sql);
    List rstList = q.list();
    if (rstList.isEmpty()) {
      OtNumber otn = new OtNumber();
      otn.setDate(date);
      otn.setJfwvNumber(number);
      super.save(otn);
    } else {
      number = (Integer) rstList.get(0);
    }

    return number;
  }

  @Override
  public int getJfovSubNumberByDate(String date) {

    int number = 1;
    Session session = getCurrentSession();
    String sql = "with updated_rows as (update ot_number set jfov_sub_number=jfov_sub_number+1 where date='" + date + "' returning *) select jfov_sub_number from updated_rows ";
    Query q = session.createSQLQuery(sql);
    List rstList = q.list();
    if (rstList.isEmpty()) {
      OtNumber otn = new OtNumber();
      otn.setDate(date);
      otn.setJfovSubNumber(number);
      super.save(otn);
    } else {
      number = (Integer) rstList.get(0);
    }

    return number;
  }
  

  @Override
  public int getFollowupNumberByDate(String date) {

    int number = 1;
    Session session = getCurrentSession();
    String sql = "with updated_rows as (update ot_number set followup_number=followup_number+1 where date='" + date + "' returning *) select followup_number from updated_rows ";
    Query q = session.createSQLQuery(sql);
    List rstList = q.list();
    if (rstList.isEmpty()) {
      OtNumber otn = new OtNumber();
      otn.setDate(date);
      otn.setFollowupNumber(number);
      super.save(otn);
    } else {
      number = (Integer) rstList.get(0);
    }

    return number;
  }
}
