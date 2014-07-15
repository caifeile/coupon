package com.dream.coupon.biz;

import java.util.ArrayList;
import java.util.HashMap;

import com.dream.coupon.R;
import com.dream.coupon.entity.Category;

public class CategoryBiz {
	private ArrayList<Category> categorys;
	private int[] res = new int[] {R.drawable.ic_food,R.drawable.ic_play,R.drawable.ic_shop,R.drawable.ic_hotel,R.drawable.ic_photo,R.drawable.ic_more};

	public CategoryBiz(){
		categorys = new ArrayList<Category>();
		
		Category category = new Category();
		category.setId(-1);
		category.setRes(res[5]);
		category.setName("全部");
		categorys.add(category);
		
		category = new Category();
		category.setId(18);
		category.setRes(res[0]);
		category.setName("餐饮美食");
		categorys.add(category);
		
		category = new Category();
		category.setId(19);
		category.setRes(res[1]);
		category.setName("休闲娱乐");
		categorys.add(category);
		
		category = new Category();
		category.setId(20);
		category.setRes(res[2]);
		category.setName("购物生活");
		categorys.add(category);
		
		category = new Category();
		category.setId(21);
		category.setRes(res[3]);
		category.setName("酒店度假");
		categorys.add(category);
		
		category = new Category();
		category.setId(22);
		category.setRes(res[4]);
		category.setName("摄影婚庆");
		categorys.add(category);
		
	}
	
	public ArrayList<Category> getCategorys(){
		return categorys;
	}
	
	public ArrayList<HashMap<String,Object>> getGridViewCategorys(){
		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();  
		HashMap<String, Object> map = new HashMap<String, Object>();  
		map.put("id", -1);
		map.put("image", res[5]);//添加图像资源的ID  
		map.put("text", "全部");//分类名称
		lstImageItem.add(map);
		
		map = new HashMap<String, Object>();
		map.put("id", 18);
		map.put("image", res[0]);
		map.put("text", "餐饮美食");
		lstImageItem.add(map);
		
		map = new HashMap<String, Object>();
		map.put("id", 19);
		map.put("image", res[1]);
		map.put("text", "休闲娱乐");
		lstImageItem.add(map);
		
		map = new HashMap<String, Object>();
		map.put("id", 20);
		map.put("image", res[2]);
		map.put("text", "购物生活");
		lstImageItem.add(map);
		
		map = new HashMap<String, Object>();
		map.put("id", 21);
		map.put("image", res[3]);
		map.put("text", "酒店度假");
		lstImageItem.add(map);
		
		map = new HashMap<String, Object>();
		map.put("id", 22);
		map.put("image", res[4]);
		map.put("text", "婚庆摄影");
		lstImageItem.add(map);
		
		return lstImageItem;
	}
}
