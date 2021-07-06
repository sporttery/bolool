package com.bolool.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.bolool.model.TDanchangMatch;
import com.bolool.mybatis.TDanchangMatchMapper;
import com.bolool.service.TDanchangMatchService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Servlet implementation class AjaxServlet
 */
@WebServlet(value = { "/danchang/*" }, loadOnStartup = 1)
public class DanchangServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	Connection getConnection(){
		String mybatisConfigPath = "mybatis-config.xml";
		InputStream inputStream = null;
		try {
			inputStream = Resources.getResourceAsStream(mybatisConfigPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession session = sqlSessionFactory.openSession();
		Connection conn = session.getConnection();
		return conn;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String url = req.getRequestURI();
		if(url.indexOf(".jsp")!=-1) {
			req.getRequestDispatcher("/jsp"+url).forward(req,resp);
			return;
		}
		resp.addHeader("Access-Control-Allow-Origin", "*");
		resp.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		resp.addHeader("Access-Control-Allow-Headers",
				"Content-Type,X-Requested-With,accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers,token");
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		String data = req.getParameter("data");
		TDanchangMatch[] matchArr = null;
		Gson gson = new Gson();
		try {
			matchArr =gson.fromJson(data, TDanchangMatch[].class);
		}catch (Exception e) {
			System.out.println("批量转换成数组出错"+e.getMessage()+"，单条转换，找原因");
			JsonElement je = JsonParser.parseString(data);
			if(je.isJsonArray()) {
				matchArr = new TDanchangMatch[je.getAsJsonArray().size()];
				JsonArray ja = je.getAsJsonArray();
				for(int i=0;i<ja.size();i++) {
					JsonElement matchJo = ja.get(i);
					System.out.println("正在处理第"+i+"条数据");
					TDanchangMatch match = gson.fromJson(matchJo, TDanchangMatch.class);
					matchArr[i] = match;
				}
			}
		}
		
		System.out.println("matchCount:"+matchArr.length);
		List<TDanchangMatch> list = new ArrayList<TDanchangMatch>();
		for (int i = 0; i < matchArr.length; i++) {
			TDanchangMatch match = matchArr[i];
			if(match==null) {
				System.out.println(i+" match is null");
			}else {
				list.add(match);
			}
		}
		int c = 0;

//		String mybatisConfigPath = "mybatis-config.xml";
//		InputStream inputStream = Resources.getResourceAsStream(mybatisConfigPath);
//		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
//		try (SqlSession session = sqlSessionFactory.openSession()) {
//			TDanchangMatchMapper mapper = session.getMapper(TDanchangMatchMapper.class);
//			c = mapper.insertMultiple(list);
//			session.commit();
//		}
		
		TDanchangMatchService srv = new TDanchangMatchService(true);
		c = srv.getMapper().insertMultiple(list);
//		srv.commit();
		srv.closeSession();
		System.out.println("successCount:"+c);
		resp.getWriter().print("{\"successCount\":" + c+"}");
	}
}
