package com.dream.coupon.home;

import com.dream.coupon.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class GalleryAdapter extends BaseAdapter {
	private int windowWith;
	Context mContext;
	int[] res = new int[] { R.drawable.ad1, R.drawable.ad2,
			R.drawable.ad3, R.drawable.ad4, R.drawable.ad5,
			R.drawable.ad6, R.drawable.ad7, R.drawable.ad8,
			};

	public GalleryAdapter(Context cnt , int windowWith) {
		this.mContext = cnt;
		this.windowWith = windowWith;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return res[position % res.length];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		
		ImageView imageView = new ImageView(mContext); 
		imageView.setImageResource(res[position % res.length]);
		   //设置显示比例类型
		   //设置布局图片以屏幕宽度显示 （简单解释——设置数字不一样，图片的显示大小不一样）
		imageView.setLayoutParams(new Gallery.LayoutParams(windowWith, 29*windowWith/60)); 
		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER); 
		return imageView; 
//		if (arg1 == null) {
//			arg1 = LayoutInflater.from(mContext).inflate(R.layout.gallery_item,
//					null);
//		}
//		ImageView img = (ImageView) arg1.findViewById(R.id.home_img);
//		
//		img.setImageResource(res[arg0]);
////		img.setLayoutParams(new LayoutParams(windowWith, 29*windowWith/60));
////		  // 设置显示比例类型
////		img.setScaleType(ImageView.ScaleType.FIT_CENTER);
//		return arg1;
	}
}
