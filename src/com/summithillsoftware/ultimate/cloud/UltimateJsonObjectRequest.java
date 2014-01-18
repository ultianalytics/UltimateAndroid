package com.summithillsoftware.ultimate.cloud;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.summithillsoftware.ultimate.model.Preferences;

public class UltimateJsonObjectRequest extends JsonObjectRequest {

	public UltimateJsonObjectRequest(int method, String url,
			JSONObject jsonRequest, Listener<JSONObject> listener,
			ErrorListener errorListener) {
		super(method, url, jsonRequest, listener, errorListener);
	}

	public UltimateJsonObjectRequest(String url, JSONObject jsonRequest,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		super(url, jsonRequest, listener, errorListener);
	}

	
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		Map<String, String> headers = new HashMap<String, String>();
		String authCookie = Preferences.current().getCloudAuthenticationCookie();
		if (authCookie != null) {
			headers.put("Cookie", Preferences.current().getCloudAuthenticationCookie());
		}
		return headers;
	}
}
