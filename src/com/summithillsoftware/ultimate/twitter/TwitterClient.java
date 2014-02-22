package com.summithillsoftware.ultimate.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.net.Uri;

import com.summithillsoftware.ultimate.cloud.CloudClient;
import com.summithillsoftware.ultimate.model.Preferences;
import com.summithillsoftware.ultimate.util.UltimateLogger;

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

public class TwitterClient {
	public static final String TWITTER_CALLBACK_PATH = "/twitter-oauth-callback.jsp";
	public static final String TWITTER_CALLBACK_URL = CloudClient.SCHEME_HOST + TWITTER_CALLBACK_PATH;
	public static final String TWITTER_API_CONSUMER_KEY = "60ShZrkvkw28IqELlstXXA";
	public static final String TWITTER_API_CONSUMER_SECRET = "QXabIEDdPW1HZC3hm3JkOVpD8vhXLbO5kKOjakbKivg";
	public static final String APP_ONLY_AUTH_URL = "https://api.twitter.com/oauth2/token";
	public static final String REQUEST_TOKEN_URL = "https://api.twitter.com/oauth/request_token";
	public static final String AUTHORIZE_URL = "https://api.twitter.com/oauth2/token";
	public static final String ACCESS_TOKEN_URL = "https://api.twitter.com/oauth/access_token";
	
	// Twitter oauth urls
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
	
	private static TwitterClient Current;
	
	private Twitter twitter;
	private RequestToken requestToken;
	
	static {
		Current = new TwitterClient();
	}
	
	private TwitterClient() {

	}
	
	public static TwitterClient current() {
		return Current;
	}
	
	public boolean isSignedIn() {
		String moniker = getTwitterMoniker();
		return moniker != null && !moniker.isEmpty();
	}
	
	public boolean isAuthentiationCallbackUrl(String url) {
		return url.contains(TWITTER_CALLBACK_PATH);
	}
	
	// IMPORTANT: Do not call on UI thread
	public String getAuthenticationURL() {
	    TwitterFactory factory = new TwitterFactory(getTwitterConfiguration());
	    Twitter twitter = factory.getInstance();
	    try {
	    	requestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
	    	return requestToken.getAuthenticationURL();
	    } catch (TwitterException e) {
            // Error in updating status
            UltimateLogger.logError("Twitter oAuth failure...unable to get authentication request token for the app", e);
            return null;
        }
	}
	
	// IMPORTANT: Do not call on UI thread
	public boolean setTwitterCredentialsFromCallbackUrl(String callbackUrl) {
		AccessToken accessToken;
		try {
			Uri uri =  Uri.parse(callbackUrl);
			String verifier = uri.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);
			accessToken = getTwitter().getOAuthAccessToken(requestToken, verifier);
		} catch (TwitterException e) {
			UltimateLogger.logError("Twitter error: unable to getOAuthAccessToken", e);
			return false;
		}
		try {
			setTwitterCredentials(accessToken.getToken(), accessToken.getTokenSecret());
			return true;
		} catch (Exception e) {
			UltimateLogger.logError("Twitter error: could not glean token and secret from twitter response", e);
			requestToken = null;
			return false;
		}
	}
	
	private void setTwitterCredentials(String twitterOAuthUserAccessToken, String twitterOAuthUserAccessTokenSecret) {
		Preferences.current().setTwitterOAuthUserAccessToken(twitterOAuthUserAccessTokenSecret);
		Preferences.current().setTwitterOAuthUserAccessTokenSecret(twitterOAuthUserAccessTokenSecret);
		Preferences.current().save();
		UltimateLogger.logInfo("Twitter credentials saved for this user");
	}

	public String getTwitterMoniker() {
		return Preferences.current().getTwitterMoniker();
	}
	
	public void setTwitterMoniker(String twitterMoniker) {
		Preferences.current().setTwitterMoniker(twitterMoniker);
		Preferences.current().save();
	}
	
	private String getTwitterOAuthUserAccessTokenSecret() {
		return Preferences.current().getTwitterOAuthUserAccessTokenSecret();
	}
	
	private String getTwitterOAuthUserAccessToken() {
		return Preferences.current().getTwitterOAuthUserAccessToken();
	}
	
	private Twitter getTwitter() {
		if (twitter == null) {
			TwitterFactory factory = new TwitterFactory(getTwitterConfiguration());
			Twitter twitter = factory.getInstance();
			return twitter;
		}
		return twitter;
	}
	
	private Configuration getTwitterConfiguration() {
		ConfigurationBuilder builder = new ConfigurationBuilder();	
		
	    builder.setOAuthConsumerKey(TWITTER_API_CONSUMER_KEY);
	    builder.setOAuthConsumerSecret(TWITTER_API_CONSUMER_SECRET);
	    Configuration configuration = builder.build();
	    
	    return configuration;
	}

}
