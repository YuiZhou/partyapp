package com.fdparty.partyapp;

import org.json.JSONObject;

import com.example.partyapp.R;
import com.fdparty.common.HttpResponseProcess;
import com.fdparty.common.HttpValue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.widget.TextView;

public class NewsDetailActivity extends Activity {
	private String id;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_detail);
		
		Intent intent = getIntent();
		this.id = intent.getStringExtra("id");
		//Log.d("Debug",username);
		
		loadData();
		
		/* bind listener */
	}

	private void loadData() {
		String url = HttpValue.Server+"?m=Index&a=getNews&newsid="+id;
		
		try{
			HttpResponseProcess process = new HttpResponseProcess(url);
			String res = process.toString();
			JSONObject jsonObj = new JSONObject(res);
			
			/* set the view */
			TextView titleView = (TextView)findViewById(R.id.news_detail_title);
			titleView.setText(jsonObj.getString("title"));
			
			TextView dateView = (TextView)findViewById(R.id.news_detail_date);
			dateView.setText(jsonObj.getString("date"));
			
			processContent(jsonObj.getString("content"));
		}catch(Exception e){
				e.printStackTrace();
		}
		
	}

	
	/**
	 * The content may contains url of images, it should be converted.
	 */
	private void processContent(String content) {
		TextView titleView = (TextView)findViewById(R.id.news_detail_content);
		titleView.setText(content);
		
	}
	
	
	/**
	 *  finish this activity when the back button is clicked 
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 
        if (keyCode == KeyEvent.KEYCODE_BACK
                 && event.getRepeatCount() == 0) {
            this.finish();
             return true;
         }
         return super.onKeyDown(keyCode, event);
     }
}
