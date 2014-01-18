package com.summithillsoftware.ultimate.cloud;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.webkit.CookieManager;

import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.summithillsoftware.ultimate.Constants;
import com.summithillsoftware.ultimate.UltimateApplication;
import com.summithillsoftware.ultimate.model.Team;


public class CloudClient {
	public static final String HOST = "www.ultimate-numbers.com";
//	private static final String HOST = "local.appspot.com:8888";
//	private static final String HOST = "local.appspot.com:8890"; // tcp monitor
	public static final String SCHEME_HOST = "http://" + HOST;
	
	private static CloudClient Current;
	
	private RequestQueue queue;
	
	static {
		Current = new CloudClient();
	}
	
	private CloudClient() {
		queue = Volley.newRequestQueue(UltimateApplication.current());
	}
	
	public static CloudClient current() {
		return Current;
	}
	
	public void clearCookies() {
	    CookieManager cookieManager = CookieManager.getInstance();
	    cookieManager.removeAllCookie();	
	}
	
	public void submitRetrieveTeams(final CloudResponseHandler responseHandler) {
		UltimateJsonArrayRequest request = new UltimateJsonArrayRequest(getUrl("/rest/mobile/teams"), new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(final JSONArray jsonArray) {
				List<Team> teams = new  ArrayList<Team>();
				try {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject teamJson = jsonArray.getJSONObject(i);
						Team team = Team.fromJsonObject(teamJson);
						teams.add(team);
					}
					responseHandler.onResponse(CloudResponseStatus.Ok, teams);
				} catch (JSONException e) {
					Log.e(Constants.ULTIMATE, "Unable to convert JSON teams to Team objects", e);
					responseHandler.onResponse(CloudResponseStatus.MarshallingError, null);
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				handleError(responseHandler, error);
			}
		});
		
		addRequestToQueue(request, responseHandler);
	}
	
	private String getUrl(String relativePath) {
		return SCHEME_HOST + relativePath;
	}
	
	private void handleError(final CloudResponseHandler responseHandler, final VolleyError error) {
		CloudResponseStatus errorStatus = interpretError(error);
		String errorMessage = "Cloud error" + errorStatus;
		if (error.networkResponse != null) {
			errorMessage += " http status=" + error.networkResponse.statusCode;
			errorMessage += " more..." + error.networkResponse.toString();
		}
		Log.e(Constants.ULTIMATE, errorMessage, error);
		responseHandler.onResponse(errorStatus, null);
	}
	
	private CloudResponseStatus interpretError(VolleyError error) {
		if (!isConnected()) {
			return CloudResponseStatus.NotConnectedToInternet;
		} else if (error instanceof TimeoutError) {
			return CloudResponseStatus.Timeout;
		} else if (error instanceof ParseError) {
			return CloudResponseStatus.MarshallingError;
		} else if (error instanceof TimeoutError) {
			return CloudResponseStatus.Timeout;
		} else if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
			return CloudResponseStatus.Unauthorized;
		}
		return CloudResponseStatus.Unknown;
	}
	
	private boolean isConnected() {
		ConnectivityManager cm = (ConnectivityManager) UltimateApplication.current().getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();  
		// if no network is available then networkInfo will be null
		if (networkInfo == null) { 
			 return false;
		} 
		return networkInfo.isConnected();
	}
	
	private void addRequestToQueue(Request<?> request, CloudResponseHandler handler) {
		if (isConnected()) {
			queue.add(request);
		} else {
			handler.onResponse(CloudResponseStatus.NotConnectedToInternet, null);
		}
	}

}
