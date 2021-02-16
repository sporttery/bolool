package com.bolool.vo;

import com.google.gson.Gson;

public class Bolool {

	//{"hscore":40,"hsection":6,"hresult":"负负胜负负负平负平负胜胜负平平平胜负平平胜平胜负平平胜胜平胜负平平","hstrong":"弱","ascore":43,"asection":6,
	//"aresult":"负平胜负平负胜平平平负负胜负胜平平负胜负负平胜平胜平平负胜胜平胜平","astrong":"强","topN":33,"friendly":0,"matchId":1114283}
	private int hscore;
	private int ascore;
	private int hsection;
	private int asection;
	private String hresult;
	private String aresult;
	private String hstrong;
	private String  astrong;
	private int topN;
	private int friendly;
	private int matchId;
	
	
	
	public int getHscore() {
		return hscore;
	}
	public void setHscore(int hscore) {
		this.hscore = hscore;
	}
	public int getAscore() {
		return ascore;
	}
	public void setAscore(int ascore) {
		this.ascore = ascore;
	}
	public int getHsection() {
		return hsection;
	}
	public void setHsection(int hsection) {
		this.hsection = hsection;
	}
	public int getAsection() {
		return asection;
	}
	public void setAsection(int asection) {
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
	public String getHstrong() {
		return hstrong;
	}
	public void setHstrong(String hstrong) {
		this.hstrong = hstrong;
	}
	public String getAstrong() {
		return astrong;
	}
	public void setAstrong(String astrong) {
		this.astrong = astrong;
	}
	public int getTopN() {
		return topN;
	}
	public void setTopN(int topN) {
		this.topN = topN;
	}
	public int getFriendly() {
		return friendly;
	}
	public void setFriendly(int friendly) {
		this.friendly = friendly;
	}
	public int getMatchId() {
		return matchId;
	}
	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
