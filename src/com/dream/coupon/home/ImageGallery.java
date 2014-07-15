package com.dream.coupon.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;

/**
 * 图片gallery 每次只能滑动一个
 * 
 * @author azha
 * @date 2013-05-21
 * @version 1.0
 */
public class ImageGallery extends Gallery {
	public ImageGallery(Context context, AttributeSet paramAttributeSet) {
		super(context, paramAttributeSet);
	}

	@Override
	public boolean onFling(MotionEvent e1,
			MotionEvent e2, float velocityX, float paramFloat2) {
//		int count = this.getCount();
//		int currentPosition = this.getSelectedItemPosition();
//		Log.i("coupon","onFling,currentPosition = "+currentPosition+",count = "+count+",e1.getX() = "+e1.getX()+",e2.getX() = "+e2.getX() +"velcocityX = " +velocityX);
//		if (e1.getX() - e2.getX() > 50 && Math.abs(velocityX) > 2000) {
//			// 从右向左 下一幅
//			if(currentPosition == count-1){
//				currentPosition = 0;
//			}
//		} else if (e2.getX() - e1.getX() > 50 && Math.abs(velocityX) > 2000) {
//			// 从左向右，上一幅
//			if(currentPosition == 0){
//				currentPosition = count-1;
//			}
//		}
//		this.setSelection(currentPosition);
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1,
			MotionEvent e2, float paramFloat1, float paramFloat2) {
		float f = 1.4F * paramFloat1;
		return super.onScroll(e1, e2, f,
				paramFloat2);
	}

	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		return true;
	}
}