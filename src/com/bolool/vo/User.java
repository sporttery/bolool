package com.bolool.vo;

import java.util.Date;

import com.bolool.util.Const;
import com.google.gson.GsonBuilder;

public class User {
	private Integer id;
	private String userName;
	private String userPwd;
	private String userMobile;
	private Integer userStatus;
	private Integer commission;
	private Integer earningsYield;
	private Integer userMoney;
	private Integer firstMoney;
	private Date leidaExpire;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public String getUserMobile() {
		return userMobile;
	}
	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}
	public Integer getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(Integer userStatus) {
		this.userStatus = userStatus;
	}
	public Integer getCommission() {
		return commission;
	}
	public void setCommission(Integer commission) {
		this.commission = commission;
	}
	public Integer getEarningsYield() {
		return earningsYield;
	}
	public void setEarningsYield(Integer earningsYield) {
		this.earningsYield = earningsYield;
	}
	public Date getLeidaExpire() {
		return leidaExpire;
	}
	public void setLeidaExpire(Date leidaExpire) {
		this.leidaExpire = leidaExpire;
	}
	
	public Integer getUserMoney() {
		return userMoney;
	}
	public void setUserMoney(Integer userMoney) {
		this.userMoney = userMoney;
	}
	public Integer getFirstMoney() {
		return firstMoney;
	}
	public void setFirstMoney(Integer firstMoney) {
		this.firstMoney = firstMoney;
	}
	@Override
	public String toString() {
		return new GsonBuilder().setDateFormat(Const.YMD_HMS).create().toJson(this);
	}
}
