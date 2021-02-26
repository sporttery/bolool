package com.bolool.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Const {
	
	
	/**
	 * utf-8
	 */
	public final static String DEFAULT_ENCODING="utf-8";
	
	static {
		PropKit.use("config.properties");
	}
	
	public final static String curlMatchHistoryFileName = "curlMatchHistory.sh";
	
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public final static String YMD_HMS = "yyyy-MM-dd HH:mm:ss";
	/**
	 * yyyyMMddHHmmss
	 */
	public final static String YMDHMS = "yyyyMMddHHmmss";
	/**
	 * 1000 * 60 * 5
	 */
	public final static int WEB_CACHE_SECONDS = 1000 * 60 * 5;
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public final static Gson YMD_HMS_GSON = new GsonBuilder().setDateFormat(YMD_HMS).create(); 
	/**
	 * yyyyMMddHHmmss
	 */
	public  final static Gson YMDHMS_GSON = new GsonBuilder().setDateFormat(YMDHMS).create(); 
	
	/**
	 * new Gson
	 */
	public final static Gson GSON_DEFAULT = new Gson(); 
	
	
	/**
	 * insert into t_match(id,leagueId,leagueName,leagueType,seasonId,seasonName,round,homeId,homeName,awayId,awayName,playtime,fullscore,halfscore,result,goalscore)  values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)  ON DUPLICATE KEY UPDATE `version`=`version` + 1 , `fullscore`=values(`fullscore`),`goalscore`=values(`goalscore`),`result`=values(`result`),`halfscore`=values(`halfscore`) ";
	 */
	public final static String MATCH_SQL = "insert into t_match(id,leagueId,leagueName,leagueType,seasonId,seasonName,round,homeId,homeName,awayId,awayName,playtime,fullscore,halfscore,result,goalscore)  values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)  ON DUPLICATE KEY UPDATE `version`=`version` + 1 , `fullscore`=values(`fullscore`),`goalscore`=values(`goalscore`),`result`=values(`result`),`halfscore`=values(`halfscore`) ";
	

	/**
	 * "insert into t_bolool33(id,hscore,ascore,hresult,aresult,hsection,asection) values (?,?,?,?,?,?,?)  ON DUPLICATE KEY UPDATE `version` = `version` + 1,hscore = values(hscore),ascore = values(ascore),hresult = values(hresult),aresult = values(aresult),hsection = values(hsection),asection = values(asection)" ;
	 */
	public final static String BOLOOL_SQL30 =
			  "insert into t_bolool30(id,hscore,ascore,hresult,aresult,hsection,asection) values (?,?,?,?,?,?,?)  ON DUPLICATE KEY UPDATE `version` = `version` + 1,hscore = values(hscore),ascore = values(ascore),hresult = values(hresult),aresult = values(aresult),hsection = values(hsection),asection = values(asection)" ;
	/**
	 * "insert into t_bolool33(id,hscore,ascore,hresult,aresult,hsection,asection) values (?,?,?,?,?,?,?)  ON DUPLICATE KEY UPDATE `version` = `version` + 1,hscore = values(hscore),ascore = values(ascore),hresult = values(hresult),aresult = values(aresult),hsection = values(hsection),asection = values(asection)" ;
	 */
	public final static String BOLOOL_SQL33 =
			  "insert into t_bolool33(id,hscore,ascore,hresult,aresult,hsection,asection) values (?,?,?,?,?,?,?)  ON DUPLICATE KEY UPDATE `version` = `version` + 1,hscore = values(hscore),ascore = values(ascore),hresult = values(hresult),aresult = values(aresult),hsection = values(hsection),asection = values(asection)" ;
	
	/**
	 * "insert into t_match_odds(matchId,s,p,f,companyId) values (?,?,?,?,27)  ON DUPLICATE KEY UPDATE `version` = `version` + 1,s = values(s),p = values(p),f = values(f)" ;
	 */
	public final static String MATCH_ODDS_EUROPE =
			  "insert into t_match_odds(matchId,s,p,f,companyId) values (?,?,?,?,27)  ON DUPLICATE KEY UPDATE `version` = `version` + 1,s = values(s),p = values(p),f = values(f)" ;
	
	/**
	 * "insert into t_match_odds(matchId,s,p,f,companyId) values (?,?,?,?,27)  ON DUPLICATE KEY UPDATE `version` = `version` + 1,s = values(s),p = values(p),f = values(f)" ;
	 */
	public final static String MATCH_ODDS_ASIA =
			  "insert into t_match_odds(matchId,h,pan,a,companyId) values (?,?,?,?,27)  ON DUPLICATE KEY UPDATE `version` = `version` + 1,h = values(h),pan = values(pan),a = values(a)" ;
	
	
	/**
	 * update t_match_odds set s=?,p=?,f=?,version=version +1 where matchId = ?
	 */
	public final static String MATCH_ODDS_EUROPE_UPDATE =
			  "update t_match_odds set s=?,p=?,f=?,version=version +1 where matchId = ?" ;
	
	/**
	 * update t_match_odds set h=?,pan=?,a=?,version=version +1 where matchId = ?
	 */
	public  final static String MATCH_ODDS_ASIA_UPDATE =
			  "update t_match_odds set h=?,pan=?,a=?,version=version +1 where matchId = ?" ;
	
	/**
	 * update t_match_odds set s=?,p=?,f=?,h=?,pan=?,a=?,version=version +1 where matchId = ?
	 */
	public final static String MATCH_ODDS_UPDATE =
			  "update t_match_odds set s=?,p=?,f=?,h=?,pan=?,a=?,version=version +1 where matchId = ?" ;
	
	/**
	 * insert into t_match_odds (matchId,s,p,f,h,pan,a,companyId) values(?,?,?,?,?,?,?,27)
	 */
	public final static String MATCH_ODDS_INSERT =
			  "insert into t_match_odds (matchId,s,p,f,h,pan,a,companyId) values(?,?,?,?,?,?,?,27) " ;
	
	
	/**
	 * insert into t_match_history(id,matchlist) values (?,?)  ON DUPLICATE KEY UPDATE `version`=`version` + 1 
	 */
	public static final String MATCH_HISTORY_SQL = "insert into t_match_history(id,matchlist) values (?,?)  ON DUPLICATE KEY UPDATE `version`=`version` + 1  ";
	
	/**
	 * "update t_match set fullscore=?,halfscore=?,result=?,goalscore=? where id = ?"
	 */
	public static final String UPDATE_MATCH_SCORE = "update t_match set fullscore=?,halfscore=?,result=?,goalscore=? where id = ?";
	
	/**
	 * "true".equalsIgnoreCase(PropKit.get("showSql"))
	 */
	public static final boolean showSql = "true".equalsIgnoreCase(PropKit.get("showSql"));
	
	
	public static final String DB_URL = PropKit.get("jdbcUrl");
	public static final String DB_UID = PropKit.get("user");
	public static final String DB_PWD = PropKit.get("password");
	public static final boolean ENABLE_RUNNABLE = "true".equalsIgnoreCase(PropKit.get("enableRunnable"));
	
}
