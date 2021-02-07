package com.bolool.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
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
		}else {
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

	// private static Logger logger = Logger.getLogger(HttpUtil.class);

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
	public static String doGet(String url,String charset) {
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
				}
			}
			try {
				httpclient.close();
			} catch (IOException e) {
				log.error(e);
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
}