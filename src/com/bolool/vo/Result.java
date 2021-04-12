package com.bolool.vo;

import java.util.HashMap;

import com.google.gson.Gson;

public class Result {
	
	public Result() {
		// TODO Auto-generated constructor stub
	}
	
	public Result(int code,String msg){
		this.code = code;
		this.msg = msg;
	}
	
	private int code;
	private String msg;
	private HashMap<String,Object> data;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public HashMap<String, Object> getData() {
		return data;
	}
	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
	
}
