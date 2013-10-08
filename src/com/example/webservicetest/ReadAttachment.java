package com.example.webservicetest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import junit.framework.Test;

import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;
 
 public class ReadAttachment extends Activity  {
	
	WebView 		webview;
	String 			attach,file_name="http://www.hadopi.fr/sites/default/files/page/pdf/Rapport_streaming_2013.pdf";
	
    // declare the dialog as a member field of your activity
    ProgressDialog mProgressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
  	 		
	        // instantiate it within the onCreate method
	        mProgressDialog = new ProgressDialog(ReadAttachment.this);
	        mProgressDialog.setMessage("Please Wait ...");
	        mProgressDialog.setIndeterminate(false);
	        mProgressDialog.setMax(100);
	        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

	        
	    	 
	        // execute this when the downloader must be fired
	        DownloadFile downloadFile = new DownloadFile();
	       
	        downloadFile.execute(file_name );
 
	       
	        
	        
 	  }
  
 public String GetFileExt(String FileName)
 {       
      String ext = FileName.substring((FileName.lastIndexOf(".") + 1), FileName.length());
      return ext;
 }
public void OpenFile(){
	 
	
    String file1 = "/sdcard/file1.pdf"  ; //Local - work fine GetFileExt(file1)
  		 Log.i("________________",GetFileExt(file1)+"++++++++");

         Intent intent = new Intent();
	     	intent.setAction(android.content.Intent.ACTION_VIEW);
	     	File file = new File(file1);

         if(GetFileExt(file1).equalsIgnoreCase("doc") || GetFileExt(file1).equalsIgnoreCase("docx") ){
         	
        	 intent.setDataAndType(Uri.fromFile(file), "application/msword");
          }
         else if (GetFileExt(file1).equalsIgnoreCase("txt")  ){
         	intent.setDataAndType(Uri.fromFile(file), "text/plain");
 
          }
         else if (GetFileExt(file1).equalsIgnoreCase("pdf")){
         	intent.setDataAndType(Uri.fromFile(file), "application/pdf");
 
         }

         try {
             startActivity(intent);
         }
         catch (ActivityNotFoundException e) {
             Log.w("Debug",e);
             Toast.makeText( ReadAttachment.this,
                 "No Application Available to View PDF"+ e.toString(),
                 100000).show();
         } 


}
	
	
	 private class DownloadFile extends AsyncTask<String, Integer, String>  {
	     protected String doInBackground(String... sUrl){
	    	 
	    	// downloadFile(sUrl[0]);
	        try {
		 	 		Log.i("______________3_1", sUrl[0]);

	             URL url = new URL(sUrl[0]);
	             URLConnection connection = url.openConnection();
		 	 		Log.i("______________3-2", "connection" );
		 	 		if (Build.VERSION.SDK != null
		 	 				&& Build.VERSION.SDK_INT > 13) {
		 	 			connection.setRequestProperty("Connection", "close");
		 	 				}
	             connection.connect();
		 	 		Log.i("______________3-2-2", "connection" );

	             // this will be useful so that you can show a typical 0-100% progress bar
	             int fileLength = connection.getContentLength();
		 	 		Log.i("______________3-2-3", "__________"+fileLength );


	             // download the file
 
	             InputStream input = new BufferedInputStream(url.openStream());
		 	 		Log.i("______________3-3", "BufferedInputStream");

		 	 		
		 	 		 
		 	 		
	             OutputStream output = new FileOutputStream("/sdcard/file1.pdf" );
	 	 		Log.i("______________3-4", "FileOutputStream");

	             byte data[] = new byte[1024*4];
	             long total = 0;
	             int count;
	             while ((count = input.read(data)) != -1) {
	                 total += count;
	                 // publishing the progress....
	                 publishProgress((int) (total * 100 / fileLength));
	                 output.write(data, 0, count);
	             }
	             output.flush();
	             output.close();
	         } catch (Exception e) {
	        	 e.printStackTrace();
		 	 		Log.i("______________Exception","___"+e.toString());

	         }
             

	         return null;
	     }
	     
 	     @Override
	     protected void onPreExecute() {
	         super.onPreExecute();
	         mProgressDialog.show();
 
	 	 		Log.i("______________43","onPreExecute");
	     }
 
	     @Override
	     protected void onPostExecute(String result) {
	      // TODO Auto-generated method stub
	      super.onPostExecute(result);
	       //  mProgressDialog.dismiss();
	 	 		Log.i("______________43","onPostExecute");

 	         OpenFile();
 	     }
	     @Override
	     protected void onProgressUpdate(Integer... progress) {
	         super.onProgressUpdate(progress);
	         mProgressDialog.setProgress(progress[0]);
	     }
	 }
	 }
	 
 