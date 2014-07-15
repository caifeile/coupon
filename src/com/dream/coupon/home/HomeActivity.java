package com.dream.coupon.home;

import java.util.ArrayList;
import java.util.HashMap;
import com.dream.coupon.activity.PartnerListActivity;
import com.dream.coupon.biz.CategoryBiz;
import com.dream.coupon.entity.Category;
import com.dream.coupon.widget.FlowIndicator;
import com.dream.coupon.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ExpandableListView;
//import android.widget.Gallery;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class HomeActivity extends Activity {
	static final int SCROLL_ACTION = 0;
	ExpandableListView mExpandableListView;
//	private ListView lvCategory;	//分类列表
//	Gallery mGallery;
	private CategoryBiz categoryBiz;
	private GridView gridview;
	private ArrayList<HashMap<String,Object>> lstImageItem;
	ArrayList<Category> categorys;
	int[] tags = new int[] { 0, 0, 0, 0, 0 };
	private GestureDetector detector;
	ImageGallery gallery; //rarb
	GalleryAdapter mGalleryAdapter;
	FlowIndicator mMyView;
	FlowIndicator myView; //rarb
//	Timer mTimer;
	int windowWith = 480;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		detector = new GestureDetector(new MyGestureListener());
		windowWith = getWindowManager().getDefaultDisplay().getWidth();
		categoryBiz = new CategoryBiz();
		categorys = categoryBiz.getCategorys();
		lstImageItem = categoryBiz.getGridViewCategorys();
		prepareView();
		addListener();
		
//		mTimer = new Timer();
//		mTimer.scheduleAtFixedRate(new MyTask(), 0, 5000);
	}

	private void prepareView() {
		gridview = (GridView) findViewById(R.id.categoryGrid);
//		lvCategory = (ListView) findViewById(R.id.lvCategory);
//		LvCategoryAdapter lvCategoryAdapter = new LvCategoryAdapter(this, categorys);
		gallery = (ImageGallery) this.findViewById(R.id.rarb_home_gallery);  //rarb
		myView = (FlowIndicator) this.findViewById(R.id.rarb_myView);   //rarb
		mGalleryAdapter = new GalleryAdapter(this,windowWith);
		//myView.setCount(mGalleryAdapter.getCount());   //rarb
		myView.setCount(mGalleryAdapter.res.length);
		gallery.setAdapter(mGalleryAdapter);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				myView.setSeletion(arg2 % myView.getCount());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
//		lvCategory.setAdapter(lvCategoryAdapter);
		
		//生成适配器的ImageItem <====> 动态数组的元素，两者一一对应 
		SimpleAdapter gridviewSimpleAdapter = new SimpleAdapter(this,lstImageItem,R.layout.item_gridview_category,new String[]{"image","text"},new int[]{R.id.ItemImage,R.id.ItemText});
		gridview.setAdapter(gridviewSimpleAdapter);  //添加并且显示
		
	}

	private void addListener() {
//		lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				Intent intent = new Intent();
//				intent.setClass(HomeActivity.this, PartnerListActivity.class);
//				Category category = categorys.get(position);
//				intent.putExtra("categoryId", category.getId());
//				intent.putExtra("categoryName", category.getName());
//				startActivity(intent);
//			}
//		});
		gridview.setOnItemClickListener(new ItemClickListener());
	}
	
//	private class MyTask extends TimerTask {
//		@Override
//		public void run() {
//			mHandler.sendEmptyMessage(SCROLL_ACTION);
//		}
//	}
//
//	Handler mHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			// TODO Auto-generated method stub
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case SCROLL_ACTION:
//				MotionEvent e1 = MotionEvent.obtain(SystemClock.uptimeMillis(),
//						SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN,
//						89.333336f, 265.33334f, 0);
//				MotionEvent e2 = MotionEvent.obtain(SystemClock.uptimeMillis(),
//						SystemClock.uptimeMillis(), MotionEvent.ACTION_UP,
//						300.0f, 238.00003f, 0);
//				if(gallery.getSelectedItemPosition() == gallery.getCount()-1){
//					gallery.setSelection(0);
//				}else
//					gallery.onScroll(e1, e2, -2300, 0);
//				break;
//
//			default:
//				break;
//			}
//		}
//	};
//	
	class ItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			
			Intent intent = new Intent();
			intent.setClass(HomeActivity.this, PartnerListActivity.class);
			HashMap<String,Object> category = lstImageItem.get(position);
			intent.putExtra("categoryId", Integer.parseInt(category.get("id").toString()));
			intent.putExtra("categoryName", category.get("text").toString());
//			Category category = categorys.get(position);
//			intent.putExtra("categoryId", category.getId());
//			intent.putExtra("categoryName", category.getName());
			startActivity(intent);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return detector.onTouchEvent(event);
	}
	
	private class MyGestureListener extends SimpleOnGestureListener {

//		@Override
//		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//				float velocityY) {
//			// TODO Auto-generated method stub
//			int currentPosition = gallery.getSelectedItemPosition();
//			if (e1.getX() - e2.getX() > 50 && Math.abs(velocityX) > 30) {
//				// 从右向左 下一幅
//				if(++currentPosition==gallery.getCount()){
//					currentPosition = 0;
//				}
//			} else if (e2.getX() - e1.getX() > 50 && Math.abs(velocityY) > 30) {
//				// 从左向右，上一幅
//				if(--currentPosition<0){
//					currentPosition = gallery.getCount()-1;
//				}
//			}
//			
//			gallery.setSelection(currentPosition);
//			return super.onFling(e1, e2, velocityX, velocityY);
//		}

	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
	
}
