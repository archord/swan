/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.dao;

import com.gwac.model.CcdPixFilter;
import com.gwac.model.OtLevel2;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository
public class CcdPixFilterDaoImpl extends BaseHibernateDaoImpl<CcdPixFilter> implements CcdPixFilterDao {

  @Override
  public void removeAll() {
    Session session = getCurrentSession();
    String sql = "delete from ccd_pix_filter";
    session.createSQLQuery(sql).executeUpdate();
  }

  /**
   *
   * @param ot2
   * @return 如果匹配成功，则返回其对应的类型ID，否则返回0；
   */
  @Override
  public Short filterOT2(OtLevel2 ot2) {

    Session session = getCurrentSession();
    String sql = "select cpf.ot_type_id from ccd_pix_filter cpf "
            + "inner join ot_level2 ot2 on ot2.ot_id=" + ot2.getOtId() + " and ot2.dpm_id=cpf.dpm_id "
            + "inner join ot_observe_record oor on oor.ot_id=ot2.ot_id and oor.ff_number=ot2.first_ff_number "
            + "where oor.x>=cpf.min_x and oor.x<=cpf.max_x and oor.y>=min_y and oor.y<=max_y;";

    Query q = session.createSQLQuery(sql);
    List rst = q.list();

    Short otTypeId = (short) 0;
    if (rst.size() > 0) {
      otTypeId = (Short) rst.get(0);
    }

    return otTypeId;
  }

}
