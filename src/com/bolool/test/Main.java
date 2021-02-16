package com.bolool.test;

import java.util.HashMap;
import java.util.Map;

import com.bolool.util.HttpUtil;
import com.bolool.vo.Bolool;
import com.google.gson.Gson;

public class Main {

	public static void main(String[] args) {
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
		
		String boloolJSON = "{\"top33\":{\"hscore\":40,\"hsection\":6,\"hresult\":\"负负胜负负负平负平负胜胜负平平平胜负平平胜平胜负平平胜胜平胜负平平\",\"hstrong\":\"弱\",\"ascore\":43,\"asection\":6,\"aresult\":\"负平胜负平负胜平平平负负胜负胜平平负胜负负平胜平胜平平负胜胜平胜平\",\"astrong\":\"强\",\"topN\":33,\"friendly\":0,\"matchId\":1114283},\"top30\":{\"hscore\":38,\"hsection\":6,\"hresult\":\"负负胜负负负平负平负胜胜负平平平胜负平平胜平胜负平平胜胜平胜\",\"hstrong\":\"强\",\"ascore\":36,\"asection\":6,\"aresult\":\"负平胜负平负胜平平平负负胜负胜平平负胜平负负平胜平胜平平负胜\",\"astrong\":\"弱\",\"topN\":30,\"friendly\":1,\"matchId\":1114283},\"top6\":{\"hscore\":3,\"hsection\":9,\"hresult\":\"负负胜负负负\",\"hstrong\":\"弱\",\"ascore\":5,\"asection\":9,\"aresult\":\"负平胜负平负\",\"astrong\":\"强\",\"topN\":6,\"friendly\":1,\"matchId\":1114283},\"top3\":{\"hscore\":3,\"hsection\":9,\"hresult\":\"负负胜\",\"hstrong\":\"弱\",\"ascore\":4,\"asection\":9,\"aresult\":\"负平胜\",\"astrong\":\"强\",\"topN\":3,\"friendly\":1,\"matchId\":1114283}}";
		Map<String,Object> map  = new Gson().fromJson(boloolJSON, Map.class);
		HashMap<String,Bolool> boloolData  = new HashMap<String, Bolool>();
		//System.out.println(new Gson().toJson(boloolData.get("top33")));
		map.forEach((key,value)->{
			boloolData.put(key, new Gson().fromJson(new Gson().toJson(value), Bolool.class));
		});
		System.out.println(boloolData.get("top33").getAresult());
		
	}

}
