package com.codepath.apps.mysimpletweets.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Myprofile;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class Composetweet extends ActionBarActivity {

    private TwitterClient client;
    EditText etComposeTweet;
    TextView tvUsername;
    TextView tvScreenName;
    ImageView ivProfileImg;
    ImageView ivBannerImg;
    
    static String MYPROFILE="Amanleen Puri";
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final int height = getResources().getDisplayMetrics().heightPixels;
        final int width = getResources().getDisplayMetrics().widthPixels;
        //getWindow().setLayout((int)(width*0.8), (int)(height*0.6));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_composetweet);

        Intent intent = getIntent();
        final User user = (User)intent.getSerializableExtra(MYPROFILE);


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_twitter_icon);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        client = TwitterApplication.getRestClient();//Singleton Client

        tvUsername = (TextView)findViewById(R.id.tvUsername);
        tvScreenName = (TextView)findViewById(R.id.tvScreenName);
        ivProfileImg = (ImageView)findViewById(R.id.ivProfileImg);
        ivBannerImg = (ImageView)findViewById(R.id.ivBannerImg);

        tvUsername.setText(user.getUsername());
        tvScreenName.setText(user.getScreenName());
        Picasso.with(getBaseContext()).load(user.getProfileImageUrl()).into(ivProfileImg);
        Picasso.with(getBaseContext()).load(user.getBannerUrl()).into(ivBannerImg);
        //setMyProfile();

        etComposeTweet = (EditText)findViewById(R.id.etComposeTweet);

    }

    private void setMyProfile() {
        client.getMyProfile(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("USER",response.toString());
                Myprofile profile = new Myprofile();
                try {
                    profile.setUsername(response.getString("name"));
                    profile.setDisplayName("@"+response.getString("screen_name"));
                    profile.setProfileUrl(response.getString("profile_image_url"));
                    tvUsername.setText(profile.getUsername());
                    tvScreenName.setText(profile.getDisplayName());
                    Picasso.with(getBaseContext()).load(profile.getProfileUrl()).into(ivProfileImg);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("USER", responseString);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_composetweet, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_tweet) {

            //submit comment to twitter
            client.postMystatus(new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("POST",response.toString());
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("POST", responseString);
                }
            }, etComposeTweet.getText().toString());
            //return to updated timeline activity
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
