package com.bolool.servlet;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.bolool.vo.Bolool;
import com.bolool.vo.Match;
import com.bolool.vo.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import timer.NewMatchHistoryRunnable;
import timer.NewMatchRunnable;

/**
 * Servlet implementation class AjaxServlet
 */
@WebServlet(value = { "/ajaxServlet/*", "/xml/*", "/ajax/*", "/api/*" }, loadOnStartup = 1)
public class AjaxServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(AjaxServlet.class);
	private static final long serialVersionUID = 1L;
	private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
	
	

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		if(HttpUtil.curlMatchHistoryFile == null) {
			HttpUtil.curlMatchHistoryFile = getServletContext().getRealPath("/"+Const.curlMatchHistoryFileName);
			if(!new File(HttpUtil.curlMatchHistoryFile).exists()) {
				HttpUtil.noCurl=true;
			}
		}
		try {
			DataSourceFactory.init();
			if(Const.ENABLE_RUNNABLE) {
				Runnable newMatchRunnable = new NewMatchRunnable();
//			Runnable newOddsRunnable = new NewOddsRunnable();
				Runnable newMatchHistoryRunnable = new NewMatchHistoryRunnable();
				log.info("开启定时任务更新赛程比分,5小时执行一次");
				newMatchRunnable.run();
//				newOddsRunnable.run();
				newMatchHistoryRunnable.run();
				// 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
				service.scheduleAtFixedRate(newMatchRunnable, 5, 5, TimeUnit.HOURS);
//				log.info("开启定时任务更新比赛赔率 ,30分钟执行一次");
//				service.scheduleAtFixedRate(newOddsRunnable, 30, 30, TimeUnit.MINUTES);
				log.info("开启定时任务更新比赛历史和菠萝指数 ,2小时执行一次");
				service.scheduleAtFixedRate(newMatchHistoryRunnable, 2, 2, TimeUnit.HOURS);
			}else {
				log.info("定时任务未开启");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AjaxServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
		response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Content-Type");
		response.addHeader("Access-Control-Max-Age", "86400");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8"); // 设置 HttpServletResponse使用utf-8编码
		response.setHeader("Content-Type", "text/html;charset=utf-8"); // 设置响应头的编码
		@SuppressWarnings("unchecked")
		HashMap<String, Object> cacheMap = (HashMap<String, Object>) request.getSession().getAttribute("web-cache");
		if (cacheMap == null) {
			cacheMap = new HashMap<String, Object>();
			request.getSession().setAttribute("web-cache", cacheMap);
		}
		String uri = request.getRequestURI();
		if (uri.equals("/xml/odds_jc.txt") || uri.equals("/xml/bf_jc.txt") || uri.equals("/xml/goallottery8.txt")) {
			getWin007XmlTxt(response, cacheMap, uri);
		} else if (uri.equals("/api/userLogin")) {
			userLogin(request, response);
		} else if (uri.equals("/api/saveConfig")) {
			saveConfig(request, response);
		} else if (uri.equals("/api/myOrder")) {
			getMyOrder(request, response);
		} else if (uri.equals("/api/getOkoooJc")) {
			getOkoooJc(request, response, cacheMap);
		} else if (uri.equals("/api/getMatchHistory")) {
			getMatchHistory(request, response);
		} else if (uri.equals("/api/getMatchHistoryNew")) {
			getMatchHistoryNew(request, response);
		} else if (uri.equals("/api/saveEuropeOdds")) {
			saveEuropeOdds(request, response);
		} else if (uri.equals("/api/saveAsiaOdds")) {
			saveAsiaOdds(request, response);
		} else if (uri.equals("/api/getBoloolByIds")) {
			getBoloolByIds(request, response);
		} else if (uri.equals("/api/getBoloolList")) {
			getBoloolList(request, response);
		} else if (uri.equals("/api/saveBolool")) {
			saveBolool(request, response);
		}  else if (uri.equals("/api/saveMatchScore")) {
			saveMatchScore(request, response);
		} else if (uri.equals("/api/saveZhuzhuo")) {
			saveZhuzhuo(request, response);
		} else if (uri.equals("/api/saveCaiTong")) {
			saveCaiTong(request, response);
		} else if (uri.equals("/api/getHistoryById")) {
			String mId = request.getParameter("id");
			String content = NewMatchHistoryRunnable.getHistoryById(mId);
			response.setHeader("Content-Type", "application/json; charset=UTF-8 "); // 设置响应头的编码
			response.getWriter().append(content);
		} else if(uri.equals("/api/saveJczqMatch")){
			saveJczqMatch(request, response);
		}else {
			response.getWriter().append("Served at: ").append(request.getContextPath());
		}
	}
	
	/**
	 * https://www.lottery.gov.cn/jc/zqsgkj/
	 * @param request
	 * @param response
	 * 打开官网的赛果开奖页面，
	 * var beginDate="2021-01-20",//开始日期
	 * var endDate="2021-03-22";//结束日期
	 * var isFix=0;//是否单关
	 * var pageSize=30;//每页显示多少条
	 * var pageNo = 1; //从第一页开始
	 * var total=0;
	 * function getData(){
	 * jQuery.get("https://webapi.sporttery.cn/gateway/jc/football/getMatchResultV1.qry?matchPage=1&matchBeginDate="+startDate+"&matchEndDate="+endDate+"&leagueId=&pageSize="+pageSize+"&pageNo="+pageNo+"&isFix="+isFix+"&pcOrWap=0",function(data){
			if(data.success){
				var value =data.value;
				var pages = value.pages;
				var matchResult = value.matchResult;
				if(total==0){
					total=value.total;
				}
				jQuery.post("http://127.0.0.1:8080/api/saveJczqMatch",{matchResult:JSON.stringify(matchResult)},function(d){
					console.log(d);
				});
				if(page < pages){
					getData();
				}else{
					console.log({beginDate，endDate，isFix，pageSize},"已经处理完成,共"+total);
				}
			}
			
		})
	 * }
	 * 
	 */

	private void saveJczqMatch(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String matchResult = request.getParameter("matchResult");
		java.lang.reflect.Type type = new TypeToken<List<Map<String, String>>>() {
		}.getType();
		List<Map<String, String>> list = new Gson().fromJson(matchResult, type);
		StringBuilder sb = new StringBuilder();
		list.forEach(map->{
			//ctime,mtime,intime,title,intro,url
//			String title = map.get("title");
//			String ctime = map.get("ctime");
//			String intro = map.get("intro");
//			String url = map.get("url");
//			String mtime = map.get("mtime");
//			String intime = map.get("intime");
//			sb.append(",("+ctime+","+mtime+","+intime+",'"+DBHelper.getSafeSqlParam(title)+"','"+DBHelper.getSafeSqlParam(intro)+"','"+url+"')");
		});
//		boolean result = false;
//		if(list.size()>0) {
//			String sql = "insert into t_caitong(ctime,mtime,intime,title,intro,url) values " + sb.substring(1);
//			result = DBHelper.insertOrUpdate(sql);
//		}
//		response.getWriter().write(""+result);
	}

	/*
	 http://caitong.sina.com.cn/
	 var page=1,num=40,total=0;
	 $.getScript("http://feed.mix.sina.com.cn/api/roll/get?pageid=189&lid=1758&num="+num+"&versionNumber=1.2.8&page="+page+"&encode=utf-8&callback=feedCardJsonpCallback1111");
	 feedCardJsonpCallback1111=function(d){
	 try{
	 	var arr = [];
	 	d.result.data.forEach(e=>{
	 	var ctime = e.ctime;
	 	var mtime = e.mtime;
	 	var intime=e.intime;
	 	var title = e.title;
	 	var intro = e.intro;
	 	var url = e.url;
	 	arr.push({ctime,mtime,intime,title,intro,url});
	 	});
	 	if(page==1){
	 	total=d.result.total;
	 	}
	 	total-=40;
	 	if(total<40){
	 		num=total;
	 	}
	 	
	 	$.post("http://127.0.0.1:8080/api/saveCaiTong",{arr:JSON.stringify(arr)},function(r){
	 		console.log(page,arr.length,r);
	 		if(r=="true"){
	 			page++;
	 			$.getScript("http://feed.mix.sina.com.cn/api/roll/get?pageid=189&lid=1758&num="+num+"&versionNumber=1.2.8&page="+page+"&encode=utf-8&callback=feedCardJsonpCallback1111");
	 		}
	 	});
	 	}catch(e){
	 	console.log(e);
	 	}
	 }
	 
	 
	 *
	 */
	private void saveCaiTong(HttpServletRequest request, HttpServletResponse response)throws IOException {
		String arr = request.getParameter("arr");
		java.lang.reflect.Type type = new TypeToken<List<Map<String, String>>>() {
		}.getType();
		List<Map<String, String>> list = new Gson().fromJson(arr, type);
		StringBuilder sb = new StringBuilder();
		list.forEach(map->{
			//ctime,mtime,intime,title,intro,url
			String title = map.get("title");
			String ctime = map.get("ctime");
			String intro = map.get("intro");
			String url = map.get("url");
			String mtime = map.get("mtime");
			String intime = map.get("intime");
			sb.append(",("+ctime+","+mtime+","+intime+",'"+DBHelper.getSafeSqlParam(title)+"','"+DBHelper.getSafeSqlParam(intro)+"','"+url+"')");
		});
		boolean result = false;
		if(list.size()>0) {
			String sql = "insert into t_caitong(ctime,mtime,intime,title,intro,url) values " + sb.substring(1);
			result = DBHelper.insertOrUpdate(sql);
		}
		response.getWriter().write(""+result);
		
	}

	/**
	 * 从网上抓取 软件著作权信息
	 * @param request
	 * @param response
	 * @throws IOException
	 * 
	 * https://www.xaecong.com/zhuzuo.asp
	 var start=2,end=300376;
	 function getData(i){
return function(d){
var leftStart=d.indexOf("<DIV id=left>");
var rightStart=d.indexOf("<div id=\"right\">");
if(leftStart!=-1 && rightStart> leftStart){
d = d.substring(leftStart,rightStart);
var divs = $(d).find(".Listtitle");
var arr=[];
divs.each((idx,el)=>{
var a=$(el).find("a");
var c=$(el).next();
var title = a.attr("title");
var data={title:title.substring(0,title.length-5),id:a.attr("href").replace(/\D/g,""),content:c.text().split("。")[0]};
arr.push(data);
console.log(data)
});
$.post("http://127.0.0.1:8080/api/saveZhuzhuo",{arr:JSON.stringify(arr)},function(d1){
console.log("i="+i+",result="+d1)
});
console.log("已经处理完第"+i+"页，还剩下"+(end - i)+"页");
if(start < end){
start=start+1;
$.get("/zhuzuo.asp?page="+start,getData(start));
}
}
}
}
 $.get("/zhuzuo.asp?page="+start,getData(start));
	 * 
	 */
	private void saveZhuzhuo(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String arr = request.getParameter("arr");
		java.lang.reflect.Type type = new TypeToken<List<Map<String, String>>>() {
		}.getType();
		List<Map<String, String>> list = new Gson().fromJson(arr, type);
		StringBuilder sb = new StringBuilder();
		list.forEach(map->{
			String title = map.get("title");
			String id = map.get("id");
			String content = map.get("content");
			sb.append(",("+id+",'"+DBHelper.getSafeSqlParam(title)+"','"+DBHelper.getSafeSqlParam(content)+"')");
		});
		boolean result = false;
		if(list.size()>0) {
			String sql = "insert into t_zhuzhuo(oid,title,content) values " + sb.substring(1);
			result = DBHelper.insertOrUpdate(sql);
		}
		response.getWriter().write(""+result);
	}

	private void saveMatchScore(HttpServletRequest request, HttpServletResponse response)throws IOException {
		String data = request.getParameter("data");
		java.lang.reflect.Type type = new TypeToken<Map<String, String[]>>() {
		}.getType();
		Map<String, String[]> map = new Gson().fromJson(data, type);
		HashMap<String, Integer> resultMap = new HashMap<String, Integer>();
		map.forEach((key, value) -> {
			int result = DBHelper.insertOrUpdatePre(Const.UPDATE_MATCH_SCORE, value[0],value[1],value[2],value[3],Integer.valueOf(key));
			resultMap.put(key, result);
		});
		response.setHeader("Content-Type", "application/json; charset=UTF-8 "); // 设置响应头的编码
		response.getWriter().write(Const.GSON_DEFAULT.toJson(resultMap));
	}

	private void saveAsiaOdds(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String odds = request.getParameter("odds");
		java.lang.reflect.Type type = new TypeToken<Map<String, String[]>>() {
		}.getType();
		Map<String, String[]> map = new Gson().fromJson(odds, type);
		HashMap<String, Integer> resultMap = new HashMap<String, Integer>();
		map.forEach((key, value) -> {
			int result = DBHelper.insertOrUpdatePre(Const.MATCH_ODDS_ASIA, Integer.valueOf(key), value[0], value[1],
					value[2]);
			resultMap.put(key, result);
		});
		response.setHeader("Content-Type", "application/json; charset=UTF-8 "); // 设置响应头的编码
		response.getWriter().write(Const.GSON_DEFAULT.toJson(resultMap));
	}

	private void saveEuropeOdds(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		String odds = request.getParameter("odds");
		java.lang.reflect.Type type = new TypeToken<Map<String, String[]>>() {
		}.getType();
		Map<String, String[]> map = new Gson().fromJson(odds, type);
		HashMap<String, Integer> resultMap = new HashMap<String, Integer>();
		map.forEach((key, value) -> {
			int result = DBHelper.insertOrUpdatePre(Const.MATCH_ODDS_EUROPE, Integer.valueOf(key), value[0], value[1],
					value[2]);
			resultMap.put(key, result);
		});
		response.setHeader("Content-Type", "application/json; charset=UTF-8 "); // 设置响应头的编码
		response.getWriter().write(Const.GSON_DEFAULT.toJson(resultMap));
	}

	private void saveBolool(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// {"id":1114283,"leagueId":238,"leagueName":"葡超","leagueType":"league","homeId":11271,"homeName":"法马利康","awayId":217,"awayName":"摩雷伦斯","fullscore":"0-2","halfscore":"0-2",
		// "playtime":20210205053000,"result":"负","goalscore":0,"seasonId":14257,"seasonName":"20/21","round":"第17轮"}
		String matchJSON = request.getParameter("match");
		// "{\"h\":[{\"id\":1114275,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":139,\"homeName\":\"葡萄牙国民\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"2-1\",\"halfscore\":\"0-1\",\"playtime\":20210130233000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1114265,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":245,\"awayName\":\"吉马良斯\",\"fullscore\":\"0-1\",\"halfscore\":\"0-1\",\"playtime\":20210125041500,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1114259,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":274,\"homeName\":\"圣克拉拉\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"1-2\",\"halfscore\":\"0-0\",\"playtime\":20210118013000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1114247,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":138,\"awayName\":\"波尔图\",\"fullscore\":\"1-4\",\"halfscore\":\"1-2\",\"playtime\":20210109050000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1114246,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":12934,\"homeName\":\"通德拉\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"1-0\",\"halfscore\":\"0-0\",\"playtime\":20210103210000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1114229,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":41,\"awayName\":\"吉维森特\",\"fullscore\":\"0-1\",\"halfscore\":\"0-1\",\"playtime\":20201227230000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1114226,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":1329,\"homeName\":\"波尔蒂芒斯\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"0-0\",\"halfscore\":\"0-0\",\"playtime\":20201219043000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1122155,\"leagueId\":336,\"leagueName\":\"葡萄牙杯\",\"leagueType\":\"cup\",\"homeId\":666,\"homeName\":\"里奥阿维\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"2-1\",\"halfscore\":\"1-0\",\"playtime\":20201212230000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1114211,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":216,\"awayName\":\"里斯本竞技\",\"fullscore\":\"2-2\",\"halfscore\":\"1-2\",\"playtime\":20201206020000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1114203,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":250,\"homeName\":\"费雷拉\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"2-0\",\"halfscore\":\"1-0\",\"playtime\":20201128030000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1120556,\"leagueId\":336,\"leagueName\":\"葡萄牙杯\",\"leagueType\":\"cup\",\"homeId\":12936,\"homeName\":\"里斯本东方\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"0-3\",\"halfscore\":\"0-0\",\"playtime\":20201121191500,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1114193,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":252,\"awayName\":\"马里迪莫\",\"fullscore\":\"2-1\",\"halfscore\":\"2-1\",\"playtime\":20201108020000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1114188,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":256,\"homeName\":\"布拉加\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"1-0\",\"halfscore\":\"0-0\",\"playtime\":20201103024500,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1114175,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":165,\"awayName\":\"博阿维斯塔\",\"fullscore\":\"2-2\",\"halfscore\":\"0-0\",\"playtime\":20201026013000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1114172,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":12981,\"homeName\":\"法伦斯\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"3-3\",\"halfscore\":\"2-0\",\"playtime\":20201019003000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1114157,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":666,\"awayName\":\"里奥阿维\",\"fullscore\":\"1-1\",\"halfscore\":\"1-0\",\"playtime\":20201005013000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1114150,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":218,\"homeName\":\"比兰尼塞斯\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"1-2\",\"halfscore\":\"1-0\",\"playtime\":20200929024500,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1114139,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":233,\"awayName\":\"本菲卡\",\"fullscore\":\"1-5\",\"halfscore\":\"0-3\",\"playtime\":20200919020000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1094986,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":252,\"homeName\":\"马里迪莫\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"3-3\",\"halfscore\":\"1-1\",\"playtime\":20200726020000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1094976,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":165,\"awayName\":\"博阿维斯塔\",\"fullscore\":\"2-2\",\"halfscore\":\"2-0\",\"playtime\":20200719041500,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1094967,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":234,\"homeName\":\"塞图巴尔\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"1-2\",\"halfscore\":\"1-1\",\"playtime\":20200714041500,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1094961,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":233,\"awayName\":\"本菲卡\",\"fullscore\":\"1-1\",\"halfscore\":\"0-1\",\"playtime\":20200710043000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1094954,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":12934,\"homeName\":\"通德拉\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"0-1\",\"halfscore\":\"0-0\",\"playtime\":20200706021500,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1094943,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":1329,\"awayName\":\"波尔蒂芒斯\",\"fullscore\":\"0-1\",\"halfscore\":\"0-1\",\"playtime\":20200701000000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1094936,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"1-1\",\"halfscore\":\"0-0\",\"playtime\":20200625041500,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1094929,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":256,\"awayName\":\"布拉加\",\"fullscore\":\"0-0\",\"halfscore\":\"0-0\",\"playtime\":20200620041500,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1094912,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":41,\"homeName\":\"吉维森特\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"1-3\",\"halfscore\":\"0-2\",\"playtime\":20200610040000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1094905,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":138,\"awayName\":\"波尔图\",\"fullscore\":\"2-1\",\"halfscore\":\"0-0\",\"playtime\":20200604041500,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1066883,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":218,\"homeName\":\"比兰尼塞斯\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"0-0\",\"halfscore\":\"0-0\",\"playtime\":20200308230000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1066878,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":216,\"awayName\":\"里斯本竞技\",\"fullscore\":\"3-1\",\"halfscore\":\"2-1\",\"playtime\":20200304040000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1066865,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":250,\"homeName\":\"费雷拉\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"2-1\",\"halfscore\":\"0-0\",\"playtime\":20200223230000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1066860,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":1016,\"awayName\":\"阿维斯\",\"fullscore\":\"1-1\",\"halfscore\":\"0-0\",\"playtime\":20200217040000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1084416,\"leagueId\":336,\"leagueName\":\"葡萄牙杯\",\"leagueType\":\"cup\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":233,\"awayName\":\"本菲卡\",\"fullscore\":\"1-1\",\"halfscore\":\"0-1\",\"playtime\":20200212044500,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1066851,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":245,\"awayName\":\"吉马良斯\",\"fullscore\":\"0-7\",\"halfscore\":\"0-2\",\"playtime\":20200208233000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1084414,\"leagueId\":336,\"leagueName\":\"葡萄牙杯\",\"leagueType\":\"cup\",\"homeId\":233,\"homeName\":\"本菲卡\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"3-2\",\"halfscore\":\"0-0\",\"playtime\":20200205031500,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1066838,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":666,\"homeName\":\"里奥阿维\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"2-2\",\"halfscore\":\"0-2\",\"playtime\":20200201051500,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1066833,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":274,\"awayName\":\"圣克拉拉\",\"fullscore\":\"0-1\",\"halfscore\":\"0-1\",\"playtime\":20200126230000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1066820,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":252,\"awayName\":\"马里迪莫\",\"fullscore\":\"1-1\",\"halfscore\":\"0-1\",\"playtime\":20200120013000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1082455,\"leagueId\":336,\"leagueName\":\"葡萄牙杯\",\"leagueType\":\"cup\",\"homeId\":250,\"homeName\":\"费雷拉\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"0-1\",\"halfscore\":\"0-0\",\"playtime\":20200116040000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1066816,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":165,\"homeName\":\"博阿维斯塔\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"0-1\",\"halfscore\":\"0-0\",\"playtime\":20200112020000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1066802,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":234,\"awayName\":\"塞图巴尔\",\"fullscore\":\"3-0\",\"halfscore\":\"1-0\",\"playtime\":20200106040000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1080807,\"leagueId\":336,\"leagueName\":\"葡萄牙杯\",\"leagueType\":\"cup\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":12931,\"awayName\":\"马弗拉\",\"fullscore\":\"3-0\",\"halfscore\":\"0-0\",\"playtime\":20191220054500,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1066797,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":233,\"homeName\":\"本菲卡\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"4-0\",\"halfscore\":\"1-0\",\"playtime\":20191215020000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1066784,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":12934,\"awayName\":\"通德拉\",\"fullscore\":\"2-3\",\"halfscore\":\"1-2\",\"playtime\":20191208020000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1066778,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":1329,\"homeName\":\"波尔蒂芒斯\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"2-1\",\"halfscore\":\"1-0\",\"playtime\":20191201043000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1079628,\"leagueId\":336,\"leagueName\":\"葡萄牙杯\",\"leagueType\":\"cup\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":246,\"awayName\":\"科英布拉大学\",\"fullscore\":\"1-0\",\"halfscore\":\"1-0\",\"playtime\":20191124004500,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1066766,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"3-3\",\"halfscore\":\"2-0\",\"playtime\":20191110043000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1066759,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":256,\"homeName\":\"布拉加\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"2-2\",\"halfscore\":\"0-0\",\"playtime\":20191104041500,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1066748,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":41,\"awayName\":\"吉维森特\",\"fullscore\":\"2-1\",\"halfscore\":\"1-0\",\"playtime\":20191031050000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1066742,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":138,\"homeName\":\"波尔图\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"3-0\",\"halfscore\":\"1-0\",\"playtime\":20191028013000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1079020,\"leagueId\":336,\"leagueName\":\"葡萄牙杯\",\"leagueType\":\"cup\",\"homeId\":42637,\"homeName\":\"卢西塔尼亚\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"1-1\",\"halfscore\":\"0-0\",\"playtime\":20191020220000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1066730,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":218,\"awayName\":\"比兰尼塞斯\",\"fullscore\":\"3-1\",\"halfscore\":\"0-1\",\"playtime\":20190929043000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1066725,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":216,\"homeName\":\"里斯本竞技\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"1-2\",\"halfscore\":\"1-0\",\"playtime\":20190924040000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1066712,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":250,\"awayName\":\"费雷拉\",\"fullscore\":\"4-2\",\"halfscore\":\"1-0\",\"playtime\":20190914233000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1066707,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":1016,\"homeName\":\"阿维斯\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"2-3\",\"halfscore\":\"1-1\",\"playtime\":20190831233000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1066698,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":245,\"homeName\":\"吉马良斯\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"1-1\",\"halfscore\":\"0-0\",\"playtime\":20190826040000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1066685,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":666,\"awayName\":\"里奥阿维\",\"fullscore\":\"1-0\",\"halfscore\":\"0-0\",\"playtime\":20190817033000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1066680,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":274,\"homeName\":\"圣克拉拉\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"0-2\",\"halfscore\":\"0-1\",\"playtime\":20190810233000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1074213,\"leagueId\":327,\"leagueName\":\"葡联杯\",\"leagueType\":\"cup\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":2139,\"awayName\":\"科维良\",\"fullscore\":\"0-2\",\"halfscore\":\"0-2\",\"playtime\":20190804010000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1061910,\"leagueId\":853,\"leagueName\":\"球会友谊\",\"leagueType\":\"friend\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":666,\"awayName\":\"里奥阿维\",\"fullscore\":\"2-1\",\"halfscore\":\"1-0\",\"playtime\":20190728023000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1060773,\"leagueId\":853,\"leagueName\":\"球会友谊\",\"leagueType\":\"friend\",\"homeId\":1016,\"homeName\":\"阿维斯\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"0-1\",\"halfscore\":\"0-0\",\"playtime\":20190725030000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1060784,\"leagueId\":853,\"leagueName\":\"球会友谊\",\"leagueType\":\"friend\",\"homeId\":1020,\"homeName\":\"费伦斯\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"4-0\",\"halfscore\":\"-\",\"playtime\":20190720173000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1067410,\"leagueId\":853,\"leagueName\":\"球会友谊\",\"leagueType\":\"friend\",\"homeId\":245,\"homeName\":\"吉马良斯\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"5-2\",\"halfscore\":\"2-0\",\"playtime\":20190715010000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1066191,\"leagueId\":853,\"leagueName\":\"球会友谊\",\"leagueType\":\"friend\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":274,\"awayName\":\"圣克拉拉\",\"fullscore\":\"2-1\",\"halfscore\":\"1-1\",\"playtime\":20190711010000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034356,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":766,\"homeName\":\"埃斯托里尔\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"2-1\",\"halfscore\":\"1-1\",\"playtime\":20190519220000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1034347,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":2167,\"awayName\":\"奥利韦伦斯\",\"fullscore\":\"3-2\",\"halfscore\":\"3-1\",\"playtime\":20190512181500,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034335,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":249,\"homeName\":\"瓦兹姆\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"1-4\",\"halfscore\":\"1-2\",\"playtime\":20190504180000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034329,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":39782,\"awayName\":\"甘马雷斯B队\",\"fullscore\":\"4-1\",\"halfscore\":\"3-1\",\"playtime\":20190428181500,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034318,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":39779,\"homeName\":\"本菲卡B队\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"1-3\",\"halfscore\":\"1-3\",\"playtime\":20190421000000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034305,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":246,\"awayName\":\"科英布拉大学\",\"fullscore\":\"2-0\",\"halfscore\":\"1-0\",\"playtime\":20190413180000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034298,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":1511,\"homeName\":\"雷克斯欧斯\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"2-3\",\"halfscore\":\"1-1\",\"playtime\":20190407181500,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034295,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":12930,\"awayName\":\"科瓦皮达德\",\"fullscore\":\"3-1\",\"halfscore\":\"2-0\",\"playtime\":20190401010000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034284,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":2139,\"homeName\":\"科维良\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"2-0\",\"halfscore\":\"0-0\",\"playtime\":20190317230000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1034270,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":39778,\"awayName\":\"布拉加B队\",\"fullscore\":\"1-2\",\"halfscore\":\"0-1\",\"playtime\":20190309190000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1034263,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":39780,\"homeName\":\"波尔图B队\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"1-1\",\"halfscore\":\"0-0\",\"playtime\":20190303230000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1034257,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":12931,\"awayName\":\"马弗拉\",\"fullscore\":\"2-1\",\"halfscore\":\"2-0\",\"playtime\":20190223190000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034250,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":12908,\"awayName\":\"维塞乌\",\"fullscore\":\"3-2\",\"halfscore\":\"1-1\",\"playtime\":20190217010000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034238,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":2420,\"homeName\":\"阿罗卡\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"2-1\",\"halfscore\":\"1-0\",\"playtime\":20190211010000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1034232,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":250,\"awayName\":\"费雷拉\",\"fullscore\":\"0-1\",\"halfscore\":\"0-0\",\"playtime\":20190206041500,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1048166,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":764,\"homeName\":\"佩纳菲耶尔\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"1-2\",\"halfscore\":\"0-1\",\"playtime\":20190129030000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034222,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":12981,\"awayName\":\"法伦斯\",\"fullscore\":\"0-0\",\"halfscore\":\"0-0\",\"playtime\":20190119190000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1034212,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":766,\"awayName\":\"埃斯托里尔\",\"fullscore\":\"2-0\",\"halfscore\":\"0-0\",\"playtime\":20190115041500,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034200,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":249,\"awayName\":\"瓦兹姆\",\"fullscore\":\"0-0\",\"halfscore\":\"0-0\",\"playtime\":20190106191500,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1047084,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":39782,\"homeName\":\"甘马雷斯B队\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"0-1\",\"halfscore\":\"0-1\",\"playtime\":20190102230000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034192,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":39779,\"awayName\":\"本菲卡B队\",\"fullscore\":\"0-0\",\"halfscore\":\"0-0\",\"playtime\":20181222190000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1034179,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":246,\"homeName\":\"科英布拉大学\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"0-1\",\"halfscore\":\"0-0\",\"playtime\":20181218041500,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034172,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":1511,\"awayName\":\"雷克斯欧斯\",\"fullscore\":\"1-0\",\"halfscore\":\"1-0\",\"playtime\":20181208230000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034169,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":12930,\"homeName\":\"科瓦皮达德\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"0-0\",\"halfscore\":\"0-0\",\"playtime\":20181201230000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1047999,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":2167,\"homeName\":\"奥利韦伦斯\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"1-2\",\"halfscore\":\"0-1\",\"playtime\":20181119010000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034158,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":2139,\"awayName\":\"科维良\",\"fullscore\":\"2-1\",\"halfscore\":\"0-1\",\"playtime\":20181111230000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034144,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":39778,\"homeName\":\"布拉加B队\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"2-0\",\"halfscore\":\"0-0\",\"playtime\":20181103230000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1034137,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":39780,\"awayName\":\"波尔图B队\",\"fullscore\":\"4-2\",\"halfscore\":\"0-2\",\"playtime\":20181027180000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034131,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":12931,\"homeName\":\"马弗拉\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"1-1\",\"halfscore\":\"0-1\",\"playtime\":20181006230000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1047020,\"leagueId\":336,\"leagueName\":\"葡萄牙杯\",\"leagueType\":\"cup\",\"homeId\":142431,\"homeName\":\"阿格达\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"1-0\",\"halfscore\":\"1-0\",\"playtime\":20180930220000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1034124,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":12908,\"homeName\":\"维塞乌\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"3-4\",\"halfscore\":\"1-4\",\"playtime\":20180923230000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1045372,\"leagueId\":853,\"leagueName\":\"球会友谊\",\"leagueType\":\"friend\",\"homeId\":245,\"homeName\":\"吉马良斯\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"1-1\",\"halfscore\":\"0-1\",\"playtime\":20180908173000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1034112,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":2420,\"awayName\":\"阿罗卡\",\"fullscore\":\"2-0\",\"halfscore\":\"1-0\",\"playtime\":20180903000000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034106,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":250,\"homeName\":\"费雷拉\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"1-2\",\"halfscore\":\"1-2\",\"playtime\":20180827000000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034092,\"leagueId\":239,\"leagueName\":\"葡甲\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":764,\"awayName\":\"佩纳菲耶尔\",\"fullscore\":\"2-0\",\"halfscore\":\"1-0\",\"playtime\":20180820000000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3}],\"a\":[{\"id\":1114278,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":256,\"awayName\":\"布拉加\",\"fullscore\":\"0-4\",\"halfscore\":\"0-3\",\"playtime\":20210202034500,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1114271,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":1329,\"awayName\":\"波尔蒂芒斯\",\"fullscore\":\"2-2\",\"halfscore\":\"2-1\",\"playtime\":20210125013000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1114258,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":139,\"homeName\":\"葡萄牙国民\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"0-1\",\"halfscore\":\"0-0\",\"playtime\":20210117230000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1123117,\"leagueId\":336,\"leagueName\":\"葡萄牙杯\",\"leagueType\":\"cup\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":274,\"awayName\":\"圣克拉拉\",\"fullscore\":\"1-2\",\"halfscore\":\"0-1\",\"playtime\":20210113010000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1114253,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":245,\"awayName\":\"吉马良斯\",\"fullscore\":\"2-2\",\"halfscore\":\"1-1\",\"playtime\":20210110010000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1114243,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":138,\"homeName\":\"波尔图\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"3-0\",\"halfscore\":\"1-0\",\"playtime\":20210104050000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1114235,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":274,\"awayName\":\"圣克拉拉\",\"fullscore\":\"1-0\",\"halfscore\":\"0-0\",\"playtime\":20201230024500,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1114228,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":12934,\"homeName\":\"通德拉\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"0-0\",\"halfscore\":\"0-0\",\"playtime\":20201220020000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1122158,\"leagueId\":336,\"leagueName\":\"葡萄牙杯\",\"leagueType\":\"cup\",\"homeId\":12930,\"homeName\":\"科瓦皮达德\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"1-1\",\"halfscore\":\"1-1\",\"playtime\":20201213190000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1114216,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":41,\"awayName\":\"吉维森特\",\"fullscore\":\"1-1\",\"halfscore\":\"0-0\",\"playtime\":20201205233000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1121009,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":250,\"awayName\":\"费雷拉\",\"fullscore\":\"0-1\",\"halfscore\":\"0-1\",\"playtime\":20201202054500,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1114209,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":216,\"homeName\":\"里斯本竞技\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"2-1\",\"halfscore\":\"1-1\",\"playtime\":20201129043000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1120557,\"leagueId\":336,\"leagueName\":\"葡萄牙杯\",\"leagueType\":\"cup\",\"homeId\":2468,\"homeName\":\"梅雷林斯\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"0-1\",\"halfscore\":\"0-0\",\"playtime\":20201122223000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1114190,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":666,\"homeName\":\"里奥阿维\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"2-0\",\"halfscore\":\"1-0\",\"playtime\":20201101020000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1114180,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":252,\"awayName\":\"马里迪莫\",\"fullscore\":\"2-1\",\"halfscore\":\"2-0\",\"playtime\":20201025230000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1114168,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":218,\"homeName\":\"比兰尼塞斯\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"0-0\",\"halfscore\":\"0-0\",\"playtime\":20201018220000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1114162,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":165,\"awayName\":\"博阿维斯塔\",\"fullscore\":\"1-1\",\"halfscore\":\"0-1\",\"playtime\":20201003020000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1114151,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":233,\"homeName\":\"本菲卡\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"2-0\",\"halfscore\":\"1-0\",\"playtime\":20200927013000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1114143,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":12981,\"awayName\":\"法伦斯\",\"fullscore\":\"2-0\",\"halfscore\":\"1-0\",\"playtime\":20200921013000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1116728,\"leagueId\":853,\"leagueName\":\"球会友谊\",\"leagueType\":\"friend\",\"homeId\":245,\"homeName\":\"吉马良斯\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"0-0\",\"halfscore\":\"0-0\",\"playtime\":20200910031500,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1094992,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":12934,\"awayName\":\"通德拉\",\"fullscore\":\"1-2\",\"halfscore\":\"0-1\",\"playtime\":20200727023000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1094981,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":138,\"homeName\":\"波尔图\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"6-1\",\"halfscore\":\"1-1\",\"playtime\":20200721041500,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1094972,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":250,\"awayName\":\"费雷拉\",\"fullscore\":\"1-1\",\"halfscore\":\"1-0\",\"playtime\":20200716000000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1094965,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":218,\"homeName\":\"比兰尼塞斯\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"0-1\",\"halfscore\":\"0-0\",\"playtime\":20200712021500,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1094956,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":216,\"awayName\":\"里斯本竞技\",\"fullscore\":\"0-0\",\"halfscore\":\"0-0\",\"playtime\":20200707040000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1094940,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":1016,\"homeName\":\"阿维斯\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"0-1\",\"halfscore\":\"0-0\",\"playtime\":20200630000000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1094936,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":11271,\"awayName\":\"法马利康\",\"fullscore\":\"1-1\",\"halfscore\":\"0-0\",\"playtime\":20200625041500,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1094928,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":245,\"homeName\":\"吉马良斯\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"1-1\",\"halfscore\":\"1-0\",\"playtime\":20200620020000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1094918,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":666,\"awayName\":\"里奥阿维\",\"fullscore\":\"0-1\",\"halfscore\":\"0-1\",\"playtime\":20200613020000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1094911,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":165,\"homeName\":\"博阿维斯塔\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"0-1\",\"halfscore\":\"0-0\",\"playtime\":20200607041500,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1066887,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":252,\"awayName\":\"马里迪莫\",\"fullscore\":\"2-0\",\"halfscore\":\"1-0\",\"playtime\":20200308230000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1066879,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":233,\"homeName\":\"本菲卡\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"1-1\",\"halfscore\":\"0-0\",\"playtime\":20200303044500,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1066867,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":274,\"awayName\":\"圣克拉拉\",\"fullscore\":\"2-1\",\"halfscore\":\"1-1\",\"playtime\":20200223230000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1066859,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":1329,\"homeName\":\"波尔蒂芒斯\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"1-1\",\"halfscore\":\"1-1\",\"playtime\":20200215233000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1066855,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":234,\"awayName\":\"塞图巴尔\",\"fullscore\":\"1-1\",\"halfscore\":\"0-1\",\"playtime\":20200209230000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1066843,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":41,\"homeName\":\"吉维森特\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"1-5\",\"halfscore\":\"0-2\",\"playtime\":20200202230000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1066830,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":256,\"awayName\":\"布拉加\",\"fullscore\":\"1-2\",\"halfscore\":\"0-2\",\"playtime\":20200130041500,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1066828,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":12934,\"homeName\":\"通德拉\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"1-1\",\"halfscore\":\"0-0\",\"playtime\":20200119020000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1066814,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":138,\"awayName\":\"波尔图\",\"fullscore\":\"2-4\",\"halfscore\":\"2-2\",\"playtime\":20200111051500,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1066803,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":250,\"homeName\":\"费雷拉\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"1-0\",\"halfscore\":\"1-0\",\"playtime\":20200105230000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1066800,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":218,\"awayName\":\"比兰尼塞斯\",\"fullscore\":\"2-1\",\"halfscore\":\"0-1\",\"playtime\":20191215230000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1066792,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":216,\"homeName\":\"里斯本竞技\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"1-0\",\"halfscore\":\"0-0\",\"playtime\":20191209013000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1066780,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":1016,\"awayName\":\"阿维斯\",\"fullscore\":\"3-2\",\"halfscore\":\"2-1\",\"playtime\":20191130233000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1079620,\"leagueId\":336,\"leagueName\":\"葡萄牙杯\",\"leagueType\":\"cup\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":12931,\"awayName\":\"马弗拉\",\"fullscore\":\"1-3\",\"halfscore\":\"0-1\",\"playtime\":20191124230000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1066766,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"3-3\",\"halfscore\":\"2-0\",\"playtime\":20191110043000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1066764,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":245,\"awayName\":\"吉马良斯\",\"fullscore\":\"1-1\",\"halfscore\":\"0-0\",\"playtime\":20191103043000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1066756,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":666,\"homeName\":\"里奥阿维\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"1-1\",\"halfscore\":\"0-1\",\"playtime\":20191031010000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1066745,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":165,\"awayName\":\"博阿维斯塔\",\"fullscore\":\"1-1\",\"halfscore\":\"1-0\",\"playtime\":20191027033000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1079033,\"leagueId\":336,\"leagueName\":\"葡萄牙杯\",\"leagueType\":\"cup\",\"homeId\":40374,\"homeName\":\"巴列罗\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"1-3\",\"halfscore\":\"0-1\",\"playtime\":20191019220000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1066734,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":252,\"homeName\":\"马里迪莫\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"2-1\",\"halfscore\":\"0-0\",\"playtime\":20190928233000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1066726,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":233,\"awayName\":\"本菲卡\",\"fullscore\":\"1-2\",\"halfscore\":\"0-0\",\"playtime\":20190922033000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1066714,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":274,\"homeName\":\"圣克拉拉\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"2-0\",\"halfscore\":\"1-0\",\"playtime\":20190915230000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1066706,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":1329,\"awayName\":\"波尔蒂芒斯\",\"fullscore\":\"1-0\",\"halfscore\":\"1-0\",\"playtime\":20190831020000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1066702,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":234,\"homeName\":\"塞图巴尔\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"0-0\",\"halfscore\":\"0-0\",\"playtime\":20190824020000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1066690,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":41,\"awayName\":\"吉维森特\",\"fullscore\":\"3-0\",\"halfscore\":\"2-0\",\"playtime\":20190817233000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1066677,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":256,\"homeName\":\"布拉加\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"3-1\",\"halfscore\":\"1-0\",\"playtime\":20190812040000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1068748,\"leagueId\":327,\"leagueName\":\"葡联杯\",\"leagueType\":\"cup\",\"homeId\":234,\"homeName\":\"塞图巴尔\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"1-0\",\"halfscore\":\"0-0\",\"playtime\":20190803230000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1072754,\"leagueId\":853,\"leagueName\":\"球会友谊\",\"leagueType\":\"friend\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":250,\"awayName\":\"费雷拉\",\"fullscore\":\"1-0\",\"halfscore\":\"0-0\",\"playtime\":20190728010000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1072766,\"leagueId\":853,\"leagueName\":\"球会友谊\",\"leagueType\":\"friend\",\"homeId\":41,\"homeName\":\"吉维森特\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"1-1\",\"halfscore\":\"1-0\",\"playtime\":20190724173000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1034663,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":245,\"awayName\":\"吉马良斯\",\"fullscore\":\"1-3\",\"halfscore\":\"1-1\",\"playtime\":20190520030000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1034652,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":1016,\"homeName\":\"阿维斯\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"0-1\",\"halfscore\":\"0-0\",\"playtime\":20190511033000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034640,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":666,\"awayName\":\"里奥阿维\",\"fullscore\":\"1-2\",\"halfscore\":\"1-1\",\"playtime\":20190504033000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1034632,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":165,\"homeName\":\"博阿维斯塔\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"3-1\",\"halfscore\":\"1-0\",\"playtime\":20190429030000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1034625,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":1012,\"awayName\":\"沙维什\",\"fullscore\":\"0-1\",\"halfscore\":\"0-0\",\"playtime\":20190420223000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1034613,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":274,\"homeName\":\"圣克拉拉\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"1-1\",\"halfscore\":\"1-1\",\"playtime\":20190413223000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1034610,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":256,\"awayName\":\"布拉加\",\"fullscore\":\"1-0\",\"halfscore\":\"0-0\",\"playtime\":20190407033000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034599,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":1329,\"homeName\":\"波尔蒂芒斯\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"0-2\",\"halfscore\":\"0-1\",\"playtime\":20190330043000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034591,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":233,\"awayName\":\"本菲卡\",\"fullscore\":\"0-4\",\"halfscore\":\"0-2\",\"playtime\":20190318013000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1034577,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":252,\"homeName\":\"马里迪莫\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"3-2\",\"halfscore\":\"1-2\",\"playtime\":20190309233000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1034568,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":234,\"awayName\":\"塞图巴尔\",\"fullscore\":\"1-1\",\"halfscore\":\"1-1\",\"playtime\":20190303020000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1034559,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":1020,\"homeName\":\"费伦斯\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"1-3\",\"halfscore\":\"0-2\",\"playtime\":20190223233000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034549,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":12934,\"awayName\":\"通德拉\",\"fullscore\":\"2-0\",\"halfscore\":\"1-0\",\"playtime\":20190217230000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034541,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":138,\"awayName\":\"波尔图\",\"fullscore\":\"1-1\",\"halfscore\":\"0-0\",\"playtime\":20190209043000,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1034536,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":218,\"homeName\":\"比兰尼塞斯\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"0-1\",\"halfscore\":\"0-0\",\"playtime\":20190205041500,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034522,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":139,\"awayName\":\"葡萄牙国民\",\"fullscore\":\"2-1\",\"halfscore\":\"1-0\",\"playtime\":20190129030000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034520,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":216,\"homeName\":\"里斯本竞技\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"2-1\",\"halfscore\":\"2-1\",\"playtime\":20190120020000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1034508,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":245,\"homeName\":\"吉马良斯\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"1-0\",\"halfscore\":\"0-0\",\"playtime\":20190112051500,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1034499,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":1016,\"awayName\":\"阿维斯\",\"fullscore\":\"1-0\",\"halfscore\":\"0-0\",\"playtime\":20190108010000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034488,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":666,\"homeName\":\"里奥阿维\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"1-2\",\"halfscore\":\"1-0\",\"playtime\":20190103041500,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034476,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":165,\"awayName\":\"博阿维斯塔\",\"fullscore\":\"2-1\",\"halfscore\":\"2-1\",\"playtime\":20181223043000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1048603,\"leagueId\":336,\"leagueName\":\"葡萄牙杯\",\"leagueType\":\"cup\",\"homeId\":138,\"homeName\":\"波尔图\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"4-3\",\"halfscore\":\"2-2\",\"playtime\":20181219044500,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1034472,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":1012,\"homeName\":\"沙维什\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"1-2\",\"halfscore\":\"0-0\",\"playtime\":20181215233000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034461,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":274,\"awayName\":\"圣克拉拉\",\"fullscore\":\"0-1\",\"halfscore\":\"0-0\",\"playtime\":20181209230000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1034454,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":256,\"homeName\":\"布拉加\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"2-0\",\"halfscore\":\"2-0\",\"playtime\":20181202043000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1047318,\"leagueId\":336,\"leagueName\":\"葡萄牙杯\",\"leagueType\":\"cup\",\"homeId\":2139,\"homeName\":\"科维良\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"0-3\",\"halfscore\":\"0-0\",\"playtime\":20181125223000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034448,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":1329,\"awayName\":\"波尔蒂芒斯\",\"fullscore\":\"2-0\",\"halfscore\":\"1-0\",\"playtime\":20181110030000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034438,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":233,\"homeName\":\"本菲卡\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"1-3\",\"halfscore\":\"1-3\",\"playtime\":20181103043000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034424,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":252,\"awayName\":\"马里迪莫\",\"fullscore\":\"1-0\",\"halfscore\":\"0-0\",\"playtime\":20181028230000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034415,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":234,\"homeName\":\"塞图巴尔\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"3-0\",\"halfscore\":\"1-0\",\"playtime\":20181006223000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1034406,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":1020,\"awayName\":\"费伦斯\",\"fullscore\":\"1-0\",\"halfscore\":\"0-0\",\"playtime\":20180929233000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034396,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":12934,\"homeName\":\"通德拉\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"2-0\",\"halfscore\":\"1-0\",\"playtime\":20180923230000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1034388,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":138,\"homeName\":\"波尔图\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"3-0\",\"halfscore\":\"2-0\",\"playtime\":20180903033000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1034383,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":218,\"awayName\":\"比兰尼塞斯\",\"fullscore\":\"1-1\",\"halfscore\":\"0-0\",\"playtime\":20180828031500,\"result\":\"平\",\"goalscore\":1,\"hresult\":\"平\",\"hgoalscore\":1},{\"id\":1034369,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":139,\"homeName\":\"葡萄牙国民\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"1-2\",\"halfscore\":\"0-1\",\"playtime\":20180819230000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1034362,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":216,\"awayName\":\"里斯本竞技\",\"fullscore\":\"1-3\",\"halfscore\":\"1-1\",\"playtime\":20180813013000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1038719,\"leagueId\":327,\"leagueName\":\"葡联杯\",\"leagueType\":\"cup\",\"homeId\":249,\"homeName\":\"瓦兹姆\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"2-0\",\"halfscore\":\"1-0\",\"playtime\":20180730000000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":1038874,\"leagueId\":853,\"leagueName\":\"球会友谊\",\"leagueType\":\"friend\",\"homeId\":11271,\"homeName\":\"法马利康\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"0-2\",\"halfscore\":\"0-1\",\"playtime\":20180725170000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"胜\",\"hgoalscore\":3},{\"id\":1038601,\"leagueId\":853,\"leagueName\":\"球会友谊\",\"leagueType\":\"friend\",\"homeId\":217,\"homeName\":\"摩雷伦斯\",\"awayId\":165,\"awayName\":\"博阿维斯塔\",\"fullscore\":\"0-3\",\"halfscore\":\"0-1\",\"playtime\":20180722010000,\"result\":\"负\",\"goalscore\":0,\"hresult\":\"负\",\"hgoalscore\":0},{\"id\":967644,\"leagueId\":238,\"leagueName\":\"葡超\",\"leagueType\":\"league\",\"homeId\":233,\"homeName\":\"本菲卡\",\"awayId\":217,\"awayName\":\"摩雷伦斯\",\"fullscore\":\"1-0\",\"halfscore\":\"0-0\",\"playtime\":20180514010000,\"result\":\"胜\",\"goalscore\":3,\"hresult\":\"负\",\"hgoalscore\":0}]}"
		String matchlistJSON = request.getParameter("matchlist");
		// {"top33":{"hscore":40,"hsection":6,"hresult":"负负胜负负负平负平负胜胜负平平平胜负平平胜平胜负平平胜胜平胜负平平","hstrong":"弱","ascore":43,"asection":6,"aresult":"负平胜负平负胜平平平负负胜负胜平平负胜负负平胜平胜平平负胜胜平胜平","id":1114283},"top30":{"hscore":38,"hsection":6,"hresult":"负负胜负负负平负平负胜胜负平平平胜负平平胜平胜负平平胜胜平胜","hstrong":"强","ascore":36,"asection":6,"aresult":"负平胜负平负胜平平平负负胜负胜平平负胜平负负平胜平胜平平负胜","id":1114283}}}
		String boloolJSON = request.getParameter("bolool");
//		System.out.println(matchJSON);
//		System.out.println(matchlistJSON);
//		System.out.println(boloolJSON);

		Match match = Const.YMDHMS_GSON.fromJson(matchJSON, Match.class);
		Map<String, Object> map = new Gson().fromJson(boloolJSON, Map.class);
		HashMap<String, Bolool> boloolData = new HashMap<String, Bolool>();
		int boloolCount = 0;
		map.forEach((key, value) -> {
			Bolool bolool = new Gson().fromJson(new Gson().toJson(value), Bolool.class);
			boloolData.put(key, bolool);
			// id,hscore,ascore,hresult,aresult,hsection,asection
		});
		Bolool bolool = boloolData.get("top30");
		boloolCount += DBHelper.insertOrUpdatePre(Const.BOLOOL_SQL30, bolool.getId(), bolool.getHscore(),
				bolool.getAscore(), bolool.getHresult(), bolool.getAresult(), bolool.getHsection(),
				bolool.getAsection());
		bolool = boloolData.get("top33");
		boloolCount += DBHelper.insertOrUpdatePre(Const.BOLOOL_SQL33, bolool.getId(), bolool.getHscore(),
				bolool.getAscore(), bolool.getHresult(), bolool.getAresult(), bolool.getHsection(),
				bolool.getAsection());
		int matchCount = DBHelper.insertOrUpdatePre(Const.MATCH_SQL, match.getId(), match.getLeagueId(),
				match.getLeagueName(), match.getLeagueType(), match.getSeasonId(), match.getSeasonName(),
				match.getRound(), match.getHomeId(), match.getHomeName(), match.getAwayId(), match.getAwayName(),
				match.getPlaytime(), match.getFullscore(), match.getHalfscore(), match.getResult(),
				match.getGoalscore());

		int historyCount = DBHelper.insertOrUpdatePre(Const.MATCH_HISTORY_SQL, match.getId(), matchlistJSON);
		response.setHeader("Content-Type", "application/json; charset=UTF-8 "); // 设置响应头的编码
		response.getWriter().write("{\"matchCount\":" + matchCount + ",\"historyCount\":" + historyCount
				+ ",\"boloolCount\":" + boloolCount + "}");
	}

	private void getBoloolList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String europe = request.getParameter("europe");
		String asia = request.getParameter("asia");
		String topN = request.getParameter("topN");
//		String hsection = request.getParameter("hsection");
//		String asection = request.getParameter("asection");
		if (StringUtils.isBlank(europe) && StringUtils.isBlank(asia)) {
			response.getWriter().write("参数不符合要求");
			return;
		}
		if (topN == null || !StringUtils.isNumeric(topN)) {
			response.getWriter().write("参数不符合要求");
			return;
		}
		String europeArr[] = DBHelper.getSafeSqlParam(europe).split(" ");
		String asiaArr[] = DBHelper.getSafeSqlParam(asia).split(" ");
		if (europeArr.length != 3 && asiaArr.length != 3) {
			response.getWriter().write("参数不符合要求");
			return;
		}

		String column = "id,leagueName,leagueId,seasonName,round,playtime,homeName,homeId,awayId,awayName,fullscore,halfscore,s,p,f,h,pan,a,hresult,aresult,hscore,ascore,hsection,asection";
		String sql = "select m." + column
				+ " from t_match_odds o left join t_match m on o.matchId = m.id left join t_bolool" + topN
				+ " b on m.id = b.id ";
//		String column = "id,leagueName,leagueId,seasonName,round,playtime,homeName,homeId,awayId,awayName,fullscore,halfscore,s,p,f,h,pan,a,matchlist";
//		String sql = "select m."+column+" from t_match m left join t_match_odds o on m.id = o.matchId left join t_match_history h on m.id = h.id ";
		if (europeArr.length == 3 && asiaArr.length == 3) {
			String sql1 = sql + " where o.s = " + europeArr[0] + " and o.p = " + europeArr[1] + " and o.f = "
					+ europeArr[2];
			String sql2 = sql + " where o.h = " + asiaArr[0] + " and o.pan = '" + asiaArr[1] + "' and o.a="
					+ asiaArr[2];
			sql = "select * from ( " + sql1 + " union all " + sql2 + ") t where leagueName is not null and  fullscore<>'-' ";
		} else if (asiaArr.length == 3) {
			sql += " where o.h = " + asiaArr[0] + " and o.pan = '" + asiaArr[1] + "' and o.a=" + asiaArr[2] + " and leagueName is not null and  fullscore<>'-' ";
		} else {
			sql += " where o.s = " + europeArr[0] + " and o.p = " + europeArr[1] + " and o.f = " + europeArr[2]+ " and leagueName is not null and  fullscore<>'-' ";
		}
		List<HashMap<String, String>> list = DBHelper.selectListSql(sql + "  order by concat(h,pan,f,s,p,f) ", column.split(","));
		response.setHeader("Content-Type", "application/json; charset=UTF-8 "); // 设置响应头的编码
		response.getWriter().write(new Gson().toJson(list));
	}

	/**
	 * 根据ids从数据库里查菠萝指数数据
	 * 
	 * @param request
	 * @param response
	 */
	private void getMatchHistory(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String ids = request.getParameter("matchIds");
		if (StringUtils.isEmpty(ids)) {
			response.getWriter().write("参数不符合要求");
		} else {
			ids = DBHelper.getSafeSqlParam(ids);
			String column = "id,leagueName,leagueId,seasonName,round,playtime,homeName,homeId,awayId,awayName,fullscore,halfscore,matchlist";
			String sql = "select m." + column
					+ " from t_match m left join t_match_history h on m.id = h.id where m.id in (" + ids + ")";
			List<HashMap<String, String>> historyMatchList = DBHelper.selectListSql(sql, column.split(","));
			response.setHeader("Content-Type", "application/json; charset=UTF-8 "); // 设置响应头的编码
			response.getWriter().write(new Gson().toJson(historyMatchList));
		}

	}

	/**
	 * 根据ids从数据库里查菠萝指数数据
	 * 
	 * @param request
	 * @param response
	 */
	private void getMatchHistoryNew(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String ids = request.getParameter("matchIds");
		String topN = request.getParameter("topN");
		if (StringUtils.isBlank(topN)) {
			topN = "30";
		}
		if (StringUtils.isEmpty(ids)) {
			response.getWriter().write("参数不符合要求");
		} else {
			ids = DBHelper.getSafeSqlParam(ids);
			String column = "id,s,p,f,h,pan,a,hresult,aresult,hscore,ascore,hsection,asection";
			String sql = "select o.matchId as " + column + " from  t_match_odds o left join t_bolool" + topN
					+ " b on o.matchId = b.id where o.matchId in (" + ids + ") and companyId=27";
			List<HashMap<String, String>> historyMatchList = DBHelper.selectListSql(sql, column.split(","));
			response.setHeader("Content-Type", "application/json; charset=UTF-8 "); // 设置响应头的编码
			response.getWriter().write(new Gson().toJson(historyMatchList));
		}

	}

	/**
	 * 根据ids从数据库里查菠萝指数数据
	 * 
	 * @param request
	 * @param response
	 */
	private void getBoloolByIds(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String ids = request.getParameter("ids");
		String topN = request.getParameter("topN");
		if (StringUtils.isBlank(topN)) {
			topN = "30";
		}
		if (StringUtils.isEmpty(ids)) {
			response.getWriter().write("参数不符合要求");
		} else {
			ids = DBHelper.getSafeSqlParam(ids);
			String column = "id,hresult,aresult,hscore,ascore,hsection,asection";
			String sql = "select " + column + " from t_bolool" + topN + " b  where b.id in (" + ids + ") ";
			List<HashMap<String, String>> boloolList = DBHelper.selectListSql(sql, column.split(","));
			response.setHeader("Content-Type", "application/json; charset=UTF-8 "); // 设置响应头的编码
			response.getWriter().write(new Gson().toJson(boloolList));
		}

	}

	/**
	 * 从win007网上获取xml,txt 竞彩数据并返回，
	 * 
	 * @param response
	 * @param cacheMap
	 * @param uri
	 * @throws IOException
	 */
	private void getWin007XmlTxt(HttpServletResponse response, HashMap<String, Object> cacheMap, String uri)
			throws IOException {
		String content = null;
		if (cacheMap.get(uri) == null
				|| (Long) cacheMap.get(uri + "_datetime") < System.currentTimeMillis() - Const.WEB_CACHE_SECONDS) {
			content = HttpUtil.getFromWin007(uri);
			cacheMap.put(uri, content);
			cacheMap.put(uri + "_datetime", System.currentTimeMillis());
		} else {
			content = (String) cacheMap.get(uri);
		}
		response.getWriter().write(content);
	}

	/**
	 * 判断用户登录
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void userLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String userName = request.getParameter("userName");
		String userPwd = request.getParameter("userPwd");
		String userMobile = request.getParameter("userMobile");
		String msg = null;
		if (userName == null || userName.length() < 3 || userPwd == null || userPwd.length() < 3 || userMobile == null
				|| userMobile.length() < 3) {
			msg = "参数不符合要求";
			response.getWriter().write(msg);
		} else {
			User user = doLogin(userName, userPwd, userMobile);
			if (user == null) {
				msg = "找不到用户";
			} else if (user.getUserStatus().intValue() == 0) {
				msg = "用户未激活，请联系管理员激活";
			} else if (user.getUserStatus().intValue() == 2) {
				msg = "用户异常，请联系管理员";
			} else if (user.getLeidaExpire().getTime() < System.currentTimeMillis()) {
				msg = "用户已经过期，请联系管理员";
			}
			if (msg == null) {
				msg = "登录成功，可以使用会员所有功能;" + user;
				request.getSession().setAttribute("user", user);
			}
			response.getWriter().write(msg);
		}
	}

	/**
	 * 保存用户信息
	 */
	private void saveConfig(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String msg;
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			msg = "这属于高级功能，只有会员用户才能使用";
			response.getWriter().write(msg);
			return;
		}
		String firstMoney = request.getParameter("firstMoney");
		String commission = request.getParameter("commission");
		String earningsYield = request.getParameter("earningsYield");
		if (!StringUtils.isNumeric(firstMoney) || !StringUtils.isNumeric(commission)
				|| !StringUtils.isNumeric(earningsYield)) {
			msg = "参数不符合要求";
			response.getWriter().write(msg);
		} else {
			Integer fm = Integer.parseInt(firstMoney);
			Integer cs = Integer.parseInt(commission);
			Integer ey = Integer.parseInt(earningsYield);
			String sql = "update t_user set firstMoney=" + fm + ",commission=" + cs + ",earningsYield=" + ey
					+ " where id = " + user.getId();
			if (DBHelper.insertOrUpdate(sql)) {
				msg = "保存成功";
			} else {
				msg = "保存失败";
			}
			response.getWriter().write(msg);
		}
	}

	/**
	 * 查看我的订单
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	private void getMyOrder(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String msg;
		int count = 0;
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			msg = "这属于高级功能，只有会员用户才能使用";
			response.getWriter().write(msg);
			return;
		}
		String totalCountSql = "select count(*) as c from t_user_order where userId=" + user.getId();
		count = Integer.parseInt(DBHelper.selectListSql(totalCountSql).get(0));
		String columns = "id,money,earningsYield,odds,isWin,inserttime";
		String pageNoStr = request.getParameter("pageNo");
		String pageSizeStr = request.getParameter("pageSize");
		int pageSize = 50;
		int pageNo = 1;
		if (StringUtils.isNumeric(pageSizeStr)) {
			pageSize = Integer.parseInt(pageSizeStr);
		}
		if (StringUtils.isNumeric(pageNoStr)) {
			pageNo = Integer.parseInt(pageNoStr);
		}
		String sql = "select " + columns + " from t_user_order where userId= " + user.getId()
				+ " order by id desc limit " + ((pageNo - 1) * pageSize) + "," + pageSize;
		List<HashMap<String, String>> list = DBHelper.selectListSql(sql, columns.split(","));
//			List<UserOrder> listOrder = new ArrayList<UserOrder>(list.size());
//			list.forEach(map->{
//				String id = map.get("id");
//				String money = map.get("money");
//				String earningsYield = map.get("earningsYield");
//				String odds = map.get("odds");
//				String isWin = map.get("isWin");
//				String inserttime = map.get("inserttime");
//				UserOrder order = new UserOrder(id,money,earningsYield,odds,isWin,inserttime);
//				listOrder.add(order);
//			});
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("totalCount", count);
		data.put("pageSize", pageSize);
		data.put("pageNo", pageNo);
		data.put("list", list);
		response.setHeader("Content-Type", "application/json; charset=UTF-8 "); // 设置响应头的编码
		response.getWriter().write(new Gson().toJson(data));
	}

	/**
	 * 从okooo网上获取数据并返回
	 * 
	 * @param request
	 * @param response
	 * @param cacheMap
	 * @throws IOException
	 */
	private void getOkoooJc(HttpServletRequest request, HttpServletResponse response, HashMap<String, Object> cacheMap)
			throws IOException {
		String url = request.getParameter("url");
		String args = request.getParameter("args");
		String method = request.getParameter("method");
		String content = null;
		String cacheKey = url + args + method;
		if (cacheMap.get(cacheKey) == null
				|| (Long) cacheMap.get(cacheKey + "_datetime") < System.currentTimeMillis() - Const.WEB_CACHE_SECONDS) {
			content = HttpUtil.getFromOkooo(url, method, args);
			cacheMap.put(cacheKey, content);
			cacheMap.put(cacheKey + "_datetime", System.currentTimeMillis());
		} else {
			content = (String) cacheMap.get(cacheKey);
		}
		response.getWriter().write(content);
	}

	/**
	 * 验证登录并返回user
	 * 
	 * @param userName
	 * @param userPwd
	 * @param userMobile
	 * @return
	 */
	private User doLogin(String userName, String userPwd, String userMobile) {

		String columns = "id,userName,userPwd,userMobile,userStatus,commission,earningsYield,leidaExpire,userMoney,firstMoney";
		String sql = "select " + columns + " from t_user where userName = '" + DBHelper.getSafeSqlParam(userName)
				+ "' and userPwd = '" + DBHelper.getSafeSqlParam(userPwd) + "' and userMobile = '"
				+ DBHelper.getSafeSqlParam(userMobile) + "' ";
		HashMap<String, String> userInfo = DBHelper.selectSql(sql, columns.split(","));
		if (userInfo == null || userInfo.size() == 0) {
			return null;
		}
		User user = new User();
		user.setCommission(Integer.parseInt(userInfo.get("commission")));
		user.setEarningsYield(Integer.parseInt(userInfo.get("earningsYield")));
		user.setUserStatus(Integer.parseInt(userInfo.get("userStatus")));
		user.setUserMoney(Integer.parseInt(userInfo.get("userMoney")));
		user.setFirstMoney(Integer.parseInt(userInfo.get("firstMoney")));
		user.setId(Integer.parseInt(userInfo.get("id")));
		try {
			user.setLeidaExpire(new SimpleDateFormat(Const.YMD_HMS).parse(userInfo.get("leidaExpire")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		user.setUserName(userName);
		user.setUserMobile(userMobile);
		return user;
	}

	

	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
		try {
			DataSourceFactory.shutDownDataSource();
			service.shutdownNow();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
