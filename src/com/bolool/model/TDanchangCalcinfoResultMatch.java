package com.bolool.model;

import java.math.BigDecimal;
import javax.annotation.Generated;

public class TDanchangCalcinfoResultMatch {

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source field: t_danchang_calcinfo_result_match.id")
	private String id;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source field: t_danchang_calcinfo_result_match.calcinfo_result_id")
	private String calcinfoResultId;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source field: t_danchang_calcinfo_result_match.danchang_id")
	private String danchangId;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source field: t_danchang_calcinfo_result_match.bet_odds")
	private BigDecimal betOdds;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source field: t_danchang_calcinfo_result_match.bet_money")
	private Integer betMoney;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source field: t_danchang_calcinfo_result_match.award_status")
	private String awardStatus;

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source field: t_danchang_calcinfo_result_match.id")
	public String getId() {
		return id;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source field: t_danchang_calcinfo_result_match.id")
	public void setId(String id) {
		this.id = id;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source field: t_danchang_calcinfo_result_match.calcinfo_result_id")
	public String getCalcinfoResultId() {
		return calcinfoResultId;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source field: t_danchang_calcinfo_result_match.calcinfo_result_id")
	public void setCalcinfoResultId(String calcinfoResultId) {
		this.calcinfoResultId = calcinfoResultId;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source field: t_danchang_calcinfo_result_match.danchang_id")
	public String getDanchangId() {
		return danchangId;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source field: t_danchang_calcinfo_result_match.danchang_id")
	public void setDanchangId(String danchangId) {
		this.danchangId = danchangId;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source field: t_danchang_calcinfo_result_match.bet_odds")
	public BigDecimal getBetOdds() {
		return betOdds;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source field: t_danchang_calcinfo_result_match.bet_odds")
	public void setBetOdds(BigDecimal betOdds) {
		this.betOdds = betOdds;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source field: t_danchang_calcinfo_result_match.bet_money")
	public Integer getBetMoney() {
		return betMoney;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source field: t_danchang_calcinfo_result_match.bet_money")
	public void setBetMoney(Integer betMoney) {
		this.betMoney = betMoney;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source field: t_danchang_calcinfo_result_match.award_status")
	public String getAwardStatus() {
		return awardStatus;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.216+08:00", comments = "Source field: t_danchang_calcinfo_result_match.award_status")
	public void setAwardStatus(String awardStatus) {
		this.awardStatus = awardStatus;
	}
}