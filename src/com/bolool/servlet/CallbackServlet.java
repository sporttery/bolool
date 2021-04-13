package com.bolool.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bolool.util.DBHelper;
import com.bolool.util.DataSourceFactory;
import com.google.gson.Gson;

/**
 * Servlet implementation class CallbackServlet secret callback
 */
@WebServlet(value = { "/cb/*" })
public class CallbackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Log log = LogFactory.getLog(CallbackServlet.class);

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		try {
			DataSourceFactory.init();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CallbackServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8"); // 设置 HttpServletResponse使用utf-8编码
		response.setHeader("Content-Type", "text/html;charset=utf-8"); // 设置响应头的编码
		String uri = request.getRequestURI();
		//http://api.secretzq.com/cb/login?open_id=df70149d-ea04-a1d1-cdb8-fc6f2350ac21&session_id=cf416b6ff7d856c9ad54c366da13fe4a&expired=1618131493&avatar=3-1-641f3dbe-08c8-4c21-8c64-770efcb9a455&nickname=David908683&t=1618045093&s=c8c075571b837740af24588f9a5815ea
		if(uri.contentEquals("/cb/login")) {
			HashMap<String,String> res = checkSign(request.getParameterMap());
			if(res!=null && res.get("exists").contentEquals("0")) {
				String sql = "insert into t_user_info(id,open_id,app_id,nickname,avatar) values(?,?,?,?,?,?)";
				int b = DBHelper.insertOrUpdatePre(sql, UUID.randomUUID().toString(),res.get("open_id"),res.get("app_id"),res.get("nickname"),res.get("avatar"));
				if(b<1) {
					log.error(new Gson().toJson(res) + " 入库失败");
				}
				response.getWriter().append("success");
			}else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		}else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	

	private HashMap<String,String> checkSign(Map<String, String[]> map) {
		if(!map.containsKey("open_id") || !map.containsKey("s")) {
			return null;
		}
		HashMap<String,String> res = new HashMap<String,String>();
		res.put("exists", "0");
		String app_id = null;
		String open_id = map.get("open_id")[0];
		app_id = DBHelper.selectOne("select app_id from t_user_info where open_id='"+open_id+"'");
		String sql = null;
		if(app_id == null) {
			res.put("exists", "1");
			 sql = "select app_id,secretKey from t_app_info where status = 1";
		}else {
			 sql = "select app_id,secretKey from t_app_info where status = 1 and app_id = '"+app_id+"'";
		}
		List<HashMap<String,String>> list  = DBHelper.selectListSql(sql, new String[] {"app_id","secretKey"});
		if(list==null || list.size()==0) {
			return null;
		}
		String s = map.remove("s")[0];
		Set<String> keyset = map.keySet();
		keyset.stream().sorted();
		String base = "";
		for (String key : keyset) {
			base += map.get(key)[0];
			res.put(key, map.get(key)[0]);
		}
		boolean b = false;
		for(HashMap<String,String> map1 :list) {
			String secretKey = map1.get("secretKey");
			String appId = map1.get("app_id");
			String md5 = DigestUtils.md5Hex((base + secretKey).getBytes());
			b= md5.equals(s);
			if(b) {
				res.put("app_id", appId);
				return res;
			}
			log.error("checkSignErr: map="+new Gson().toJson(map)+",base="+base+", s="+s+",md5="+md5 +",app_id="+app_id);
		}
		return null;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
