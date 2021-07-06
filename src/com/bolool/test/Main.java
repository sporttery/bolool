package com.bolool.test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;

import com.bolool.model.TDanchangMatch;
import com.bolool.mybatis.TDanchangMatchDynamicSqlSupport;
import com.bolool.service.TDanchangMatchService;
import com.bolool.util.DBHelper;

import static org.mybatis.dynamic.sql.SqlBuilder.*;

public class Main {

	public static void main(String[] args) throws IOException, Exception {
		// TODO Auto-generated method stub
//		String abc="asdfasd'werw'werwe-werew--ererwr'werw--sdfs";
//		System.out.println(abc.replaceAll("['-]", " "));

//		String url = "http://127.0.0.1:9090/soccer/match/1124651/history/";
//		String url = "https://www.okooo.com/soccer/match/1124651/history/";
//		String get = HttpUtil.doGet(url,"GBK");
//		System.out.println(get);
//		String id = url.replaceAll("[^\\d]", "");
//		String file = ("/Users/zhangwei/eclipse-workspace/bolool/WebContent/curlMatchHistory.sh");
//		String result= HttpUtil.execCurl(new String[] {file,id});
//		System.out.println(result);

//		String boloolJSON = "{\"top33\":{\"hscore\":40,\"hsection\":6,\"hresult\":\"负负胜负负负平负平负胜胜负平平平胜负平平胜平胜负平平胜胜平胜负平平\",\"hstrong\":\"弱\",\"ascore\":43,\"asection\":6,\"aresult\":\"负平胜负平负胜平平平负负胜负胜平平负胜负负平胜平胜平平负胜胜平胜平\",\"astrong\":\"强\",\"topN\":33,\"friendly\":0,\"matchId\":1114283},\"top30\":{\"hscore\":38,\"hsection\":6,\"hresult\":\"负负胜负负负平负平负胜胜负平平平胜负平平胜平胜负平平胜胜平胜\",\"hstrong\":\"强\",\"ascore\":36,\"asection\":6,\"aresult\":\"负平胜负平负胜平平平负负胜负胜平平负胜平负负平胜平胜平平负胜\",\"astrong\":\"弱\",\"topN\":30,\"friendly\":1,\"matchId\":1114283},\"top6\":{\"hscore\":3,\"hsection\":9,\"hresult\":\"负负胜负负负\",\"hstrong\":\"弱\",\"ascore\":5,\"asection\":9,\"aresult\":\"负平胜负平负\",\"astrong\":\"强\",\"topN\":6,\"friendly\":1,\"matchId\":1114283},\"top3\":{\"hscore\":3,\"hsection\":9,\"hresult\":\"负负胜\",\"hstrong\":\"弱\",\"ascore\":4,\"asection\":9,\"aresult\":\"负平胜\",\"astrong\":\"强\",\"topN\":3,\"friendly\":1,\"matchId\":1114283}}";
//		Map<String,Object> map  = new Gson().fromJson(boloolJSON, Map.class);
//		HashMap<String,Bolool> boloolData  = new HashMap<String, Bolool>();
//		//System.out.println(new Gson().toJson(boloolData.get("top33")));
//		map.forEach((key,value)->{
//			boloolData.put(key, new Gson().fromJson(new Gson().toJson(value), Bolool.class));
//		});
//		System.out.println(boloolData.get("top33").getAresult());
//		Integer [] highNum = new Integer[] {1,2,3,4};
//		
//		Integer num = new Integer(25);
//		
//		boolean has = Arrays.asList(highNum).contains(num);
//		
//		System.err.println(has);

//		String mybatisConfigPath = "mybatis-config.xml";
//		InputStream inputStream = Resources.getResourceAsStream(mybatisConfigPath);
//		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
//		try (SqlSession session = sqlSessionFactory.openSession()) {
//			List<Object> list = session.getConnection();
//			System.out.println(list);
//		}

//		Connection conn = srv.getConnection();
//		PreparedStatement ps = conn.prepareStatement("select distinct issue from t_match_danchang order by playtime asc");
//		ResultSet rs = ps.executeQuery();
//		List<String> list = new ArrayList<String>();
//		while(rs.next()) {
//			list.add(rs.getString("issue"));
//		}
//		ps.close();
//		ps = conn.prepareStatement("select * from t_match_danchang where issue = ?");
//		for (int i = 0; i < list.size(); i++) {
//			String issue = list.get(i);
//			ps.setString(1, issue);
//			rs = ps.executeQuery();
//			while(rs.next()) {
//				
//			}
//		}
//		rs.close();
//		ICalc calc = new Calcinfo();
		// 获取数据并计算入库
		// calc.calc2db();
		// 获得计算好的数据
//		BigDecimal aoBoWinRace = new BigDecimal(1.0d);
//		ICalc calc = new CalcAllHigh(aoBoWinRace);
//		calc.calcByIssue("180801", null);
//		calc.calc4db();
//		Integer []highNum = new Integer[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14};
//		ICalc calc = new CalcHighWithNum(highNum);
//		calc.calcByIssue("180801",null);
//		calc.calc4db();
//		ICalc calc = new CalcAllDraw();
//		calc.calc4db();

//		ICalc calc = new CalcAllLow();
//		calc.calc4db();
		
		
//		calcMaxAvg();
		
		
	}
	//按照期号，同一期比赛时间间隔100分钟以上
	//按照参考赔率，小赔和中赔进行模拟
	public static void calcByIssue() throws Exception{
		String columns = "issue,playtime,home,away,league_name,rq,num,fullscore,sp1,sp2,sp3,sp,result";
		String sql = "SELECT "+columns+" FROM t_danchang_match where result is not null and sp1 is not null order by  playtime asc;";
		int times = 10000;
		int chuan = 15;
		String mybatisConfigPath = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(mybatisConfigPath);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		DecimalFormat df = new DecimalFormat("#");
		long PLAYTIME_INTERVAL = 60 * 1000 * 100;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Connection conn = session.getConnection();
			List<HashMap<String, String>>  list = DBHelper.selectListSql(sql,columns.split(","), conn);
			if(!conn.isClosed()) {
				conn.close();
			}
			HashMap<String,List<TDanchangMatch>> issueMap = new HashMap<String,List<TDanchangMatch>> ();
			HashMap<String,List<TDanchangMatch>> issueMapChuan = new HashMap<String,List<TDanchangMatch>> ();
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			list.forEach(item->{
				TDanchangMatch match= new TDanchangMatch();
				match.setIssue(item.get("issue"));
				try {
					match.setPlaytime(sdf.parse(item.get("playtime")));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				match.setHome(item.get("home"));
				match.setAway(item.get("away"));
				match.setLeagueName(item.get("league_name"));
				match.setNum(Integer.valueOf(item.get("num")));
				match.setRq(item.get("rq"));
				match.setFullscore(item.get("fullscore"));
				match.setSp1(new BigDecimal(item.get("sp1")));
				match.setSp2(new BigDecimal(item.get("sp2")));
				match.setSp3(new BigDecimal(item.get("sp3")));
				match.setSp(new BigDecimal(item.get("sp")));
				match.setResult(item.get("result"));
				List<TDanchangMatch> issueMatchList = issueMap.get(match.getIssue());
				if(issueMatchList==null) {
					issueMatchList = new ArrayList<TDanchangMatch>();
					issueMap.put(match.getIssue(),issueMatchList);
				}
				issueMatchList.add(match);
			});
			
			issueMap.forEach((issue,issueList)->{
				List<TDanchangMatch> newList = new ArrayList<TDanchangMatch>();
				TDanchangMatch matchPre = issueList.get(0);
				newList.add(matchPre);
				for (int i = 1; i < issueList.size(); i++) {
					TDanchangMatch match = issueList.get(i);
					if(match.getPlaytime().getTime() - matchPre.getPlaytime().getTime() > PLAYTIME_INTERVAL) {
						newList.add(match);
						matchPre = match;
					}
				}
				issueMapChuan.put(issue, newList);
			});
			
			
		}
	}
	
