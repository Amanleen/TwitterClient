package com.codepath.apps.mysimpletweets;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "04Om8oWHZcgc2YJIN1i5BhA2h";       // Change this
	public static final String REST_CONSUMER_SECRET = "Sgq47TZPX1M5XNUG8KQ3t52lwbsOJqDS89jh1mCUiO6CREeIPG"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}



    //METHOD == ENDPOINT
    //Hometimeline- Gets us the home timeline

//    GET https://api.twitter.com/1.1/statuses/home_timeline.json
//    count=25
//    since_id=1(all tweets sorted by date)

    public void getHomeTimeline(AsyncHttpResponseHandler handler, int count, long maxId, long startId, boolean isRefresh){
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        //Specify the params
        RequestParams params = new RequestParams();
        params.put("count",count);
        if(maxId!=-1 && !isRefresh) {
            params.put("max_id", maxId);
        }
        if(isRefresh && startId!=-1){
            params.put("since_id", startId);
        }

//        params.put("since_id", 1);
        //Execute the request
        getClient().get(apiUrl, params, handler);
    }

    public void getMyProfile(AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("account/verify_credentials.json");
        //Specify the params
        RequestParams params = new RequestParams();
//        params.put("count",25);
//        params.put("since_id", 1);
        //Execute the request
        getClient().get(apiUrl, params, handler);
    }

    //post status
    public void postMystatus(AsyncHttpResponseHandler handler, String message){
        String apiUrl = getApiUrl("statuses/update.json");
        //Specify the params
        RequestParams params = new RequestParams();
      params.put("status", message);
        //Execute the request
       // getClient().get(apiUrl, params, handler);
        getClient().post(apiUrl, params, handler);
    }


    public void getMyTweetsProfile(AsyncHttpResponseHandler handler, int count, long maxId, long startId, boolean isRefresh, String displayName){
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        //Specify the params
        RequestParams params = new RequestParams();
        params.put("count",count);
        params.put("screen_name", displayName);
        if(maxId!=-1 && !isRefresh) {
            params.put("max_id", maxId);
        }
        if(isRefresh && startId!=-1){
            params.put("since_id", startId);
        }
        getClient().get(apiUrl, params, handler);
    }

    public void getMentionsTimeline(AsyncHttpResponseHandler handler, int count, long maxId, long startId, boolean isRefresh){
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        //Specify the params
        RequestParams params = new RequestParams();
        params.put("count",count);
        if(maxId!=-1 && !isRefresh) {
            params.put("max_id", maxId);
        }
        if(isRefresh && startId!=-1){
            params.put("since_id", startId);
        }
        getClient().get(apiUrl, params, handler);
    }

    //COMPOSE TWEET

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}