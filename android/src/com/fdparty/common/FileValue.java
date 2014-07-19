package com.fdparty.common;

public enum FileValue {
	loginInfo("conf"),
	userInfo("user");
	
	
	
	private String context;
	
	private FileValue(String context) {
		this.context = context;
	}
	
	public String toString(){
		return this.context;
	}
}
