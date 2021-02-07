package com.bolool.vo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.bolool.util.Const;
import com.google.gson.GsonBuilder;

public class UserOrder {

	public UserOrder() {}
	public UserOrder(String id,String money,String earningsYield,String odds,String isWin,String inserttime) {
		this.id = Integer.valueOf(id);
		this.money = Integer.valueOf(money);
		this.earningsYield = Integer.valueOf(earningsYield);
		this.odds = Float.valueOf(odds);
		this.isWin = Integer.valueOf(isWin);
		try {
			this.inserttime = new SimpleDateFormat(Const.ymd_hms).parse(inserttime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private Integer id;
	private Integer money;
	private Integer earningsYield;
	private Float odds;
	private Integer isWin;
	private Date inserttime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMoney() {
		return money;
	}
	public void setMoney(Integer money) {
		this.money = money;
	}
	public Integer getEarningsYield() {
		return earningsYield;
	}
	public void setEarningsYield(Integer earningsYield) {
		this.earningsYield = earningsYield;
	}
	public Float getOdds() {
		return odds;
	}
	public void setOdds(Float odds) {
		this.odds = odds;
	}
	public Integer getIsWin() {
		return isWin;
	}
	public void setIsWin(Integer isWin) {
		this.isWin = isWin;
	}
	public Date getInserttime() {
		return inserttime;
	}
	public void setInserttime(Date inserttime) {
		this.inserttime = inserttime;
	}
	
	
	@Override
	public String toString() {
		return new GsonBuilder().setDateFormat(Const.ymd_hms).create().toJson(this);
	}
}
