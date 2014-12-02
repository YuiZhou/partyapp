package com.fdparty.partyapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.partyapp.R;

/**
 * This class implements the management for a leader.
 * Only in the leader's privilege, the user can see this page.
 */

@SuppressLint({ "ValidFragment", "NewApi" }) 
public class LeaderFragment extends Fragment implements OnItemClickListener{
	/*
	 * I reuse the news fragment's layout file.
	 * For there are three chooses for the leader, so I maintain a array with length 3.
	 * 
	 * This class also implements the interface of itemClickListener.
	 */
	private Activity activity;
	private String username;
	private ListView list;
	
	public LeaderFragment(Activity activity, String username) {
		this.activity = activity;
		this.username = username;

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater
				.inflate(R.layout.leader_fragment, container, false);
		
		list = (ListView)layout.findViewById(R.id.list);
		ArrayList<Map<String, Object>> arrList = new ArrayList<Map<String,Object>>();
		String[][] titles = {
				{"党支部名单","查看你管理的党支部成员名单"},
				{"发布新闻","你所在的党支部的成员将收到你的通知"},
				{"添加新成员","添加积极向党组织靠拢的成员"}
		};
		
		for(int i = 0; i < titles.length; i++){
			Map<String,Object> item = new HashMap<String,Object>();
			//JSONObject jsonObj = (JSONObject)jsonObjs.opt(i);
			item.put("title", titles[i][0]);
			item.put("hint", titles[i][1]);
//			item.put("title", jsonObj.get("title"));
//			item.put("date", jsonObj.get("date"));
//			item.put("id", jsonObj.get("id"));
//			
//			//Log.d("Debug","title: "+ jsonObj.get("title")+"date: "+jsonObj.get("date"));
			arrList.add(item);
		}
		
		SimpleAdapter adapter = new SimpleAdapter(this.activity,  
                arrList, 
                R.layout.leader_title, 
                new String[] {"title", "hint"},   
                new int[] {R.id.newsTitleView,R.id.newsDateView});
		this.list.setAdapter(adapter); 
		this.list.setOnItemClickListener(this);
		
//		list_tag = (TextView)layout.findViewById(R.id.leader_user_list);
//		news_tag = (TextView)layout.findViewById(R.id.leader_add_news);
//		user_tag = (TextView)layout.findViewById(R.id.leader_add_user);
//		
//		list_tag.setOnClickListener(this);
//		news_tag.setOnClickListener(this);
//		user_tag.setOnClickListener(this);
		
		return layout;
	}

//	public void onClick(View v) {
//		Intent intent = new Intent();
//		switch(v.getId()){
//		case R.id.leader_user_list:
//			intent.setClass(this.activity, UserList.class);
//			break;
//		case R.id.leader_add_news:
//			intent.setClass(this.activity, AddNews.class);
//			break;
//		case R.id.leader_add_user:
//			intent.setClass(this.activity, AddUser.class);
//			break;
//		default:
//			return;
//		}
//		intent.putExtra("username", username);
//		
//		startActivity(intent);
//	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
		Intent intent = new Intent();
		switch(index){
		case 0:
			intent.setClass(this.activity, UserList.class);
			break;
		case 1:
			intent.setClass(this.activity, AddNews.class);
			break;
		case 2:
			intent.setClass(this.activity, AddUser.class);
			break;
		default:
			return;
		}
		intent.putExtra("username", username);
		
		startActivity(intent);
		
	}
}