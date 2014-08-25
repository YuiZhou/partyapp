package com.fdparty.common;

public enum HttpValue {
	Server("http://192.168.0.101/partyapp/server/index.php/");
	
	
	
	private String context;
	
	private HttpValue(String context) {
		this.context = context;
	}
	
	public String toString(){
		return this.context;
	}
}
