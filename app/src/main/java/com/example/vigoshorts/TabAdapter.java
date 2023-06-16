package com.example.vigoshorts;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {

    private int numTabs;

    public TabAdapter(FragmentManager fm, int numTabs) {
        super(fm);
        this.numTabs = numTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // TODO: Return the appropriate Fragment for each tab
        // For example, you can create separate fragments for User, Hashtag, Music, and Videos
        // and return the corresponding fragment based on the position

        return null;
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}