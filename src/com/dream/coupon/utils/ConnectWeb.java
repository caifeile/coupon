package com.dream.coupon.utils;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.dream.coupon.CurrentUser;
import com.dream.coupon.DataShare;

public class ConnectWeb {
	
	private String connWeb(String url) {
		String str = "";
		try {
			HttpUriRequest request = new HttpGet(url); 
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				str = EntityUtils.toString(response.getEntity());	
				Header[] headers = response.getHeaders("set-cookie");
                //保存服务器返回的session
                for (int i = 0; i < headers.length; i++) {
                        String value = headers[i].getValue();
                        DataShare.JSESSIONID = value
                                        .substring(0, value.indexOf(";"));
                }
			}
		} catch (ConnectTimeoutException e) {
			Log.i("coupon", "连接超时");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	private String connWeb(Context context,String url) {
		String str = "";
		BasicHttpParams httpParams = new BasicHttpParams(); 
		HttpConnectionParams.setConnectionTimeout(httpParams,DataShare.TIMEOUT_DURATION);
		HttpConnectionParams.setSoTimeout(httpParams,DataShare.TIMEOUT_DURATION);
		try {
			HttpUriRequest request = new HttpGet(url); 
			HttpClient httpClient = new DefaultHttpClient(httpParams);
			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				str = EntityUtils.toString(response.getEntity());	
				Header[] headers = response.getHeaders("set-cookie");
                //保存服务器返回的session
                for (int i = 0; i < headers.length; i++) {
                        String value = headers[i].getValue();
                        DataShare.JSESSIONID = value
                                        .substring(0, value.indexOf(";"));
                }
			}
		} catch (ConnectTimeoutException e) {
			Toast.makeText(context, "网络连接超时,请检查网络重新登录", Toast.LENGTH_LONG).show();
			return "";
		} catch (SocketTimeoutException e){
			Toast.makeText(context, "网络连接超时,请检查网络重新登录", Toast.LENGTH_LONG).show();
			return "";
		} catch (HttpHostConnectException e) {
			Toast.makeText(context, "请检查网络是否开启.", Toast.LENGTH_LONG).show();
			return "";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	private String connWeb(String url,String sessionId) {
		String str = "";
		try {
			HttpUriRequest request = new HttpGet(url);
			request.setHeader("Cookie",sessionId); 
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				str = EntityUtils.toString(response.getEntity());	
				Header[] headers = response.getHeaders("set-cookie");
                //保存服务器返回的session
                for (int i = 0; i < headers.length; i++) {
                        String value = headers[i].getValue();
                        DataShare.JSESSIONID = value
                                        .substring(0, value.indexOf(";"));
                }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public CurrentUser userLogin(Context context, String email,String pwd) {
		HashMap<String , Object> map = new HashMap<String , Object>();   
		CurrentUser user=null;
		String url = HttpUtils.BASE_URL + "user/login.action?email=" + email + "&password=" + pwd;
		String str = connWeb(context,url);
		try {
			if(str == null || "".equals(str)){
				return null;
			}
			JSONObject job = new JSONObject(str);
			if(job.getInt("message") == 2){
				Toast.makeText(context, "用户名或密码错误", Toast.LENGTH_LONG).show();
			}
			JSONObject job2 =(JSONObject) job.get("user");
			user=new CurrentUser();
			user.setId(job2.getInt("id"));
			user.setUserName(job2.getString("userName"));
			user.setMobile(job2.getString("mobile"));
			user.setCouponScore(job2.getInt("couponScore"));
			user.setPro_differ(job2.getInt("pro_differ"));
			map.put("message",1);
			map.put("currentUser",user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	
	
	public int addBill(int userId,String couponId) {
		int message = -1;
		String str;
		String url = HttpUtils.BASE_URL + "coupon/addOrder?userId=" + userId + "&couponId="+couponId;
		if(!"".equals(DataShare.JSESSIONID) && DataShare.JSESSIONID != null){
			str = connWeb(url,DataShare.JSESSIONID);
//			Log.i("coupon","addBill:DataShare.JSESSIONID="+DataShare.JSESSIONID+",str="+str+"userId="+userId+"couponId="+couponId);
		}else{
//			Log.i("coupon","addBill:return");
			return -1;
		}
		try {
			JSONObject job = new JSONObject(str);
			message=job.getInt("message");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}
	
	public int deleteBill(int userId,int couponId){
		int message = -1;
		String str;
		String url = HttpUtils.BASE_URL + "coupon/deleteOrder?userId=" + userId + "&couponId="+couponId;
		if(!"".equals(DataShare.JSESSIONID) && DataShare.JSESSIONID != null){
			str = connWeb(url,DataShare.JSESSIONID);
		}else{
			return -1;
		}
		try {
			JSONObject job = new JSONObject(str);
			message=job.getInt("message");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}
}
