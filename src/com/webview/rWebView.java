package com.webview;

import com.phonegap.DroidGap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.graphics.Bitmap;

public class rWebView extends DroidGap {
	
	private static final String TAG = "Brower";
    /** Called when the activity is first created. */

   private AlertDialog.Builder builder;
   private static final int MENU_EXIT = Menu.FIRST;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        Log.e(TAG, "onCreate");
 
        builder = new AlertDialog.Builder(this);

        if (CheckInternet(3))
		{
        	//openOptionsDialog("pass");
			super.loadUrl("http://123.204.250.65/index.asp");			
			//super.loadUrl("file:///android_asset/www/index.html");			
		}
		else
		{
			super.loadUrl("file:///android_asset/www/wifi.html");		
		}
       
		showNotification();
    }

    
    private boolean CheckInternet(int retry)
    {
    	boolean has = false;
    	for (int i=0; i<=retry; i++)
    	{
    		has = HaveInternet();
    		if (has == true) break;    		
    	}
    	
		return has;
    }
    
    private boolean HaveInternet()
    {
	     boolean result = false;
	     
	     ConnectivityManager connManager = (ConnectivityManager) 
	                                getSystemService(Context.CONNECTIVITY_SERVICE); 
	      
	     NetworkInfo info = connManager.getActiveNetworkInfo();
	     
	     if (info == null || !info.isConnected())
	     {
	    	 result = false;
	     }
	     else 
	     {
		     if (!info.isAvailable())
		     {
		    	 result =false;
		     }
		     else
		     {
		    	 result = true;
		     }
     }
    
     return result;
    }
    	
	public boolean onCreateOptionsMenu(Menu menu)
	{
	    super.onCreateOptionsMenu(menu);
	    
	    menu.add(0 , MENU_EXIT, 1 ,R.string.menu_exit).setIcon(R.drawable.exit)
	    .setAlphabeticShortcut('E');
	    
	    return true;  
	}

	 @Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		    switch (item.getItemId())
		    { 		      
		          case MENU_EXIT:
		        	  delenot();
		        	  finish();
		          break ;
		    }
		      return true ;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{  
		String url = "";
		
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && super.appView.canGoBack()) 
	    {  
	    	 //super.appView.goBack();
	         return true;
	    }  
	    else if (keyCode == KeyEvent.KEYCODE_BACK)
	    {
            builder.setMessage("Are you exit?");
            builder.setCancelable(false);
           
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) 
                {
                	delenot();
                	finish();
                }
            });
           
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) 
                {
                	
                }
            });
            
          AlertDialog alert = builder.create();
          alert.show();
          
          return true;        	  	
	    }
	    else if (keyCode == KeyEvent.KEYCODE_MENU)
	    {
	    	return super.onKeyDown(keyCode, event);
	    }
	    
        return super.onKeyDown(keyCode, event);        	  	
 	   	    
	}
	
	protected void showNotification() 
	{
        CharSequence from ="WebViewP";
        CharSequence message ="running";


		//Intent intent = new Intent(this, rWebView.class);
        Intent intent = this.getIntent();
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		Notification notif = new Notification(R.drawable.icon , "WebViewP",  System.currentTimeMillis());
		
		notif.setLatestEventInfo(this, from, message, contentIntent);
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(R.string.app_name, notif);

    }

	void delenot() 
	{
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(R.string.app_name);
    }
	
    //error message
    private void openOptionsDialog(String info)
	{
	    new AlertDialog.Builder(this)
	    .setTitle("message")
	    .setMessage(info)
	    .setPositiveButton("OK",
	        new DialogInterface.OnClickListener()
	        {
	         public void onClick(DialogInterface dialoginterface, int i)
	         {
	         }
	         }
	        )
	    .show();
	}
}
