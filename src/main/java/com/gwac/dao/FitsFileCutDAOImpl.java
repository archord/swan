/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.FitsFileCut;
import com.gwac.model.OtNumber;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class FitsFileCutDAOImpl extends BaseHibernateDaoImpl<FitsFileCut> implements FitsFileCutDAO {

  /**
   * 通过OT名称，查询该OT所有的切图
   *
   * @param otName
   * @return OT切图路径和名称Map
   */
  public List<FitsFileCut> getCutImageByOtName(String otName) {

    List<FitsFileCut> objs = new ArrayList<FitsFileCut>();
    Session session = getCurrentSession();
//    String sql = "select ffc.store_path, ffc.file_name, ffc.number "
//	    + "from fits_file_cut ffc "
//	    + "where ffc.ot_id=(select ot_id from ot_base ob where ob.name='" + otName + "') "
//	    + "order by ffc.number;";
    String sql = "select * "
	    + "from fits_file_cut ffc "
	    + "where ffc.ot_id=(select ot_id from ot_base ob where ob.name='" + otName + "') "
	    + "order by ffc.number;";
    Query q = session.createSQLQuery(sql).addEntity(FitsFileCut.class);
    List rstList = q.list();
//    for (int i = 0; i < rstList.size(); i++) {
//      Object[] row = (Object[]) rstList.get(i);
//      try {
//	FitsFileCut obj = new FitsFileCut();
//	obj.setStorePath((String) row[0]);
//	obj.setFileName((String) row[1]);
//	obj.setNumber((Integer) row[2]);
//	objs.add(obj);
//      } catch (ClassCastException cce) {
//	cce.printStackTrace();
//      }
//    }
    return rstList;
  }

}
