package com.bolool.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bolool.util.DBHelper;
import com.bolool.util.DataSourceFactory;
import com.bolool.vo.Result;
import com.google.gson.Gson;
@WebServlet(value = { "/adminS/*"})
public class AdminServlet extends HttpServlet {
	private static final Log log = LogFactory.getLog(AdminServlet.class);
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		try {
			DataSourceFactory.init();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
		response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Content-Type");
		response.addHeader("Access-Control-Max-Age", "86400");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8"); // 设置 HttpServletResponse使用utf-8编码
		response.setHeader("Content-Type", "text/html;charset=utf-8"); // 设置响应头的编码
		String uri = request.getRequestURI();
		if(uri.equals("/adminS/login")) {
			login(request,response);
		}else if(uri.equals("/adminS/index") || uri.equals("/adminS")) {
			request.getRequestDispatcher("/admin/index.html").forward(request, response);
		}else if(uri.equals("/adminS/checkLogin")) {
			String userName = (String) request.getSession().getAttribute("userName");
			Result result = new Result(-1,"没有登录");
			if(userName!=null) {
				result.setCode(0);
				result.setMsg(userName.toString());
			}
			response.getWriter().write(new Gson().toJson(result));
		}else if(uri.contentEquals("/adminS/userlist")){
			userlist(request,response);
		}else if(uri.contentEquals("/adminS/matchlist")){
			matchlist(request,response);
		}else {
			response.getWriter().append("Served at: ").append(request.getContextPath());
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	public void matchlist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}
	public void userlist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String username = req.getParameter("username");
		String start = req.getParameter("start");
		String end = req.getParameter("end");
		String pageNo = req.getParameter("pageNo");
		if(pageNo==null || !StringUtils.isNumeric(pageNo)) {
			pageNo = "1";
		}
		int pageSize = 20;
		Result result = new Result();
		String wherestr="";
		boolean hasAnd = false;
		if(!StringUtils.isBlank(username)) {
			wherestr += "nickname = '"+DBHelper.getSafeSqlParam(username)+"'";
			hasAnd = true;
		}
		if(!StringUtils.isBlank(start)) {
			if(hasAnd) {
				wherestr += " and ";
			}
			wherestr += " insert_time >='"+DBHelper.getSafeSqlParam(start)+"'";
			hasAnd = true;
		}
		if(!StringUtils.isBlank(end)) {
			if(hasAnd) {
				wherestr += " and ";
			}
			wherestr += " insert_time <='"+DBHelper.getSafeSqlParam(end)+"'";
			hasAnd = true;
		}
		String sql ="select * from t_user_info ";
		if(hasAnd) {
			sql += " where "+wherestr;
		}
		int limitPage = Integer.parseInt(pageNo) ;
		String countSql = sql.replace("*", "count(*)");
		String count = DBHelper.selectOne(countSql);
		if(count==null) {
			count = "0";
		}
		sql += " limit " + (limitPage * pageSize) +"," + (limitPage+1) * pageSize;
		java.util.List<HashMap<String,String>> list = DBHelper.selectListSql(sql,new String[] {"id","open_id","app_id","nickname","insert_time","avatar"});
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("count", count);
		data.put("pageNo", limitPage);
		data.put("pageSize", pageSize);
		data.put("list",list);
		result.setData(data);
		resp.getWriter().write(result.toString());
	}
	public void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userName = req.getParameter("username");
		String pwd = req.getParameter("password");
		Result result = new Result();
		result.setCode(0);
		if(userName==null || pwd==null ) {
			String msg = "请输入用户名和密码！";
			result.setCode(-1);
			result.setMsg(msg);
		}else {
			java.util.List<String> list = DBHelper.selectListSql("select password from t_sys_user where userName = '"+DBHelper.getSafeSqlParam(userName)+"'");
			String dbpwd = "haha2342432q42q3rqrqwefaf";
			if(list!=null && list.size() > 0) {
				 dbpwd = list.get(0);
			}
			if(pwd.equals(dbpwd)) {
				req.getSession().setAttribute("userName", userName);
				DBHelper.insertOrUpdate("update t_sys_user set lastlogin=now() where userName='"+DBHelper.getSafeSqlParam(userName)+"'");
			}else {
				String msg = "用户名和密码不匹配";
				result.setCode(-2);
				result.setMsg(msg);
			}
		}
		resp.getWriter().write(new Gson().toJson(result));
		
	}
	
}
