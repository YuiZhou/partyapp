package com.fdparty.partyapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.partyapp.R;
import com.fdparty.common.HttpResponseProcess;
import com.fdparty.common.HttpValue;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;


@SuppressLint({ "NewApi", "ValidFragment" }) 
public class NewsFragment extends Fragment {
	
	private int startId = 0;
	private String username;
	
	private Activity activity;
	private ListView list;
	private ArrayList<Map<String,Object>> arrList;
	
	public NewsFragment(Activity activity, String username){
		this.activity = activity;
		this.arrList = new ArrayList<Map<String,Object>>();
		this.username = username;
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View layout = inflater.inflate(R.layout.list_fragment, container, false);
		
		list = (ListView)layout.findViewById(R.id.list);
		
		if(list == null)
			Log.e("Error", "list is null");
		
		loadData();
		return layout;
	}

	private void loadData() {
		String url = HttpValue.Server.toString()+"Index/index/usrid/"+username+"/start/"+startId;
		
		try{
			HttpResponseProcess process = new HttpResponseProcess(url);
			String res = process.toString();
			JSONArray jsonObjs = new JSONArray(res);
			for(int i = 0; i < jsonObjs.length(); i++){
				Map<String,Object> item = new HashMap<String,Object>();
				JSONObject jsonObj = (JSONObject)jsonObjs.opt(i);
				
				item.put("title", jsonObj.get("title"));
				item.put("date", jsonObj.get("date"));
				item.put("id", jsonObj.get("id"));
				
				//Log.d("Debug","title: "+ jsonObj.get("title")+"date: "+jsonObj.get("date"));
				arrList.add(item);
			}
			
			SimpleAdapter adapter = new SimpleAdapter(this.activity,  
                    arrList, 
                    R.layout.news_title, 
                    new String[] {"title", "date"},   
                    new int[] {R.id.newsTitleView,R.id.newsDateView});
			this.list.setAdapter(adapter); 
			
			
			/* set the OnItemClickListener */
			this.list.setOnItemClickListener(new OnItemClickListener() {  
				  
	            @Override  
	            public void onItemClick(AdapterView<?> arg0, View arg1, int index,  
	                    long arg3) {  
	            	Map<String,Object> item = arrList.get(index);
	            	String id = (String) item.get("id");
	            	readNews(id);
	            }  
	        });  
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	protected void readNews(String id) {
		Intent intent = new Intent();
		intent.setClass(this.activity, NewsDetailActivity.class);
		//intent.putExtra("username", username);
		intent.putExtra("id", id);
		
		startActivity(intent);
		
	}
	
//	@Override
//	public void onStart(){
//		super.onStart();
//		ListView list = (ListView) getActivity().findViewById(R.id.news_list);
//		Log.d("Debug","jhkj");
//	}
	

}
