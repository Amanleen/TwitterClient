package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by aman on 2/20/15.
 */

//Taking tweet objects & turning them into views displayed in the list
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {
    String twitterFormat;
    SimpleDateFormat sf;
    public TweetsArrayAdapter(Context context, List<Tweet> objects) {

        super(context, 0, objects);
        twitterFormat="EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        // Important note. Only ENGLISH Locale works.
        sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);
    }

    //override and setup custom template

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet,parent, false);
        }
        ImageView ivProileImage = (ImageView)convertView.findViewById(R.id.ivProfileImg);
        TextView tvUsername = (TextView)convertView.findViewById(R.id.tvUsername);
        TextView tvBody = (TextView)convertView.findViewById(R.id.tvBody);
        TextView tvRetweetCount = (TextView)convertView.findViewById(R.id.tvRetweetCount);
        TextView tvFavCount = (TextView)convertView.findViewById(R.id.tvFavCount);
        TextView tvCreatedtime = (TextView)convertView.findViewById(R.id.tvCreatedtime);
        TextView tvDisplayname = (TextView)convertView.findViewById(R.id.tvDisplayname);

        tvUsername.setText(tweet.getUser().getUsername());
        tvBody.setText(tweet.getBody());
        ivProileImage.setImageResource(android.R.color.transparent);//erase existing image recycle view
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProileImage);
        tvRetweetCount.setText("" + tweet.getRetweetCount());
        tvFavCount.setText(""+tweet.getFavCount());
        tvDisplayname.setText("@"+tweet.getScreen_name());

//        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZZZ");
        //Sun Feb 22 01:47:26 +0000 2015
//        format.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {
            Date date = sf.parse(tweet.getCreatedAt());
            CharSequence createdTime = DateUtils.getRelativeTimeSpanString(
                    date.getTime(),
                    Calendar.getInstance().getTimeInMillis(),
                    DateUtils.SECOND_IN_MILLIS,
                    DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_ABBREV_TIME | DateUtils.FORMAT_ABBREV_RELATIVE);
            tvCreatedtime.setText(createdTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // apply ViewHolder pattern
        return convertView;

    }
}
