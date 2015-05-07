/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.ImageStatusParameter;
import com.gwac.model.UploadFileUnstore;
import com.gwac.util.CommonFunction;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author xy
 */
public class ImageStatusParameterDaoImpl extends BaseHibernateDaoImpl<ImageStatusParameter> implements ImageStatusParameterDao {

  private static final Log log = LogFactory.getLog(ImageStatusParameterDaoImpl.class);

  @Override
  public void moveDataToHisTable() {

    Session session = getCurrentSession();
    String sql = "WITH moved_rows AS ( DELETE FROM image_status_parameter RETURNING * ) INSERT INTO image_status_parameter_his SELECT * FROM moved_rows;";
    session.createSQLQuery(sql).executeUpdate();
  }

  @Override
  public List<ImageStatusParameter> getCurAllParm() {
    Session session = getCurrentSession();
    String sql = "select * from image_status_parameter";
    Query q = session.createSQLQuery(sql).addEntity(ImageStatusParameter.class);
    return q.list();
  }

  @Override
  public List<ImageStatusParameter> getLatestParmOfAllDpm() {
    Session session = getCurrentSession();
    String sql = "WITH sr AS ( select dpm_id, max(prc_num) maxnum from image_status_parameter group by dpm_id ) "
            + "SELECT isp.* FROM image_status_parameter isp "
            + "inner join sr on isp.dpm_id=sr.dpm_id and isp.prc_num=sr.maxnum "
            + "order by isp.dpm_id;";
    Query q = session.createSQLQuery(sql).addEntity(ImageStatusParameter.class);
    return q.list();
  }

  @Override
  public List<ImageStatusParameter> getImgStatusParmByDate(String dateStr) {
    Session session = getCurrentSession();
    String sql = "select isp.* from image_status_parameter isp where isp.is_match=2 and isp.date_str='" + dateStr + "'";
    Query q = session.createSQLQuery(sql).addEntity(ImageStatusParameter.class);
    return q.list();
  }

  @Override
  public List<ImageStatusParameter> getImgStatusParmByDpmId(String dpmId) {
    Session session = getCurrentSession();
    String sql = "select * from image_status_parameter where dpm_id=" + dpmId + ";";
    Query q = session.createSQLQuery(sql).addEntity(ImageStatusParameter.class);
    if (!q.list().isEmpty()) {
      return q.list();
    } else {
      return null;
    }
  }

}
