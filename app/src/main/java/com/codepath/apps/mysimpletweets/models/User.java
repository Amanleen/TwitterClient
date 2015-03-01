package com.codepath.apps.mysimpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by aman on 2/20/15.
 */

/**
 * "user": {
 "name": "OAuth Dancer",
 "profile_sidebar_fill_color": "DDEEF6",
 "profile_background_tile": true,
 "profile_sidebar_border_color": "C0DEED",
 "profile_image_url": "http://a0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
 "created_at": "Wed Mar 03 19:37:35 +0000 2010",
 "location": "San Francisco, CA",
 "follow_request_sent": false,
 "id_str": "119476949",
 "is_translator": false,
 "profile_link_color": "0084B4" }
 */

public class User implements Serializable {
    //list the attributes

    private String username;
    private long uid;
    private String screenName;
    private String profileImageUrl;

    private String bannerUrl;

    private int tweetsCount;
    private int followersCount;
    private int followingCount;

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public int getTweetsCount() {
        return tweetsCount;
    }

    public void setTweetsCount(int tweetsCount) {
        this.tweetsCount = tweetsCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    //deserialize the user json =>  User
    public static User fromJSON(JSONObject json){
        User u = new User();
        try {
            u.setUsername(json.getString("name"));
            u.setUid(json.getLong("id"));
            u.setScreenName(json.getString("screen_name"));
            u.setProfileImageUrl(json.getString("profile_image_url"));
            u.setFollowersCount(json.getInt("followers_count"));
            u.setFollowingCount(json.getInt("friends_count"));
            u.setTweetsCount(json.getInt("statuses_count"));
            if(json.has("profile_background_image_url")) {
                u.setBannerUrl(json.getString("profile_background_image_url"));
            }else if(json.has("profile_banner_url")){
                u.setBannerUrl(json.getString("profile_banner_url"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }
}
