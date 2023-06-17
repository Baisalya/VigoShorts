package com.example.vigoshorts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class EditProfileActivity extends Activity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView profileImageView;
    private EditText nameEditText;
    private EditText usernameEditText;
    private EditText bioEditText;
    private Spinner genderSpinner;
    private Button saveButton;

   // private User user; // User object to hold profile details

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profileImageView = findViewById(R.id.profileImageView);
        nameEditText = findViewById(R.id.nameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        bioEditText = findViewById(R.id.bioEditText);
        genderSpinner = findViewById(R.id.genderSpinner);
       // saveButton = findViewById(R.id.saveButton);

        // Populate the gender spinner
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.genders_array,
                android.R.layout.simple_spinner_item
        );
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        // Instantiate the User object
        //user = new User();

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle profile image click here
                // Open image picker or capture an image
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });
    }

    private void saveProfile() {
        // Get the user input from the fields
        String name = nameEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String bio = bioEditText.getText().toString();
        String gender = genderSpinner.getSelectedItem().toString();

        // Set the user details in the User object
     /*   user.setName(name);
        user.setUsername(username);
        user.setBio(bio);
        user.setGender(gender);*/

        // Save the profile information to a database or perform any desired actions

        // Return to the previous activity
        finish();
    }
}
