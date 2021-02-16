package com.bolool.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

/**
 * Description: 数据库连接池类
 * 
 * @filename DataSourceFactory.java
 * @date 2013年8月21日 19:47:21
 * @author Herman.Xiong
 * @version 1.0 Copyright (c) 2013 Company,Inc. All Rights Reserved.
 */
public class DataSourceFactory {
	private static Logger log = Logger.getLogger(DataSourceFactory.class);
	private static BasicDataSource bs = null;

//	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String driver = "com.mysql.cj.jdbc.Driver";
	static {
		PropKit.use("config.properties");
	}
	private static final String url = PropKit.get("jdbcUrl");
	private static final String uid = PropKit.get("user");
	private static final String pwd = PropKit.get("password");
	
	
	public static void init() {
		if(bs==null) {
			log.info("数据库连接信息：[driver:" + driver + ",url:" + url + ",userName:" + uid + ",password:" + pwd + "]");
			bs = new BasicDataSource();
			bs.setDriverClassName(driver);
			bs.setUrl(url);
			bs.setUsername(uid);
			bs.setPassword(pwd);
			bs.setMaxActive(200);// 设置最大并发数
			bs.setInitialSize(30);// 数据库初始化时，创建的连接个数
			bs.setMinIdle(50);// 最小空闲连接数
			bs.setMaxIdle(200);// 数据库最大连接数
			bs.setMaxWait(1000);
			bs.setMinEvictableIdleTimeMillis(60 * 1000);// 空闲连接60秒中后释放
			bs.setTimeBetweenEvictionRunsMillis(5 * 60 * 1000);// 5分钟检测一次是否有死掉的线程
			bs.setTestOnBorrow(true);
		}
	}

	/**
	 * 创建数据源
	 * 
	 * @return
	 */
	public static BasicDataSource getDataSource() throws Exception {
		if (bs == null) {
			init();
		}
		return bs;
	}

	/**
	 * 释放数据源
	 */
	public static void shutDownDataSource() throws Exception {
		if (bs != null) {
			bs.close();
		}
	}

	/**
	 * 获取数据库连接
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		Connection con = null;
		try {
			if (bs != null) {
				con = bs.getConnection();
			} else {
				con = getDataSource().getConnection();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return con;
	}

	/**
	 * 关闭连接
	 */
	public static void closeCon(ResultSet rs, PreparedStatement ps, Connection con) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				log.error("关闭结果集ResultSet异常！" + e.getMessage(), e);
			}
		}
		if (ps != null) {
			try {
				ps.close();
			} catch (Exception e) {
				log.error("预编译SQL语句对象PreparedStatement关闭异常！" + e.getMessage(), e);
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (Exception e) {
				log.error("关闭连接对象Connection异常！" + e.getMessage(), e);
			}
		}
	}

	/**
	 * 关闭连接
	 */
	public static void closeCon(ResultSet rs, Statement stmp, Connection con) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				log.error("关闭结果集ResultSet异常！" + e.getMessage(), e);
			}
		}
		if (stmp != null) {
			try {
				stmp.close();
			} catch (Exception e) {
				log.error("预编译SQL语句对象PreparedStatement关闭异常！" + e.getMessage(), e);
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (Exception e) {
				log.error("关闭连接对象Connection异常！" + e.getMessage(), e);
			}
		}
	}
}