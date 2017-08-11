package com.example.administrator.foodapp.bean;

public class Love {
	private String  loginName;
	private String  love;
	private String id ;
	public Love(){}
	public Love(String loginName,String love,String id){
		this.loginName = loginName;
		this.love = love;
		this.id = id;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLove() {
		return love;
	}
	public void setLove(String love) {
		this.love = love;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}