/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.ConfigFile;
import java.math.BigInteger;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xy
 */
@Repository
public class ConfigFileDaoImpl extends BaseHibernateDaoImpl<ConfigFile> implements ConfigFileDao {
  
  public void moveDataToHisTable() {

    Session session = getCurrentSession();
    String sql = "WITH moved_rows AS ( DELETE FROM config_file RETURNING * ) INSERT INTO config_file_his SELECT * FROM moved_rows;";
    session.createSQLQuery(sql).executeUpdate();
  }

  public Boolean exist(ConfigFile obj) {
    Boolean flag = false;
    Session session = getCurrentSession();
    String sql = "select cf_id from config_file where file_name='" + obj.getFileName() + "' ";
    Query q = session.createSQLQuery(sql);
    if (!q.list().isEmpty()) {
      BigInteger cfId = (BigInteger) q.list().get(0);
      obj.setCfId(cfId.longValue());
      flag = true;
    }
    return flag;
  }

  @Override
  public List<ConfigFile> getTopNUnSync(int topn) {

    String sql = "with updated_rows as"
            + "(with tmp as (select min(cf_id) min_id from config_file_his where is_sync=false) "
            + "update config_file_his set is_sync=true "
            + "where cf_id<(select min_id+" + topn + " from tmp) and cf_id>=(select min_id from tmp) returning *) "
            + "select * from updated_rows;";

    Session session = getCurrentSession();
    Query q = session.createSQLQuery(sql).addEntity(ConfigFile.class);
    return q.list();
  }
}
