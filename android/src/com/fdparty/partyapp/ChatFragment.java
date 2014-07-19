package com.fdparty.partyapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.partyapp.R;
import com.fdparty.common.HttpResponseProcess;
import com.fdparty.common.HttpValue;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

@SuppressLint({ "NewApi", "ValidFragment" })
public class ChatFragment extends Fragment {

	private Activity activity;
	private String username;
	ArrayList<Map<String, Object>> arrList;

	/* controllers */
	private EditText commentView;
	private Button submitBt;
	private ListView list;

	public ChatFragment(Activity activity, String username) {
		this.activity = activity;
		this.arrList = new ArrayList<Map<String, Object>>();
		this.username = username;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater
				.inflate(R.layout.chat_fragment, container, false);

		commentView = (EditText) layout.findViewById(R.id.comment_input);
		submitBt = (Button) layout.findViewById(R.id.submit_comment);
		list = (ListView) layout.findViewById(R.id.comment_list);

		submitBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String comment = commentView.getText().toString().trim();
				if (comment.length() == 0) { /* check the value */
					Toast.makeText(activity, "�����ı�", Toast.LENGTH_SHORT).show();
					return;
				} else if (comment.length() > 140) {
					Toast.makeText(activity, "����������140����", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				pushData(comment);
				commentView.setText("");
			}
		});

		loadData();
		
		return layout;
	}

	protected void pushData(String comment) {
		String url = HttpValue.Server.toString() + "User/pushCmt/usrid/"
				+ username + "/cmt/" + comment;

		try {
			HttpResponseProcess process = new HttpResponseProcess(url);
			loadData();
		} catch (Exception e) {
			Toast.makeText(activity, "�����쳣���Ժ�����", Toast.LENGTH_SHORT).show();
		}
	}

	private void loadData() {
		arrList.clear();

		String url = HttpValue.Server.toString() + "User/getCmt";

		try {
			HttpResponseProcess process = new HttpResponseProcess(url);
			String res = process.toString();
			JSONArray jsonObjs = new JSONArray(res);
			for (int i = 0; i < jsonObjs.length(); i++) {
				Map<String, Object> item = new HashMap<String, Object>();
				JSONObject jsonObj = (JSONObject) jsonObjs.opt(i);

				item.put("userid", jsonObj.get("userid"));
				item.put("username", jsonObj.get("username"));
				item.put("content", jsonObj.get("content"));

				// Log.d("Debug","title: "+
				// jsonObj.get("title")+"date: "+jsonObj.get("date"));
				arrList.add(item);
			}

			SimpleAdapter adapter = new SimpleAdapter(this.activity, arrList,
					R.layout.comment, new String[] { "userid", "username" ,"content"},
					new int[] { R.id.comment_userid, R.id.comment_username, R.id.comment_content });
			this.list.setAdapter(adapter);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}