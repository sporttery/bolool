package com.bolool.model;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Generated;

public class TDanchangCalcinfo {
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source field: t_danchang_calcinfo.id")
	private String id;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source field: t_danchang_calcinfo.info")
	private String info;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source field: t_danchang_calcinfo.issue")
	private String issue;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source field: t_danchang_calcinfo.min_odds")
	private BigDecimal minOdds;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source field: t_danchang_calcinfo.status")
	private String status;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source field: t_danchang_calcinfo.count")
	private Integer count;
	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source field: t_danchang_calcinfo.total_count")
	private Integer totalCount;

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source field: t_danchang_calcinfo.id")
	public String getId() {
		return id;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source field: t_danchang_calcinfo.id")
	public void setId(String id) {
		this.id = id;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source field: t_danchang_calcinfo.info")
	public String getInfo() {
		return info;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source field: t_danchang_calcinfo.info")
	public void setInfo(String info) {
		this.info = info;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source field: t_danchang_calcinfo.issue")
	public String getIssue() {
		return issue;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source field: t_danchang_calcinfo.issue")
	public void setIssue(String issue) {
		this.issue = issue;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source field: t_danchang_calcinfo.min_odds")
	public BigDecimal getMinOdds() {
		return minOdds;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source field: t_danchang_calcinfo.min_odds")
	public void setMinOdds(BigDecimal minOdds) {
		this.minOdds = minOdds;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source field: t_danchang_calcinfo.status")
	public String getStatus() {
		return status;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source field: t_danchang_calcinfo.status")
	public void setStatus(String status) {
		this.status = status;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source field: t_danchang_calcinfo.count")
	public Integer getCount() {
		return count;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source field: t_danchang_calcinfo.count")
	public void setCount(Integer count) {
		this.count = count;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source field: t_danchang_calcinfo.total_count")
	public Integer getTotalCount() {
		return totalCount;
	}

	@Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2021-07-01T17:48:34.21+08:00", comments = "Source field: t_danchang_calcinfo.total_count")
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public TDanchangCalcinfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TDanchangCalcinfo(String info, String issue, BigDecimal minOdds, String status, Integer totalCount,
			List<TDanchangMatch> matchList) {
		super();
		this.info = info;
		this.issue = issue;
		this.minOdds = minOdds;
		this.status = status;
		this.matchList = matchList;
		if (matchList != null) {
			this.count = matchList.size();
		} else {
			this.count = 0;
		}
		this.totalCount = totalCount;
	}

	public List<TDanchangMatch> getMatchList() {
		return matchList;
	}

	public void setMatchList(List<TDanchangMatch> matchList) {
		this.matchList = matchList;
	}

	private List<TDanchangMatch> matchList;

}