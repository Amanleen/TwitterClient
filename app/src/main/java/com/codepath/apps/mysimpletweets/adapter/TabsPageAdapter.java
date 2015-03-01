package com.codepath.apps.mysimpletweets.adapter;

/**
 * Created by aman on 2/26/15.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.mysimpletweets.fragments.MentionsListFragment;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;

public class TabsPageAdapter extends FragmentPagerAdapter {
    TweetsListFragment tlf;
    MentionsListFragment mlf;
    public TabsPageAdapter(FragmentManager fm) {
        super(fm);
        tlf = new TweetsListFragment();
        mlf = new MentionsListFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return tlf;
            case 1:
                return mlf;
        }
        return null;
    }
}

