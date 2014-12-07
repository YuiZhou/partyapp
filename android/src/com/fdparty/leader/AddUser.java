package com.fdparty.leader;

import com.example.partyapp.R;
import com.fdparty.common.HttpResponseProcess;
import com.fdparty.common.HttpValue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddUser extends Activity {
	private String username;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_user);
		
		Intent intent = getIntent();
		this.username = intent.getStringExtra("username");
	}
	
	public void addUserHandler(View v){
		EditText idField = (EditText)findViewById(R.id.add_user_id);
		EditText nameField = (EditText)findViewById(R.id.add_user_name);
		
		String id = idField.getText().toString().trim();
		String name = nameField.getText().toString().trim();
		
		if(addUser(id, name)){
			//Toast.makeText(this, "add successful", Toast.LENGTH_SHORT).show();
			this.finish();
		}else{
			Toast.makeText(this, "add failed", Toast.LENGTH_SHORT).show();
		}
	}

	private boolean addUser(String id, String name) {
		String url = HttpValue.Server.toString()+"?m=Leader&a=addUser&usrid="+username+"&newUser="+id+"&name="+name;
		
		try{
			HttpResponseProcess process = new HttpResponseProcess(url);
			String res = process.toString();
			
			if(res.equals("true")){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
		
		
	}
}
