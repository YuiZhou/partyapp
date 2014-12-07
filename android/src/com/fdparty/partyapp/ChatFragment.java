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
import android.os.Handler;
import android.os.Message;
import android.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

@SuppressLint({ "NewApi", "ValidFragment" })
public class ChatFragment extends Fragment implements OnScrollListener {

	private Activity activity;
	private String username;
	ArrayList<Map<String, Object>> arrList;
	private int startID = 0;

	/* controllers */
	private EditText commentView;
	private Button submitBt;
	private ListView list;
	private boolean hasLoad = false;

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
				final String comment = commentView.getText().toString().trim();
				if (comment.length() == 0) { /* check the value */
					Toast.makeText(activity, "输入文本", Toast.LENGTH_SHORT).show();
					return;
				} else if (comment.length() > 140) {
					Toast.makeText(activity, "请输入少于140个字", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				new Thread(){
					public void run() {
						Message msg = new Message();
						msg.what = 2;
						Bundle data = new Bundle();
						data.putString("comment", comment);
						msg.setData(data);
						handler.sendMessage(msg);
						//pushData(comment);
					};
				}.start();
				commentView.setText("");
			}
		});
		
		SimpleAdapter adapter = new SimpleAdapter(activity,
				arrList, R.layout.news_title, new String[] {
						"userid", "content" }, new int[] {
						R.id.newsTitleView, R.id.newsDateView });
		list.setAdapter(adapter);
		list.setOnScrollListener(this);

		loadData();

		return layout;
	}

	protected void pushData(String comment) {
		String url = HttpValue.Server.toString() + "?m=User&a=pushCmt&usrid="
				+ username + "&cmt=" + comment;
		String toast = "未知错误";
		try {
			HttpResponseProcess process = new HttpResponseProcess(url);
			loadData();
			toast = "您的留言\n已发送成功！";
		} catch (Exception e) {
			toast = "网络异常\n请稍后再试！";
		}finally{
			Toast toastFiled = Toast.makeText(activity,
				     toast, Toast.LENGTH_LONG);
			toastFiled.setGravity(Gravity.CENTER, 0, 0);
//			toastFiled.setView(view);
			toastFiled.show();
		}
	}

	private void loadData() {
		new Thread() {
			public void run() {
				startID = 0;
				hasLoad = true;
				loadMoreData(true);
			}

		}.start();
	}
	
	/**
	 * After adding a new chat message, the chat part should be reloaded.
	 */
	protected void loadMoreData(boolean isReload) {
		String url = HttpValue.Server.toString() + "?m=User&a=getCmt&start="+startID;
		try {
			HttpResponseProcess process = new HttpResponseProcess(url);
			String res = process.toString();
			JSONArray jsonObjs = new JSONArray(res);
			if(isReload){
				arrList.clear();
			}
			for (int i = 0; i < jsonObjs.length(); i++) {
				Map<String, Object> item = new HashMap<String, Object>();
				JSONObject jsonObj = (JSONObject) jsonObjs.opt(i);

				item.put("userid", jsonObj.get("userid") + " "
						+ jsonObj.get("username"));
				item.put("content", jsonObj.get("content"));

				// Log.d("Debug","title: "+
				// jsonObj.get("title")+"date: "+jsonObj.get("date"));
				arrList.add(item);
			}
			
			Message msg = new Message();
			msg.what = 1;
			handler.sendMessage(msg);
			hasLoad = false;

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case 1:	// add news to the chat fragment
				SimpleAdapter adapter = (SimpleAdapter) list.getAdapter();
				adapter.notifyDataSetChanged();
				break;
			case 2:
				Bundle data = msg.getData();
				pushData(data.getString("comment"));
			}
		}
	};
	
	/******************************************************
	 * The following function process the automatically 
	 * load data when the scroll come to the bottom
	 ******************************************************/
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
		int visibleItemCount, int totalItemCount) {
		int lastItemId = list.getLastVisiblePosition();	/* get the last visible one */
		if (!hasLoad && (lastItemId + 1) == totalItemCount) {	/* if it reaches bottom */
			startID = totalItemCount;
			hasLoad = true;
			new Thread() {
				public void run() {
					loadMoreData(false);
				}
			}.start();
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
	}
}
