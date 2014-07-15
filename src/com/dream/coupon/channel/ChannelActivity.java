package com.dream.coupon.channel;

import java.util.ArrayList;

import com.dream.coupon.R;
import com.dream.coupon.activity.PartnerListActivity;
import com.dream.coupon.biz.CategoryBiz;
import com.dream.coupon.entity.Category;
import com.dream.coupon.home.LvCategoryAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class ChannelActivity extends Activity {
	private ListView lvCategory;	//分类列表
	private CategoryBiz categoryBiz;
	ArrayList<Category> categorys;
	int[] tags = new int[] { 0, 0, 0, 0, 0 };
	TextView mTitleView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.channel_activity);
		categoryBiz = new CategoryBiz();
		categorys = categoryBiz.getCategorys();
		prepareView();
		addListener();
	}

	private void prepareView() {
		mTitleView = (TextView) findViewById(R.id.title_text);
		mTitleView.setText(R.string.category_channel);
		lvCategory = (ListView) findViewById(R.id.lvCategory);
		LvCategoryAdapter lvCategoryAdapter = new LvCategoryAdapter(this, categorys);
		lvCategory.setAdapter(lvCategoryAdapter);
	}
	
	private void addListener() {
		lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(ChannelActivity.this, PartnerListActivity.class);
				Category category = categorys.get(position);
				intent.putExtra("categoryId", category.getId());
				intent.putExtra("categoryName", category.getName());
				startActivity(intent);
			}
		});
	}
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
