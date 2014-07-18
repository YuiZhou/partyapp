package com.fdparty.partyapp;

import com.example.partyapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class AddNews extends Activity {
	private String username;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);
		
		Intent intent = getIntent();
		this.username = intent.getStringExtra("username");
	}
}
