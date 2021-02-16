package com.bolool.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DBHelper {
	private static final Log log = LogFactory.getLog(DBHelper.class);

	/**
	 * 
	 * @param param
	 * @return param.replaceAll("['-]", " ");
	 */
	public static String getSafeSqlParam(String param) {
		return param.replaceAll("['-]", " ");
	}

	public static Connection getConnection() {
		return DataSourceFactory.getConnection();
	}

	public static void closeDB(Connection conn, Statement stmt, ResultSet rs) {
		DataSourceFactory.closeCon(rs, stmt, conn);
	}

	public static void closeDB(Connection conn, PreparedStatement ps, ResultSet rs) {
		DataSourceFactory.closeCon(rs, ps, conn);
	}

	public static int insertOrUpdateC(String sql, Connection conn) {
		Statement stmt = null;
		try {
			if (conn == null) {
				conn = getConnection();
			}
			stmt = conn.createStatement();
			int count = stmt.executeUpdate(sql);
			return count;
		} catch (SQLException e) {
			log.error(e.getMessage() + " sql:" + sql);
			e.printStackTrace();
		} finally {
			closeDB(conn, stmt, null);
		}
		return 0;
	}

	public static int insertOrUpdateC(String sql) {
		return insertOrUpdateC(sql, null);
	}

	public static boolean insertOrUpdate(String sql, Connection conn) {
		Statement stmt = null;
		boolean result = false;
		try {
			if (conn == null) {
				conn = getConnection();
			}
			stmt = conn.createStatement();
			int count = stmt.executeUpdate(sql);
			if (count > 0)
				result = true;
		} catch (SQLException e) {
			log.error(e.getMessage() + " sql:" + sql);
			e.printStackTrace();
		} finally {
			closeDB(conn, stmt, null);
		}
		return result;
	}

	public static boolean insertOrUpdate(String sql) {
		return insertOrUpdate(sql, null);
	}

	public static boolean insertOrUpdateMany(List<String> sqls, Connection conn) {
		boolean result = false;
		Statement stmt = null;
		try {
			if (conn == null) {
				conn = getConnection();
			}
			conn.setAutoCommit(false);
			stmt = conn.createStatement();
			for (int i = 0; i < sqls.size(); i++) {
				stmt.addBatch(sqls.get(i));
			}
			int[] count = stmt.executeBatch();
			conn.commit();
			if (count.length > 0)
				result = true;
		} catch (SQLException e) {
			log.error(e.getMessage() + " sql:" + sqls);
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			closeDB(conn, stmt, null);
		}
		return result;
	}

	public static HashMap<String, String> selectSql(String sql, String[] columns, Connection conn) {
		HashMap<String, String> rtnmap = new HashMap<String, String>();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			if (conn == null) {
				conn = getConnection();
			}
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				for (String column : columns) {
					rtnmap.put(column, rs.getString(column));
				}
			}
		} catch (SQLException e) {
			log.error(e.getMessage() + " sql:" + sql);
			e.printStackTrace();
		} finally {
			closeDB(conn, stmt, rs);
		}

		return rtnmap;
	}

	public static List<HashMap<String, String>> selectListSql(String sql, String[] columns, Connection conn) {
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			if (conn == null) {
				conn = getConnection();
			}
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				HashMap<String, String> map = new HashMap<String, String>();
				for (String column : columns) {
					map.put(column, rs.getString(column));
				}
				list.add(map);
			}
		} catch (SQLException e) {
			log.error(e.getMessage() + " sql:" + sql);
			e.printStackTrace();
		} finally {
			closeDB(conn, stmt, rs);
		}

		return list;
	}

	public static List<String> selectListSql(String sql, Connection conn) {
		List<String> list = new ArrayList<String>();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			if (conn == null) {
				conn = getConnection();
			}
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				list.add(rs.getString(1));
			}
		} catch (SQLException e) {
			log.error(e.getMessage() + " sql:" + sql);
			e.printStackTrace();
		} finally {
			closeDB(conn, stmt, rs);
		}

		return list;
	}

	public static HashMap<String, String> selectSql(String sql, String[] columns) {
		return selectSql(sql, columns, null);
	}

	public static List<HashMap<String, String>> selectListSql(String sql, String[] columns) {
		return selectListSql(sql, columns, null);
	}

	public static List<String> selectListSql(String sql) {
		List<String> list = new ArrayList<String>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				list.add(rs.getString(1));
			}
		} catch (SQLException e) {
			log.error(e.getMessage() + " sql:" + sql);
			e.printStackTrace();
		} finally {
			closeDB(conn, stmt, rs);
		}

		return list;
	}

	public static int insertOrUpdatePre(String sql, Object... os) {
		PreparedStatement ps = null;
		Connection conn = null;
		try {
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			int parameterIndex = 1;
			for (Object o : os) {
				if (o instanceof String) {
					ps.setString(parameterIndex++, (String) o);
				} else if (o instanceof Integer) {
					ps.setInt(parameterIndex++, (Integer) o);
				} else if (o instanceof Float) {
					ps.setFloat(parameterIndex++, (Float) o);
				} else if (o instanceof java.sql.Date) {
					ps.setDate(parameterIndex++, (java.sql.Date) o);
				}else if(o instanceof Date) {
					ps.setDate(parameterIndex++, new java.sql.Date(((Date)o).getTime()));
				}
			}
			int count = ps.executeUpdate();
			return count;
		} catch (SQLException e) {
			log.error(e.getMessage() + " sql:" + sql);
			e.printStackTrace();
		} finally {
			closeDB(conn, ps, null);
		}
		return 0;
	}

	public static void main(String[] args) {
		System.out.println("1111");
	}
}
