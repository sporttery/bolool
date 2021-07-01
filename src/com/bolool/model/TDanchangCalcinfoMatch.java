package com.bolool.model;

import javax.annotation.Generated;

public class TDanchangCalcinfoMatch {

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source field: t_danchang_calcinfo_match.id")
	private String id;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source field: t_danchang_calcinfo_match.calcinfo_id")
	private String calcinfoId;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source field: t_danchang_calcinfo_match.danchang_id")
	private String danchangId;

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source field: t_danchang_calcinfo_match.id")
	public String getId() {
		return id;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.211+08:00", comments = "Source field: t_danchang_calcinfo_match.id")
	public void setId(String id) {
		this.id = id;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source field: t_danchang_calcinfo_match.calcinfo_id")
	public String getCalcinfoId() {
		return calcinfoId;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source field: t_danchang_calcinfo_match.calcinfo_id")
	public void setCalcinfoId(String calcinfoId) {
		this.calcinfoId = calcinfoId;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source field: t_danchang_calcinfo_match.danchang_id")
	public String getDanchangId() {
		return danchangId;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.212+08:00", comments = "Source field: t_danchang_calcinfo_match.danchang_id")
	public void setDanchangId(String danchangId) {
		this.danchangId = danchangId;
	}
}