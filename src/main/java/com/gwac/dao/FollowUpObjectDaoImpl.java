/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */
package com.gwac.dao;

import com.gwac.model.FollowUpObject;
import java.math.BigInteger;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class FollowUpObjectDaoImpl extends BaseHibernateDaoImpl<FollowUpObject> implements FollowUpObjectDao {

  @Override
  public int countTypeNumber(FollowUpObject obj) {
    Session session = getCurrentSession();
    String sql = "select count(*) from follow_up_object where ot_id=" + obj.getOtId() + " and fuo_type_id=" + obj.getFuoTypeId();
    Query q = session.createSQLQuery(sql);
    return ((BigInteger) q.list().get(0)).intValue();
  }

  @Override
  public List<FollowUpObject> exist(FollowUpObject obj, float errorBox) {

    Session session = getCurrentSession();
    String sql = "select * from follow_up_object "
            + " where ot_id=" + obj.getOtId() + " and fuo_type_id=" + obj.getFuoTypeId()
            + " and sqrt(power(last_x-" + obj.getLastX() + ", 2)+power(last_y-" + obj.getLastY() + ", 2))<" + errorBox;
    Query q = session.createSQLQuery(sql).addEntity(FollowUpObject.class);
    return q.list();
  }

  /**
   * 需要按照后随结果类型fuo_type_id排序，将CHECK类型的排在前面
   * @param otId
   * @param queryHis
   * @return 
   */
  @Override
  public List<FollowUpObject> getByOtId(long otId, Boolean queryHis) {

    String sql1 = "select * from follow_up_object where ot_id=" + otId;

    String sql2 = "select * from follow_up_object_his where ot_id=" + otId;

    String unionSql;
    if (queryHis) {
      unionSql = "(" + sql1 + ") union (" + sql2 + ") order by fuo_type_id, fuo_name";
    } else {
      unionSql = sql1 + " order by fuo_type_id, fuo_name";
    }

    Session session = getCurrentSession();
    Query q = session.createSQLQuery(unionSql).addEntity(FollowUpObject.class);
    return q.list();
  }

  @Override
  public List<FollowUpObject> getByOtIdTypeId(long otId, short fuoTypeId, Boolean queryHis) {

    String sql1 = "select * from follow_up_object where ot_id=" + otId + "and fuo_type_id=" + fuoTypeId;

    String sql2 = "select * from follow_up_object_his where ot_id=" + otId + "and fuo_type_id=" + fuoTypeId;

    String unionSql;
    if (queryHis) {
      unionSql = "(" + sql1 + ") union (" + sql2 + ") order by fuo_id";
    } else {
      unionSql = sql1 + " order by fuo_id";
    }

    Session session = getCurrentSession();
    Query q = session.createSQLQuery(unionSql).addEntity(FollowUpObject.class);
    return q.list();
  }
}
