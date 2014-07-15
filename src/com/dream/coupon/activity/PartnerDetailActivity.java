package com.dream.coupon.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.message.BasicNameValuePair;
import org.xml.sax.SAXException;
import com.dream.coupon.DataShare;
import com.dream.coupon.R;
import com.dream.coupon.entity.Partner;
import com.dream.coupon.utils.ConnectWeb;
import com.dream.coupon.utils.HttpUtils;
import com.dream.coupon.utils.PartnerDetailXmlParser;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class PartnerDetailActivity extends Activity {
	private static final int MSG_TAG_STARTED = 1;
	private static final int MSG_TAG_FINISHED = 2;
	private static final int MSG_TAG_NETWORK_ERROR = 3;
	private static final int MSG_TAG_HTTPHOST_CONNECT_EXCEPTION = 4;
	private Handler handler;
	private ProgressDialog progressDialog;
	private ListView lvCoupons;
	private int partnerId;
	private Bitmap bm; 
	private ArrayList<HashMap<String,Object>> couponsMap;
	private SimpleAdapter adapter;
	private String[] items = { "购买折扣券，花费 2 积分", "查看详情"};
	private AlertDialog dialog;
	private int choice = 1;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
//		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//        .detectDiskReads()
//        .detectDiskWrites()
//        .detectAll()   
//        .penaltyLog()
//        .build());
//     StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//        .detectLeakedSqlLiteObjects()
//        .detectAll()
//        .penaltyLog()
//        .penaltyDeath()
//        .build());
//		
		setContentView(R.layout.layout_partner_detail);
		// 获取折扣券Id
		Bundle bundle = this.getIntent().getExtras();
		bm = (Bitmap)bundle.get("bm");
		partnerId = bundle.getInt("partnerId");
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MSG_TAG_FINISHED:// 解析完成
					setupView((HashMap<String,Object>) msg.obj);
					progressDialog.cancel();
					break;
				case MSG_TAG_STARTED:// 开始解析
					progressDialog = new ProgressDialog(PartnerDetailActivity.this);
					progressDialog.setTitle("提示");
					progressDialog.setIcon(android.R.drawable.ic_dialog_info);
					progressDialog.setMessage("数据加载中,请稍后...");
					progressDialog.setIndeterminate(false);
					progressDialog.setCancelable(true);
					progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					progressDialog.show();
					break;
				case MSG_TAG_NETWORK_ERROR:
					progressDialog.cancel();
					Toast.makeText(PartnerDetailActivity.this, "网络连接超时，请重新连接。",2000).show();
					finish();
					break;
				case MSG_TAG_HTTPHOST_CONNECT_EXCEPTION:
					Toast.makeText(PartnerDetailActivity.this, "请检查网络是否开启。",2000).show();
					finish();
					break;
				}
			};
		};
		lvCoupons = (ListView) findViewById(R.id.layout_discount);
		prepareView();
	}
	
	private void addListener(){
		lvCoupons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
//				Intent intent = new Intent();
//				intent.setClass(PartnerListActivity.this, PartnerDetailActivity.class);
				
				dialog = new Builder(PartnerDetailActivity.this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(items, 1, new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						choice = which;
					}
				}).setPositiveButton("确定", new OnClickListener(){
					public void onClick(DialogInterface dialog, int which) {
						if(choice == 0){
							if(DataShare.currentUser.getCouponScore()<2 && DataShare.currentUser.getId() != 0){
								Toast.makeText(PartnerDetailActivity.this, "积分不足,您当前的积分为"+DataShare.currentUser.getCouponScore(),
										2000).show();
								return;
							}
							if(couponsMap!=null){
								HashMap<String, Object> coupon = couponsMap.get(position);
								String couponId = coupon.get("id").toString();
								int message = new ConnectWeb().addBill(DataShare.currentUser.getId(), couponId);
								switch(message){
								case -1:
									Intent intent = new Intent();
									intent.setClass(PartnerDetailActivity.this, LoginActivity.class);
									startActivity(intent);
									break;
								case 0:
									Toast.makeText(PartnerDetailActivity.this, "您已经购买过此折扣券。",
											2000).show();
									break;
								case 1:
									Toast.makeText(PartnerDetailActivity.this, "购买成功",
											2000).show();
									DataShare.currentUser.setCouponScore(DataShare.currentUser.getCouponScore() - 2);
									Toast.makeText(PartnerDetailActivity.this, "积分 -2",
											2000).show();
									break;
								case 2:
									Toast.makeText(PartnerDetailActivity.this, "积分不足,您当前的积分为"+DataShare.currentUser.getCouponScore(),
											2000).show();
								}
							}
						}else if(choice == 1){
							Toast.makeText(PartnerDetailActivity.this, "新建详情Activity",
									2000).show();
						}
					}
					
				}).setNegativeButton("取消", null).create();
				dialog.show();
