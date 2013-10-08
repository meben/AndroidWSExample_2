package com.example.webservicetest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	
	EditText editLogin,editPassword;
	Button   btn_connect;
	String str_login,str_pass;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		editLogin = (EditText)findViewById(R.id.editText1);
		editPassword = (EditText)findViewById(R.id.editText2);
		btn_connect = (Button)findViewById(R.id.button1);
		btn_connect.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
public void autentifUser(){
		
		String login_str,pass_str;
 		if (str_login.equalsIgnoreCase("") || str_pass.equalsIgnoreCase(""))
 		{
 			 
 				Toast.makeText(getApplicationContext(), "Login ou password vide !!!", 3000).show();
 	 		  
 		}
		
 		else{
			//webservice authentification
			InputStream is = null;
			String result = "";
 			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("user","r"));

			// Envoie de la commande http
			try{
				
				
				HttpClient httpClient = new DefaultHttpClient();
			    HttpContext localContext = new BasicHttpContext();
			    HttpGet httpGet = new HttpGet("http://10.0.2.2/webservice/authen.php");
			    HttpResponse response;
			    try {
			        response = httpClient.execute(httpGet, localContext);
 					HttpEntity entity = response.getEntity();
 					is = entity.getContent();

			    } catch (ClientProtocolException e) {
			        // TODO Auto-generated catch block
			        e.printStackTrace();
			    } catch (IOException e) {
			        // TODO Auto-generated catch block
			        e.printStackTrace();
			    }

 
			}catch(Exception e){
				Log.e("log_tag ", "Error in http connection " + e.toString());
			}
			
			// Convertion de la requête en string
			try{
				BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				result=sb.toString();
				Log.i("log_tag ", " " + result);

			}catch(Exception e){
				Log.e("log_tag ", "Error converting result " + e.toString());
			}
			// Parse les données JSON
			try{
				JSONArray jArray = new JSONArray(result);
				for(int i=0;i<jArray.length();i++){
					JSONObject json_data = jArray.getJSONObject(i);
					login_str =json_data.getString("login");
					pass_str = json_data.getString("password");


					if( str_login.equalsIgnoreCase(login_str)  && str_pass.equalsIgnoreCase(pass_str))
							 
					{
						
			              Toast.makeText(getApplicationContext(), "  connexion: Login ou password correct ", 5000).show();

 					}
					else {
			              Toast.makeText(getApplicationContext(), "Erreur connexion: Login ou password incorrect !!!", 3000).show();
					}
   				}
			}catch(JSONException e){
				Log.e("log_tag++++++++++++++++++++++", "Error parsing data " + e.toString());
			}}
			}

@Override
public void onClick(View arg0) {


	str_login= editLogin.getText().toString();
	str_pass = editPassword.getText().toString();
	autentifUser();
	
	Intent i = new Intent(MainActivity.this,ReadAttachment.class);
	startActivity(i);
	
	
}
	

}
