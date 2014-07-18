package com.fdparty.common;

import android.util.Log;

public class User {
	private String username;
	
	public User(String username){
		this.username = username; 
	}

	public boolean isLeader() {
		String url = HttpValue.Server.toString()+"User/isLeader/usrid/"+username;
		try{
			HttpResponseProcess process = new HttpResponseProcess(url);
			String res = process.toString();
			
			Log.d("Debug",url);
			
			if(res.equals("true")){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

}
