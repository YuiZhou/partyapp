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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

@SuppressLint({ "NewApi", "ValidFragment" })
public class NewsFragment extends Fragment implements OnScrollListener {

	private int expandedViewID = -1;
	private int startId = 0;
	private String username;

	private Activity activity;
	private ListView list;
	private ArrayList<Map<String, Object>> arrList;
	private boolean hasLoad = false;

	public NewsFragment(Activity activity, String username) {
		this.activity = activity;
		this.arrList = new ArrayList<Map<String, Object>>();
		this.username = username;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View layout = inflater
				.inflate(R.layout.news_fragment, container, false);

		list = (ListView) layout.findViewById(R.id.list);

		if (list == null)
			Log.e("Error", "list is null");

		loadData();
		/* set the OnItemClickListener */
		this.list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int index,
					long id) {
				/* hide the old expanded one and the new one visible */
				if(expandedViewID >= 0){
					parent.getChildAt(expandedViewID).findViewById(R.id.newsPreview)
						.setVisibility(View.GONE);
				}
				if(expandedViewID != index){
					view.findViewById(R.id.newsPreview).setVisibility(View.VISIBLE);
					expandedViewID = index;
				}else{
					expandedViewID = -1;
				}
				Map<String, Object> item = arrList.get(index);
				String newsId = (String) item.get("id");
				readNews(view, newsId);
			}
		});
		SimpleAdapter adapter = new SimpleAdapter(activity,
				arrList, R.layout.news_title, new String[] {
						"title", "date" }, new int[] {
						R.id.newsTitleView, R.id.newsDateView });
		this.list.setAdapter(adapter);
		this.list.setOnScrollListener(this);
		return layout;
	}

	public void onResume() {
		super.onResume();
		loadData();
	}

	private void loadData() {
		
		new Thread() {
			public void run() {
				startId = 0;
				loadMoreData(true);
			}
		}.start();
	}

	protected void loadMoreData(boolean isReload) {
		String url = HttpValue.Server.toString()
				+ "?m=Index&a=index&usrid=" + username + "&start="
				+ startId;
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

				item.put("title", jsonObj.get("title"));
				item.put("date", jsonObj.get("date"));
				item.put("id", jsonObj.get("id"));

				// Log.d("Debug","title: "+
				// jsonObj.get("title")+"date: "+jsonObj.get("date"));
				arrList.add(item);
			}
			
			Message msg = new Message();
			msg.what = 1;
			handler.sendMessage(msg);
			
			hasLoad  = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Here the view is a news item to preview a piece of news.
	 * 
	 * @view display panel
	 * @id news id
	 */
	protected void readNews(View view, String id) {
//		Intent intent = new Intent();
//		intent.setClass(this.activity, NewsDetailActivity.class);
//		// intent.putExtra("username", username);
//		intent.putExtra("id", id);
//		startActivity(intent);
		
		/* prepare the gallery */
		/* show the text */
		TextView previewText = (TextView) view.findViewById(R.id.newsPreviewText);
		previewText.setText(preview(id));

	}
	
	/**
	 * get a preview string with a target id of news
	 */
	private String preview(String id) {
		String url = HttpValue.Server+"?m=Index&a=getPreview&newsid="+id;
		
		try{
			HttpResponseProcess process = new HttpResponseProcess(url);
			return process.toString();
//			JSONObject jsonObj = new JSONObject(res);
//			return jsonObj.getString("LEFT(content,20)");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "";
		
	}

	// @Override
	// public void onStart(){
	// super.onStart();
	// ListView list = (ListView) getActivity().findViewById(R.id.news_list);
	// Log.d("Debug","jhkj");
	// }

	// this.list.setAdapter(adapter);
	//
	//

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//list.setAdapter((SimpleAdapter) msg.obj);
			switch(msg.what){
			case 1:
				SimpleAdapter adapter = (SimpleAdapter) list.getAdapter();
				adapter.notifyDataSetChanged();
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
			hasLoad = true;
			startId = totalItemCount;
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
