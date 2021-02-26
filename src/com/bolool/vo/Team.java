package com.bolool.vo;

import com.bolool.util.Const;

public class Team {
	private String name;//":"安提瓜和巴布达","
	private String parentName;//":"友谊赛"
	private Integer id;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return Const.GSON_DEFAULT.toJson(this);
	}
}
