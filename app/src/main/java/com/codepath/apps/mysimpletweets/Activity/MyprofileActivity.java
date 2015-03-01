package com.codepath.apps.mysimpletweets.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.adapter.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.models.Myprofile;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyprofileActivity extends ActionBarActivity {
    public static String MYPROFILE="Amanleen Puri";
    User user;
    
    EditText etComposeTweet;
    TextView tvUsername;
    TextView tvScreenName;
    TextView tvTweetsCount;
    TextView tvFollowingCount;
    TextView tvFollowersCount;
    ImageView ivProfileImg;
    ImageView ivBannerImg;

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    ListView lvMyTweets;

    private long start_index= 0;
    private long minIdSoFar = -1;
    private long maxIdSoFar = -1;
    private int count=25;
    private long lastQueryTime = -1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);
        
        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra(MYPROFILE);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_twitter_icon);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        setView();

        lvMyTweets = (ListView)findViewById(R.id.lvMyTweets);
        tweets = new ArrayList<>();
        //Construct the adapter from data source
        aTweets = new TweetsArrayAdapter(this, tweets);
        //connect adapter to the list view
        lvMyTweets.setAdapter(aTweets);
        lvMyTweets.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(totalItemCount!=0 && firstVisibleItem+visibleItemCount==totalItemCount){
                    populateTimeline(false);
                }
            }
        });

        client = TwitterApplication.getRestClient();//Singleton Client
        populateTimeline(false);
        
    }

    private void populateTimeline(final boolean isRefresh) {
        Log.d("RESPONSE", " "+isRefresh);
        if(lastQueryTime !=-1 && System.currentTimeMillis() - lastQueryTime < 5000){
//            swipeContainer.setRefreshing(false);
//            Log.d("DEBUG", "Too early to request");
            return;
        }
//        Log.d("DEBUG", "Making request, minId:" + minIdSoFar +" maxId="+maxIdSoFar+" isRefresh="+isRefresh);
        lastQueryTime = System.currentTimeMillis();
        client.getMyTweetsProfile(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                Log.d("RESPONSE", response.toString());
//                swipeContainer.setRefreshing(false);
                List<Tweet> newTweets = Tweet.fromJSONArray(response);
//                Log.d("TWEETS", newTweets.toString());
                if(isRefresh){
                    tweets.addAll(0,newTweets);
                    aTweets.notifyDataSetChanged();
                }else {
                    aTweets.addAll(newTweets);
                }
//                Log.d("TWEETS", aTweets.toString());
                if (newTweets.size() > 0) {
                    long minId = Long.MAX_VALUE;
                    long maxId = Long.MIN_VALUE;
                    for (int i = 0; i < newTweets.size(); i++) {
                        Tweet tweeti = newTweets.get(i);
                        long tempId = tweeti.getUid();
                        if (tempId < minId) {
                            minId = tempId;
                        }
                        if(tempId>maxId){
                            maxId = tempId;
                        }
                    }//for
                    minIdSoFar = minId - 1;
                    if(maxId>maxIdSoFar) {
                        maxIdSoFar = maxId;
                    }
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject erroeResponse) {
                Log.d("RESPONSE", ""+erroeResponse);
//                swipeContainer.setRefreshing(false);
            }
        }, count, minIdSoFar, maxIdSoFar, isRefresh, user.getScreenName());
    }
   

    private void setView(){
        tvUsername = (TextView)findViewById(R.id.tvUsername);
        tvScreenName = (TextView)findViewById(R.id.tvScreenName);
        ivProfileImg = (ImageView)findViewById(R.id.ivProfileImg);
        tvTweetsCount = (TextView)findViewById(R.id.tvTweetsCount);
        tvFollowingCount = (TextView)findViewById(R.id.tvFollowingCount);
        tvFollowersCount = (TextView)findViewById(R.id.tvFollowersCount);
        ivBannerImg = (ImageView)findViewById(R.id.ivBannerImg);
        lvMyTweets = (ListView)findViewById(R.id.lvMyTweets);

        if(user.getTweetsCount()>0){
            tvTweetsCount.setText(String.valueOf(user.getTweetsCount()));
        }else {
            tvTweetsCount.setText(String.valueOf(0));
        }

        if(user.getFollowingCount()>0){
            tvFollowingCount.setText(String.valueOf(user.getFollowingCount()));
        }else {
            tvFollowingCount.setText(String.valueOf(0));
        }
        if(user.getFollowersCount()>0){
            tvFollowersCount.setText(String.valueOf(user.getFollowersCount()));
        }else {
            tvFollowersCount.setText(String.valueOf(0));
        }

        tvUsername.setText(user.getUsername());
        tvScreenName.setText("@"+user.getScreenName());
        Picasso.with(getBaseContext()).load(user.getProfileImageUrl()).into(ivProfileImg);
        if(user.getBannerUrl()!=null) {
            Picasso.with(getBaseContext()).load(user.getBannerUrl()).into(ivBannerImg);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_myprofile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
