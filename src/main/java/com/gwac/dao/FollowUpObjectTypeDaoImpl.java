/*
 * mseeworld工作室，致力于人工智能研究。Email: xyag.902@163.com
 */

package com.gwac.dao;

import com.gwac.model.FollowUpObjectType;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class FollowUpObjectTypeDaoImpl extends BaseHibernateDaoImpl<FollowUpObjectType> implements FollowUpObjectTypeDao{
  
  @Override
  public FollowUpObjectType getOtTypeByTypeName(String typeName) {

    String sql = "select * from follow_up_object_type where fuo_type_name='" + typeName + "'";
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql).addEntity(FollowUpObjectType.class);
    List<FollowUpObjectType> otts = q.list();
    FollowUpObjectType tobj = null;
    if (otts.size() > 0) {
      tobj = otts.get(0);
    }
    return tobj;
  }
  
  public FollowUpObjectType getByTypeId(Short typeId) {

    String sql = "select * from follow_up_object_type where fuo_type_id=" + typeId + "";
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql).addEntity(FollowUpObjectType.class);
    List<FollowUpObjectType> otts = q.list();
    FollowUpObjectType tobj = null;
    if (otts.size() > 0) {
      tobj = otts.get(0);
    }
    return tobj;
  }

  @Override
  public List<FollowUpObjectType> getOtTypes() {
    String sql = "select * from follow_up_object_type order by fuo_type_id asc";
    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql).addEntity(FollowUpObjectType.class);
    return q.list();
  }
}
