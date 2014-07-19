package com.fdparty.partyapp;

import org.json.JSONObject;

import com.example.partyapp.R;
import com.fdparty.common.HttpResponseProcess;
import com.fdparty.common.HttpValue;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ShowUserInfo extends ActionBarActivity implements OnClickListener {
	private String username;
	
	private TextView nameView;
	private TextView idView;
	private TextView partyView;
	private TextView levelView;
	private TextView invokeView;
	private TextView submitView;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_fragment);
		
		Intent intent = getIntent();
		this.username = intent.getStringExtra("username");
		//Log.d("Debug",username);
		
		this.nameView = (TextView) findViewById(R.id.profile_name);
		this.idView = (TextView) findViewById(R.id.profile_stu_id);
		this.partyView = (TextView) findViewById(R.id.profile_party);
		this.levelView = (TextView) findViewById(R.id.profile_level);
		this.invokeView = (TextView) findViewById(R.id.profile_invoke_date);
		this.submitView = (TextView) findViewById(R.id.profile_submit_date);

		loadInfo();
		
	}
	
	private void loadInfo() {
		String url = HttpValue.Server.toString() + "User/getUserInfo/usrid/"
				+ username;

		try {
			HttpResponseProcess process = new HttpResponseProcess(url);
			String res = process.toString();
			JSONObject jsonObj = new JSONObject(res);

			/* set text */
			this.nameView.setText(getString(R.string.username)+jsonObj.getString("username"));
			this.idView.setText(getString(R.string.studentid)+jsonObj.getString("usrid"));
			this.partyView.setText(getString(R.string.party)+jsonObj.getString("party"));
			this.levelView.setText(getString(R.string.level)+jsonObj.getString("level"));

			/* what's the status? */
			String invoke_date = "";
			switch (jsonObj.getInt("status")) {
			case 0:
				return;
			case 1:
				invoke_date = getString(R.string.interst_date);
				break;
			case 2:
				invoke_date = getString(R.string.intern_date);
				break;
			case 3:
				invoke_date = getString(R.string.full_date);
				break;
			}

			this.invokeView.setText(invoke_date
					+ jsonObj.getString("invoke_date"));

			String nextSubmit = jsonObj.getString("submit_date");
			if (!nextSubmit.equals("false")) {
				this.submitView.setText(getString(R.string.next_submit_date)
						+ nextSubmit);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
