package com.summithillsoftware.ultimate.twitter;

import com.summithillsoftware.ultimate.cloud.CloudClient;

/*
	Application settings
	
	Your application's API keys are used to authenticate requests to the Twitter Platform.
	Access level: Read, write, and direct messages
	Callback URL: http://www.ultimate-numbers.com/rest/view/twitter-oauth-callback
	Sign in with Twitter: No
	App-only authentication: https://api.twitter.com/oauth2/token
	Request token URL: https://api.twitter.com/oauth/request_token
	Authorize URL: https://api.twitter.com/oauth/authorize
	Access token URL: https://api.twitter.com/oauth/access_token
	
	API key: 60ShZrkvkw28IqELlstXXA
	API secret: QXabIEDdPW1HZC3hm3JkOVpD8vhXLbO5kKOjakbKivg
	Owner: velohomme_shs
	Owner ID: 2351746723

 */

public class Tweeter {
	public static final String TWITTER_CALLBACK_URL = CloudClient.SCHEME_HOST + "/rest/view/twitter-oauth-callback";
	public static final String TWITTER_API_CONSUMER_KEY = "60ShZrkvkw28IqELlstXXA";
	public static final String TWITTER_API_CONSUMER_SECRET = "QXabIEDdPW1HZC3hm3JkOVpD8vhXLbO5kKOjakbKivg";
	public static final String APP_ONLY_AUTH_URL = "https://api.twitter.com/oauth2/token";
	public static final String REQUEST_TOKEN_URL = "https://api.twitter.com/oauth/request_token";
	public static final String AUTHORIZE_URL = "https://api.twitter.com/oauth2/token";
	public static final String ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";

}
