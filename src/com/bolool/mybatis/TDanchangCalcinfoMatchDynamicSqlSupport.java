package com.bolool.mybatis;

import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class TDanchangCalcinfoMatchDynamicSqlSupport {

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source Table: t_danchang_calcinfo_match")
	public static final TDanchangCalcinfoMatch TDanchangCalcinfoMatch = new TDanchangCalcinfoMatch();
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source field: t_danchang_calcinfo_match.id")
	public static final SqlColumn<String> id = TDanchangCalcinfoMatch.id;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source field: t_danchang_calcinfo_match.calcinfo_id")
	public static final SqlColumn<String> calcinfoId = TDanchangCalcinfoMatch.calcinfoId;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source field: t_danchang_calcinfo_match.danchang_id")
	public static final SqlColumn<String> danchangId = TDanchangCalcinfoMatch.danchangId;

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source Table: t_danchang_calcinfo_match")
	public static final class TDanchangCalcinfoMatch extends SqlTable {
		public final SqlColumn<String> id = column("id", JDBCType.VARCHAR);
		public final SqlColumn<String> calcinfoId = column("calcinfo_id", JDBCType.VARCHAR);
		public final SqlColumn<String> danchangId = column("danchang_id", JDBCType.VARCHAR);

		public TDanchangCalcinfoMatch() {
			super("t_danchang_calcinfo_match");
		}
	}
}