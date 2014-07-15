package com.dream.coupon.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dream.coupon.R;
import com.dream.coupon.entity.Coupon;

public class MyCouponsAdapter extends BaseAdapter{
	private ArrayList<Coupon> coupons;
	private LayoutInflater inflater;
	
	public void setCoupons(ArrayList<Coupon> coupons){
		if(coupons != null)
			this.coupons = coupons;
		else
			this.coupons = new ArrayList<Coupon>();
	}
	
	public MyCouponsAdapter(Context context,ArrayList<Coupon> coupons){
		this.setCoupons(coupons);
		this.inflater = LayoutInflater.from(context);
	}
	
	public void changeData(ArrayList<Coupon> coupons) {
		this.setCoupons(coupons);
		this.notifyDataSetChanged();// 更新listView
	}
	
	@Override
	public int getCount() {
		return coupons.size();
	}

	@Override
	public Object getItem(int position) {
		return coupons.get(position);
	}

	@Override
	public long getItemId(int position) {
		return coupons.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_my_coupons, null);
			holder = new ViewHolder();
			holder.tvPartnerName = (TextView) convertView.findViewById(R.id.text_shopname);
			holder.tvDesciption = (TextView) convertView.findViewById(R.id.text_desciption);
			holder.tvOverDate = (TextView) convertView.findViewById(R.id.text_overdate);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		// 获取数据
		final Coupon coupon = coupons.get(position);
		
		//绑定到界面
		holder.tvPartnerName.setText(coupon.getPartnerName());
		holder.tvDesciption.setText(coupon.getTitle());
		holder.tvOverDate.setText(coupon.getIndate());
		
		return convertView;
	}

	class ViewHolder {
		private TextView tvPartnerName;
		private TextView tvDesciption;
		private TextView tvOverDate;
	}
	
}
