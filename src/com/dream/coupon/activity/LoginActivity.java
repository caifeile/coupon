package com.dream.coupon.activity;

import com.dream.coupon.CurrentUser;
import com.dream.coupon.DataShare;
import com.dream.coupon.R;
import com.dream.coupon.utils.ConnectWeb;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private EditText username;//声明EditText类型变量
	private EditText password;//声明EditText类型变量
	private Button loginBut;//声明Button类型变量
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		 StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
         .detectDiskReads()
         .detectDiskWrites()
         .detectAll()   
         .penaltyLog()
         .build());
      StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
         .detectLeakedSqlLiteObjects()
         .detectAll()
         .penaltyLog()
         .penaltyDeath()
         .build());
		this.setContentView(R.layout.viewlogin);//加载布局资源文件viewlogin.xml
		
		username=(EditText) this.findViewById(R.id.username);//加载布局资源文件中的EditText组件
		password=(EditText) this.findViewById(R.id.password);//加载布局资源文件中的EditText组件
		loginBut=(Button) this.findViewById(R.id.loginBut);//加载布局资源文件中的Button组件
		
		
		loginBut=(Button) this.findViewById(R.id.loginBut);//加载布局资源文件中的Button组件
		loginBut.setOnClickListener(new OnClickListener(){//Button组件的单击事件

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//验证用户名和密码是否正确
				CurrentUser currentUser=new ConnectWeb().userLogin(LoginActivity.this,username.getText().toString(),password.getText().toString());
				if(currentUser!=null){
					DataShare.currentUser.setId(currentUser.getId());//设置用户id
					DataShare.currentUser.setUserName(currentUser.getUserName());//设置用户昵称
					DataShare.currentUser.setMobile(currentUser.getMobile());
					DataShare.currentUser.setCouponScore(currentUser.getCouponScore());//设置用户密码
					Toast.makeText(LoginActivity.this, "登录成功", 2000).show();
					//登录成功后进入“我的”
					if(currentUser.getPro_differ() != 0)
						Toast.makeText(LoginActivity.this, "积分 + " + currentUser.getPro_differ(), 2000).show();
					setResult(DataShare.RESULT_LOGIN_SUCCESS);
					finish();
				}
			}
		});
	}
	
}
