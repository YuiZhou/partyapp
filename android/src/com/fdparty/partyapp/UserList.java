package com.fdparty.partyapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.partyapp.R;
import com.fdparty.common.HttpResponseProcess;
import com.fdparty.common.HttpValue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class UserList extends Activity {
	private String username;
	private ArrayList<Map<String,Object>> arrList;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_fragment);
		
		this.arrList = new ArrayList<Map<String,Object>>();
		
		Intent intent = getIntent();
		this.username = intent.getStringExtra("username");
		
		loadData();
	}
	
	private void loadData() {
		String url = HttpValue.Server.toString()+"Leader/getUserList/usrid/"+username;
		
		try{
			HttpResponseProcess process = new HttpResponseProcess(url);
			String res = process.toString();
			JSONArray jsonObjs = new JSONArray(res);
			for(int i = 0; i < jsonObjs.length(); i++){
				Map<String,Object> item = new HashMap<String,Object>();
				JSONObject jsonObj = (JSONObject)jsonObjs.opt(i);
				
				item.put("id", jsonObj.get("usrid"));
				item.put("name", jsonObj.get("username"));
				
				//Log.d("Debug","title: "+ jsonObj.get("title")+"date: "+jsonObj.get("date"));
				arrList.add(item);
			}
			
			SimpleAdapter adapter = new SimpleAdapter(this,  
                    arrList, 
                    R.layout.user_list, 
                    new String[] {"id", "name"},   
                    new int[] {R.id.list_stu_id,R.id.list_stu_name});
			
			ListView list = (ListView)findViewById(R.id.list);
			list.setAdapter(adapter); 
			
			/* set the OnItemClickListener */
			list.setOnItemClickListener(new OnItemClickListener() {  
				  
	            @Override  
	            public void onItemClick(AdapterView<?> arg0, View arg1, int index,  
	                    long arg3) {  
	            	Map<String,Object> item = arrList.get(index);
	            	String id = (String) item.get("id");
	            	loadUser(id);
	            }
	        });  
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * go to the user information activity 
	 */
	protected void loadUser(String id) {
		Intent intent = new Intent();
		intent.setClass(UserList.this, ShowUserInfo.class);
		intent.putExtra("username", id);
		
		startActivity(intent);
		//this.finish();
	}
}
