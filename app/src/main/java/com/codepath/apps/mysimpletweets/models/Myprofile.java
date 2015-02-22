package com.codepath.apps.mysimpletweets.models;

import org.json.JSONObject;

/**
 * Created by aman on 2/21/15.
 */
public class Myprofile {
    private String username;
    private String displayName;
    private String profileUrl;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
