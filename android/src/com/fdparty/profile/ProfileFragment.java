package com.fdparty.profile;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONObject;

import com.example.partyapp.R;
import com.fdparty.common.FileValue;
import com.fdparty.common.HttpResponseProcess;
import com.fdparty.common.HttpValue;
import com.fdparty.common.Level;
import com.fdparty.common.User;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

@SuppressLint({ "NewApi", "ValidFragment" })
public class ProfileFragment extends Fragment implements OnClickListener {

	private TextView nameView;
	private TextView idView;
	private TextView partyView;
	private TextView levelView;
	private TextView invokeView;
	private TextView submitView;

	private Activity activity;
	private String username;

	public ProfileFragment(Activity activity, String username) {
		this.activity = activity;
		this.username = username;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.profile_fragment, container,
				false);

		/* init the view controllers */
		this.nameView = (TextView) layout.findViewById(R.id.profile_name);
		this.idView = (TextView) layout.findViewById(R.id.profile_stu_id);
		this.partyView = (TextView) layout.findViewById(R.id.profile_party);
		this.levelView = (TextView) layout.findViewById(R.id.profile_level);
		this.invokeView = (TextView) layout
				.findViewById(R.id.profile_invoke_date);
		this.submitView = (TextView) layout
				.findViewById(R.id.profile_submit_date);
		
		(layout.findViewById(R.id.logout)).setOnClickListener(this);;

		loadInfo();
		return layout;
	}

	private void loadInfo() {
		User user = User.getUser();

		// String url = HttpValue.Server.toString() + "User/getUserInfo/usrid/"
		// + username;

		try {
			// HttpResponseProcess process = new HttpResponseProcess(url);
			// String res = process.toString();
			// JSONObject jsonObj = new JSONObject(res);

			/* set text */
			this.nameView.setText(getString(R.string.username)
					+ user.getUsername());
			this.idView.setText(getString(R.string.studentid) + this.username);
			this.partyView.setText(getString(R.string.party) + user.getParty());
			this.levelView.setText(getString(R.string.level) + user.getLevel());

			/* what's the status? */
			// String invoke_date = "";
			this.invokeView.setText("成为" + Level.getLevel(user.getStatus())
					+ "日期：" + user.getInvokeDate());

			this.submitView.setText(getString(R.string.next_submit_date)
					+ user.getNextSubmit());
			switch (user.getStatus()) {
			case 0:
				this.invokeView.setVisibility(View.GONE);
			case 3:
				this.submitView.setVisibility(View.GONE);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.logout:
			/* logout the app */
			SharedPreferences sp = activity.getSharedPreferences(FileValue.loginInfo.toString(), Context.MODE_PRIVATE);
			Editor ed = sp.edit();
			
			ed.clear();
			ed.commit();
			
			sp = activity.getSharedPreferences(FileValue.userInfo.toString(), Context.MODE_PRIVATE);
			ed = sp.edit();
			ed.clear();
			ed.commit();
			
			System.exit(0);
			break;
		}
		
	}
}
