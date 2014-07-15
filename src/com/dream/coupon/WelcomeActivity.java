package com.dream.coupon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class WelcomeActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_activity);

//		PackageManager pm = getPackageManager();
//		try {
//		PackageInfo pi = pm.getPackageInfo("com.lyt.android", 0);
//		TextView versionNumber = (TextView) findViewById(R.id.versionNumber);
//		versionNumber.setText("Version " + pi.versionName);
//		} catch (NameNotFoundException e) {
//		e.printStackTrace();
//		}

		new Handler().postDelayed(new Runnable(){
			@Override
			public void run() {
			Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
			startActivity(intent);
			WelcomeActivity.this.finish();
			}
		}, 2500);
	}
}
