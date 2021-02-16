package com.bolool.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Const {
	/**
	 * utf-8
	 */
	public static String DEFAULT_ENCODING="utf-8";
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static String YMD_HMS = "yyyy-MM-dd HH:mm:ss";
	/**
	 * yyyyMMddHHmmss
	 */
	public static String YMDHMS = "yyyyMMddHHmmss";
	/**
	 * 1000 * 60 * 5
	 */
	public static int WEB_CACHE_SECONDS = 1000 * 60 * 5;
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static Gson YMD_HMS_GSON = new GsonBuilder().setDateFormat(YMD_HMS).create(); 
	/**
	 * yyyyMMddHHmmss
	 */
	public static Gson YMDHMS_GSON = new GsonBuilder().setDateFormat(YMDHMS).create(); 
	
	/**
	 * insert into t_match(id,leagueId,leagueName,leagueType,seasonId,seasonName,round,homeId,homeName,awayId,awayName,playtime,fullscore,halfscore,result,goalscore)  values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)  ON DUPLICATE KEY UPDATE `version`=`version` + 1 , `fullscore`=values(`fullscore`),`goalscore`=values(`goalscore`),`result`=values(`result`),`halfscore`=values(`halfscore`) ";
	 */
	public static String MATCH_SQL = "insert into t_match(id,leagueId,leagueName,leagueType,seasonId,seasonName,round,homeId,homeName,awayId,awayName,playtime,fullscore,halfscore,result,goalscore)  values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)  ON DUPLICATE KEY UPDATE `version`=`version` + 1 , `fullscore`=values(`fullscore`),`goalscore`=values(`goalscore`),`result`=values(`result`),`halfscore`=values(`halfscore`) ";
	

	/**
	 * insert into t_bolool(matchId,hscore,ascore,hresult,aresult,hsection,asection,hstrong,astrong,topN,friendly) values (?,?,?,?,?,?,?,?,?,?,?)"  ON DUPLICATE KEY UPDATE `version` = `version` + 1,hscore = values(hscore),ascore = values(ascore),hresult = values(hresult),aresult = values(aresult),hsection = values(hsection),asection = values(asection),hstrong = values(hstrong),astrong = values(astrong)
	 */
	public static String BOLOOL_SQL =
			  "insert into t_bolool(matchId,hscore,ascore,hresult,aresult,hsection,asection,hstrong,astrong,topN,friendly) values (?,?,?,?,?,?,?,?,?,?,?)  ON DUPLICATE KEY UPDATE `version` = `version` + 1,hscore = values(hscore),ascore = values(ascore),hresult = values(hresult),aresult = values(aresult),hsection = values(hsection),asection = values(asection),hstrong = values(hstrong),astrong = values(astrong)" ;
	/**
	 * insert into t_match_history(id,matchlist) values (?,?)  ON DUPLICATE KEY UPDATE `version`=`version` + 1 
	 */
	public static String MATCH_HISTORY_SQL = "insert into t_match_history(id,matchlist) values (?,?)  ON DUPLICATE KEY UPDATE `version`=`version` + 1  ";
}
