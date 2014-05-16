/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.OtBase;
import java.math.BigInteger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class OtBaseDaoImpl extends BaseHibernateDaoImpl<OtBase> implements OtBaseDao {

  private static final Log log = LogFactory.getLog(OtBaseDaoImpl.class);
  
  @Override
  public OtBase getOtBaseByName(String otName) {
    Session session = getCurrentSession();
    String sql = "select * from ot_base where name='" + otName + "';";
    Query q = session.createSQLQuery(sql).addEntity(OtBase.class);
    if (!q.list().isEmpty()) {
      return (OtBase) q.list().get(0);
    }else{
      return null;
    }
  }

  @Override
  public Boolean exist(OtBase obj) {
    Boolean flag = false;
    Session session = getCurrentSession();
    String sql = "select ot_id from ot_base where identify='"
            + obj.getIdentify()
            + "' and abs(xtemp-" + obj.getXtemp() + ")<2 "
            + " and abs(ytemp-" + obj.getYtemp() + ")<2 ";
    Query q = session.createSQLQuery(sql);
    if (!q.list().isEmpty()) {
      BigInteger otId = (BigInteger) q.list().get(0);
      obj.setOtId(otId.longValue());
      flag = true;
    }
    return flag;
  }
}
