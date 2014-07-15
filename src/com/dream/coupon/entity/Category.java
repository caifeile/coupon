package com.dream.coupon.entity;

public class Category {
	private int id;
	private int res;
	private String name;
//	private String ename;
//	private int sortOrder;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRes() {
		return res;
	}
	public void setRes(int res) {
		this.res = res;
	}
}