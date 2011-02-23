/*
 * PhoneGap is available under *either* the terms of the modified BSD license *or* the
 * MIT License (2008). See http://opensource.org/licenses/alphabetical for full text.
 * 
 * Copyright (c) 2005-2010, Nitobi Software Inc.
 * Copyright (c) 2010, IBM Corporation
 */
package com.phonegap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;

/**
 * This class exposes methods in DroidGap that can be called from JavaScript.
 */
public class App extends Plugin {
	
    /**
     * Executes the request and returns PluginResult.
     *
     * @param action        The action to execute.
     * @param args          JSONArry of arguments for the plugin.
     * @param callbackId    The callback id used when calling back into JavaScript.
     * @return              A PluginResult object with a status and message.
     */
    public PluginResult execute(String action, JSONArray args, String callbackId) {
        PluginResult.Status status = PluginResult.Status.OK;
        String result = "";

        try {
        	if (action.equals("clearCache")) {
        		this.clearCache();
        	}
        	else if (action.equals("loadUrl")) {
            	this.loadUrl(args.getString(0), args.optJSONObject(1));
            }
        	else if (action.equals("cancelLoadUrl")) {
            	this.cancelLoadUrl();
            }
        	else if (action.equals("clearHistory")) {
            	this.clearHistory();
            }
        	else if (action.equals("addService")) {
            	this.addService(args.getString(0), args.getString(1));
            }
            return new PluginResult(status, result);
        } catch (JSONException e) {
            return new PluginResult(PluginResult.Status.JSON_EXCEPTION);
        }
    }

    //--------------------------------------------------------------------------
    // LOCAL METHODS
    //--------------------------------------------------------------------------

	/**
	 * Clear the resource cache.
	 */
	public void clearCache() {
		((DroidGap)this.ctx).clearCache();
	}
	
    /**
     * Load the url into the webview.
     * 
     * @param url
     * @param props			Properties that can be passed in to the DroidGap activity (i.e. loadingDialog, wait, ...)
     * @throws JSONException 
     */
	public void loadUrl(String url, JSONObject props) throws JSONException {
		System.out.println("App.loadUrl("+url+","+props+")");
		int wait = 0;
		
		// If there are properties, then set them on the Activity
		if (props != null) {
			JSONArray keys = props.names();
			for (int i=0; i<keys.length(); i++) {
				String key = keys.getString(i); 
				if (key.equals("wait")) {
					wait = props.getInt(key);
				}
				else {
					Object value = props.get(key);
					if (value == null) {
						
					}
					else if (value.getClass().equals(String.class)) {
						this.ctx.getIntent().putExtra(key, (String)value);
					}
					else if (value.getClass().equals(Boolean.class)) {
						this.ctx.getIntent().putExtra(key, (Boolean)value);
					}
					else if (value.getClass().equals(Integer.class)) {
						this.ctx.getIntent().putExtra(key, (Integer)value);
					}
				}
			}
		}
		
		// If wait property, then delay loading
		if (wait > 0) {
			((DroidGap)this.ctx).loadUrl(url, wait);
		}
		else {
			((DroidGap)this.ctx).loadUrl(url);
		}
	}

	/**
	 * Cancel loadUrl before it has been loaded.
	 */
	public void cancelLoadUrl() {
		((DroidGap)this.ctx).cancelLoadUrl();
	}
	
    /**
     * Clear web history in this web view.
     */
    public void clearHistory() {
    	((DroidGap)this.ctx).clearHistory();
    }

    /**
     * Add a class that implements a service.
     * 
     * @param serviceType
     * @param className
     */
    public void addService(String serviceType, String className) {
    	this.ctx.addService(serviceType, className);
    }
}
