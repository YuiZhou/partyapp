package com.fdparty.partyapp;

import com.example.partyapp.R;
import com.fdparty.common.HttpResponseProcess;
import com.fdparty.common.HttpValue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddNews extends Activity {
	private String username;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_news);

		Intent intent = getIntent();
		this.username = intent.getStringExtra("username");
	}

	public void addNewsHandler(View v) {
		EditText titleField = (EditText) findViewById(R.id.add_news_title);
		EditText contentField = (EditText) findViewById(R.id.add_news_content);

		String title = titleField.getText().toString().trim();
		String content = contentField.getText().toString().trim();

		if (addNews(title, content)) {
			// Toast.makeText(this, "add successful",
			// Toast.LENGTH_SHORT).show();
			this.finish();
		} else {
			Toast.makeText(this, "add failed", Toast.LENGTH_SHORT).show();
		}

	}

	public boolean addNews(String title, String content) {
		String url = HttpValue.Server.toString() + "Leader/addNews/usrid/"
				+ username + "/title/" + title + "/content/" + content;

		try {
			HttpResponseProcess process = new HttpResponseProcess(url);
			String res = process.toString();

			if (res.equals("true")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
}
