package com.fdparty.partyapp;

import com.example.partyapp.R;
import com.fdparty.common.FileValue;
import com.fdparty.common.User;

import android.annotation.SuppressLint;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class IndexActivity extends ActionBarActivity implements OnClickListener {
	
	/* fragments */
	private NewsFragment newsFragment;
	private ProfileFragment profileFragment;
	private ChatFragment chatFragment;
	private LeaderFragment leaderFragment;
	private Fragment nowFragment;
	
	private String username;
	
	private FragmentManager fragmentManager;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);
		
		Intent intent = getIntent();
		this.username = intent.getStringExtra("username");
		//Log.d("Debug",username);
		
		if(username.equals("0")){
			/* invisible some views */
			findViewById(R.id.news_tag).setVisibility(View.GONE);
			findViewById(R.id.profile_tag).setVisibility(View.GONE);
			findViewById(R.id.chat_tag).setVisibility(View.GONE);
			findViewById(R.id.leader_tag).setVisibility(View.GONE);
		}else if(!(User.getUser().isLeader())){
			findViewById(R.id.leader_tag).setVisibility(View.GONE);
		}
		
//		Log.d("Debug",(new User(username)).isLeader()+"########"+username);
		
		/* bind listener */
		findViewById(R.id.news_tag).setOnClickListener(this);
		findViewById(R.id.chat_tag).setOnClickListener(this);
		findViewById(R.id.profile_tag).setOnClickListener(this);
		findViewById(R.id.leader_tag).setOnClickListener(this);
		
		setTag(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.index, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.logout) {
			/* logout the app */
			SharedPreferences sp = getSharedPreferences(FileValue.loginInfo.toString(), Context.MODE_PRIVATE);
			Editor ed = sp.edit();
			
			ed.clear();
			ed.commit();
			
			sp = getSharedPreferences(FileValue.userInfo.toString(), Context.MODE_PRIVATE);
			ed = sp.edit();
			ed.clear();
			ed.commit();
			
			System.exit(0);
			return true;
		}else if(id == R.id.quit){
			System.exit(0);
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	/**
	 * 1 - news
	 * 2 - chatRoom
	 * 3 - Profile 
	 */
	public void onClick(View v){
		switch(v.getId()){
		case R.id.news_tag:
			setTag(0);
			break;
		case R.id.chat_tag:
			setTag(1);
			break;
		case R.id.profile_tag:
			setTag(2);
			break;
		case R.id.leader_tag:
			setTag(3);
			break;
		}
	}
	
	@SuppressLint("NewApi") 
	private void setTag(int id){
		if(fragmentManager == null)
			fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction(); 
		
		if(nowFragment != null){
			transaction.hide(nowFragment);
		}
		
		switch(id){
		case 0:
			if(newsFragment == null){
				newsFragment = new NewsFragment(this, username);
				transaction.add(R.id.container, newsFragment);
			}
			nowFragment = newsFragment;
			break;
		case 1:
			if(chatFragment == null){
				chatFragment = new ChatFragment(this,username);
				transaction.add(R.id.container, chatFragment);
			}
			nowFragment = chatFragment;
			break;
		case 2:
			if(profileFragment == null){
				profileFragment = new ProfileFragment(this,username);
				transaction.add(R.id.container, profileFragment);
			}
			nowFragment = profileFragment;
			break;
		case 3:
			if(leaderFragment == null){
				leaderFragment = new LeaderFragment(this, username);
				transaction.add(R.id.container,  leaderFragment);
			}
			
			nowFragment = leaderFragment;
			break;
		}
		
		transaction.show(nowFragment);
		transaction.commit();
		
	}
	
}