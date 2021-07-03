package com.bolool.mybatis;

import java.math.BigDecimal;
import java.sql.JDBCType;
import javax.annotation.Generated;
import org.mybatis.dynamic.sql.SqlColumn;
import org.mybatis.dynamic.sql.SqlTable;
import java.util.Date;

public final class TDanchangCalcinfoResultDynamicSqlSupport {

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.934+08:00", comments = "Source Table: t_danchang_calcinfo_result")
	public static final TDanchangCalcinfoResult TDanchangCalcinfoResult = new TDanchangCalcinfoResult();
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.934+08:00", comments = "Source field: t_danchang_calcinfo_result.id")
	public static final SqlColumn<String> id = TDanchangCalcinfoResult.id;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.934+08:00", comments = "Source field: t_danchang_calcinfo_result.issue")
	public static final SqlColumn<String> issue = TDanchangCalcinfoResult.issue;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.934+08:00", comments = "Source field: t_danchang_calcinfo_result.award_type")
	public static final SqlColumn<String> awardType = TDanchangCalcinfoResult.awardType;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.934+08:00", comments = "Source field: t_danchang_calcinfo_result.bd_award")
	public static final SqlColumn<BigDecimal> bdAward = TDanchangCalcinfoResult.bdAward;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.934+08:00", comments = "Source field: t_danchang_calcinfo_result.award_num")
	public static final SqlColumn<Integer> awardNum = TDanchangCalcinfoResult.awardNum;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.934+08:00", comments = "Source field: t_danchang_calcinfo_result.win_odds")
	public static final SqlColumn<BigDecimal> winOdds = TDanchangCalcinfoResult.winOdds;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.934+08:00", comments = "Source field: t_danchang_calcinfo_result.win_bet_money")
	public static final SqlColumn<Integer> winBetMoney = TDanchangCalcinfoResult.winBetMoney;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.935+08:00", comments = "Source field: t_danchang_calcinfo_result.total_bet_money")
	public static final SqlColumn<Integer> totalBetMoney = TDanchangCalcinfoResult.totalBetMoney;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.935+08:00", comments = "Source field: t_danchang_calcinfo_result.award_win_money")
	public static final SqlColumn<BigDecimal> awardWinMoney = TDanchangCalcinfoResult.awardWinMoney;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.935+08:00", comments = "Source field: t_danchang_calcinfo_result.total_win_money")
	public static final SqlColumn<BigDecimal> totalWinMoney = TDanchangCalcinfoResult.totalWinMoney;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.935+08:00", comments = "Source field: t_danchang_calcinfo_result.min_odds_interval")
	public static final SqlColumn<BigDecimal> minOddsInterval = TDanchangCalcinfoResult.minOddsInterval;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.935+08:00", comments = "Source field: t_danchang_calcinfo_result.max_odds_interval")
	public static final SqlColumn<BigDecimal> maxOddsInterval = TDanchangCalcinfoResult.maxOddsInterval;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.935+08:00", comments = "Source field: t_danchang_calcinfo_result.min_odds_interval_race")
	public static final SqlColumn<BigDecimal> minOddsIntervalRace = TDanchangCalcinfoResult.minOddsIntervalRace;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.935+08:00", comments = "Source field: t_danchang_calcinfo_result.max_odds_interval_race")
	public static final SqlColumn<BigDecimal> maxOddsIntervalRace = TDanchangCalcinfoResult.maxOddsIntervalRace;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.935+08:00", comments = "Source field: t_danchang_calcinfo_result.playtime_min")
	public static final SqlColumn<Date> playtimeMin = TDanchangCalcinfoResult.playtimeMin;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.935+08:00", comments = "Source field: t_danchang_calcinfo_result.playtime_max")
	public static final SqlColumn<Date> playtimeMax = TDanchangCalcinfoResult.playtimeMax;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.935+08:00", comments = "Source field: t_danchang_calcinfo_result.info")
	public static final SqlColumn<String> info = TDanchangCalcinfoResult.info;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.935+08:00", comments = "Source field: t_danchang_calcinfo_result.remark")
	public static final SqlColumn<String> remark = TDanchangCalcinfoResult.remark;

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-03T18:40:03.934+08:00", comments = "Source Table: t_danchang_calcinfo_result")
	public static final class TDanchangCalcinfoResult extends SqlTable {
		public final SqlColumn<String> id = column("id", JDBCType.VARCHAR);
		public final SqlColumn<String> issue = column("issue", JDBCType.VARCHAR);
		public final SqlColumn<String> awardType = column("award_type", JDBCType.VARCHAR);
		public final SqlColumn<BigDecimal> bdAward = column("bd_award", JDBCType.DECIMAL);
		public final SqlColumn<Integer> awardNum = column("award_num", JDBCType.INTEGER);
		public final SqlColumn<BigDecimal> winOdds = column("win_odds", JDBCType.DECIMAL);
		public final SqlColumn<Integer> winBetMoney = column("win_bet_money", JDBCType.INTEGER);
		public final SqlColumn<Integer> totalBetMoney = column("total_bet_money", JDBCType.INTEGER);
		public final SqlColumn<BigDecimal> awardWinMoney = column("award_win_money", JDBCType.DECIMAL);
		public final SqlColumn<BigDecimal> totalWinMoney = column("total_win_money", JDBCType.DECIMAL);
		public final SqlColumn<BigDecimal> minOddsInterval = column("min_odds_interval", JDBCType.DECIMAL);
		public final SqlColumn<BigDecimal> maxOddsInterval = column("max_odds_interval", JDBCType.DECIMAL);
		public final SqlColumn<BigDecimal> minOddsIntervalRace = column("min_odds_interval_race", JDBCType.DECIMAL);
		public final SqlColumn<BigDecimal> maxOddsIntervalRace = column("max_odds_interval_race", JDBCType.DECIMAL);
		public final SqlColumn<Date> playtimeMin = column("playtime_min", JDBCType.TIMESTAMP);
		public final SqlColumn<Date> playtimeMax = column("playtime_max", JDBCType.TIMESTAMP);
		public final SqlColumn<String> info = column("info", JDBCType.VARCHAR);
		public final SqlColumn<String> remark = column("remark", JDBCType.VARCHAR);

		public TDanchangCalcinfoResult() {
			super("t_danchang_calcinfo_result");
		}
	}
}