package com.webview;

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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.graphics.Bitmap;

public class rWebView extends Activity {
	
	private static final String TAG = "Brower";
    /** Called when the activity is first created. */

   private WebView mWebView;
   private AlertDialog.Builder builder;
   private ProgressDialog mProgressDialog;
   
	private static final int MENU_EXIT = Menu.FIRST;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.e(TAG, "onCreate");
        init();
    }

	private void init() 
	{
		builder = new AlertDialog.Builder(this);

		mWebView = (WebView)findViewById(R.id.webview);
		//mWebView.getSettings().setJavaScriptEnabled(true);		
		mWebView.getSettings().setDefaultTextEncodingName("big5");
		mWebView.getSettings().setSupportZoom(false);

		if (CheckInternet(3))
		{
			mWebView.loadUrl("http://211.72.204.156/index.html");			
		}
		
		mWebView.setWebViewClient(new WebViewClient()
		{  
		    public boolean shouldOverrideUrlLoading(WebView view, String url) 
		    {  
		        view.loadUrl(url);  
		        return true;  
		    }  
		    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) 
		    {
		    	openOptionsDialog(failingUrl);
		    	CheckInternet(3);
		    }
		});
		
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
    	
		//Check Internet
		if (has == false)
		{
			String folder = Environment.getExternalStorageDirectory().toString();
			mWebView.loadUrl("file://" + folder + "/error.html");		
			//openOptionsDialog("file://" + folder + "/error.html");
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
		
	    if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) 
	    {  
	    	  url = mWebView.getUrl();
	    	  openOptionsDialog(url);
	         mWebView.goBack();
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
          
	    }
	    else if (keyCode == KeyEvent.KEYCODE_MENU)
	    {
	    	return super.onKeyDown(keyCode, event);
	    }
        return super.onKeyDown(keyCode, event);        	  	
 	   	    
	}
	
	protected void showNotification() 
	{
        CharSequence from ="WebView";
        CharSequence message ="running";


		Intent intent = new Intent(this, rWebView.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
	                        intent, 0/*PendingIntent.FLAG_NO_CREATE*/);
		Notification notif = new Notification(R.drawable.icon , "WebView",  System.currentTimeMillis());
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
