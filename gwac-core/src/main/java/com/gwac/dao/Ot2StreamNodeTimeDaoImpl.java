/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.Ot2StreamNodeTime;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author msw
 */
@Repository
public class Ot2StreamNodeTimeDaoImpl extends BaseHibernateDaoImpl<Ot2StreamNodeTime> implements Ot2StreamNodeTimeDao {
  
  @Override
  public Date getMinDate() {
    Session session = getCurrentSession();
    String sql = "SELECT min(found_time_utc) + INTERVAL '8 hour' from ot_level2 ";
    Date rst = null;
    Query q = session.createSQLQuery(sql);
    if (q.list().size() > 0) {
      rst = (Date) q.list().get(0);
    }
    return rst;
  }

  @Override
  public String getJson() {
    Session session = getCurrentSession();
    String sql = "SELECT TEXT ( JSON_AGG ( ( SELECT r FROM (SELECT ccd_name, ot2_list) r ) ) ) "
            + "FROM ( SELECT ccd_name, JSON_AGG (t1.*) AS ot2_list "
            + "		FROM "
            + "			( "
            + "				SELECT "
            + "					ot2. NAME ot2_name, "
            + "					cam. NAME ccd_name, "
            + "					ff1.gen_time + INTERVAL '8 hour' ot11_gen, "
            + "					ufu1.upload_date ot11_up, "
            + "					ufu1.process_done_time ot11_done, "
            + "					ff2.gen_time + INTERVAL '8 hour' ot12_gen, "
            + "					ufu2.upload_date ot12_up, "
            + "					ufu2.process_done_time ot12_done, "
            + "					osnt.ot2_gen_time ot2_gen, "
            + "					ffc1.insert_time ffc1_gen, "
            + "					ffc1.request_time ffc1_req, "
            + "					ffc1.upload_time ffc1_up, "
            + "					ffc2.insert_time ffc2_gen, "
            + "					ffc2.request_time ffc2_req, "
            + "					ffc2.upload_time ffc2_up, "
            + "					ffcr.insert_time ffcr_gen, "
            + "					ffcr.request_time ffcr_req, "
            + "					ffcr.upload_time ffcr_up, "
            + "					osnt.lookback_time ot2_lb, "
            + "					osnt.lookup_time ot2_lu, "
            + "					osnt.lookup_result_time ot2_lur "
            + "				FROM "
            + "					ot2_stream_node_time osnt "
            + "				INNER JOIN ot_level2 ot2 ON ot2.ot_id = osnt.ot_id "
            + "				INNER JOIN camera cam ON cam.camera_id = ot2.dpm_id "
            + "				INNER JOIN ot_observe_record oor1 ON oor1.oor_id = osnt.oor_id1 "
            + "				INNER JOIN ot_observe_record oor2 ON oor2.oor_id = osnt.oor_id2 "
            + "				INNER JOIN fits_file2 ff1 ON ff1.ff_id = oor1.ff_id "
            + "				INNER JOIN fits_file2 ff2 ON ff2.ff_id = oor2.ff_id "
            + "				INNER JOIN upload_file_unstore ufu1 ON ufu1.file_type = '1' "
            + "				AND SUBSTRING (ufu1.file_name FROM 1 FOR 29) = SUBSTRING (ff1.img_name FROM 1 FOR 29) "
            + "				INNER JOIN upload_file_unstore ufu2 ON ufu2.file_type = '1' "
            + "				AND SUBSTRING (ufu2.file_name FROM 1 FOR 29) = SUBSTRING (ff2.img_name FROM 1 FOR 29) "
            + "				INNER JOIN fits_file_cut ffc1 ON ffc1.ffc_id = oor1.ffc_id "
            + "				INNER JOIN fits_file_cut ffc2 ON ffc2.ffc_id = oor2.ffc_id "
            + "				INNER JOIN fits_file_cut_ref ffcr ON ffcr.ot_id = osnt.ot_id "
            + "				ORDER BY ot2.ot_id "
            + "			) AS t1 "
            + "		GROUP BY ccd_name "
            + "		ORDER BY ccd_name "
            + "	) AS t2; ";

    String rst = "";
    Query q = session.createSQLQuery(sql);
    if (q.list().size() > 0) {
      rst = (String) q.list().get(0);
    }
    return rst;
  }

  @Override
  public void save(Ot2StreamNodeTime obj) {
    Session session = getCurrentSession();
    String sql = "insert into ot2_stream_node_time(ot_id,oor_id1,oor_id2)values(?,?,?)";
    Query q = session.createSQLQuery(sql);
    q.setLong(0, obj.getOtId());
    q.setLong(1, obj.getOorId1());
    q.setLong(2, obj.getOorId2());
    q.executeUpdate();
  }

  @Override
  public void updateLookBackTime(long otId) {
    Session session = getCurrentSession();
    String sql = "update ot2_stream_node_time set lookback_time=now() where lookback_time is NULL and ot_id=" + otId + "";
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateLookBackTime(String otName) {
    Session session = getCurrentSession();
    String sql = "update ot2_stream_node_time set lookback_time=now() where lookback_time is NULL and ot_id=(select ot_id from ot_level2 where name='" + otName.trim() + "')";
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateLookUpTime(long otId) {
    Session session = getCurrentSession();
    String sql = "update ot2_stream_node_time set lookup_time=now() where lookup_time is NULL and ot_id=" + otId + "";
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateLookUpResultTime(long otId) {
    Session session = getCurrentSession();
    String sql = "update ot2_stream_node_time set lookup_result_time=now() where ot_id=" + otId + " and lookup_result_time is NULL";
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateLookUpResultTime(String otName) {
    Session session = getCurrentSession();
    String sql = "update ot2_stream_node_time set lookup_result_time=now() where lookup_result_time is NULL and ot_id=(select ot_id from ot_level2 where name='" + otName.trim() + "')";
    session.createSQLQuery(sql).executeUpdate();
  }
}
