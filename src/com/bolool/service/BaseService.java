package com.bolool.service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public abstract class BaseService<T> {
	protected String mybatisConfigPath = "mybatis-config.xml";
	protected SqlSessionFactory sqlSessionFactory = null;

	protected SqlSession session = null;

	protected Connection conn = null;

	protected boolean autoCommit = false;

	protected  BaseService() {
		InputStream inputStream;
		try {
			inputStream = Resources.getResourceAsStream(mybatisConfigPath);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SqlSession getSession() {
		if (session == null) {
			session = sqlSessionFactory.openSession(autoCommit);
		}
		return session;
	}

	public T getMapper() {
		Type[] ts = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
		Class<T> tClass = (Class<T>) ts[0];
		return getSession().getMapper(tClass);
	}

	public void commit() {
		getSession().commit();
	}
	
	public void rollback() {
		getSession().rollback();
	}

	public void closeSession() {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		session.close();
	}

	public Connection getConnection() {
		if (conn == null) {
			conn = getSession().getConnection();
		}
		return conn;
	}

}
