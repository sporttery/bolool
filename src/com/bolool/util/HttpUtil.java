package com.bolool.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * @author 马弦
 * @date 2017年10月23日 下午2:49 HttpClient工具类
 */
public class HttpUtil {
	private static Logger log = Logger.getLogger(HttpUtil.class);
	private static String contentType = "application/json";
	public static boolean noCurl = false;
	public static String curlMatchHistoryFile = null;

	public static void setContentType(String type) {
		contentType = type;
	}

	public static String safeHtml(String d) {
		return d.replaceAll("[\\r\\n]", "").replaceAll("<head.+?<\\/head>", "").replaceAll("<script.+?<\\/script>", "")
				.replaceAll("<img.+?>", "").replaceAll("<link.+?>", "").replaceAll("<style.+?<\\/style>", "");
	}

	private static HttpClient makeHttpClient(Map header) {
		List<Header> headers = new ArrayList<>();
		HttpClientBuilder builder = HttpClientBuilder.create().useSystemProperties();
		CloseableHttpClient client = builder.build();
//	    headers.add(new BasicHeader(WEBMATE_USER_HEADERKEY, authInfo.emailAddress));
//	    headers.add(new BasicHeader(WEBMATE_APITOKEN_HEADERKEY, authInfo.apiKey));

		if (header != null) {
			for (Iterator iter = header.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String value = String.valueOf(header.get(name));
				headers.add(new BasicHeader(name, value));
			}
		} else {
			headers.add(new BasicHeader("Content-Type", contentType));
			headers.add(new BasicHeader("User-Agent",
					"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Safari/605.1.15"));
			headers.add(new BasicHeader("Accept-Language", "zh-cn"));
			headers.add(new BasicHeader("Accept-Encoding", "br, gzip, deflate"));
			headers.add(new BasicHeader("Connection", "keep-alive"));
			headers.add(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"));

		}

		builder.setDefaultHeaders(headers);
		return builder.build();
	}

	public static String sendGet(String url) {
		return sendGet(url,null,"utf-8");
	}
	
	public static String sendGet(String url, String param,String encoding) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url;
			if (param != null) {
				if(url.indexOf("?")!=-1){
					urlNameString = url + "&" + param;
				}else{
					urlNameString = url + "?" + param;
				}
			}
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("Accept", "*/*");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection
					.setRequestProperty(
							"User-agent",
							"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.85 Safari/537.36");
			connection.setRequestProperty("Referer",
					"https://www.sporttery.cn/");
			connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			connection.setRequestProperty("Accept-Encoding",
					"deflate, sdch");
			connection.setRequestProperty("Cache-Control", "max-age=0");
			connection
					.setRequestProperty(
							"Cookie",
							"BIGipServerPool_apache_web=1224802570.20480.0000; Hm_lvt_860f3361e3ed9c994816101d37900758=1446550156,1446694723,1447759021,1447903576; Hm_lpvt_860f3361e3ed9c994816101d37900758=1447912833");
			connection.setRequestProperty("Host", "i.sporttery.cn");
			connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
			// 建立实际的连接
			connection.connect();
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream(),encoding));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			log.error("发送GET请求出现异常！" + e.getMessage());
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * get请求
	 * 
	 * @param url
	 * @param charset
	 * @param header
	 * @return
	 */
	public static String doGet(String url, String charset, Map header) {
		try {
			HttpClient client = makeHttpClient(null);
			// 发送get请求
			HttpGet request = new HttpGet(url);
			HttpResponse response = client.execute(request);
			int code = response.getStatusLine().getStatusCode();
			/** 请求发送成功，并得到响应 **/
			if (code == HttpStatus.SC_OK) {
				/** 读取服务器返回过来的json字符串数据 **/
				String strResult = EntityUtils.toString(response.getEntity(), charset);

				return strResult;
			} else {
				log.error("url: " + url + " 状态码：" + code);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * get请求
	 * 
	 * @param url
	 * @return
	 */
	public static String doGet(String url) {
		return doGet(url, "utf-8", null);
	}

	/**
	 * get请求
	 * 
	 * @param url
	 * @param charset
	 * @return
	 */
	public static String doGet(String url, String charset) {
		return doGet(url, charset, null);
	}

	/**
	 * get请求
	 * 
	 * @param url
	 * @param header
	 * @return
	 */
	public static String doGet(String url, Map header) {
		return doGet(url, "utf-8", header);
	}

	/**
	 * post请求(用于key-value格式的参数)
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 */
	public static String doPost(String url, Map params, String charset, Map header) {

		BufferedReader in = null;
		try {
			// 定义HttpClient
			HttpClient client = makeHttpClient(header);

			// 实例化HTTP方法
			HttpPost request = new HttpPost();
			request.setURI(new URI(url));

			// 设置参数
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for (Iterator iter = params.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String value = String.valueOf(params.get(name));
				nvps.add(new BasicNameValuePair(name, value));

				// System.out.println(name +"-"+value);
			}
			request.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

			HttpResponse response = client.execute(request);
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) { // 请求成功
				in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), charset));
				StringBuffer sb = new StringBuffer("");
				String line = "";
				String NL = System.getProperty("line.separator");
				while ((line = in.readLine()) != null) {
					sb.append(line + NL);
				}

				in.close();

				return sb.toString();
			} else { //
				log.error("url: " + url + " params: " + params + " 状态码：" + code);
				return null;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * post请求（用于请求json格式的参数）
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 */
	public static String doPost(String url, String params, String charset, Map header) throws Exception {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);// 创建httpPost
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-Type", "application/json");
		if (header != null) {
			for (Iterator iter = header.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String value = String.valueOf(header.get(name));
				httpPost.setHeader(name, value);
			}
		}
		StringEntity entity = new StringEntity(params, Const.DEFAULT_ENCODING);
		httpPost.setEntity(entity);
		CloseableHttpResponse response = null;

		try {

			response = httpclient.execute(httpPost);
			StatusLine status = response.getStatusLine();
			int state = status.getStatusCode();
			if (state == HttpStatus.SC_OK) {
				HttpEntity responseEntity = response.getEntity();
				String jsonString = EntityUtils.toString(responseEntity, Const.DEFAULT_ENCODING);
				return jsonString;
			} else {
				// logger.error("请求返回:"+state+"("+url+")");
				log.error("url: " + url + " params: " + params + " 状态码：" + state);
			}
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					log.error(e);
					e.printStackTrace();
				}
			}
			try {
				httpclient.close();
			} catch (IOException e) {
				log.error(e);
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * post请求(用于key-value格式的参数)
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String doPost(String url, Map params) throws Exception {
		return doPost(url, params, "utf-8", null);
	}

	/**
	 * post请求(用于key-value格式的参数)
	 * 
	 * @param url
	 * @param params
	 * @param header
	 * @return
	 */
	public static String doPost(String url, Map params, Map header) throws Exception {
		return doPost(url, params, "utf-8", header);
	}

	/**
	 * post请求（用于请求json格式的参数）
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String url, String params) throws Exception {
		return doPost(url, params, "utf-8", null);
	}

	/**
	 * post请求（用于请求json格式的参数）
	 * 
	 * @param url
	 * @param params
	 * @param header
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String url, String params, Map header) throws Exception {
		return doPost(url, params, "utf-8", header);
	}

	public static String execCurl(String[] cmds) {
		ProcessBuilder process = new ProcessBuilder(cmds);
		Process p;
		try {
			p = process.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
				builder.append(System.getProperty("line.separator"));
			}
			return builder.toString();

		} catch (IOException e) {
			log.error(e);
			e.printStackTrace();
		}
		return null;
	}

	public static String getFromOkooo(String url, String method, String args) {
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
		HashMap<String, String> header = new HashMap<String, String>();

		if ("get".equalsIgnoreCase(method)) {
			if (noCurl) {
				return HttpUtil.doGet(url, "gbk");
			}
			return HttpUtil.execCurl(new String[] { "/bin/sh", curlMatchHistoryFile, url });
		} else {
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
			try {
				if (args != null) {
					HashMap<String, String> map = new HashMap<String, String>();
					String arr[] = args.split("&");
					for (int i = 0; i < arr.length; i++) {
						String ps[] = arr[i].split("=");
						map.put(ps[0], ps[1]);
					}
					return HttpUtil.doPost(url, map, header);
				} else {
					return HttpUtil.doPost(url, "", header);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				msg = e.getMessage();
			}
		}
		return msg;
	}

	public static String getFromWin007(String url) {
		return HttpUtil.doGet("http://jc.win007.com" + url);
	}

	public static String getFromOkooo(String url) {
		return getFromOkooo(url, "get");
	}

	private static String getFromOkooo(String url, String method) {
		return getFromOkooo(url, method, null);
	}
}