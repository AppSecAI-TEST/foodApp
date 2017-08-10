package com.example.administrator.foodapp.bean;

public class Order {
	private String loginName;
	private String food;
	private String buy_id;

	public Order() {
	}

	public Order(String loginName, String food, String buy_id) {
		this.loginName = loginName;
		this.food = food;
		this.buy_id = buy_id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getFood() {
		return food;
	}

	public void setFood(String food) {
		this.food = food;
	}

	public String getBuy_id() {
		return buy_id;
	}

	public void setBuy_id(String buy_id) {
		this.buy_id = buy_id;
	}

	@Override
	public String toString() {
		return "loginName:" + loginName + ",:food:" + food + ",buy_id:"
				+ buy_id;
	}
}