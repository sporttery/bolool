package com.bolool.mybatis;

import java.math.BigDecimal;
import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class TDanchangCalcinfoResultMatchDynamicSqlSupport {

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source Table: t_danchang_calcinfo_result_match")
	public static final TDanchangCalcinfoResultMatch TDanchangCalcinfoResultMatch = new TDanchangCalcinfoResultMatch();
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source field: t_danchang_calcinfo_result_match.id")
	public static final SqlColumn<String> id = TDanchangCalcinfoResultMatch.id;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source field: t_danchang_calcinfo_result_match.calcinfo_result_id")
	public static final SqlColumn<String> calcinfoResultId = TDanchangCalcinfoResultMatch.calcinfoResultId;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source field: t_danchang_calcinfo_result_match.danchang_id")
	public static final SqlColumn<String> danchangId = TDanchangCalcinfoResultMatch.danchangId;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source field: t_danchang_calcinfo_result_match.bet_odds")
	public static final SqlColumn<BigDecimal> betOdds = TDanchangCalcinfoResultMatch.betOdds;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source field: t_danchang_calcinfo_result_match.bet_money")
	public static final SqlColumn<Integer> betMoney = TDanchangCalcinfoResultMatch.betMoney;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source field: t_danchang_calcinfo_result_match.award_status")
	public static final SqlColumn<String> awardStatus = TDanchangCalcinfoResultMatch.awardStatus;

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source Table: t_danchang_calcinfo_result_match")
	public static final class TDanchangCalcinfoResultMatch extends SqlTable {
		public final SqlColumn<String> id = column("id", JDBCType.VARCHAR);
		public final SqlColumn<String> calcinfoResultId = column("calcinfo_result_id", JDBCType.VARCHAR);
		public final SqlColumn<String> danchangId = column("danchang_id", JDBCType.VARCHAR);
		public final SqlColumn<BigDecimal> betOdds = column("bet_odds", JDBCType.DECIMAL);
		public final SqlColumn<Integer> betMoney = column("bet_money", JDBCType.INTEGER);
		public final SqlColumn<String> awardStatus = column("award_status", JDBCType.VARCHAR);

		public TDanchangCalcinfoResultMatch() {
			super("t_danchang_calcinfo_result_match");
		}
	}
}