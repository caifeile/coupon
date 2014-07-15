package com.dream.coupon;

import org.apache.http.client.CookieStore;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;
public class BaseApp extends Application {
	public static Context context;
	public static final String BASE_URL = "Http://192.168.8.191:8080";
	private static Handler handler;
	private CookieStore cookies;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = getBaseContext();
		handler = new Handler();
	}

	public static void showToast(final int resId) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public static void showToast(final String text) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public CookieStore getCookies() {
		return cookies;
	}
	public void setCookies(CookieStore cookies) {
		this.cookies = cookies;
	}
}
