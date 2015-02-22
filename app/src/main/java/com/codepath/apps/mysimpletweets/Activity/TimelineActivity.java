package com.codepath.apps.mysimpletweets.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TimelineActivity extends ActionBarActivity {

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;
    private long start_index=0;
    private long minIdSoFar =0;
    private int count=25;

    private long lastQueryTime = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_follow_text);
        //getSupportActionBar().setTitle("Twitter");
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        lvTweets = (ListView)findViewById(R.id.lvTweets);
        //create arraylist(data src)
        tweets = new ArrayList<>();
        //Construct the adapter from data source
        aTweets = new TweetsArrayAdapter(this, tweets);
        //connect adapter to the list view
        lvTweets.setAdapter(aTweets);
        lvTweets.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(totalItemCount!=0 && firstVisibleItem+visibleItemCount==totalItemCount){
//                    Toast.makeText(TimelineActivity.this, "End of list view ", Toast.LENGTH_SHORT).show();
                   // pageforNewTweets(totalItemCount);
                   populateTimeline();
                }
            }
        });

        client = TwitterApplication.getRestClient();//Singleton Client
        populateTimeline();
    }


    private void populateTimeline() {
        if(lastQueryTime!=-1 && System.currentTimeMillis()-lastQueryTime < 5000){
            return;
        }
        lastQueryTime = System.currentTimeMillis();
        client.getHomeTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
               Log.d("RESPONSE", response.toString());
                List<Tweet> tweets = Tweet.fromJSONArray(response);
               aTweets.addAll(tweets);
                if (tweets.size() > 0) {
                    long minId = Long.MAX_VALUE;
                    for (int i = 0; i < tweets.size(); i++) {
                        Tweet tweeti = tweets.get(i);
                        long tempId = tweeti.getUid();
                        if (tempId < minId) {
                            minId = tempId;
                        }
                    }//for
                    minIdSoFar = minId - 1;
                    Log.d("Aman",minIdSoFar+"");
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject erroeResponse) {
                Log.d("RESPONSE", erroeResponse.toString());
            }
        }, count, minIdSoFar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
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
//            Toast.makeText(this, "Action item", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, Composetweet.class);
            startActivityForResult(i, 0);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK){
            return;
        }else{
            aTweets.clear();
            populateTimeline();
        }

    }
}
