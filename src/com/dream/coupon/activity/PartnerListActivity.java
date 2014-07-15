package com.dream.coupon.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;
import org.xml.sax.SAXException;
import com.dream.coupon.R;
import com.dream.coupon.adapter.CouponAdapter;
import com.dream.coupon.entity.Coupon;
import com.dream.coupon.utils.CouponXmlParser;
import com.dream.coupon.utils.HttpUtils;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.dream.coupon.adapter.CouponAdapter.ViewHolder;

public class PartnerListActivity extends Activity {
	private static final int MSG_TAG_STARTED = 1;
	private static final int MSG_TAG_FINISHED = 2;
	private static final int MSG_TAG_NETWORK_ERROR = 3;
	private static final int MSG_TAG_HTTPHOST_CONNECT_EXCEPTION = 4;
	private ProgressDialog progressDialog;
	private String categoryName;
	private int categoryId;
	
	private TextView mTitleView;
	private ListView lvCoupons;
//	private CouponBiz biz;
	private ArrayList<Coupon> coupons;
	private CouponAdapter adapter;
	private Handler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_partnerlist_category);
		//获取分类名称和Id
		Bundle bundle = this.getIntent().getExtras();
		categoryName = bundle.getString("categoryName");
		categoryId = bundle.getInt("categoryId");
		
//		biz = new CouponBiz();
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MSG_TAG_FINISHED:// 解析完成
					// 更新Listiew
					adapter.changeData((ArrayList<Coupon>) msg.obj);
					progressDialog.cancel();
					break;

				case MSG_TAG_STARTED:// 开始解析
					progressDialog = new ProgressDialog(PartnerListActivity.this);
					progressDialog.setTitle("提示");
					progressDialog.setIcon(android.R.drawable.ic_dialog_info);
					progressDialog.setMessage("数据加载中,请稍后...");
//					progressDialog.setIcon(R.drawable.ic_launcher);
					progressDialog.setIndeterminate(false);
					progressDialog.setCancelable(true);
					progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					progressDialog.show();
					break;
				case MSG_TAG_NETWORK_ERROR:
					progressDialog.cancel();
					Toast.makeText(PartnerListActivity.this, "网络连接超时，请重新连接。",2000).show();
					finish();
					break;
				case MSG_TAG_HTTPHOST_CONNECT_EXCEPTION:
					Toast.makeText(PartnerListActivity.this, "请检查网络是否开启。",2000).show();
					finish();
					break;
				}
			};
		};
		prepareView();
		addListener();
	}

	private void addListener(){
		lvCoupons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(PartnerListActivity.this, PartnerDetailActivity.class);
				Coupon coupon = coupons.get(position);
//				Bitmap bm = ((ViewHolder)view.getTag()).ivPartnerImage.getDrawingCache();
				ImageView iv = ((ViewHolder)view.getTag()).ivPartnerImage;
				iv.setDrawingCacheEnabled(true);
				Bitmap bm = Bitmap.createBitmap(iv.getDrawingCache());
				iv.setDrawingCacheEnabled(false);
				intent.putExtra("bm",bm);
//				Log.i("coupon",bm.toString());
				intent.putExtra("couponId", coupon.getId());
				intent.putExtra("partnerId", coupon.getPartnerId());
				startActivity(intent);
			}
		});
	}
	
	private void prepareView() {
		mTitleView = (TextView) findViewById(R.id.text_title);
		mTitleView.setText(categoryName);
		
		// 初始化listView
		lvCoupons = (ListView) findViewById(R.id.list_shop);
		adapter = new CouponAdapter(this, null, lvCoupons);
		lvCoupons.setAdapter(adapter);

		// 启动工作线程，联网解析xml
		new Thread() {
			public void run() {
				try {
					/* 联网解析xml */
					// 发送消息到主线程，提示开始解析
					handler.sendEmptyMessage(MSG_TAG_STARTED);
					HttpEntity entity;
					// 连接服务端获得响应实体对象
					if(categoryId == -1){					//全部折扣券
						entity = HttpUtils.getEntity(HttpUtils.BASE_URL
								+ "coupon/coupons", null, HttpUtils.METHOD_GET);
					}else{									//指定分类折扣券
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
						nameValuePairs.add(new BasicNameValuePair("categoryId",String.valueOf(categoryId)));
						entity = HttpUtils.getEntity(HttpUtils.BASE_URL
								+ "coupon/coupons", nameValuePairs, HttpUtils.METHOD_GET);
					}
					if(entity == null){
						handler.sendEmptyMessage(MSG_TAG_NETWORK_ERROR);
						return;
					}
					// 根据实体对象获取实体输入流
					InputStream in = HttpUtils.getStream(entity);
					// 解析输入流，获取数据集
					coupons = new CouponXmlParser().parse(in);

					/* 发送消息到主线程 */
					// 创建消息对象
					Message msg = Message.obtain(handler, MSG_TAG_FINISHED,
							coupons);
					// 发送消息
					msg.sendToTarget();
				} catch (ConnectTimeoutException e) {
					handler.sendEmptyMessage(MSG_TAG_NETWORK_ERROR);
					e.printStackTrace();
				} catch (SocketTimeoutException e){
					handler.sendEmptyMessage(MSG_TAG_NETWORK_ERROR);
					e.printStackTrace();
				} catch (HttpHostConnectException e) {
					handler.sendEmptyMessage(MSG_TAG_HTTPHOST_CONNECT_EXCEPTION);
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		}.start();
	}

}
