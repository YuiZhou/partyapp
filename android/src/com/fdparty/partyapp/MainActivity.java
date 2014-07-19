package com.fdparty.partyapp;

import com.example.partyapp.R;
import com.fdparty.common.FileValue;
import com.fdparty.common.HttpValue;
import com.fdparty.common.Login;
import com.fdparty.common.User;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/* enable the network */
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork() // or
																		// .detectAll()
																		// for
																		// all
																		// detectable
																		// problems
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
				.penaltyLog().penaltyDeath().build());

		SharedPreferences sp = getSharedPreferences(
				FileValue.loginInfo.toString(), Context.MODE_PRIVATE);
		String usrid = sp.getString("usr", "");
		String password = sp.getString("pwd", "");

		// Log.d("Debug", "###"+HttpValue.Server.toString() +
		// "User/login/usr/"+usrid+"/pwd/"+password);

		Login lg = new Login(usrid, password);
		if (lg.login()) {
			User.getUser(
					getSharedPreferences(FileValue.userInfo.toString(),
							Context.MODE_PRIVATE), usrid);
			goToIndex(usrid);
			return;
		}
		// Button loginBt = (Button)findViewById(R.id.login);
		// loginBt.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// }
		// });
		// if (savedInstanceState == null) {
		// getSupportFragmentManager().beginTransaction()
		// .add(R.id.container, new PlaceholderFragment()).commit();
		// }
	}

	public void anonymousLoginHandler(View v) {
		goToIndex("0");
	}

	public void loginHandler(View v) {
		/* get the username */
		EditText usridInput = (EditText) findViewById(R.id.username);
		String usrid = usridInput.getText().toString().trim();

		/* get the password */
		EditText passwordInput = (EditText) findViewById(R.id.password);
		String password = passwordInput.getText().toString().trim();

		Login lg = new Login(usrid, password);
		if (!lg.login()) {
			// TODO something will be showed when login failed.
			Toast.makeText(this, "login failed", Toast.LENGTH_SHORT).show();
		} else {
			// TODO save info and go to the index page
			SharedPreferences sp = getSharedPreferences(
					FileValue.loginInfo.toString(), Context.MODE_PRIVATE);
			Editor ed = sp.edit();
			ed.putString("usr", usrid);
			ed.putString("pwd", password);
			ed.commit();

			SharedPreferences info = getSharedPreferences(
					FileValue.userInfo.toString(), Context.MODE_PRIVATE);
			User.getUser(usrid, info);
			/* switch */
			goToIndex(usrid);
		}

	}

	private void goToIndex(String usrid) {
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, IndexActivity.class);
		intent.putExtra("username", usrid);

		startActivity(intent);
		this.finish();

	}

	/**
	 * A placeholder fragment containing a simple view.
	 */

}
