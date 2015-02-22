package com.codepath.apps.mysimpletweets.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by aman on 2/19/15.
 */
/*
    [
        {
            "text": "just another test",
    "contributors": null,
    "id": 240558470661799936,
    "retweet_count": 0,
    "in_reply_to_status_id_str": null,
    "geo": null,
    "retweeted": false,
    "in_reply_to_user_id": null,
    "place": null,
    "source": "<a href="//realitytechnicians.com\"" rel="\"nofollow\"">OAuth Dancer Reborn</a>",
    "user": {
      "name": "OAuth Dancer",
      "profile_sidebar_fill_color": "DDEEF6",
      "profile_background_tile": true,
      "profile_sidebar_border_color": "C0DEED",
      "profile_image_url": "http://a0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",

        },
        {

        }

    ]
* */

//Parse JSON + store data, encapsulate state logic or display logic
 public class Tweet {
    //List out the attributes
    private String body;
    private long uid; //unique id for the tweet
    private User user;
    private String createdAt;
    private int retweetCount;
    private int favCount;
    private String screen_name;

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        Log.d("Aman", screen_name);
        this.screen_name = screen_name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public int getFavCount() {
        return favCount;
    }

    public void setFavCount(int favCount) {
        this.favCount = favCount;
    }

    //Deserialize JSON & build tweet objects
    //Tweet.fromJSON("{}") => <Tweet>
    public static Tweet fromJSON(JSONObject json){
        Tweet tweet = new Tweet();
        //extract values from JSON, store & return
        try {
            tweet.setBody(json.getString("text"));
            tweet.setUid(json.getLong("id"));
            tweet.setCreatedAt(json.getString("created_at"));
            tweet.setUser(User.fromJSON(json.getJSONObject("user")));
            tweet.setRetweetCount(json.getInt("retweet_count"));
            tweet.setFavCount(json.getInt("favorite_count"));
            tweet.setScreen_name(json.getJSONObject("user").getString("screen_name"));
            //tweet.user = ..
        } catch (JSONException e) {
            //e.printStackTrace();
        }

        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray){
        ArrayList<Tweet> tweets = new ArrayList<>();
        for(int i=0; i<jsonArray.length();i++){
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);
                if(tweet!=null){
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }
}