//				intent.putExtra("couponId", coupon.getId());
//				intent.putExtra("partnerId", coupon.getPartnerId());
//				startActivity(intent);
			}
		});
	}
	
	private void prepareView(){
		// 启动工作线程，联网解析xml
		new Thread() {
			public void run() {
				try {
					/* 联网解析xml */
					// 发送消息到主线程，提示开始解析
					handler.sendEmptyMessage(MSG_TAG_STARTED);
					// 连接服务端获得响应实体对象
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
					nameValuePairs.add(new BasicNameValuePair("pid",String.valueOf(partnerId)));
					HttpEntity entity = HttpUtils.getEntity(HttpUtils.BASE_URL
							+ "coupon/merchant", nameValuePairs, HttpUtils.METHOD_GET);
					// 根据实体对象获取实体输入流
					if(entity == null){
						handler.sendEmptyMessage(MSG_TAG_NETWORK_ERROR);
						return;
					}
					InputStream in = HttpUtils.getStream(entity);
					// 解析输入流，获取数据集
					HashMap<String,Object> hashmap = new PartnerDetailXmlParser().parse(in);
					/* 发送消息到主线程 */
					// 创建消息对象
					Message msg = Message.obtain(handler, MSG_TAG_FINISHED,
							hashmap);
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
	
	public void setupView(HashMap<String,Object> map){
		Partner partner = (Partner) map.get("partner");
		if(partner != null){
			ImageView ivPartnerImage = (ImageView)findViewById(R.id.image_shop);
			ivPartnerImage.setImageBitmap(bm);
			TextView tvPartnerName = (TextView)findViewById(R.id.text_shopname);
			TextView tvPartnerAddress = (TextView)findViewById(R.id.text_address);
			TextView tvPartnerPhone = (TextView)findViewById(R.id.text_phone);
			TextView tvPartnerContent = (TextView)findViewById(R.id.text_intro_content);
			TextView tvPartnerTime = (TextView)findViewById(R.id.text_intro_time);
			tvPartnerName.setText(partner.getTitle());
			tvPartnerAddress.setText(partner.getAddress());
			tvPartnerPhone.setText(partner.getPhone());
			tvPartnerContent.setText(partner.getContent());
			tvPartnerTime.append(partner.getHours());
		}
		
		String[] from = { "partnerName","title", "indate"};
		int[] to = { R.id.text_shopname,R.id.text_desciption, R.id.text_overdate};
		couponsMap = (ArrayList<HashMap<String,Object>>)map.get("coupons");
		adapter = new SimpleAdapter(this,couponsMap, R.layout.item_partner_discountlist,
				from, to);
		lvCoupons.setAdapter(adapter);
		
		int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, lvCoupons);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
		ViewGroup.LayoutParams params = lvCoupons.getLayoutParams();
        params.height = totalHeight;
        lvCoupons.setLayoutParams(params);
        addListener();
	}
}