	//随机抽取n场比赛，计算串关的平均奖金
	public static void calcMaxAvg() throws Exception {
		String sql = "SELECT sp FROM t_danchang_match where sp is not null";
		int times = 10000;
		int chuan = 15;
		String mybatisConfigPath = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(mybatisConfigPath);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		DecimalFormat df = new DecimalFormat("#");
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Connection conn = session.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			List<Double> list = new ArrayList<Double>();
			while (rs.next()) {
				list.add(rs.getDouble("sp"));
			}
			rs.close();
			ps.close();
			conn.close();
			int[] resultArr = new int[times];
			int retry = 1000;
			while (retry-- > 0) {
				for (int i = 0; i < times; i++) {
					int min = Integer.MAX_VALUE;
					double sdd = 1d;
					for (int j = 0; j < chuan; j++) {
						Random random = new Random(i+j+retry);
						int n = random.nextInt(list.size());
						sdd *= list.get(n);
					}
					if (sdd > 5000000) {
						sdd = 5000000;
					}
					if(min > sdd) {
						min = (int)sdd;
					}
					resultArr[i] = (int) sdd;
				}
				double avg = Arrays.stream(resultArr).average().orElse(Double.NaN);
				int min = Arrays.stream(resultArr).min().getAsInt();
//				int max = Arrays.stream(resultArr).max().getAsInt();
				System.out.println("平均值：" + df.format(avg)+",min:"+min);
			}
		}
	}

}
