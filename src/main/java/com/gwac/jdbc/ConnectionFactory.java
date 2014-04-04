package com.gwac.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

//数据库连接类构造工厂，获得数据库的连接
public class ConnectionFactory {

	private static String dbName;                                       //数据库用户名
	private static String dbPassword;                                   //数据库用户密码
	private static String dbUrl;                                        //数据库连接地址
	private static String dbDriver;                                     //数据库驱动名
	private static String configFileName = "database.properties";       //存放数据库配置的属性文件名

	static {
		try {
			Properties props = new Properties();  //新建一个属性集对象

			// 得到当前类的类加载器,以流的方式读取配置文件
			props.load(ConnectionFactory.class.getClassLoader()
					.getResourceAsStream(configFileName));

			dbDriver = props.getProperty("jdbc.driverClassName");   //读取驱动名
			dbUrl = props.getProperty("jdbc.url");         //读取数据库连接地址
			dbName = props.getProperty("jdbc.username");       //读取数据库名
			dbPassword = props.getProperty("jdbc.password");//读取数据库密码

			Class.forName(dbDriver); // 加载驱动程序

		} catch (Exception e) {
			System.out.println("数据库驱动加载失败！");
			e.printStackTrace();
		}

	}

	// 建立数据库连接，返回Connection
	public static Connection getConnection() {
		try {
			Connection con = DriverManager.getConnection(dbUrl, dbName,
					dbPassword);
			return con;
		} catch (Exception e) {
			System.out.println("获取数据库连接失败！");
			e.printStackTrace();
			return null;
		}
	}

}
