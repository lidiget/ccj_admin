package com.example.democommon.unit;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class HttpClientHelper {

	/**
	 * 发送httpget请求,不带输入参数
	 * 
	 * @param url
	 * @throws IOException
	 */
	public static String doGet(String url) throws IOException {
		return doGet(url, new HashMap<String, String>());
	}

	
	
	/**
	 * 发送httpget请求，带参数 禁止url中带参数
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 *
	 */
	public static String doGet(String url, Map<String, String> params) throws IOException {
		String apiUrl = url;
		StringBuffer param = new StringBuffer();
		int i = 0;
		for (String key : params.keySet()) {
			if (i == 0) {
				param.append("?");
			} else {
				param.append("&");
			}
			param.append(key).append("=").append(params.get(key));
			i++;
		}
		apiUrl += param;

		String result = null;
		// 创建一个默认的client实例
		CloseableHttpClient client = HttpClients.createDefault();
		// 创建一个httpget对象
		HttpGet httpGet = new HttpGet(apiUrl);
		CloseableHttpResponse resp = null;
		try {
			// 执行GET请求并获取响应对象
			resp = client.execute(httpGet);
			// 获取响应体
			HttpEntity entity = resp.getEntity();
			result = EntityUtils.toString(entity, "UTF-8");
		} finally {
			// 关闭
			resp.close();
			client.close();
		}
		return result;
	}

	/**
	 * 发送post请求，不带参数
	 * 
	 * @param url
	 * @throws IOException
	 */
	public static String doPost(String url) throws IOException {
		return doPost(url, new HashMap<>());
	}

	/**
	 * 发送post请求，带参数且带参数名
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 * 
	 */
	public static String doPost(String url, Map<String, String> params) throws IOException {
		String result = null;

		// 循环请求参数
		List<NameValuePair> pairList = new ArrayList<>(params.size());
		for (Map.Entry<String, String> entry : params.entrySet()) {
			NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue().toString());
			pairList.add(pair);
		}

		// 创建一个默认的client实例
		CloseableHttpClient client = HttpClients.createDefault();
		// 创建httpPost对象
		HttpPost httpPost = new HttpPost(url);

		HttpEntity reqEntity = new UrlEncodedFormEntity(pairList, "UTF-8");
		httpPost.setEntity(reqEntity);
		CloseableHttpResponse resp = null;
		try {
			resp = client.execute(httpPost);
			HttpEntity respEntity = resp.getEntity();
			result = EntityUtils.toString(respEntity, "UTF-8");
		} finally {
			resp.close();
			client.close();
		}
		return result;
	}
    /**
	 * post请求
	 * 
	 * @param url
	 * @param json
	 * @return
	 */

	public  JSONObject doPost2(String url, JSONObject json) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		JSONObject response = null;
		try {
			StringEntity s = new StringEntity(json.toString(),Charset.forName("UTF-8"));
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json");// 发送json数据需要设置contentType
			post.setEntity(s);
			for (String key : _headers.keySet()) {
				post.addHeader(key, _headers.get(key));
			}
			HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = res.getEntity();
				String result = EntityUtils.toString(entity, "UTF-8");// 返回json格式：
				response = JSONObject.parseObject(result);

			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				client.close();
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return response;
	}

	/**
	 * 发送post请求带参数无参数名
	 * 
	 * @param url
	 * @param postParams
	 * @return
	 * @throws IOException
	 *
	 */
	public static String doPost(String url, String postParam) throws IOException {
		String result = null;
		// 创建一个默认的client实例
		CloseableHttpClient client = HttpClients.createDefault();
		// 创建httpPost对象
		HttpPost httpPost = new HttpPost(url);
		StringEntity paramEntity = new StringEntity(postParam, "UTF-8");
		httpPost.setEntity(paramEntity);
		CloseableHttpResponse resp = null;
		try {
			resp = client.execute(httpPost);
			HttpEntity respEntity = resp.getEntity();
			result = EntityUtils.toString(respEntity, "UTF-8");
		} finally {
			resp.close();
			client.close();
		}
		return result;
	}

	/**
	 * 获取输入流
	 * 
	 * @param url
	 * @param postParam
	 * @return
	 * @throws IOException
	 */
	public static InputStream post(String url, String postParam) throws IOException {

		// 创建一个默认的client实例
		CloseableHttpClient client = HttpClients.createDefault();
		// 创建httpPost对象
		HttpPost httpPost = new HttpPost(url);
		StringEntity paramEntity = new StringEntity(postParam, "UTF-8");
		httpPost.setEntity(paramEntity);
		CloseableHttpResponse resp = null;

		resp = client.execute(httpPost);
		HttpEntity respEntity = resp.getEntity();
		InputStream in = respEntity.getContent();

		return in;
	}

	/**
	 * Web端开始 HTTP请求配置
	 */

	private static Logger logger = LoggerFactory.getLogger(HttpClientHelper.class);
	private RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(15000).setConnectTimeout(15000)
			.setConnectionRequestTimeout(15000).build();
	private HashMap<String, String> _headers = new HashMap<String, String>();

	/**
	 * 构造函数
	 */
	private HttpClientHelper() {
	}

	private int _retry = 0;

	/**
	 * 获取实例
	 *
	 * @return HTTP客户端助手类
	 */
	public static HttpClientHelper getInstance() {
		return new HttpClientHelper();
	}

	public void addHeader(String key, String val) {
		_headers.put(key, val);
	}

	


}
