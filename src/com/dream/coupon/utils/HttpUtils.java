package com.dream.coupon.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import android.util.Log;

import com.dream.coupon.DataShare;

public class HttpUtils {
	public static final String BASE_URL = "http://61.164.108.177/datuan/";
//	public static final String BASE_URL = "http://192.168.8.191:8080/datuan/";
	public static final int METHOD_GET = 1;
	public static final int METHOD_POST = 2;

	/**
	 * 从指定uri，获取响应实体
	 * 
	 * @param uri
	 * @param params
	 * @param method
	 * @return
	 * @throws IOException
	 */
	public static HttpEntity getEntity(String uri,
			List<? extends NameValuePair> params, int method)
			throws ConnectTimeoutException, ClientProtocolException,IOException {
		HttpEntity entity = null;
		BasicHttpParams httpParams = new BasicHttpParams(); 
		HttpConnectionParams.setConnectionTimeout(httpParams,DataShare.TIMEOUT_DURATION);
		HttpConnectionParams.setSoTimeout(httpParams,DataShare.TIMEOUT_DURATION);
		// 创建客户端对象
		HttpClient client = new DefaultHttpClient(httpParams);
		// 设置连接属性
		// 创建请求对象
		HttpUriRequest request = null;
		switch (method) {
		case METHOD_GET:
			// 拼接uri
			StringBuilder sb = new StringBuilder(uri);
			if (params != null && !params.isEmpty()) {
				sb.append('?');
				for (NameValuePair pair : params) {
					sb.append(pair.getName()).append('=')
							.append(pair.getValue()).append('&');
				}
				sb.deleteCharAt(sb.length() - 1);
			}
			request = new HttpGet(sb.toString());
			break;
		case METHOD_POST:
			request = new HttpPost(uri);
			if (params != null && !params.isEmpty()) {
				UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(
						params);
				((HttpPost) request).setEntity(reqEntity);
			}
			break;
		}
		// 执行请求获得响应对象
		HttpResponse response = client.execute(request);
		// 判断响应码获取 实体对象
		
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			entity = response.getEntity();
		}else{
			return null;
		}
		return entity;
	}

	public static InputStream getStream(HttpEntity entity) throws IOException {
		InputStream in = null;
		if (entity != null) {
			in = entity.getContent();
		}

		return in;
	}

	public static long getLength(HttpEntity entity) {
		if (entity != null)
			return entity.getContentLength();
		return 0;
	}
}
