package com.codepath.apps.mysimpletweets.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.adapter.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aman on 2/26/15.
 */
public class MentionsListFragment extends Fragment {

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvMentions;

    private long start_index= 0;
    private long minIdSoFar = -1;
    private long maxIdSoFar = -1;
    private int count=25;
    private long lastQueryTime = -1;
    
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mentions_list, container, false);

        lvMentions = (ListView)v.findViewById(R.id.lvMentions);
        tweets = new ArrayList<>();
        //Construct the adapter from data source
        aTweets = new TweetsArrayAdapter(v.getContext(), tweets);
        //connect adapter to the list view
        lvMentions.setAdapter(aTweets);
        lvMentions.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount != 0 && firstVisibleItem + visibleItemCount == totalItemCount) {
                    populateMentionsTimeline(false);
                }
            }
        });

        client = TwitterApplication.getRestClient();//Singleton Client
        populateMentionsTimeline(false);
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != Activity.RESULT_OK){
            return;
        }else{
            populateMentionsTimeline(true);
        }
    }

    private void populateMentionsTimeline(final boolean isRefresh) {
        if(lastQueryTime !=-1 && System.currentTimeMillis() - lastQueryTime < 5000){
//            swipeContainer.setRefreshing(false);
            Log.d("DEBUG", "Too early to request");
            return;
        }
        Log.d("DEBUG", "Making request, minId:" + minIdSoFar +" maxId="+maxIdSoFar+" isRefresh="+isRefresh);
        lastQueryTime = System.currentTimeMillis();
        client.getMentionsTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
//                swipeContainer.setRefreshing(false);
                List<Tweet> newTweets = Tweet.fromJSONArray(response);
                if(isRefresh){
                    tweets.addAll(0,newTweets);
                    aTweets.notifyDataSetChanged();
                }else {
                    aTweets.addAll(newTweets);
                }
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
                Log.d("DEBUG", ""+erroeResponse);
//                swipeContainer.setRefreshing(false);
            }
        }, count, minIdSoFar, maxIdSoFar, isRefresh);
    }

}
