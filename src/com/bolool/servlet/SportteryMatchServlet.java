package com.bolool.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bolool.util.Const;
import com.bolool.util.DBHelper;
import com.bolool.util.DataSourceFactory;
import com.bolool.util.HttpUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * <pre>
 * 第一步： 
 * 1.按日期获取比赛，从2010-10-10 开始， 
 * 接口：http://i.sporttery.cn/wap/fb_match_list/get_fb_match_result/?format=jsonp&callback=initData&date=2018-01-01 
 * 2.受注赛程：
 * 接口：http://i.sporttery.cn/odds_calculator/get_odds?i_format=json&i_callback=getData&poolcode[]=hhad&poolcode[]=had 
 * 第二步： 如果没有h_id_dc ，则 按比赛ID 获取比赛信息，得到h_id_dc 和 a_id_dc 
 * 接口：http://i.sporttery.cn/wap/fb_match_info/get_match_info?mid=113760&f_callback=getMatchInfo&_=1543376495494 
 * 第三步： 如果比赛截止超过3小时，按比赛ID获取赔率及彩果
 * 接口：http://i.sporttery.cn/wap/fb_match_info/get_pool_rs/?mid=1005690&f_callback=getPoolRs&_=1543376495496 
 * 第四步： 按球队ID获取球队的历史战线
 * 接口：http://i.sporttery.cn/api/fb_match_info/get_team_rec_data?tid=885&md=2018-11-29&is_ha=all&limit=50&c_id=0&ptype[]=three_-1&f_callback=getAwayInfo
 * 开奖结果：https://i.sporttery.cn/open_v1_0/fb_match_list/get_fb_result_odds/?username=11000000&password=test_passwd&format=jsonp&callback=initData&m_id=
 * </pre>
 * <p>
 */
@WebServlet(value = { "/sporttery/*" }, loadOnStartup = 1)
public class SportteryMatchServlet extends HttpServlet {

	private static final Log log = LogFactory.getLog(SportteryMatchServlet.class);
	private static final long serialVersionUID = 1L;
	private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

