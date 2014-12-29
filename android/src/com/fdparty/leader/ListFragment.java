package com.fdparty.leader;

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
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint({ "ValidFragment", "NewApi" })
public class ListFragment extends Fragment {
	private Activity activity;
	private String username;
	private ArrayList<Map<String,Object>> arrList;

	public ListFragment(Activity activity, String username) {
		this.activity = activity;
		this.username = username;
		this.arrList = new ArrayList<Map<String,Object>>();
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater
				.inflate(R.layout.leader_list, container, false);
		
		loadData(layout);
		
		return layout;
	}
	
	private void loadData(View layout) {
		String url = HttpValue.Server.toString()+"?m=Leader&a=getUserList&usrid="+username;
		
		try{
			HttpResponseProcess process = new HttpResponseProcess(url);
			String res = process.toString();
			JSONArray jsonObjs = new JSONArray(res);
			for(int i = 0; i < jsonObjs.length(); i++){
				Map<String,Object> item = new HashMap<String,Object>();
				JSONObject jsonObj = (JSONObject)jsonObjs.opt(i);
				
				String usridString = (String) jsonObj.get("usrid");
				item.put("id", usridString);
				item.put("name", jsonObj.get("username") + " "+ usridString);
				
				//Log.d("Debug","title: "+ jsonObj.get("title")+"date: "+jsonObj.get("date"));
				arrList.add(item);
			}
			
			SimpleAdapter adapter = new SimpleAdapter(activity,  
                    arrList, 
                    R.layout.leader_list_title, 
                    new String[] {"name"},   
                    new int[] {R.id.leader_list_title_view});
			
			ListView list = (ListView)layout.findViewById(R.id.leader_list_ls);
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
		intent.setClass(activity, ShowUserInfo.class);
		intent.putExtra("username", id);
		intent.putExtra("owner", username);
		
		startActivity(intent);
//		this.finish();
	}
}
