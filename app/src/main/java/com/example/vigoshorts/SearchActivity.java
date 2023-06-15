package com.example.vigoshorts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

    private EditText searchEditText;
    private Button searchButton;
    private Button usersButton;
    private Button hashtagButton;
    private Button musicButton;
    private Button videosButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize views
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        usersButton = findViewById(R.id.usersButton);
        hashtagButton = findViewById(R.id.hashtagButton);
        musicButton = findViewById(R.id.musicButton);
        videosButton = findViewById(R.id.videosButton);

        // Search button click listener
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchEditText.getText().toString();
                // Perform search operation here
                Toast.makeText(SearchActivity.this, "Searching for: " + searchText, Toast.LENGTH_SHORT).show();
            }
        });

        // Navigation buttons click listeners
        usersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle users button click
                Toast.makeText(SearchActivity.this, "Users clicked", Toast.LENGTH_SHORT).show();
            }
        });

        hashtagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle hashtag button click
                Toast.makeText(SearchActivity.this, "Hashtags clicked", Toast.LENGTH_SHORT).show();
            }
        });

        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle music button click
                Toast.makeText(SearchActivity.this, "Music clicked", Toast.LENGTH_SHORT).show();
            }
        });

        videosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle videos button click
                Toast.makeText(SearchActivity.this, "Videos clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}