package com.fdparty.common;

public class Login {
	String username = "", password = "";
	public Login(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	public boolean login(){
		/* check the parameters */
		if(username == "" || password == "")
			return false;
		
		/* format the url */
//		username = "11302010067";
//		password = "11302010067";
		
		String url = HttpValue.Server.toString() + "?m=User&a=login&usr="+username+"&pwd="+password;
		
		try{
			
			HttpResponseProcess process = new HttpResponseProcess(url);
			String res = process.toString();
			if(res.equals("true")){
				return true;
			}else{
				return false;
			}
				
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
