package com.bolool.vo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.bolool.util.Const;

public class Match {
	// {"id":1114283,"leagueId":238,"leagueName":"葡超","leagueType":"league","homeId":11271,"homeName":"法马利康","awayId":217,"awayName":"摩雷伦斯","fullscore":"0-2","halfscore":"0-2",
	// "playtime":20210205053000,"result":"负","goalscore":0,"seasonId":14257,"seasonName":"20/21","round":"第17轮"}
	private Integer id;
	private Integer leagueId;
	private String leagueName;
	private String leagueType;
	private Integer homeId;
	private String homeName;
	private Integer awayId;
	private String awayName;
	private String fullscore;
	private String halfscore;
	private Date playtime;
	private String result;
	private Integer goalscore;
	private String hresult;
	private Integer hgoalscore;
	private Integer seasonId;
	private String seasonName;
	private String round;

	public Match() {
	}

	public Match(String id, String leagueId, String leagueName, String leagueType, String homeId, String homeName,
			String awayId, String awayName, String fullscore, String halfscore, String playtime, String result,
			int goalscore,String hresult,int hgoalscore) {
		this.id = Integer.valueOf(id);
		this.leagueId = Integer.valueOf(leagueId);
		this.leagueName = leagueName;
		this.leagueType = leagueType;
		this.homeId = Integer.valueOf(homeId);
		this.homeName = homeName;
		this.awayId = Integer.valueOf(awayId);
		this.awayName = awayName;
		this.fullscore = fullscore;
		this.halfscore = halfscore;
		try {
			if(playtime != null && playtime.length()==14) {
				this.playtime = new SimpleDateFormat(Const.YMDHMS).parse(playtime);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.result = result;
		this.goalscore = goalscore;
		this.hresult = hresult;
		this.hgoalscore = hgoalscore;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(Integer leagueId) {
		this.leagueId = leagueId;
	}

	public String getLeagueName() {
		return leagueName;
	}

	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}

	public String getLeagueType() {
		return leagueType;
	}

	public void setLeagueType(String leagueType) {
		this.leagueType = leagueType;
	}

	public Integer getHomeId() {
		return homeId;
	}

	public void setHomeId(Integer homeId) {
		this.homeId = homeId;
	}

	public String getHomeName() {
		return homeName;
	}

	public void setHomeName(String homeName) {
		this.homeName = homeName;
	}

	public Integer getAwayId() {
		return awayId;
	}

	public void setAwayId(Integer awayId) {
		this.awayId = awayId;
	}

	public String getAwayName() {
		return awayName;
	}

	public void setAwayName(String awayName) {
		this.awayName = awayName;
	}

	public String getFullscore() {
		return fullscore;
	}

	public void setFullscore(String fullscore) {
		this.fullscore = fullscore;
	}

	public String getHalfscore() {
		return halfscore;
	}

	public void setHalfscore(String halfscore) {
		this.halfscore = halfscore;
	}

	public Date getPlaytime() {
		return playtime;
	}

	public void setPlaytime(Date playtime) {
		this.playtime = playtime;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Integer getGoalscore() {
		return goalscore;
	}

	public void setGoalscore(Integer goalscore) {
		this.goalscore = goalscore;
	}
	
	public String getHResult() {
		return hresult;
	}

	public void setHResult(String hresult) {
		this.hresult = hresult;
	}

	public Integer getHGoalscore() {
		return hgoalscore;
	}

	public void setHGoalscore(Integer hgoalscore) {
		this.hgoalscore = hgoalscore;
	}

	public Integer getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(Integer seasonId) {
		this.seasonId = seasonId;
	}

	public String getSeasonName() {
		return seasonName;
	}

	public void setSeasonName(String seasonName) {
		this.seasonName = seasonName;
	}

	public String getRound() {
		return round;
	}

	public void setRound(String round) {
		this.round = round;
	}

	@Override
	public String toString() {
		return Const.YMDHMS_GSON.toJson(this);
	}
}
