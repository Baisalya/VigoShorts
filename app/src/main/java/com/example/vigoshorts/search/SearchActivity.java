package com.example.vigoshorts.search;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.viewpager.widget.ViewPager;

import com.example.vigoshorts.R;
import com.example.vigoshorts.TabAdapter;
import com.example.vigoshorts.search.tabs.UserFragment;
import com.google.android.material.tabs.TabLayout;

public class SearchActivity extends AppCompatActivity {
    private SearchView searchView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SearchPagerAdapter pagerAdapter; // Declare pagerAdapter as a member variable
    private String currentQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.searchView);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.listView);

        // Set up the ViewPager with the corresponding adapter
        SearchPagerAdapter pagerAdapter = new SearchPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // Add a listener to the searchView to perform the search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentQuery = query;
                performSearch();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Not necessary to perform search while typing
                return false;
            }
        });
    }

    private void performSearch() {
        int selectedTabPosition = tabLayout.getSelectedTabPosition();

        switch (selectedTabPosition) {
            case 0:
           /*     SearchAPIManager.searchUser(currentQuery, new ResponseListener<List<User>>() {
                    @Override
                    public void onSuccess(List<User> response) {
                        UserFragment userFragment = (UserFragment) pagerAdapter.instantiateItem(viewPager, 0);
                        userFragment.updateUserList(response);
                        // Update the UI with the user search results
                        // Use the response data to populate the user list
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Display an error message to the user
                    }
                });*/
                break;
            case 1:
          /*      SearchAPIManager.searchHashtag(currentQuery, new ResponseListener<List<Hashtag>>() {
                    @Override
                    public void onSuccess(List<Hashtag> response) {
                        // Update the UI with the hashtag search results
                        // Use the response data to populate the hashtag list
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Display an error message to the user
                    }
                });*/
                break;
            case 2:
           /*     SearchAPIManager.searchMusic(currentQuery, new ResponseListener<List<Music>>() {
                    @Override
                    public void onSuccess(List<Music> response) {
                        // Update the UI with the music search results
                        // Use the response data to populate the music list
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Display an error message to the user
                    }
                });
                break;*/
            case 3:
           /*     SearchAPIManager.searchVideos(currentQuery, new ResponseListener<List<Video>>() {
                    @Override
                    public void onSuccess(List<Video> response) {
                        // Update the UI with the video search results
                        // Use the response data to populate the video list
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Display an error message to the user
                    }
                });*/
                break;
        }
    }
}

