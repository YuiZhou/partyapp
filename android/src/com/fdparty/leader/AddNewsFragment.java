package com.fdparty.leader;

import java.util.ArrayList;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.example.partyapp.R;

@SuppressLint({ "NewApi", "ValidFragment" })
public class AddNewsFragment extends Fragment implements OnClickListener{
	private Activity activity;
	private String username;
	private ArrayList<Map<String,Object>> arrList;

	public AddNewsFragment(Activity activity, String username) {
		this.activity = activity;
		this.username = username;
		this.arrList = new ArrayList<Map<String,Object>>();
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater
				.inflate(R.layout.add_news_fragment, container, false);
		
		View button = layout.findViewById(R.id.news_frag_button);
		button.setOnClickListener(this);
		return layout;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/* when the add news button clicked, then switch to the add news activity */
		case R.id.news_frag_button:
			addNewsSwitch();
			break;
		}
		
	}
	private void addNewsSwitch() {
		Intent intent = new Intent();
		intent.setClass(activity, AddNews.class);
		intent.putExtra("username", username);
		
		startActivity(intent);
		
	}

}
