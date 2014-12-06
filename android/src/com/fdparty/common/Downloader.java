package com.fdparty.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Environment;
import android.util.Log;

public class Downloader {
	private static String Path = "/PartyApp/";
	private static String sd = Environment.getExternalStorageDirectory().getAbsolutePath(); 

	static{
		File file = new File(sd + Path);
		file.mkdir();
		Path += "img/";
		file = new File(sd + Path);
		file.mkdir();
	}
	public Downloader() {
	}

	public void setDir(String path) {
		this.Path = path;
		System.out.println(path);
		File dir = new File(path);
		if(!dir.exists() || !dir.isDirectory())
			dir.mkdir();
	}

	public String getDir() {
		return this.Path;
	}

	/**
	 * download the urlstr source
	 * 
	 *  @param urlstr
	 *  url for the source on website
	 *  @return
	 *  the path of download file on your computers
	 */
	public String download(String urlstr) {
		/* set the filename of new download file on local */
		String filename = sd + Path + getFilename(urlstr);
		File file = new File(filename);
		if(file.exists()){
			return filename;
		}
		//System.out.println("Save file to " + filename);
		/* download */
		byte[] buf = new byte[2048];
		try {
			URL url = new URL(urlstr);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.connect();

			BufferedInputStream bis = new BufferedInputStream(
					http.getInputStream());
			FileOutputStream fos = new FileOutputStream(filename);
			int size;
			while((size = bis.read(buf)) != -1 )
				fos.write(buf, 0, size);
			bis.close();
			fos.close();
			return filename;
		} catch (MalformedURLException e) {
			Log.v("error","can not set the url: " + urlstr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		/* should not reach here */
		return "";
	}

	/**
	 * parse a url to get the filename. If the url is a directory path, set the
	 * filename to "index.html"
	 * <p>
	 * e.g. the URL is http://www.baidu.com, the filename is index.html the URL
	 * is http://www.baidu.com/a.html, the filename is a.html
	 * </p>
	 * 
	 * @param url
	 *            the filename need to be download
	 * @return path of the file
	 */
	private String getFilename(String url) {
		int lastid = url.lastIndexOf('/');
		/* https:// the last '/' is 7 */
		if (lastid == url.length() - 1){
			url = url.substring(0, lastid);
			return getFilename(url)+".htm";
		}
		return url.substring(lastid + 1);
	}
}
