package com.fdparty.news;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.partyapp.R;
import com.example.partyapp.R.drawable;
import com.fdparty.common.Downloader;
import com.fdparty.common.HttpResponseProcess;
import com.fdparty.common.HttpValue;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

@SuppressLint({ "NewApi", "ValidFragment" })
public class NewsFragment extends Fragment implements OnScrollListener,
		OnClickListener, OnItemClickListener {

	private static String newsIndex = "";
	private View expandedView = null;
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
		this.list.setOnItemClickListener(this);
		SimpleAdapter adapter = new SimpleAdapter(activity, arrList,
				R.layout.news_title, new String[] { "title", "date" },
				new int[] { R.id.newsTitleView, R.id.newsDateView });
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
		String url = HttpValue.Server.toString() + "?m=Index&a=index&usrid="
				+ username + "&start=" + startId;
		try {
			HttpResponseProcess process = new HttpResponseProcess(url);
			String res = process.toString();
			JSONArray jsonObjs = new JSONArray(res);
			if (isReload) {
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

			hasLoad = false;
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
		newsIndex = id;
		/*****************************
		 * prepare the gallery
		 *****************************/
		final LinearLayout gallery = (LinearLayout) view
				.findViewById(R.id.newsGallery);
		if (gallery.getChildCount() != 0) {
			return;
		}

		// TODO find the path of images
		String[] paths = {
				"http://www.baidu.com/img/bdlogo.png",
				"http://img3.douban.com/view/photo/photo/public/p2164446560.jpg",
				"http://img5.douban.com/view/photo/thumb/public/p2164446566.jpg",
				"http://img3.douban.com/view/photo/thumb/public/p2164446574.jpg" };

		for (final String path : paths) {
			new Thread() {
				public void run() {
					Downloader d = new Downloader();
					String filepath = d.download(path);
					gallery.addView(insertPhoto(filepath));
				};
			}.start();
		}
		/* show the text */

		TextView previewText = (TextView) view
				.findViewById(R.id.newsPreviewText);
		previewText.setText(preview(id));

	}

	View insertPhoto(String path) {
		File file = new File(path);
		Log.v("File", file.exists() + " " + path);
		Bitmap bm = decodeSampledBitmapFromUri(path, 90, 120);

		LinearLayout layout = new LinearLayout(activity.getApplicationContext());
		layout.setLayoutParams(new LayoutParams(250, 250));
		layout.setGravity(Gravity.CENTER);

		ImageView imageView = new ImageView(activity.getApplicationContext());
		imageView.setLayoutParams(new LayoutParams(220, 220));
		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		imageView.setImageBitmap(bm);

		layout.addView(imageView);
		return layout;
	}

	public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth,
			int reqHeight) {
		Bitmap bm = null;

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(path, options);

		return bm;
	}

	public int calculateInSampleSize(

	BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}

		return inSampleSize;
	}

	/**
	 * get a preview string with a target id of news
	 */
	private String preview(String id) {
		String url = HttpValue.Server + "?m=Index&a=getPreview&newsid=" + id;

		try {
			HttpResponseProcess process = new HttpResponseProcess(url);
			return process.toString();
			// JSONObject jsonObj = new JSONObject(res);
			// return jsonObj.getString("LEFT(content,20)");
		} catch (Exception e) {
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
			// list.setAdapter((SimpleAdapter) msg.obj);
			switch (msg.what) {
			case 1:
				SimpleAdapter adapter = (SimpleAdapter) list.getAdapter();
				adapter.notifyDataSetChanged();
			}
		}
	};

	/******************************************************
	 * The following function process the automatically load data when the
	 * scroll come to the bottom
	 ******************************************************/
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		/* get the last visible one */
		int lastItemId = list.getLastVisiblePosition();
		/* if it reaches bottom */
		if (!hasLoad && (lastItemId + 1) == totalItemCount) {
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.viewNews:
			Intent intent = new Intent();
			intent.setClass(this.activity, NewsDetailActivity.class);
			// intent.putExtra("username", username);
			intent.putExtra("id", newsIndex);
			startActivity(intent);
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int index, long id) {
		for (int i = 0; i < parent.getChildCount(); i++) {
			Log.v("child", index + " " + i + " "
					+ parent.getChildAt(i).toString());
		}
		/* hide the old expanded one and the new one visible */
		if (expandedView != null) {
			expandedView.findViewById(R.id.newsPreview)
					.setVisibility(View.GONE);
		}
		if (view != expandedView) {
			TextView tv = (TextView) view.findViewById(R.id.newsTitleView);
			view.findViewById(R.id.newsPreview).setVisibility(View.VISIBLE);
			expandedView = view;
			Map<String, Object> item = arrList.get(index);
			String newsId = (String) item.get("id");
			readNews(view, newsId);
			TextView viewNews = (TextView) view.findViewById(R.id.viewNews);
			viewNews.setOnClickListener(this);
		} else {
			expandedView = null;
		}
	}
}

class mySimpleAdapter extends SimpleAdapter {

	public mySimpleAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = super.getView(position, convertView, parent);
		return view;
	}

}
