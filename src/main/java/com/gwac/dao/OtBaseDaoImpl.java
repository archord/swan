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
  public Boolean exist(OtBase obj) {
    System.out.println("******************************************start");
    Boolean flag = false;
    Session session = getCurrentSession();
    String sql = "select ot_id from ot_base where identify='"
            + obj.getIdentify()
            + "' and abs(xtemp-" + obj.getXtemp() + ")<1 "
            + " and abs(ytemp-" + obj.getYtemp() + ")<1 ";
    Query q = session.createSQLQuery(sql);
    if (!q.list().isEmpty()) {
      BigInteger otId = (BigInteger) q.list().get(0);
      obj.setOtId(otId.longValue());
      flag = true;
    }
    System.out.println("******************************************end");
    return flag;
  }
}
