package com.fdparty.partyapp;

import java.util.ArrayList;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.partyapp.R;



@SuppressLint({ "ValidFragment", "NewApi" }) 
public class LeaderFragment extends Fragment implements OnClickListener{
	private Activity activity;
	private String username;
	
	/* views */
	TextView list_tag;
	TextView news_tag;
	TextView user_tag;
	
	public LeaderFragment(Activity activity, String username) {
		this.activity = activity;
		this.username = username;

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater
				.inflate(R.layout.leader_fragment, container, false);
		
		list_tag = (TextView)layout.findViewById(R.id.leader_user_list);
		news_tag = (TextView)layout.findViewById(R.id.leader_add_news);
		user_tag = (TextView)layout.findViewById(R.id.leader_add_user);
		
		list_tag.setOnClickListener(this);
		news_tag.setOnClickListener(this);
		user_tag.setOnClickListener(this);
		
		return layout;
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch(v.getId()){
		case R.id.leader_user_list:
			intent.setClass(this.activity, UserList.class);
			break;
		case R.id.leader_add_news:
			intent.setClass(this.activity, AddNews.class);
			break;
		case R.id.leader_add_user:
			intent.setClass(this.activity, AddUser.class);
			break;
		default:
			return;
		}
		intent.putExtra("username", username);
		
		startActivity(intent);
	}
}