package com.bolool.vo;

import com.google.gson.Gson;

public class Bolool {

	// +--------+--------+--------+--------------------------------------------------------------------------------------------+-----------------------------------------------------------------------+----------+----------+---------------------+---------+------------+
	// | id | hscore | ascore | hresult | aresult | hsection | asection | insertTime
	// | version | updateTime |
	// +--------+--------+--------+--------------------------------------------------------------------------------------------+-----------------------------------------------------------------------+----------+----------+---------------------+---------+------------+
	// | 108549 | 57 | 26 | 负负胜平胜胜负负平胜负平平胜平负胜胜胜胜胜胜胜胜胜平胜胜胜负 | 胜胜负平负负负负负胜胜负胜负胜负平负胜负负负胜
	// | 4 | 7 | 2021-01-24 09:01:42 | 0 | NULL |
	// +--------+--------+--------+--------------------------------------------------------------------------------------------+-----------------------------------------------------------------------+----------+----------+---------------------+---------+------------+
	// 1 row in set (0.00 sec)
	private Integer hscore;
	private Integer ascore;
	private Integer hsection;
	private Integer asection;
	private String hresult;
	private String aresult;
	private Integer id;

	public Integer getHscore() {
		return hscore;
	}

	public void setHscore(Integer hscore) {
		this.hscore = hscore;
	}

	public Integer getAscore() {
		return ascore;
	}

	public void setAscore(Integer ascore) {
		this.ascore = ascore;
	}

	public Integer getHsection() {
		return hsection;
	}

	public void setHsection(Integer hsection) {
		this.hsection = hsection;
	}

	public Integer getAsection() {
		return asection;
	}

	public void setAsection(Integer asection) {
		this.asection = asection;
	}

	public String getHresult() {
		return hresult;
	}

	public void setHresult(String hresult) {
		this.hresult = hresult;
	}

	public String getAresult() {
		return aresult;
	}

	public void setAresult(String aresult) {
		this.aresult = aresult;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	private String hresult3;
	private String aresult3;

	public String getHresult3() {
		if (hresult3 == null) {
			hresult3 = hresult == null || hresult.length() < 3 ? null : hresult.substring(0, 3);
		}
		return hresult3;
	}

	public String getAresult3() {
		if (aresult3 == null) {
			aresult3 = aresult == null || aresult.length() < 3 ? null : aresult.substring(0, 3);
		}
		return aresult3;
	}

	private Integer hscore3;
	private Integer ascore3;

	public Integer getHscore3() {
		if (hscore3 == null) {
			String hresult = getHresult3();
			if (hresult != null) {
				int hscore = 0;
				char[] arr = hresult.toCharArray();
				for (int i = 0; i < arr.length; i++) {
					hscore += arr[i] == '胜' ? 3 : arr[i] == '平' ? 1 : 0;
				}
				hscore3 = hscore;
			}
		}
		return hscore3;
	}

	public Integer getAscore3() {
		if (ascore3 == null) {
			String result = getAresult3();
			if (result != null) {
				int score = 0;
				char[] arr = result.toCharArray();
				for (int i = 0; i < arr.length; i++) {
					score += arr[i] == '胜' ? 3 : arr[i] == '平' ? 1 : 0;
				}
				ascore3 = score;
			}
		}
		return ascore3;
	}

	private String hstrong3;
	private String astrong3;

	public String getHstrong3() {
		if (hstrong3 == null) {
			Integer hscore3 = getHscore3();
			Integer ascore3 = getAscore3();
			hstrong3 = hscore3 == null || ascore3 == null ? null : hscore3 > ascore3 ? "强" : hscore3 < ascore3 ? "弱" : "平";
		}
		return hstrong3;
	}

	public String astrong3() {
		if (astrong3 == null) {
			if (hstrong3 == null) {
				hstrong3 = getHstrong3();
			}
			astrong3 = hstrong3 == null ? null : "平".equals(hstrong3) ? "平" : "强".equals(hstrong3) ? "弱" : "强";
		}
		return astrong3;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