	@Override
	public void init(ServletConfig config) throws ServletException {
		try {
			DataSourceFactory.init();
			if (Const.ENABLE_RUNNABLE) {
				log.info("开启定时任务更新数据,3小时执行一次");
				service.scheduleAtFixedRate(new Runnable() {
					@Override
					public void run() {
						try {
							get_fb_match_result(null, null, null);
							get_fb_pool_rs(null, 0, 0);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}, 0, 3, TimeUnit.HOURS);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.addHeader("Access-Control-Allow-Origin", "*");
		resp.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
		resp.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Content-Type");
		resp.addHeader("Access-Control-Max-Age", "86400");
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8"); // 设置 HttpServletResponse使用utf-8编码
		resp.setHeader("Content-Type", "text/html;charset=utf-8"); // 设置响应头的编码
		String uri = req.getRequestURI();
		if (uri.equals("/sporttery/get_fb_match_result")) {
			String startDate = req.getParameter("startDate");
			String endDate = req.getParameter("endDate");
			get_fb_match_result(startDate, endDate, resp);
		} else if (uri.equals("/sporttery/get_pool_rs")) {
			String id = req.getParameter("id");
			String startIdStr = req.getParameter("startId");
			String endIdStr = req.getParameter("endId");
			int startId = 0;
			int endId = 0;
			if (StringUtils.isNumeric(startIdStr) && StringUtils.isNumeric(endIdStr)) {
				startId = Integer.parseInt(startIdStr);
				endId = Integer.parseInt(endIdStr);
			} else if (StringUtils.isNumeric(id)) {
				startId = Integer.parseInt(id);
				endId = startId;
			}
			get_fb_pool_rs(resp, startId, endId);
		} else {
			resp.getWriter().append("Served at: ").append(req.getContextPath());
		}
	}

	private void get_fb_pool_rs(HttpServletResponse resp, int startId, int endId) throws IOException {
		List<String> ids = new ArrayList<String>();
		do {
			ids = getNoResultIdsFromDb(startId, endId);
			ids.forEach(mid -> {
				try {
					boolean result = getResultById(mid);
					log.info("获取比赛结果：" + mid + " 结束，" + result);
					if (resp != null) {
						try {
							resp.getWriter().append("mid: ").append(mid).append(",result:" + result).append("\n");
						} catch (IOException e) {
							e.printStackTrace();
							try {
								resp.getWriter().append(e.getMessage());
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
				} catch (Exception e1) {
					log.error(e1.getLocalizedMessage());
					e1.printStackTrace();
				}
				try {
					Thread.sleep(300l);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			log.info("获取" + ids.size() + "场比赛结果完成");
		} while (ids.size() == 500);
		log.info("获取比赛结果" + startId + " ~ " + endId + " 完成");
		if (resp != null) {
			resp.getWriter().append("finish");
		}
	}

	private void get_fb_match_result(String startDate, String endDate, HttpServletResponse resp) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat(Const.YMD);
		if (startDate == null) {
			startDate = getMaxPlaydatestrFromDb();
			if (startDate == null) {
				startDate = "2013-10-01";
			}
		}
		if (endDate == null) {
			endDate = sdf.format(new Date());
		}

		try {
			Date d1 = sdf.parse(startDate);
			Date d2 = sdf.parse(endDate);
			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();
			c1.setTime(d1);
			c2.setTime(d2);
			do {
				String thisDate = sdf.format(c1.getTime());
				boolean result = getMatchlistByDate(thisDate);
				log.info("获取比赛赛程日期：" + thisDate + ", 结束，" + result);
				c1.add(Calendar.DAY_OF_MONTH, 1);
				if (resp != null) {
					resp.getWriter().append("date: ").append(thisDate).append(",result:" + result).append("\n");
				}
				try {
					Thread.sleep(300l);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} while (c1.before(c2));
			log.info("获取比赛赛程" + startDate + " ~ " + endDate + " 完成");
			if (resp != null) {
				resp.getWriter().append("finish");
			}
		} catch (ParseException e) {
			e.printStackTrace();
			if (resp != null) {
				resp.getWriter().append(e.getMessage());
			}
		}
	}

	/**
	 * @URL http://i.sporttery.cn/wap/fb_match_info/get_pool_rs/?mid=1005690&f_callback=getPoolRs
	 * @JSON getPoolRs({"status":{"code":0,"message":""},"result":{"pool_rs":{"ttg":{"pool_rs":"s3","prs_name":"3","goalline":"","single":"1","odds":"3.20"},"hafu":{"pool_rs":"aa","prs_name":"\u8d1f\u8d1f","goalline":"","single":"1","odds":"4.00"},"had":{"pool_rs":"a","prs_name":"\u8d1f","goalline":"","single":"0","odds":"2.42"},"crs":{"pool_rs":"0003","prs_name":"0:3","goalline":"","single":"1","odds":"22.00"},"hhad":{"pool_rs":"a","prs_name":"\u8ba9\u8d1f","goalline":"-1","single":"0","odds":"1.37"}},"odds_list":{"crs":{"single":"1","goalline":"","odds":[{"-1-a":"70.00","-1-d":"250.0","-1-h":"50.00","0000":"12.00","0001":"9.50","0002":"13.00","0003":"28.00","0004":"70.00","0005":"250.0","0100":"8.50","0101":"6.50","0102":"8.50","0103":"20.00","0104":"50.00","0105":"150.0","0200":"11.50","0201":"7.75","0202":"11.50","0203":"23.00","0204":"60.00","0205":"200.0","0300":"22.00","0301":"17.00","0302":"21.00","0303":"40.00","0400":"50.00","0401":"40.00","0402":"50.00","0500":"120.0","0501":"100.0","0502":"150.0","date":"2021-03-20","time":"09:37:38","trend":""},{"-1-a":"70.00","-1-d":"250.0","-1-h":"55.00","0000":"12.00","0001":"9.00","0002":"12.50","0003":"26.00","0004":"70.00","0005":"250.0","0100":"8.50","0101":"6.50","0102":"8.50","0103":"20.00","0104":"50.00","0105":"150.0","0200":"11.50","0201":"8.00","0202":"11.50","0203":"23.00","0204":"60.00","0205":"200.0","0300":"22.00","0301":"18.00","0302":"21.00","0303":"40.00","0400":"55.00","0401":"40.00","0402":"50.00","0500":"120.0","0501":"100.0","0502":"150.0","date":"2021-03-22","time":"17:43:16","trend":"'-1-h':'up','0001':'down','0002':'down','0003':'down','0201':'up','0301':'up','0400':'up'"},{"-1-a":"60.00","-1-d":"250.0","-1-h":"70.00","0000":"12.00","0001":"8.50","0002":"12.00","0003":"22.00","0004":"55.00","0005":"150.0","0100":"9.00","0101":"6.50","0102":"8.00","0103":"18.00","0104":"40.00","0105":"120.0","0200":"12.50","0201":"8.50","0202":"11.50","0203":"21.00","0204":"55.00","0205":"200.0","0300":"25.00","0301":"20.00","0302":"23.00","0303":"40.00","0400":"60.00","0401":"45.00","0402":"60.00","0500":"200.0","0501":"150.0","0502":"200.0","date":"2021-03-22","time":"21:32:59","trend":"'-1-a':'down','-1-h':'up','0001':'down','0002':'down','0003':'down','0004':'down','0005':'down','0100':'up','0102':'down','0103':'down','0104':'down','0105':'down','0200':'up','0201':'up','0203':'down','0204':'down','0300':'up','0301':'up','0302':'up','0400':'up','0401':'up','0402':'up','0500':'up','0501':'up','0502':'up'"}]},"had":{"single":"0","goalline":"","odds":[{"a":"2.65","d":"3.25","h":"2.20","goalline":"","date":"2021-03-20","time":"09:37:38","trend":""},{"a":"2.55","d":"3.20","h":"2.30","goalline":"","date":"2021-03-21","time":"22:13:44","trend":"'a':'down','d':'down','h':'up'"},{"a":"2.48","d":"3.20","h":"2.36","goalline":"","date":"2021-03-22","time":"17:41:33","trend":"'a':'down','h':'up'"},{"a":"2.48","d":"3.10","h":"2.42","goalline":"","date":"2021-03-22","time":"18:52:39","trend":"'d':'down','h':'up'"},{"a":"2.42","d":"3.10","h":"2.48","goalline":"","date":"2021-03-22","time":"21:29:38","trend":"'a':'down','h':'up'"}]},"hafu":{"single":"1","goalline":"","odds":[{"aa":"4.40","ad":"14.00","ah":"26.00","da":"6.00","dd":"5.40","dh":"5.30","ha":"29.00","hd":"14.00","hh":"3.70","date":"2021-03-20","time":"09:37:38","trend":""},{"aa":"4.20","ad":"14.00","ah":"26.00","da":"5.80","dd":"5.30","dh":"5.50","ha":"29.00","hd":"14.00","hh":"3.90","date":"2021-03-21","time":"22:14:09","trend":"'aa':'down','da':'down','dd':'down','dh':'up','hh':'up'"},{"aa":"4.20","ad":"14.00","ah":"26.00","da":"5.70","dd":"5.20","dh":"5.60","ha":"27.00","hd":"14.00","hh":"4.00","date":"2021-03-22","time":"19:20:24","trend":"'da':'down','dd':'down','dh':'up','ha':'down','hh':'up'"},{"aa":"4.00","ad":"14.00","ah":"27.00","da":"5.60","dd":"5.20","dh":"5.70","ha":"25.00","hd":"14.00","hh":"4.20","date":"2021-03-22","time":"21:29:58","trend":"'aa':'down','ah':'up','da':'down','dh':'up','ha':'down','hh':'up'"}]},"hhad":{"single":"0","goalline":"-1","odds":[{"a":"1.47","d":"4.00","h":"4.80","goalline":"-1.00","date":"2021-03-20","time":"09:37:38","trend":""},{"a":"1.44","d":"4.10","h":"5.00","goalline":"-1.00","date":"2021-03-21","time":"22:13:51","trend":"'a':'down','d':'up','h':'up'"},{"a":"1.41","d":"4.20","h":"5.20","goalline":"-1.00","date":"2021-03-22","time":"17:41:41","trend":"'a':'down','d':'up','h':'up'"},{"a":"1.39","d":"4.25","h":"5.40","goalline":"-1.00","date":"2021-03-22","time":"18:53:21","trend":"'a':'down','d':'up','h':'up'"},{"a":"1.37","d":"4.35","h":"5.60","goalline":"-1.00","date":"2021-03-22","time":"21:29:46","trend":"'a':'down','d':'up','h':'up'"}]},"ttg":{"single":"1","goalline":"","odds":[{"s0":"12.00","s1":"5.20","s2":"3.65","s3":"3.50","s4":"4.90","s5":"9.00","s6":"16.00","s7":"24.00","date":"2021-03-20","time":"09:37:38","trend":""},{"s0":"12.00","s1":"5.20","s2":"3.50","s3":"3.50","s4":"4.90","s5":"9.25","s6":"17.00","s7":"28.00","date":"2021-03-22","time":"16:23:06","trend":"'s2':'down','s5':'up','s6':'up','s7':'up'"},{"s0":"12.00","s1":"5.20","s2":"3.40","s3":"3.30","s4":"4.90","s5":"10.00","s6":"21.00","s7":"35.00","date":"2021-03-22","time":"18:00:57","trend":"'s2':'down','s3':'down','s5':'up','s6':'up','s7':'up'"},{"s0":"12.00","s1":"5.40","s2":"3.30","s3":"3.20","s4":"4.90","s5":"10.50","s6":"22.00","s7":"39.00","date":"2021-03-22","time":"19:15:17","trend":"'s1':'up','s2':'down','s3':'down','s5':'up','s6':'up','s7':'up'"},{"s0":"12.00","s1":"5.40","s2":"3.40","s3":"3.20","s4":"4.90","s5":"10.00","s6":"21.00","s7":"36.00","date":"2021-03-22","time":"21:42:35","trend":"'s2':'up','s5':'down','s6':'down','s7':'down'"}]}}}});
	 * @param mid
	 * @return
	 */
	private boolean getResultById(String mid) {
		String url = "http://i.sporttery.cn/wap/fb_match_info/get_pool_rs/?mid=" + mid + "&f_callback=getPoolRs";
		String jsonStr = HttpUtil.doGet(url);
		log.info("正在获取开奖结果 id：" + mid);
		List<String> sqlList = new ArrayList<>();
		if (jsonStr.indexOf("getPoolRs") != -1) {
			int startOp = jsonStr.indexOf("{");
			int endOp = jsonStr.lastIndexOf("}");
			String json = jsonStr.substring(startOp, endOp + 1);
			JsonObject data = JsonParser.parseString(json).getAsJsonObject();
			if (data.get("status").getAsJsonObject().get("code").getAsInt() == 0) {
				JsonObject resultData = data.get("result").getAsJsonObject();
				JsonElement pool_rs_je = resultData.get("pool_rs");
				if (pool_rs_je.isJsonArray()) {// 可能是无效场次
					pool_rs_je = JsonParser
							.parseString("{\"crs\":{\"prs_name\":\"取消\",\"single\":\"1\",\"odds\":\"0\"},"
									+ "\"had\":{\"prs_name\":\"取消\",\"single\":\"0\",\"odds\":\"0\"},"
									+ "\"hhad\":{\"prs_name\":\"取消\",\"single\":\"0\",\"odds\":\"0\"},"
									+ "\"hafu\":{\"prs_name\":\"取消\",\"single\":\"1\",\"odds\":\"0\"},"
									+ "\"ttg\":{\"prs_name\":\"取消\",\"single\":\"1\",\"odds\":\"0\"}}");
					log.info("比赛" + mid + "未获取到开奖结果,归纳为取消");
				}
				JsonObject pool_rs = pool_rs_je.getAsJsonObject();
				List<String> columnList = new ArrayList<>();
				columnList.add("id");
				List<String> valueList = new ArrayList<>();
				valueList.add(mid);
				Const.pool_rs_map.forEach((k, v) -> {
					if (pool_rs.has(k)) {
						JsonObject result = pool_rs.getAsJsonObject(k);
						String single = result.get("single").getAsString();
						String prs_name;
						if (result.get("prs_name").isJsonNull()) {
							if (!result.get("pool_rs").isJsonNull()) {
								prs_name = result.get("pool_rs").getAsString().replace("0", "").replace(";", ":");
							} else {
								prs_name = "null";
							}
						} else {
							prs_name = result.get("prs_name").getAsString();
						}
						String odds;
						if (!result.has("odds") || result.get("odds").isJsonNull()) {
							odds = "0";
						} else {
							odds = result.get("odds").getAsString();
						}
						columnList.add(k);
						columnList.add(k + "_result");
						columnList.add(k + "_result_odds");
						columnList.add(k + "_single");
						valueList.add(result.toString());
						valueList.add(prs_name);
						valueList.add(odds);
						valueList.add(single);
					}
				});
				String sql = "insert into t_fb_match_result(" + StringUtils.join(columnList, ",") + ")values('"
						+ StringUtils.join(valueList, "','") + "')";
				sqlList.add(sql);
				JsonElement odds_list_je = JsonParser.parseString("{}");
				if (resultData.get("odds_list").isJsonObject()) {
					odds_list_je = resultData.get("odds_list");
				}
				JsonObject odds_list = odds_list_je.getAsJsonObject();
				Const.pool_rs_map.forEach((k, v) -> {
					if (odds_list.has(k)) {
						JsonArray odds = odds_list.getAsJsonObject(k).getAsJsonArray("odds");
						for (int i = 0; i < odds.size(); i++) {
							JsonElement item = odds.get(i);
							String sql1 = getSqlByResultJson(item.getAsJsonObject(), k, v, i, mid);
							if (sql1 != null) {
								sqlList.add(sql1);
							}
						}
					}
				});
				if (sqlList.size() > 0) {
					if (DBHelper.insertOrUpdateMany(sqlList, null)) {
						return true;
					}
				}
			} else {
				log.error("url:" + url + ",响应：" + json);
			}
		} else {
			log.error("url:" + url + ",响应：" + jsonStr);
		}
		return false;
	}

	private static String getSqlByResultJson(JsonObject item, String k, String v, int idx, String id) {
		item.remove("trend");
		item.remove("goalline");
		String date = item.remove("date").getAsString();
		String time = item.remove("time").getAsString();
		String change_time = date + " " + time;

		List<String> columnList = new ArrayList<>();
		List<String> valueList = new ArrayList<>();
		columnList.add("change_time");
		columnList.add("is_first");
		columnList.add("mid");
		columnList.add("id");

		valueList.add(change_time);
		valueList.add(idx == 0 ? "1" : "0");
		valueList.add(id);
		valueList.add(id + "_" + (idx + 1));
		item.entrySet().forEach(stringJsonElementEntry -> {
			if (v.equals("crs")) {
				columnList.add(v + stringJsonElementEntry.getKey().replaceAll("-", ""));
			} else {
				columnList.add(stringJsonElementEntry.getKey());
			}
			valueList.add(stringJsonElementEntry.getValue().getAsString());
		});
		if (columnList.size() > 4) {
			String sql = "insert into t_fb_match_odds_" + k + "(" + StringUtils.join(columnList, ",") + ") values ('"
					+ StringUtils.join(valueList, "','") + "')";
			return sql;
		}
		return null;

	}

	private List<String> getNoResultIdsFromDb(int startId, int endId) {
		String sql = null;
		if (Const.DB_DBTYPE.equals("mssql")) {
			sql = "select top 500 m.id  from t_fb_match m left join t_fb_match_result r on m.id = r.id " + " where "
					+ (startId != 0 ? " m.id between " + startId + " and " + endId + " and " : "")
					+ " m.match_status='Final' and r.id is null and m.playtime <  DATEADD(HOUR,-4,GETDATE()) ";
		} else {
			sql = "select  m.id  from t_fb_match m left join t_fb_match_result r on m.id = r.id " + " where "
					+ (startId != 0 ? " m.id between " + startId + " and " + endId + " and " : "")
					+ " m.match_status='Final' and  r.id is null and m.playtime < date_add(now(), interval - 4 hour) limit 500 ";
		}
		return DBHelper.selectListSql(sql);
	}

	private String getMaxPlaydatestrFromDb() {
		String sql = "select max(playdatestr) as playdatestr  from t_fb_match";
		List<String> list = DBHelper.selectListSql(sql);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * @URL https://i.sporttery.cn/wap/fb_match_list/get_fb_match_result/?format=jsonp&callback=initData&date=2021-03-23
	 * @JSON initData({"status":{"code":8000,"message":"OK"},"data":{"result":[{"id":"1005690","num":"\u5468\u4e00003","date":"2021-03-23","time":"03:30:00","l_id":"34","l_cn":"\u5fb7\u56fd\u4e59\u7ea7\u8054\u8d5b","h_id":"1269","h_cn":"\u675c\u585e\u5c14\u591a\u592b","a_id":"139","a_cn":"\u6ce2\u9e3f","match_status":"Final","message":"","pool_status":"Payout","fixedodds":"-1.00","result_status":"FinalResultIn","half":"0:2","final":"0:3","l_cn_abbr":"\u5fb7\u4e59","h_cn_abbr":"\u675c\u585e\u591a\u592b","a_cn_abbr":"\u6ce2\u9e3f","status":"\u5df2\u5b8c\u6210"},{"id":"1005689","num":"\u5468\u4e00002","date":"2021-03-23","time":"03:00:00","l_id":"80","l_cn":"\u8377\u5170\u4e59\u7ea7\u8054\u8d5b","h_id":"1256","h_cn":"\u574e\u5e03\u5c14","a_id":"749","a_cn":"\u57c3\u56e0\u970d\u6e29FC","match_status":"Final","message":"","pool_status":"Payout","fixedodds":"-2.00","result_status":"FinalResultIn","half":"0:0","final":"3:0","l_cn_abbr":"\u8377\u4e59","h_cn_abbr":"\u574e\u5e03\u5c14","a_cn_abbr":"\u57c3\u56e0FC","status":"\u5df2\u5b8c\u6210"}],"league":{"34":{"l_id":"34","l_cn_abbr":"\u5fb7\u4e59"},"80":{"l_id":"80","l_cn_abbr":"\u8377\u4e59"}}}})
	 * @param date 日期
	 */
	private boolean getMatchlistByDate(String dateStr) {
		log.info("正在获取比赛赛程，日期：" + dateStr);
		String url = "https://i.sporttery.cn/wap/fb_match_list/get_fb_match_result/?format=jsonp&callback=initData&date="
				+ dateStr;
		String jsonStr = HttpUtil.sendGet(url);
		if (jsonStr.indexOf("initData") != -1) {
			int startOp = jsonStr.indexOf("{");
			int endOp = jsonStr.lastIndexOf("}");
			String json = jsonStr.substring(startOp, endOp + 1);
			JsonObject data = JsonParser.parseString(json).getAsJsonObject();
			if (data.get("status").getAsJsonObject().get("code").getAsInt() == 8000
					&& data.get("data").isJsonObject()) {
				JsonArray ja = data.get("data").getAsJsonObject().get("result").getAsJsonArray();
				log.info("日期：" + dateStr + ",共有数据：" + ja.size());
				StringBuilder sb = new StringBuilder();
				StringBuilder ids = new StringBuilder();
				ja.forEach(je -> {
					// "id":"1005690","num":"\u5468\u4e00003","date":"2021-03-23","time":"03:30:00","l_id":"34",
					// "l_cn":"\u5fb7\u56fd\u4e59\u7ea7\u8054\u8d5b","h_id":"1269","h_cn":"\u675c\u585e\u5c14\u591a\u592b",
					// "a_id":"139","a_cn":"\u6ce2\u9e3f","match_status":"Final","message":"","pool_status":"Payout","fixedodds":"-1.00",
					// "result_status":"FinalResultIn","half":"0:2","final":"0:3","l_cn_abbr":"\u5fb7\u4e59","h_cn_abbr":"\u675c\u585e\u591a\u592b",
					// "a_cn_abbr":"\u6ce2\u9e3f","status":"\u5df2\u5b8c\u6210"
					try {
						JsonObject jo = je.getAsJsonObject();
						String id = jo.get("id").getAsString();
						String num = jo.get("num").getAsString();
						String date = jo.get("date").getAsString();
						String time = jo.get("time").getAsString();
						String l_id = jo.get("l_id").getAsString();
						String l_cn = jo.get("l_cn").getAsString();
						String h_id = jo.get("h_id").getAsString();
						String h_cn = jo.get("h_cn").getAsString();
						String a_id = jo.get("a_id").getAsString();
						String a_cn = jo.get("a_cn").getAsString();
						String match_status = jo.get("match_status").getAsString();
//					String pool_status = jo.get("pool_status").getAsString();
						String fixedodds = "0";
						if (!jo.get("fixedodds").isJsonNull()) {
							fixedodds = jo.get("fixedodds").getAsString();
						}
						String result_status = "null";
						if (!jo.get("result_status").isJsonNull()) {
							result_status = jo.get("result_status").getAsString();
						}
						String half = jo.get("half").getAsString();
						String final0 = jo.get("final").getAsString();
						String l_cn_abbr = jo.get("l_cn_abbr").getAsString().replaceAll("'", "");
						String h_cn_abbr = jo.get("h_cn_abbr").getAsString().replaceAll("'", "");
						String a_cn_abbr = jo.get("a_cn_abbr").getAsString().replaceAll("'", "");
//					String status = jo.get("status").getAsString();
						sb.append(",('").append(id).append("','").append(num).append("','").append(date).append("','")
								.append(time).append("','").append(l_id).append("','").append(l_cn).append("','")
								.append(h_id).append("','").append(h_cn).append("','").append(a_id).append("','")
								.append(a_cn).append("','").append(match_status).append("','").append(fixedodds)
								.append("','").append(result_status).append("','").append(half).append("','")
								.append(final0).append("','").append(l_cn_abbr).append("','").append(h_cn_abbr)
								.append("','").append(a_cn_abbr).append("','").append(date).append(" ").append(time)
								.append("','").append(final0).append("')");
						ids.append(",").append(id);
					} catch (Exception ee) {
						log.error(ee.getMessage());
						ee.printStackTrace();
					}
				});
				boolean result = false;
				if (ja.size() > 0) {
					DBHelper.insertOrUpdate("delete from t_fb_match where id in (" + ids.substring(1) + ")");
					String sql = "insert into t_fb_match(id,num,playdatestr,playtimestr,l_id,l_cn,h_id,h_cn,a_id,a_cn,match_status,goalline,result_status,half,final,l_cn_abbr,h_cn_abbr,a_cn_abbr,playtime,scorefull) values "
							+ sb.substring(1);
					result = DBHelper.insertOrUpdate(sql);
				}
				return result;
			} else {
				log.error("url:" + url + ",响应：" + json);
			}
		} else {
			log.error("url:" + url + ",响应：" + jsonStr);
		}
		return false;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}

}
