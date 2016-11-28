/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.MoveObject;
import java.util.Iterator;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository(value = "moveObjectDao")
public class MoveObjectDaoImpl extends BaseHibernateDaoImpl<MoveObject> implements MoveObjectDao {

  @Override
  public String getMoveObjsByDate(String dateStr) {
    Session session = getCurrentSession();
    String sql = "SELECT text(JSON_AGG((SELECT r FROM (SELECT mov_id, tt_frm_num, mov_detail) r)))  "
            + "FROM( SELECT  "
            + "moor.mov_id as mov_id, moor.total_frame_number as tt_frm_num, JSON_AGG((SELECT r FROM (SELECT moor.ff_number, moor.ra_d, moor.dec_d, moor.date_ut) r)) as mov_detail  "
            + "FROM (  "
            + "SELECT oor.ff_number, oor.ra_d, oor.dec_d, oor.x_temp, oor.y_temp, oor.date_ut, oor.oor_id, mor.mov_id, mo.total_frame_number "
            + "FROM ot_observe_record oor  "
            + "INNER JOIN move_object_record mor ON mor.oor_id = oor.oor_id  "
            + "INNER JOIN move_object mo ON mo.mov_id = mor.mov_id "
            + "WHERE oor.ot_id=0 and mor.mov_id IS NOT NULL AND oor.date_str=? "
            + "ORDER BY mov_id, date_ut, dec_d  "
            + ")as moor  "
            + "GROUP BY moor.mov_id, moor.total_frame_number "
            + ")as moor2";

    String rst = "";
    Query q = session.createSQLQuery(sql);
    q.setString(0, dateStr);
    if (q.list().size() > 0) {
      rst = (String) q.list().get(0);
    }
    return rst;
  }

  @Override
  public String getNotMatchOTByDate(String dateStr) {
    Session session = getCurrentSession();
    String sql = "SELECT text(JSON_AGG((SELECT r FROM (SELECT ff_number, mov_detail) r))) "
            + "FROM( SELECT "
            + "	moor.ff_number as ff_number, JSON_AGG((SELECT r FROM (SELECT moor.ra_d, moor.dec_d, moor.date_ut) r)) as mov_detail "
            + "FROM ( "
            + "		SELECT oor.ff_number, oor.ra_d, oor.dec_d, oor.x_temp, oor.y_temp, oor.date_ut, oor.oor_id, mor.mov_id "
            + "		FROM ot_observe_record oor "
            + "		LEFT JOIN move_object_record mor ON mor.oor_id = oor.oor_id "
            + "		WHERE oor.ot_id=0 and mor.mov_id IS NULL AND oor.date_str=? "
            + "		ORDER BY date_ut "
            + "	)as moor "
            + "GROUP BY moor.ff_number "
            + "ORDER BY moor.ff_number "
            + ")as moor2";

    String rst = "";
    Query q = session.createSQLQuery(sql);
    q.setString(0, dateStr);
    if (q.list().size() > 0) {
      rst = (String) q.list().get(0);
    }
    return rst;
  }
}
