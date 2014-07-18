package com.fdparty.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpResponseProcess {
	String str = "";
	public HttpResponseProcess(String url) throws HttpException, ClientProtocolException, IOException{
		//processURL(url);
		HttpPost request = new HttpPost(url);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(request);
		
		if(response.getStatusLine().getStatusCode() == 200){
			showResponseResult(response);
		}else{
			throw new HttpException(response.getStatusLine()+"");
		}
	}
	
	public String toString(){
		return str.trim();
	}
	
	private void showResponseResult(HttpResponse response)
    {
        if (null == response)
        {
            return;
        }

        HttpEntity httpEntity = response.getEntity();
        try
        {
            InputStream inputStream = httpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            String line = "";
            while (null != (line = reader.readLine()))
            {
                str += line;

            }

//            System.out.println(result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

	public String process(String url) {
		// TODO Auto-generated method stub
		return null;
	}
}
