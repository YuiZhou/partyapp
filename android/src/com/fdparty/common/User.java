package com.fdparty.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class User {
	private String usrid;
	private String username;
	private String party;
	private String level;
	private String invokeDate;
	private String nextSubmit;
	private int status;
	private boolean isLeader = false;
	
	private static SharedPreferences info;
	
	private static User user;

	private User(String usrid, SharedPreferences info) {
		this.usrid = usrid;
		this.info = info;

		loadInfo();
	}
	
	private User(SharedPreferences info, String usrid){
		this.info = info;
		
		loadLocalInfo(usrid);
	}

	public static User getUser(String usrid, SharedPreferences info){
		if(user == null)
			user = new User(usrid, info);
		return user;
	}
	
	public static User getUser(SharedPreferences infop, String usrid){
		if(user == null){
			user = new User(infop, usrid);
		}
		
		return user;
	}
	
	public static User getUser(){
		return user;
	}

	private void loadInfo() {
		String url = HttpValue.Server.toString() + "User/getUserInfo/usrid/"
				+ usrid;

		try {
			HttpResponseProcess process = new HttpResponseProcess(url);
			String res = process.toString();
			JSONObject jsonObj = new JSONObject(res);

			/* set text */
			this.username = jsonObj.getString("username");
			// this.idView.setText(getString(R.string.studentid)+jsonObj.getString("usrid"));
			this.party = jsonObj.getString("party");
			this.level = jsonObj.getString("level");

			/* what's the status? */
			this.status = jsonObj.getInt("status");

			this.invokeDate = jsonObj.getString("invoke_date");

			this.nextSubmit = jsonObj.getString("submit_date");
			loadIsLeader();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		save();
	}
	
	private void loadLocalInfo(String usrid) {
		try{
			this.usrid = info.getString("usrid", "");
			this.username = info.getString("username", "");
			this.party = info.getString("party", "");
			this.level = info.getString("level", "");
			this.isLeader = info.getBoolean("isLeader", false);
			this.invokeDate = info.getString("invokeDate", "");
			this.nextSubmit = info.getString("nextSubmit", "");
			checkDate();
			this.status = info.getInt("status", 0);
		}catch(Exception e){
			user = new User(usrid, info);
		}
		
	}

	
	/**
	 * If the submit date is expired, load all the information again
	 */
	@SuppressLint("SimpleDateFormat") 
	private void checkDate() {
		
		if(this.nextSubmit.equals("false"))
			return;
		/*
		 * If expired 
		 */
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date submit = df.parse(this.nextSubmit);
			Date now = new Date();
			if(now.getTime() > submit.getTime())
				user = new User(usrid, info);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private void save() {
		Editor ed = info.edit();
		ed.putString("usrid", usrid);
		ed.putString("username", username);
		ed.putString("level", level);
		ed.putInt("status", status);
		ed.putString("party", party);
		ed.putString("invokeDate", invokeDate);
		ed.putString("nextSubmit", nextSubmit);
		ed.putBoolean("isLeader", isLeader);
		ed.commit();
		
	}

	public void loadIsLeader() {
		String url = HttpValue.Server.toString() + "User/isLeader/usrid/"
				+ usrid;
		try {
			HttpResponseProcess process = new HttpResponseProcess(url);
			String res = process.toString();


			if (res.equals("true")) {
				this.isLeader = true;
			} else {
				this.isLeader = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isLeader(){
		return this.isLeader;
	}
	
	public String getUsername(){
		return this.username;
	}
	public String getParty(){
		return this.party;
	}
	public String getLevel(){
		return this.level;
	}
	public String getInvokeDate(){
		return this.invokeDate;
	}
	public int getStatus(){
		return this.status;
	}
	public String getNextSubmit(){
		checkDate();
		return this.nextSubmit;
	}

}
