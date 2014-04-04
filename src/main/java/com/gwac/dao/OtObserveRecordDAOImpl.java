/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gwac.dao;

import com.gwac.model.OtObserveRecord;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;

/**
 *
 * @author xy
 */
public class OtObserveRecordDAOImpl extends BaseHibernateDaoImpl<OtObserveRecord> implements OtObserveRecordDAO {

  private static final Log log = LogFactory.getLog(OtObserveRecordDAOImpl.class);

  /**
   * 
   * @param fname
   * @param sql "copy testabc from '/home/xy/gwacdata/data.txt' delimiter as ' '"
   */
  public void saveOTCopy2(String fname, final String sql) {

    try {
      Session curSession = getCurrentSession();
      if (curSession == null) {
        log.error("curSession is null!");
      } else {
        curSession.doWork(new Work() {
          @Override
          public void execute(Connection connection) throws SQLException {
            Statement st = connection.createStatement();
            st.execute(sql);
          }
        });
      }
    } catch (HibernateException ex) {
      log.error("COPY数据出错.");
      ex.printStackTrace();
    }
  }

  /**
   * 
   * @param fname
   * @param sql  "COPY testabc FROM STDIN WITH DELIMITER ' '"
   */
  public void saveOTCopy(final String fname, final String sql) {

    Session curSession = getCurrentSession();
    if (curSession == null) {
      log.error("curSession is null!");
    } else {
      curSession.doWork(new Work() {
        @Override
        public void execute(Connection connection) throws SQLException {
          FileReader fr = null;
          try {
            PGConnection pgcon = (PGConnection) connection.getMetaData().getConnection();
            CopyManager cpManager = pgcon.getCopyAPI();
            fr = new FileReader(fname);
            if (fr == null) {
              System.out.println("cannot open file!");
            }
            cpManager.copyIn(sql, fr);
          } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            log.error("文件" + fname + "不存在。");
          } catch (IOException ex) {
            ex.printStackTrace();
            log.error("读文件" + fname + "出错。");
          } finally {
            try {
              if (fr != null) {
                fr.close();
              }
            } catch (IOException ex) {
              ex.printStackTrace();
              log.error("关闭文件" + fname + "出错。");
            }
          }
        }
      });
    }
  }
}
