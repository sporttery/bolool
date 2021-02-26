package timer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bolool.util.Const;
import com.bolool.util.DBHelper;
import com.bolool.util.HttpUtil;
import com.bolool.vo.Bolool;
import com.bolool.vo.Match;
import com.bolool.vo.Team;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class NewMatchHistoryRunnable implements Runnable {
	private final static Log log = LogFactory.getLog(NewMatchHistoryRunnable.class);
	private final static String MATCH_HISTORY_SQL = "select m.id from t_match m left join t_match_history h on m.id = h.id where h.id is null limit 100";
	private static Map<String, Team> teamMap = null;
	private static Match thisMatch = null;

	private static void setTeamMap() {
		java.lang.reflect.Type type = new TypeToken<Map<String, Team>>() {
		}.getType();
		String teamFilePath = HttpUtil.curlMatchHistoryFile.replace(Const.curlMatchHistoryFileName, "team.json");
		log.error("开始装载team数据，文件路径：" +  teamFilePath);
		File teamFile = new File(teamFilePath);
		if(teamFile.exists()) {
			
			try {
				teamMap = new Gson()
						.fromJson(new FileReader(teamFile), type);
				teamMap.forEach((id, team) -> {
					team.setId(Integer.parseInt(id));
				});
			} catch (JsonIOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else {
			log.error(" teamFile: " + teamFilePath + " file not found");
		}
	}

	@Override
	public void run() {
		if (teamMap == null) {
			setTeamMap();
		}
		// TODO Auto-generated method stub
		List<String> list = DBHelper.selectListSql(MATCH_HISTORY_SQL);
		if (list != null && list.size() > 0) {
			list.forEach(mId -> {
				getHistoryById(mId, false);
				try {
					Thread.sleep(System.currentTimeMillis()%1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		}
	}

	// 比赛结果
	static String getResult(int[] scores) {
		return scores[0] == scores[1] ? "平" : scores[0] > scores[1] ? "胜" : "负";
	}

	// 比赛积分
	static int getGoalscore(int[] scores) {
		return scores[0] > scores[1] ? 3 : scores[0] == scores[1] ? 1 : 0;
	}

	// 获取分区
	static int getScoreSection(Integer score, int count) {
		if (score == null) {
			return -1;
		}
		if (count < 33) {
			return 9 - score / 10;
		} else {
			return 10 - score / 10;
		}
	}

	/**
	 * 
	 * @param {*} arr 要计算的matchlist(历史对阵)
	 * @param {*} topN 前N条数据进行计算
	 * @param {*} friend 0 没有友谊赛 1 全部比赛 2 只有友谊赛
	 */

	static Bolool calcBolool(List<Match> arr, int topN, int friend) {
		List<Match> calcArr = new ArrayList<>();
		if (friend == 1) { // 全部比赛
			calcArr = arr.subList(0, topN);
		} else if (friend == 0) { // 没有友谊赛
			for (int i = 0; i < arr.size(); i++) {
				Match match = arr.get(i);
				if (!match.getLeagueType().equals("friend")) {
					calcArr.add(match);
				}
				if (calcArr.size() == topN) {
					break;
				}
			}
		} else { // 只有友谊赛
			for (int i = 0; i < arr.size(); i++) {
				Match match = arr.get(i);
				if (match.getLeagueType().equals("friend")) {
					calcArr.add(match);
				}
				if (calcArr.size() == topN) {
					break;
				}
			}
		}

		Match bMatch = new Match();
		bMatch.setGoalscore(0);
		bMatch.setResult("");
		calcArr.forEach(match -> {
			int score = match.getHGoalscore().intValue() + bMatch.getGoalscore();
			bMatch.setGoalscore(score);
			bMatch.setResult(bMatch.getResult() + match.getHResult());
		});
		int section = getScoreSection(bMatch.getGoalscore(), topN);
		String result = bMatch.getResult();
		Bolool b = new Bolool();
		b.setId(thisMatch.getId());
		b.setHresult(result);
		b.setHsection(section);
		b.setHscore(bMatch.getGoalscore());
		return b;
	}

	public static Match getMatchFromTr(Element tr) {
		String id = tr.attr("data-matchid");
		Elements tds = tr.children();

		String homeName = tds.get(2).text().replaceAll("[\\s\\r\\n]", "");
		String homeId = tds.get(2).attr("attr");
		String leagueId = tds.get(0).select("a").attr("href").split("/")[3];
		String leagueName = tds.get(0).select("a").text().replaceAll("[\\s\\r\\n]", "");
		String awayName = tds.get(4).text().replaceAll("[\\s\\r\\n]", "");
		String awayId = tds.get(4).attr("attr");
		String fullscore = tds.get(3).text().replaceAll("[\\s\\r\\n]", "");
		String halfscore = tds.get(5).text().replaceAll("[\\s\\r\\n]", "");
		String leagueType = tr.attr("data-lt");
		if (StringUtils.isBlank(leagueType)) {
			leagueType = "league";
		}
		String time = tds.get(1).select(".smalltitle").text();
		if (time.length() == 16) {
			time = time + ":00";
		} else if (time.length() == 8) {
			time = tds.get(1).select("a").text() + " " + time;
		}
		String playtime = time.replaceAll("[^\\d]", "");
		String[] scores = fullscore.split("-");
		String result = "未开";
		int goalscore = 0;
		String hresult = "未开";
		int hgoalscore = 0;
		if (scores.length == 2) {
			int scoresArr[] = new int[] { Integer.parseInt(scores[0]), Integer.parseInt(scores[1]) };
			result = getResult(scoresArr);
			goalscore = getGoalscore(scoresArr);
			if (thisMatch != null) {
				Integer ihomeId = Integer.valueOf(homeId);
				if (ihomeId.intValue() != thisMatch.getHomeId().intValue()) { // 如果这场比赛的客队是查询比赛的主队，则将比分替换下获取比赛结果和积分
					int tmp = scoresArr[0];
					scoresArr[0] = scoresArr[1];
					scoresArr[1] = tmp;
				}
				hresult = getResult(scoresArr);
				hgoalscore = getGoalscore(scoresArr);
			}
		}

		return new Match(id, leagueId, leagueName, leagueType, homeId, homeName, awayId, awayName, fullscore, halfscore,
				playtime, result, goalscore, hresult, hgoalscore);
	}

	public static String getHistoryById(String mId) {
		return getHistoryById(mId, true);
	}

	private static String getHistoryById(String mId, boolean hasRtn) {
		String url = "https://www.okooo.com/soccer/match/" + mId + "/history/";
		String content = HttpUtil.getFromOkooo(url);
		Document doc = Jsoup.parseBodyFragment(content);
		Elements matchs = doc.select(".homecomp tr");
//		System.out.println(matchs.size());
		final int[] idx = new int[] { 0 };
		List<Match> homeMatchArr = new ArrayList<Match>();
		matchs.forEach(el -> {
			if (idx[0] == 1) {
				Match match = getMatchFromTr(el);
				homeMatchArr.add(match);
			}
			if (idx[0] == 0) {
				if (el.className() != null && el.className().indexOf("jsThisMatch") != -1) {
					idx[0] = 1;
					thisMatch = getMatchFromTr(el);
				}
			}

		});

		matchs = doc.select(".awaycomp tr");
//		System.out.println(matchs.size());
		idx[0] = 0;
		List<Match> awayMatchArr = new ArrayList<Match>();
		matchs.forEach(el -> {
			if (idx[0] == 1) {
				awayMatchArr.add(getMatchFromTr(el));
			}
			if (idx[0] == 0) {
				if (el.className() != null && el.className().indexOf("jsThisMatch") != -1) {
					idx[0] = 1;
				}
			}
		});

		boolean isCountryTeamA = false, isCountryTeamH = false;
		if (teamMap == null) {
			setTeamMap();
		}
		if (teamMap.size() != 0) {
			Team team = teamMap.get(thisMatch.getHomeId() + "");
			String pname;
			if (team != null) {
				pname = team.getParentName();
				isCountryTeamH = pname.indexOf("友谊赛") != -1 || pname.indexOf("国家") != -1 || pname.indexOf("奥运") != -1
						|| pname.indexOf("欧洲") != -1 || pname.indexOf("亚洲") != -1 || pname.indexOf("亚运") != -1
						|| pname.indexOf("美洲") != -1 || pname.indexOf("非洲") != -1 || pname.indexOf("世欧预") != -1
						|| pname.indexOf("世亚预") != -1 || pname.indexOf("世南美预") != -1;
			}
			team = teamMap.get(thisMatch.getAwayId() + "");
			if (team != null) {
				pname = team.getParentName();
				isCountryTeamA = pname.indexOf("友谊赛") != -1 || pname.indexOf("国家") != -1 || pname.indexOf("奥运") != -1
						|| pname.indexOf("欧洲") != -1 || pname.indexOf("亚洲") != -1 || pname.indexOf("亚运") != -1
						|| pname.indexOf("美洲") != -1 || pname.indexOf("非洲") != -1 || pname.indexOf("世欧预") != -1
						|| pname.indexOf("世亚预") != -1 || pname.indexOf("世南美预") != -1;

			}
		} else {
			log.info("并没有找到team的定义，不单独处理国家队的友谊赛");
		}
		HashMap<String, String> resMap = new HashMap<String, String>();
		int topN = 33;
		Bolool bolool = calcBolool(homeMatchArr, topN, isCountryTeamH ? 1 : 0);
		Bolool abolool = calcBolool(awayMatchArr, topN, isCountryTeamA ? 1 : 0);
		bolool.setAscore(abolool.getHscore());
		bolool.setAresult(abolool.getHresult());
		bolool.setAsection(abolool.getHsection());
		bolool.setId(thisMatch.getId());
		if (hasRtn) {
			resMap.put("bolool33", Const.GSON_DEFAULT.toJson(bolool));
		}
		int result = DBHelper.insertOrUpdatePre(Const.BOLOOL_SQL33, bolool.getId(), bolool.getHscore(),
				bolool.getAscore(), bolool.getHresult(), bolool.getAresult(), bolool.getHsection(),
				bolool.getAsection());
		log.info("菠萝33 matchId:" + thisMatch.getId() + " 入库 " + result);

		topN = 30;
		bolool = calcBolool(homeMatchArr, topN, 1);
		abolool = calcBolool(awayMatchArr, topN, 1);
		bolool.setAscore(abolool.getHscore());
		bolool.setAresult(abolool.getHresult());
		bolool.setAsection(abolool.getHsection());
		bolool.setId(thisMatch.getId());
		if (hasRtn) {
			resMap.put("bolool30", Const.GSON_DEFAULT.toJson(bolool));
		}
		result = DBHelper.insertOrUpdatePre(Const.BOLOOL_SQL30, bolool.getId(), bolool.getHscore(), bolool.getAscore(),
				bolool.getHresult(), bolool.getAresult(), bolool.getHsection(), bolool.getAsection());
		log.info("菠萝30 matchId:" + thisMatch.getId() + " 入库 " + result);
		String json = "{\"h\":" + Const.YMDHMS_GSON.toJson(homeMatchArr) + ",\"a\":"
				+ Const.YMDHMS_GSON.toJson(awayMatchArr) + "}";
		result = DBHelper.insertOrUpdatePre(Const.MATCH_HISTORY_SQL, thisMatch.getId(), json);
		if (hasRtn) {
			resMap.put("matchlist", json);
			resMap.put("thisMatch", Const.GSON_DEFAULT.toJson(thisMatch));
		}
		log.info("历史数据插入  matchId:" + thisMatch.getId() + " 入库 " + result);
		
		thisMatch = null;
		if (hasRtn) {
			return Const.GSON_DEFAULT.toJson(resMap);
		}
		return null;
	}

	public static void main(String[] args) {
		String mId = "1126199";
		String c = getHistoryById(mId);

	}

}
