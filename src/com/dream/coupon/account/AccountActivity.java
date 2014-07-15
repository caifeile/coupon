package com.dream.coupon.account;

import com.dream.coupon.DataShare;
import com.dream.coupon.R;
import com.dream.coupon.activity.MyCouponListActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.RelativeLayout;

public class AccountActivity extends Activity {
	//private TextView tvTitleView;
	private TextView tvMyName;
	private TextView tvMobile;
	private TextView tvCouponScore;
	private RelativeLayout myRelativeLayout;
	private OnClickListener listener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_my);
		prepareView();
		addListener();
	}

	private void prepareView() {
		//tvTitleView = (TextView) findViewById(R.id.title_text);
		tvMyName = (TextView) findViewById(R.id.my_name);
		tvMobile = (TextView) findViewById(R.id.text_account);
		tvCouponScore = (TextView) findViewById(R.id.text_point);

		//tvTitleView.setText(R.string.category_account);
		tvMyName.append(DataShare.currentUser.getUserName());
		tvMobile.append(DataShare.currentUser.getMobile());
//		tvCouponScore.append(Integer.toString(DataShare.currentUser.getCouponScore()));
	}

	private void addListener(){
		listener = new OnClickListener(){
			public void onClick(View v){
				switch(v.getId()){
				case R.id.layout_discount:
					Intent intent = new Intent();
					intent.setClass(AccountActivity.this, MyCouponListActivity.class);
					intent.putExtra("uid", DataShare.currentUser.getId());
					startActivity(intent);
					break;
				case R.id.layout_focuse:
					break;
				}
			}
		};
		myRelativeLayout = (RelativeLayout)findViewById(R.id.layout_discount);
		myRelativeLayout.setOnClickListener(listener);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		tvCouponScore.setText("积分：" + DataShare.currentUser.getCouponScore());
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
