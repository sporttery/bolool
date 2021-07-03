package com.bolool.mybatis;

import java.math.BigDecimal;
import java.sql.JDBCType;
import java.util.Date;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;

public final class TDanchangMatchDynamicSqlSupport {

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.925+08:00", comments = "Source Table: t_danchang_match")
	public static final TDanchangMatch TDanchangMatch = new TDanchangMatch();
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.925+08:00", comments = "Source field: t_danchang_match.id")
	public static final SqlColumn<String> id = TDanchangMatch.id;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.925+08:00", comments = "Source field: t_danchang_match.match_id")
	public static final SqlColumn<Integer> matchId = TDanchangMatch.matchId;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.925+08:00", comments = "Source field: t_danchang_match.num")
	public static final SqlColumn<Integer> num = TDanchangMatch.num;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.925+08:00", comments = "Source field: t_danchang_match.issue")
	public static final SqlColumn<String> issue = TDanchangMatch.issue;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.925+08:00", comments = "Source field: t_danchang_match.league_name")
	public static final SqlColumn<String> leagueName = TDanchangMatch.leagueName;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.925+08:00", comments = "Source field: t_danchang_match.rq")
	public static final SqlColumn<String> rq = TDanchangMatch.rq;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.925+08:00", comments = "Source field: t_danchang_match.playtime")
	public static final SqlColumn<Date> playtime = TDanchangMatch.playtime;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.925+08:00", comments = "Source field: t_danchang_match.home")
	public static final SqlColumn<String> home = TDanchangMatch.home;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.926+08:00", comments = "Source field: t_danchang_match.away")
	public static final SqlColumn<String> away = TDanchangMatch.away;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.926+08:00", comments = "Source field: t_danchang_match.match_status")
	public static final SqlColumn<String> matchStatus = TDanchangMatch.matchStatus;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.926+08:00", comments = "Source field: t_danchang_match.halfscore")
	public static final SqlColumn<String> halfscore = TDanchangMatch.halfscore;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.926+08:00", comments = "Source field: t_danchang_match.fullscore")
	public static final SqlColumn<String> fullscore = TDanchangMatch.fullscore;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.926+08:00", comments = "Source field: t_danchang_match.result")
	public static final SqlColumn<String> result = TDanchangMatch.result;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.926+08:00", comments = "Source field: t_danchang_match.sp")
	public static final SqlColumn<BigDecimal> sp = TDanchangMatch.sp;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.926+08:00", comments = "Source field: t_danchang_match.sp1")
	public static final SqlColumn<BigDecimal> sp1 = TDanchangMatch.sp1;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.926+08:00", comments = "Source field: t_danchang_match.sp2")
	public static final SqlColumn<BigDecimal> sp2 = TDanchangMatch.sp2;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.926+08:00", comments = "Source field: t_danchang_match.sp3")
	public static final SqlColumn<BigDecimal> sp3 = TDanchangMatch.sp3;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.926+08:00", comments = "Source field: t_danchang_match.w")
	public static final SqlColumn<BigDecimal> w = TDanchangMatch.w;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.926+08:00", comments = "Source field: t_danchang_match.d")
	public static final SqlColumn<BigDecimal> d = TDanchangMatch.d;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.926+08:00", comments = "Source field: t_danchang_match.l")
	public static final SqlColumn<BigDecimal> l = TDanchangMatch.l;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.926+08:00", comments = "Source field: t_danchang_match.h")
	public static final SqlColumn<BigDecimal> h = TDanchangMatch.h;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.926+08:00", comments = "Source field: t_danchang_match.pan")
	public static final SqlColumn<String> pan = TDanchangMatch.pan;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.926+08:00", comments = "Source field: t_danchang_match.a")
	public static final SqlColumn<BigDecimal> a = TDanchangMatch.a;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.926+08:00", comments = "Source field: t_danchang_match.w1")
	public static final SqlColumn<BigDecimal> w1 = TDanchangMatch.w1;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.926+08:00", comments = "Source field: t_danchang_match.d1")
	public static final SqlColumn<BigDecimal> d1 = TDanchangMatch.d1;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.926+08:00", comments = "Source field: t_danchang_match.l1")
	public static final SqlColumn<BigDecimal> l1 = TDanchangMatch.l1;

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.925+08:00", comments = "Source Table: t_danchang_match")
	public static final class TDanchangMatch extends SqlTable {
		public final SqlColumn<String> id = column("id", JDBCType.VARCHAR);
		public final SqlColumn<Integer> matchId = column("match_id", JDBCType.INTEGER);
		public final SqlColumn<Integer> num = column("num", JDBCType.INTEGER);
		public final SqlColumn<String> issue = column("issue", JDBCType.VARCHAR);
		public final SqlColumn<String> leagueName = column("league_name", JDBCType.VARCHAR);
		public final SqlColumn<String> rq = column("rq", JDBCType.VARCHAR);
		public final SqlColumn<Date> playtime = column("playtime", JDBCType.TIMESTAMP);
		public final SqlColumn<String> home = column("home", JDBCType.VARCHAR);
		public final SqlColumn<String> away = column("away", JDBCType.VARCHAR);
		public final SqlColumn<String> matchStatus = column("match_status", JDBCType.VARCHAR);
		public final SqlColumn<String> halfscore = column("halfscore", JDBCType.VARCHAR);
		public final SqlColumn<String> fullscore = column("fullscore", JDBCType.VARCHAR);
		public final SqlColumn<String> result = column("result", JDBCType.VARCHAR);
		public final SqlColumn<BigDecimal> sp = column("sp", JDBCType.DECIMAL);
		public final SqlColumn<BigDecimal> sp1 = column("sp1", JDBCType.DECIMAL);
		public final SqlColumn<BigDecimal> sp2 = column("sp2", JDBCType.DECIMAL);
		public final SqlColumn<BigDecimal> sp3 = column("sp3", JDBCType.DECIMAL);
		public final SqlColumn<BigDecimal> w = column("w", JDBCType.DECIMAL);
		public final SqlColumn<BigDecimal> d = column("d", JDBCType.DECIMAL);
		public final SqlColumn<BigDecimal> l = column("l", JDBCType.DECIMAL);
		public final SqlColumn<BigDecimal> h = column("h", JDBCType.DECIMAL);
		public final SqlColumn<String> pan = column("pan", JDBCType.VARCHAR);
		public final SqlColumn<BigDecimal> a = column("a", JDBCType.DECIMAL);
		public final SqlColumn<BigDecimal> w1 = column("w1", JDBCType.DECIMAL);
		public final SqlColumn<BigDecimal> d1 = column("d1", JDBCType.DECIMAL);
		public final SqlColumn<BigDecimal> l1 = column("l1", JDBCType.DECIMAL);

		public TDanchangMatch() {
			super("t_danchang_match");
		}
	}
}