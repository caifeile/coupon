package com.dream.coupon.adapter;

import java.util.ArrayList;

import com.dream.coupon.R;
import com.dream.coupon.entity.Coupon;
import com.dream.coupon.utils.AsyncImageLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.dream.coupon.utils.AsyncImageLoader.Callback;

public class CouponAdapter extends BaseAdapter {

	private ArrayList<Coupon> coupons;
	private LayoutInflater inflater;
	private AsyncImageLoader loader;
	private Callback callback;

	public CouponAdapter(Context context, ArrayList<Coupon> coupons,
			final ListView lvCoupons) {
		this.setCoupons(coupons);
		this.inflater = LayoutInflater.from(context);
		this.callback = new Callback() {

			@Override
			public void imageLoaded(String path, Bitmap bitmap) {
				// TODO Auto-generated method stub
				ImageView iv = (ImageView) lvCoupons.findViewWithTag(path);
				if (iv != null && bitmap != null)
					iv.setImageBitmap(bitmap);
			}
		};
		this.loader = new AsyncImageLoader();
	}

	private void setCoupons(ArrayList<Coupon> coupons) {
		if (coupons != null)
			this.coupons = coupons;
		else
			this.coupons = new ArrayList<Coupon>();
	}

	public void quit() {
		loader.quit();
	}

	/**
	 * 更新adapter中的数据集
	 * 
	 * @param coupons
	 */
	public void changeData(ArrayList<Coupon> coupons) {
		this.setCoupons(coupons);
		this.notifyDataSetChanged();// 更新listView
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return coupons.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return coupons.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return coupons.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// 加载和复用convertView
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.couponlist_item, null);
			holder = new ViewHolder();
			holder.ivPartnerImage = (ImageView) convertView.findViewById(R.id.image_shop);
			holder.tvPartnerName = (TextView) convertView.findViewById(R.id.text_shopname);
			holder.tvPartnerAddress = (TextView) convertView.findViewById(R.id.text_shopaddress);
			holder.ivTicket = (ImageView) convertView.findViewById(R.id.image_discount);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.text_shopdiscount);
			holder.tvTag = (TextView) convertView.findViewById(R.id.text_shoptag);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 获取数据
		final Coupon coupon = coupons.get(position);

		// 绑定到界面
		holder.tvPartnerName.setText(coupon.getPartnerName());
		holder.tvPartnerAddress.setText(coupon.getPartnerAddress());
		holder.tvTitle.setText(coupon.getTitle());
		holder.tvTag.setText(coupon.getTag());

		String path = null;
		if((path = coupon.getPartnerImage()) == null){
			path = coupon.getCouponImage();
		}	
		// 设置图片路径为imageview的tag
		if(path == null || "".equals(path)){
			holder.ivPartnerImage.setTag(path);
			holder.ivPartnerImage.setImageResource(R.drawable.bg_shop_default);
			return convertView;
		}
		holder.ivPartnerImage.setTag(path);
		// 加载图片
		Bitmap bm = loader.loadBitmap(path, callback);
		// 如果存在缓存图片则显示，否则显示默认图片
		if (bm != null)
			holder.ivPartnerImage.setImageBitmap(bm);
		else
			holder.ivPartnerImage.setImageResource(R.drawable.bg_shop_default);

		return convertView;
	}

	public class ViewHolder {
		public ImageView ivPartnerImage;
		private TextView tvPartnerName;
		private TextView tvPartnerAddress;
		private ImageView ivTicket;
		private TextView tvTitle;
		private TextView tvTag;
	}
}
