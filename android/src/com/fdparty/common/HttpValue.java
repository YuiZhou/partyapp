package com.fdparty.common;

public enum HttpValue {
	Server("http://ics.fudan.edu.cn/test/index.php");
	
	
	
	private String context;
	
	private HttpValue(String context) {
		this.context = context;
	}
	
	public String toString(){
		return this.context;
	}
}
