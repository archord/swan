/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.MoveObjectRecord;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository(value = "moveObjectRecordDao")
public class MoveObjectRecordDaoImpl extends BaseHibernateDaoImpl<MoveObjectRecord> implements MoveObjectRecordDao {

  @Override
  public void save(List<MoveObjectRecord> mors) {

    Session session = getCurrentSession();
    String sql = "insert into move_object_record(mov_id, oor_id)values(?,?)";
    Query q = session.createSQLQuery(sql);
    for (MoveObjectRecord mor : mors) {
      q.setLong(0, mor.getMovId());
      q.setLong(1, mor.getOorId());
      q.executeUpdate();
    }
  }
}
