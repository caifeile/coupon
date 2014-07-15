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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dream.coupon.DataShare;
import com.dream.coupon.R;
import com.dream.coupon.adapter.MyCouponsAdapter;
import com.dream.coupon.entity.Coupon;
import com.dream.coupon.utils.ConnectWeb;
import com.dream.coupon.utils.CouponXmlParser;
import com.dream.coupon.utils.HttpUtils;

public class MyCouponListActivity extends Activity{
	private static final int MSG_TAG_STARTED = 1;
	private static final int MSG_TAG_FINISHED = 2;
	private static final int MSG_TAG_NETWORK_ERROR = 3;
	private static final int MSG_TAG_HTTPHOST_CONNECT_EXCEPTION = 4;
	private int uid;
	
	private ListView lvCoupons;
	private ArrayList<Coupon> coupons;
	private MyCouponsAdapter adapter;
	private Handler handler;
	private AlertDialog dialog;
	
	TextView mTitleView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
     
		setContentView(R.layout.layout_my_discountlist);	
		Bundle bundle = this.getIntent().getExtras();
		uid = bundle.getInt("uid");
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MSG_TAG_FINISHED:// 解析完成
					// 更新Listiew
					adapter.changeData((ArrayList<Coupon>) msg.obj);
					break;

				case MSG_TAG_STARTED:// 开始解析
					Toast.makeText(MyCouponListActivity.this, "正在连网，请稍候...",
							2000).show();
					break;
				case MSG_TAG_NETWORK_ERROR:
					dialog.cancel();
					Toast.makeText(MyCouponListActivity.this, "网络连接超时，请重新连接。",2000).show();
					finish();
					break;
				case MSG_TAG_HTTPHOST_CONNECT_EXCEPTION:
					Toast.makeText(MyCouponListActivity.this, "请检查网络是否开启。",2000).show();
					finish();
					break;
				}
			};
		};
		prepareView();
		addListener();
	}

	private void prepareView() {
		lvCoupons = (ListView) findViewById(R.id.list_discount);
		adapter = new MyCouponsAdapter(this,null);
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
								//指定分类折扣券
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
						nameValuePairs.add(new BasicNameValuePair("uid",String.valueOf(uid)));
						entity = HttpUtils.getEntity(HttpUtils.BASE_URL
								+ "coupon/orders", nameValuePairs, HttpUtils.METHOD_GET);
					// 根据实体对象获取实体输入流
					if(entity == null){
						handler.sendEmptyMessage(MSG_TAG_NETWORK_ERROR);
						return;
					}
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
	
	private void addListener(){
		lvCoupons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(MyCouponListActivity.this, coupons.get(position).getPartnerName()+":"+coupons.get(position).getTitle(),
						2000).show();
//				Intent intent = new Intent();
//				intent.setClass(MyCouponListActivity.this, PartnerDetailActivity.class);
//				Coupon coupon = coupons.get(position);
//				intent.putExtra("couponId", coupon.getId());
//				intent.putExtra("partnerId", coupon.getPartnerId());
//				startActivity(intent);
			}
		});
		lvCoupons.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				dialog = new Builder(MyCouponListActivity.this)
				.setTitle("确定删除此折扣券么？")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton("确定", new OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Coupon coupon = coupons.get(position);
						int message = new ConnectWeb().deleteBill(DataShare.currentUser.getId(), coupon.getId());
						switch(message){
						case -1:
							Intent intent = new Intent();
							intent.setClass(MyCouponListActivity.this,LoginActivity.class);
							startActivity(intent);
							break;
						case 0:
							break;
						case 1:
							Toast.makeText(MyCouponListActivity.this, "删除成功", 2000).show();
							coupons.remove(position);
							adapter.changeData(coupons);
						}
					}
				}).setNegativeButton("取消", null).create();
				dialog.show();
				return true;
			}
			
		});
//		lvCoupons.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
//
//			@Override
//			public void onCreateContextMenu(ContextMenu menu, View v,
//					ContextMenuInfo menuInfo) {
//				// TODO Auto-generated method stub
//				menu.setHeaderIcon(android.R.drawable.ic_menu_camera);
//				menu.setHeaderTitle("操作");
//				menu.add(2, MENU_CONTEXT_DETAILS, 1, "详情");
//				menu.add(2, MENU_CONTEXT_UPDATE, 2, "修改");
//				menu.add(2, MENU_CONTEXT_DELETE, 3, "删除");
//			}
//		});
	}
}
