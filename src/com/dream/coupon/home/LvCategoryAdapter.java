package com.dream.coupon.home;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.dream.coupon.R;
import com.dream.coupon.entity.Category;

public class LvCategoryAdapter extends BaseAdapter{
	private ArrayList<Category> categorys;
	private LayoutInflater inflater;
	
	public void setCategorys(ArrayList<Category> categorys){
		if(categorys != null)
			this.categorys = categorys;
		else
			this.categorys = new ArrayList<Category>();
	}
	
	public LvCategoryAdapter(Context context,ArrayList<Category> categorys){
		this.setCategorys(categorys);
		this.inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return categorys.size();
	}

	@Override
	public Object getItem(int position) {
		return categorys.get(position);
	}

	@Override
	public long getItemId(int position) {
		return categorys.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.category_item, null);
			holder = new ViewHolder();
			holder.ivTag = (ImageView) convertView.findViewById(R.id.ivTag);
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			holder.ivMore = (ImageView) convertView.findViewById(R.id.ivMore);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		// 获取数据
		final Category category = categorys.get(position);
		
		//绑定到界面
		holder.tvName.setText(category.getName());
		int res = category.getRes();
		holder.ivTag.setImageResource(res);
		holder.ivMore.setImageResource(R.drawable.ic_right);
		
		return convertView;
	}

	class ViewHolder {
		private ImageView ivTag;
		private TextView tvName;
		private ImageView ivMore;
	}
	
}
