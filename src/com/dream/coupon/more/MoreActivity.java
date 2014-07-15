package com.dream.coupon.more;

import com.dream.coupon.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

public class MoreActivity extends Activity {
	TextView mTitleView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_activity);
		prepareView();
		mTitleView.setText(R.string.category_more);
	}

	private void prepareView() {
		mTitleView = (TextView) findViewById(R.id.title_text);
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
