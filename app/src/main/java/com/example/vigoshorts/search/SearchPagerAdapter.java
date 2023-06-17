package com.example.vigoshorts.search;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.vigoshorts.search.tabs.UserFragment;

public class SearchPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_TABS = 4;

    public SearchPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        // Create and return the appropriate fragment based on the tab position
        switch (position) {
            case 0:
                return new UserFragment();
            case 1:
               // return new HashtagFragment();
            case 2:
               // return new MusicFragment();
            case 3:
               // return new VideoFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Return the title for each tab
        switch (position) {
            case 0:
                return "User";
            case 1:
                return "Hashtag";
            case 2:
                return "Music";
            case 3:
                return "Videos";
            default:
                return null;
        }
    }
}
