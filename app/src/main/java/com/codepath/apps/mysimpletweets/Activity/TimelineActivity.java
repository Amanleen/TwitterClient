package com.codepath.apps.mysimpletweets.Activity;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.adapter.TabsPageAdapter;
import com.codepath.apps.mysimpletweets.adapter.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;
import com.codepath.apps.mysimpletweets.models.Myprofile;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class TimelineActivity extends ActionBarActivity implements ActionBar.TabListener {

    private TwitterClient client;
    private  User user;

    private ActionBar actionBar;
    private TabsPageAdapter mAdapter;
    private String[] tabs = {"Home", "Mentions"};
    ViewPager vpPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_timeline);
       
        vpPager = (ViewPager)findViewById(R.id.vpPager);
        actionBar = getSupportActionBar();
        mAdapter = new TabsPageAdapter(getSupportFragmentManager());

        vpPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        for(String tab_name : tabs){
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));

        }
        
        vpPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        client = TwitterApplication.getRestClient();//Singleton Client
        setMyProfile();
//       Toast.makeText(TimelineActivity.this,"name="+myProfile.getUsername(), Toast.LENGTH_SHORT).show();

    }

   
    private void setMyProfile() {
        client.getMyProfile(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("######################### USER success", response.toString());

                
                user = User.fromJSON(response);

//                    final Myprofile profile = new Myprofile();
//                    profile.setUsername(response.getString("name"));
//                    profile.setDisplayName("@" + response.getString("screen_name"));
//                    profile.setProfileUrl(response.getString("profile_image_url"));
//                    profile.setFollowersCount(response.getInt("followers_count"));
//                    profile.setTweetsCount(response.getInt("statuses_count"));
//                    profile.setFollowingCount(response.getInt("friends_count"));
//                    profile.setBannerUrl(response.getString("profile_banner_url"));
////                    Toast.makeText(TimelineActivity.this,"name 1 ="+profile.getUsername(), Toast.LENGTH_SHORT).show();
//                    myProfile = profile;
                    getSupportActionBar().setTitle(user.getUsername());
                    getSupportActionBar().setIcon(R.drawable.ic_twitter_icon);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                Log.d("########################### USER failure", responseString);
            }
        });


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
            Intent i = new Intent(this, Composetweet.class);
            i.putExtra(Composetweet.MYPROFILE, user);
            mAdapter.getItem(vpPager.getCurrentItem()).startActivityForResult(i, 1);
            //startActivityForResult(i, 1);
            return true;
        }
        if(id == R.id.profile_icon_item){
            Intent i = new Intent(this, MyprofileActivity.class);
            i.putExtra(MyprofileActivity.MYPROFILE, user);
            mAdapter.getItem(vpPager.getCurrentItem()).startActivityForResult(i, 2);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        vpPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}
