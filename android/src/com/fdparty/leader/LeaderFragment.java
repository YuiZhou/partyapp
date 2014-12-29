package com.fdparty.leader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.partyapp.R;
import com.fdparty.news.NewsFragment;

/**
 * This class implements the management for a leader.
 * Only in the leader's privilege, the user can see this page.
 */

@SuppressLint({ "ValidFragment", "NewApi" }) 
public class LeaderFragment extends Fragment implements OnClickListener{
	/*
	 * I reuse the news fragment's layout file.
	 * For there are three chooses for the leader, so I maintain a array with length 3.
	 * 
	 * This class also implements the interface of itemClickListener.
	 */
	private Activity activity;
	private String username;
	
	private View leaderList, leaderNews, leaderUser,
	listFrag, newsFrag, userFrag;
	
	private Fragment list, news, user;
	
	public LeaderFragment(Activity activity, String username) {
		this.activity = activity;
		this.username = username;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater
				.inflate(R.layout.leader_fragment, container, false);
		
		leaderList = layout.findViewById(R.id.leader_list);
		leaderList.setOnClickListener(this);
		leaderNews = layout.findViewById(R.id.leader_news);
		leaderNews.setOnClickListener(this);
		leaderUser = layout.findViewById(R.id.leader_user);
		leaderUser.setOnClickListener(this);
		
		listFrag = layout.findViewById(R.id.leader_list_frag);
		newsFrag = layout.findViewById(R.id.leader_news_frag);
		userFrag = layout.findViewById(R.id.leader_user_frag);
		
		return layout;
	}

	@Override
	public void onClick(View v) {
		LinearLayout.LayoutParams paramsBase = new LinearLayout.LayoutParams
				(LayoutParams.MATCH_PARENT, 0);
		paramsBase.weight = 1.0f;
		
		leaderList.setLayoutParams(paramsBase);
		leaderNews.setLayoutParams(paramsBase);
		leaderUser.setLayoutParams(paramsBase);
		View hint  = null;
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
				(LayoutParams.MATCH_PARENT, 0);
		params.weight = 5.0f;
		
		// prepare the fragment manager
		FragmentManager fgm = activity.getFragmentManager();
		FragmentTransaction transaction = fgm.beginTransaction();
		
		switch (v.getId()) {
		case R.id.leader_list:
			leaderList.setLayoutParams(params);
			hint = activity.findViewById(R.id.leader_list_hint);
			listFrag.setVisibility(View.VISIBLE);
			if(list == null){
				list = new ListFragment(activity, username);
				transaction.add(R.id.leader_list_frag, list);
			}
			transaction.show(list);
			break;
		case R.id.leader_news:
			leaderNews.setLayoutParams(params);
			hint = activity.findViewById(R.id.leader_news_hint);
			newsFrag.setVisibility(View.VISIBLE);
			if(news == null){
				news = new AddNewsFragment(activity, username);
				transaction.add(R.id.leader_news_frag, news);
			}
			transaction.show(news);
			break;
		case R.id.leader_user:
			leaderUser.setLayoutParams(params);
			hint = activity.findViewById(R.id.leader_user_hint);
		}
		
		transaction.commit();
		hint.setVisibility(View.GONE);
	}

}