package com.dream.coupon;

public class CurrentUser {
	private int id=0;//用户id
	private String userName="";//用户昵称
	private String mobile="";
	private int couponScore=0;
	private int pro_differ=0;
//	private String passWord="";//用户密码
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public int getCouponScore() {
		return couponScore;
	}
	public void setCouponScore(int couponScore) {
		this.couponScore = couponScore;
	}
	public int getPro_differ() {
		return pro_differ;
	}
	public void setPro_differ(int pro_differ) {
		this.pro_differ = pro_differ;
	}
	
}

