/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.Camera;
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
@Repository
public class CameraDaoImpl extends BaseHibernateDaoImpl<Camera> implements CameraDao {

  private static final Log log = LogFactory.getLog(CameraDaoImpl.class);

  @Override
  public void updateStatus(String ccds, String status) {
    Session session = getCurrentSession();
    String sql = "update camera set status="+status+" where name in("+ccds+")";
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public String getCamersStatus() {
    Session session = getCurrentSession();
    String sql = "SELECT text(JSON_AGG((SELECT r FROM (SELECT name, status) r)))  "
            + "FROM( "
            + "SELECT name, status "
            + "FROM camera  "
            + "where camera_id<51 "
            + "ORDER BY camera_id "
            + ")as obj;";
    Query q = session.createSQLQuery(sql);
    return (String) q.list().get(0);
  }

  @Override
  public List<Camera> getAllCameras() {
    Session session = getCurrentSession();
    String sql = "select * from camera order by camera_id;";
    Query q = session.createSQLQuery(sql).addEntity(Camera.class);
    return q.list();
  }

  @Override
  public void updateLastActiveTime(String name) {
    Session session = getCurrentSession();
    String sql = "update camera set last_active_time=current_timestamp where name='" + name.toUpperCase() + "'";
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateMonitorImageTime(String name) {
    Session session = getCurrentSession();
    String sql = "update camera set monitor_image_time=current_timestamp where name='" + name + "'";
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public void updateByName(Camera dpm) {
    Session session = getCurrentSession();
    String sql = "update camera set cur_process_number=";

//    pStmt = session.connection().prepareStatement(sql);
//    rs = pStmt.executeQuery();
//    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public Camera getByName(String name) {
    Session session = getCurrentSession();
    String sql = "select * from camera where name='" + name + "';";
    Query q = session.createSQLQuery(sql).addEntity(Camera.class);
    if (!q.list().isEmpty()) {
      return (Camera) q.list().get(0);
    } else {
      return null;
    }
  }

  @Override
  public Camera getById(long id) {
    Session session = getCurrentSession();
    String sql = "select * from camera where camera_id=" + id;
    Query q = session.createSQLQuery(sql).addEntity(Camera.class);
    if (!q.list().isEmpty()) {
      return (Camera) q.list().get(0);
    } else {
      return null;
    }
  }

  @Override
  public void updateFirstRecordNumber(String dpmName, int number) {
    Session session = getCurrentSession();
    String sql = "update camera set first_record_number=" + number + " where name='" + dpmName + "'";
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public int getFirstRecordNumber(String dpmName) {
    int number = 0;
    Session session = getCurrentSession();
    String sql = "select first_record_number from camera where name='" + dpmName + "'";
    Query q = session.createSQLQuery(sql);
    List tlist = q.list();
    if (!tlist.isEmpty()) {
      number = (Integer) tlist.get(0);
    }
    return number;
  }

  @Override
  public void everyDayInit() {
    Session session = getCurrentSession();
    String sql = "update camera set first_record_number=0, cur_process_number=0;";
    session.createSQLQuery(sql).executeUpdate();
  }
}
