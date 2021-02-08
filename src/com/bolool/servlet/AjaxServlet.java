package com.bolool.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.bolool.util.Const;
import com.bolool.util.DBHelper;
import com.bolool.util.DataSourceFactory;
import com.bolool.util.HttpUtil;
import com.bolool.vo.User;
import com.bolool.vo.UserOrder;
import com.google.gson.Gson;

/**
 * Servlet implementation class AjaxServlet
 */
@WebServlet(value = { "/ajaxServlet/*", "/xml/*", "/ajax/*", "/api/*" }, loadOnStartup = 1)
public class AjaxServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
		try {
			DataSourceFactory.init();
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
			request.getSession().setAttribute("web-cache",cacheMap);
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
		}else {
			response.getWriter().append("Served at: ").append(request.getContextPath());
		}
	}

	/**
	 * 根据ids从数据库里查菠萝指数数据
	 * @param request
	 * @param response
	 */
	private void getMatchHistory(HttpServletRequest request, HttpServletResponse response)throws IOException  {
		String ids = request.getParameter("matchIds");
		if(StringUtils.isEmpty(ids) ) {
			response.getWriter().write("参数不符合要求");
		}else {
			ids = DBHelper.getSafeSqlParam(ids);
			String sql = "select id,matchlist from t_match_history where id in ("+ids+")";
			List<HashMap<String,String>> historyMatchList = DBHelper.selectListSql(sql, new String[]{"id","matchlist"});
			response.setHeader("Content-Type", "application/json; charset=UTF-8 "); // 设置响应头的编码
			response.getWriter().write(new Gson().toJson(historyMatchList));
		}
		
	}

	/**
	 * 从win007网上获取xml,txt 竞彩数据并返回，
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
			content = getFromWin007(uri);
			cacheMap.put(uri, content);
			cacheMap.put(uri + "_datetime", System.currentTimeMillis());
		} else {
			content = (String) cacheMap.get(uri);
		}
		response.getWriter().write(content);
	}
/**
 * 判断用户登录
 * @param request
 * @param response
 * @throws IOException
 */
	private void userLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String userName = request.getParameter("userName");
		String userPwd = request.getParameter("userPwd");
		String userMobile = request.getParameter("userMobile");
		String msg = null;
		if (userName == null || userName.length() < 3 || userPwd == null || userPwd.length() < 3
				|| userMobile == null || userMobile.length() < 3) {
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
		String cacheKey = url+args+method;
		if (cacheMap.get(cacheKey) == null
				|| (Long) cacheMap.get(cacheKey + "_datetime") < System.currentTimeMillis() - Const.WEB_CACHE_SECONDS) {
			content = getFromOkooo(url, method, args);
			cacheMap.put(cacheKey, content);
			cacheMap.put(cacheKey + "_datetime", System.currentTimeMillis());
		} else {
			content = (String) cacheMap.get(cacheKey);
		}
		response.getWriter().write(content);
	}

	/**
	 * 验证登录并返回user
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
			user.setLeidaExpire(new SimpleDateFormat(Const.ymd_hms).parse(userInfo.get("leidaExpire")));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		user.setUserName(userName);
		user.setUserMobile(userMobile);
		return user;
	}

	private String getFromWin007(String url) {
		return HttpUtil.doGet("http://jc.win007.com" + url);
	}

	public String getFromOkooo(String url) {
		return getFromOkooo(url, "get");
	}

	private String getFromOkooo(String url, String method) {
		return getFromOkooo(url, method, null);
	}

	private String getFromOkooo(String url, String method, String args) {
		if (!url.startsWith("http")) {
			url = "http://www.okooo.com" + url;
		}
		if (method == null) {
			method = "get";
		}
		if (args != null && method.equals("get")) {
			if (url.indexOf("?") != -1) {
				url = url + "&" + args;
			} else {
				url = url + "?" + args;
			}
		}
		String msg = "";
		if ("get".equals(method)) {
			return HttpUtil.doGet(url, "gbk");
		} else {
			try {
				if (args != null) {
					HashMap<String, String> map = new HashMap<String, String>();
					String arr[] = args.split("&");
					for (int i = 0; i < arr.length; i++) {
						String ps[] = arr[i].split("=");
						map.put(ps[0], ps[1]);
					}
					HashMap<String, String> header = new HashMap<String, String>();
					header.put("Content-Type", "application/x-www-form-urlencoded");
					header.put("Accept", "application/json, text/javascript, */*c");
					header.put("Host", "www.okooo.com");
					header.put("Accept-Language", "zh-cn");
					header.put("Accept-Encoding", "gzip, deflate");
					header.put("Origin", "http://www.okooo.com");
					header.put("Referer", "http://www.okooo.com/soccer/match/1123132/history/");
					header.put("Connection", "keep-alive");
					header.put("User-Agent",
							"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Safari/605.1.15");
					header.put("Cookie",
							"acw_tc=2f624a0716126650180178482e294335542ac159914275bb2237650f0fb9ba; Hm_lpvt_5ffc07c2ca2eda4cc1c4d8e50804c94b=1612664445; LStatus=N; LoginStr=%7B%22welcome%22%3A%22%u60A8%u597D%uFF0C%u6B22%u8FCE%u60A8%22%2C%22login%22%3A%22%u767B%u5F55%22%2C%22register%22%3A%22%u6CE8%u518C%22%2C%22TrustLoginArr%22%3A%7B%22alipay%22%3A%7B%22LoginCn%22%3A%22%u652F%u4ED8%u5B9D%22%7D%2C%22tenpay%22%3A%7B%22LoginCn%22%3A%22%u8D22%u4ED8%u901A%22%7D%2C%22weibo%22%3A%7B%22LoginCn%22%3A%22%u65B0%u6D6A%u5FAE%u535A%22%7D%2C%22renren%22%3A%7B%22LoginCn%22%3A%22%u4EBA%u4EBA%u7F51%22%7D%2C%22baidu%22%3A%7B%22LoginCn%22%3A%22%u767E%u5EA6%22%7D%2C%22snda%22%3A%7B%22LoginCn%22%3A%22%u76DB%u5927%u767B%u5F55%22%7D%7D%2C%22userlevel%22%3A%22%22%2C%22flog%22%3A%22hidden%22%2C%22UserInfo%22%3A%22%22%2C%22loginSession%22%3A%22___GlobalSession%22%7D; __utmb=56961525.9.7.1612664445276; __utmc=56961525; pm=; PHPSESSID=9843d8b6cdfe91d98e8f15c3bbc29f0cef962f72; FirstOKURL=http%3A//www.okooo.com/jingcai/; First_Source=www.okooo.com; Hm_lvt_5ffc07c2ca2eda4cc1c4d8e50804c94b=1612537278,1612623203,1612659491; LastUrl=; __utma=56961525.867081676.1612537278.1612659491.1612659494.4; __utmz=56961525.1612537278.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)");
					header.put("Proxy-Connection", "keep-alive");
					header.put("X-Requested-With", "XMLHttpRequest");
					return HttpUtil.doPost(url, map,header);
				} else {
					return HttpUtil.doPost(url, "");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				msg = e.getMessage();
			}
		}
		return msg;
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
