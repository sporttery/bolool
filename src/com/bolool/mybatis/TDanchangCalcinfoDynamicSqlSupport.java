package com.bolool.mybatis;

import java.math.BigDecimal;
import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class TDanchangCalcinfoDynamicSqlSupport {

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.929+08:00", comments = "Source Table: t_danchang_calcinfo")
	public static final TDanchangCalcinfo TDanchangCalcinfo = new TDanchangCalcinfo();
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.929+08:00", comments = "Source field: t_danchang_calcinfo.id")
	public static final SqlColumn<String> id = TDanchangCalcinfo.id;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.929+08:00", comments = "Source field: t_danchang_calcinfo.info")
	public static final SqlColumn<String> info = TDanchangCalcinfo.info;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.929+08:00", comments = "Source field: t_danchang_calcinfo.issue")
	public static final SqlColumn<String> issue = TDanchangCalcinfo.issue;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.929+08:00", comments = "Source field: t_danchang_calcinfo.min_odds")
	public static final SqlColumn<BigDecimal> minOdds = TDanchangCalcinfo.minOdds;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.929+08:00", comments = "Source field: t_danchang_calcinfo.status")
	public static final SqlColumn<String> status = TDanchangCalcinfo.status;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.929+08:00", comments = "Source field: t_danchang_calcinfo.count")
	public static final SqlColumn<Integer> count = TDanchangCalcinfo.count;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.929+08:00", comments = "Source field: t_danchang_calcinfo.total_count")
	public static final SqlColumn<Integer> totalCount = TDanchangCalcinfo.totalCount;

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.929+08:00", comments = "Source Table: t_danchang_calcinfo")
	public static final class TDanchangCalcinfo extends SqlTable {
		public final SqlColumn<String> id = column("id", JDBCType.VARCHAR);
		public final SqlColumn<String> info = column("info", JDBCType.VARCHAR);
		public final SqlColumn<String> issue = column("issue", JDBCType.VARCHAR);
		public final SqlColumn<BigDecimal> minOdds = column("min_odds", JDBCType.DECIMAL);
		public final SqlColumn<String> status = column("status", JDBCType.VARCHAR);
		public final SqlColumn<Integer> count = column("count", JDBCType.INTEGER);
		public final SqlColumn<Integer> totalCount = column("total_count", JDBCType.INTEGER);

		public TDanchangCalcinfo() {
			super("t_danchang_calcinfo");
		}
	}
}