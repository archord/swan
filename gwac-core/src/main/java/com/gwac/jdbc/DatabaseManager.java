package com.gwac.jdbc;

import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.sql.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//数据库管理类，封装基本的数据库操作集合
public class DatabaseManager {

  private static final Log log = LogFactory.getLog(DatabaseManager.class);
  private Statement statement = null;            //数据库静态结果集
  private PreparedStatement pStatement = null;   //数据库动态结果集
  private Connection con = null;                 //数据库连接对象

  public DatabaseManager() {
    this.getConnection();
  }

  public ResultSet doSelect(String sql) {//执行sql语句，主要针对数据库选择操作，并对sql语句进行校验

    if (sql == null || sql.trim().equals("")) {  //校验sql语句的合法性
      log.debug("sql语句为空！");
      return null;
    }
    //log.debug("sql语句为：" + sql);

    ResultSet rs = null;
    //getConnection(); //获得数据库连接
    try {
      statement = con.createStatement();//获得状态集对象
      rs = statement.executeQuery(sql);//执行sql语句
    } catch (SQLException e) {
      rs = null;
      log.debug("执行sql语句出错\nsql=" + sql);
      e.printStackTrace();
    }
    return rs;
  }

  public void doExecute(String sql) {//执行sql语句，主要针对增加，修改，删除操作，使用静态状态集对象，并对sql语句进行校验

    if (sql == null || sql.trim().equals("")) {
      log.debug("sql语句为空！");
    }
        //log.debug("sql语句为：" + sql);
    //getConnection();
    try {
      statement = con.createStatement();    //获得静态状态集对象
      statement.execute(sql);
    } catch (SQLException e) {
      log.debug("执行sql语句出错");
      e.printStackTrace();
    }
  }

  public ResultSet doExecute(String sql, Object[] objs) {//执行sql语句，主要针对增加，修改，删除操作，使用动态状态集对象，并对sql语句进行校验

    if (sql == null || sql.trim().equals("")) {
      log.debug("sql语句为空！");
      return null;
    }
        //log.debug("sql语句为：" + sql);
    //getConnection();
    ResultSet rs = null;
    try {
      pStatement = con.prepareStatement(sql, //获得动态状态集对象
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      if (objs == null) {
        objs = new Object[0];
      }
      for (int i = 0; i < objs.length; i++) { //对动态状态集对象中的参数进行赋值，参数位置取决于sql语句中的“？”的位置
        pStatement.setObject(i + 1, objs[i]);
      }
      pStatement.execute();
      rs = pStatement.getResultSet();
    } catch (SQLException e) {
      rs = null;
      log.debug("执行sql语句出错:" + sql);
      e.printStackTrace();
    }
    return rs;
  }

  public ResultSet doExecuteWithClob(String sql, Object[] objs, int[] flag) {//执行sql语句，主要针对增加，修改，删除操作，使用动态状态集对象，并对sql语句进行校验

    if (sql == null || sql.trim().equals("")) {
      log.debug("sql语句为空！");
      return null;
    }
        //log.debug("sql语句为：" + sql);
    //getConnection();
    ResultSet rs = null;
    try {
      pStatement = con.prepareStatement(sql, //获得动态状态集对象
              ResultSet.TYPE_SCROLL_INSENSITIVE,
              ResultSet.CONCUR_READ_ONLY);
      if (objs == null) {
        objs = new Object[0];
      }
      for (int i = 0; i < objs.length; i++) { //对动态状态集对象中的参数进行赋值，参数位置取决于sql语句中的“？”的位置
        if (flag[i] == 1) {
          Reader clobReader = new StringReader((String) objs[i]); // 将 text转成流形式
          long len = ((String) objs[i]).length();
          //log.debug("len="+len);
          pStatement.setCharacterStream(i + 1, clobReader, (int) len);// 替换sql语句中的？

        } else {
          pStatement.setObject(i + 1, objs[i]);
        }
      }
      pStatement.execute();
      rs = pStatement.getResultSet();
    } catch (SQLException e) {
      rs = null;
      log.debug("执行sql语句出错:" + sql);
      e.printStackTrace();
    }
    return rs;
  }

  public int getRowCount(String tableName, String condition) {  //获取制定表的数据的行数
    ResultSet rs;
    String sql = "select count(*) from " + tableName;
    if (condition != null) {
      sql += " where " + condition;
    }
    int rowCount = 0;
    rs = doSelect(sql);
    try {
      if (rs.next()) {
        rowCount = rs.getInt(1);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return rowCount;
  }

  protected void getConnection() {    //获得数据库连接
    if (con == null) {
      con = ConnectionFactory.getConnection();
    }
  }

  public void close() {  //关闭数据库连接
    try {
      if (statement != null) {
        statement.close();
      }
      if (pStatement != null) {
        pStatement.close();
      }
      if (con != null) {
        con.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
