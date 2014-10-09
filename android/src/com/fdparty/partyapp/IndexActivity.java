package com.fdparty.partyapp;

import com.example.partyapp.R;
import com.fdparty.common.FileValue;
import com.fdparty.common.User;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

public class IndexActivity extends Activity implements OnClickListener {
	
	/* fragments */
	private NewsFragment newsFragment;
	private ProfileFragment profileFragment;
	private ChatFragment chatFragment;
	private LeaderFragment leaderFragment;
	private Fragment nowFragment;
	
	private String username;
	
	private FragmentManager fragmentManager;
	
	@SuppressLint("NewApi") protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);
		
//		ImageView index_logo = (ImageView)findViewById(R.id.index_logo);
//		LayoutParams para= index_logo.getLayoutParams();
//		para.width = para.height;
//		index_logo.setLayoutParams(para);
		
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
		
		findViewById(R.id.news_tag).callOnClick();
//		setTag(0);
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
	@SuppressLint("ResourceAsColor") 
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
		v.setBackgroundResource(R.drawable.index_chosed);
		((TextView)v).setTextColor(this.getResources().getColor(R.color.choseTag));
	}
	
	@SuppressLint("NewApi") 
	private void setTag(int id){
		if(fragmentManager == null)
			fragmentManager = getFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction(); 
		
		if(nowFragment != null){
			resetTag();
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

	/**
	 * reset the tag of noeFragment to the init background, as it has not been chosed
	 */
	@SuppressLint("ResourceAsColor") 
	private void resetTag() {
		TextView view = null;
		if(nowFragment == newsFragment){
			view = (TextView) findViewById(R.id.news_tag);
		}else if(nowFragment == chatFragment){
			view = (TextView) findViewById(R.id.chat_tag);
		}else if(nowFragment == profileFragment){
			view = (TextView) findViewById(R.id.profile_tag);
		}else{
			view = (TextView) findViewById(R.id.leader_tag);
		}
		view.setTextColor(this.getResources().getColor(R.color.notChoseTag));
		view.setBackgroundResource(R.drawable.login_background);
		
	}
	
}
