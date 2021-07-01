package com.bolool.test;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Array;
import java.util.Arrays;

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
		BigDecimal aoBoWinRace = new BigDecimal(1.0d);
		ICalc calc = new CalcAllHigh(aoBoWinRace);
//		calc.calcByIssue("180801", null);
		calc.calc4db();
//		Integer []highNum = new Integer[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14};
//		ICalc calc = new CalcHighWithNum(highNum);
//		calc.calcByIssue("180801",null);
//		calc.calc4db();
//		ICalc calc = new CalcAllDraw();
//		calc.calc4db();
		
//		ICalc calc = new CalcAllLow();
//		calc.calc4db();
		
	}

}
