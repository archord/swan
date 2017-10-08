/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.FileNumber;
import java.math.BigInteger;
import java.util.Iterator;
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
@Repository(value = "fnumDao")
public class FileNumberDaoImpl extends BaseHibernateDaoImpl<FileNumber> implements FileNumberDao {

  private static final Log log = LogFactory.getLog(FileNumberDaoImpl.class);

  @Override
  public int getNextNumber(FileNumber fnum) {

    int number = 1;
    Session session = getCurrentSession();
    String sql = "select * from file_number where sky_id=? and and cam_id=? and date_str=?";
    Query q = session.createSQLQuery(sql).addEntity(FileNumber.class);
    q.setInteger(0, fnum.getSkyId());
    q.setInteger(1, fnum.getCamId());
    q.setString(2, fnum.getDateStr());
    List rstList = q.list();
    if (rstList.isEmpty()) {
      fnum.setFfNumber(number);
      super.save(fnum);
    } else {
      FileNumber otn = (FileNumber) rstList.get(0);
      try {
        number = otn.getFfNumber() + 1;
        otn.setFfNumber(number);
        super.update(otn);
      } catch (ClassCastException cce) {
        log.error(cce);
      }
    }

    return number;
  }

}
