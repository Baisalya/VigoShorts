package com.example.vigoshorts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText user_name, full_name, e_mail, pass_word;
    private TextView alreadyHaveAccount;
    private ProgressBar progress;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        user_name = findViewById(R.id.username);
        full_name = findViewById(R.id.fullname);
        e_mail = findViewById(R.id.email);
        pass_word = findViewById(R.id.password);

        alreadyHaveAccount = findViewById(R.id.loginText);
        registerBtn = findViewById(R.id.buttonSignUp);
        progress=findViewById(R.id.progress);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String txtFullname, txtUsername, txtEmail, txtPassword;

                txtFullname = String.valueOf(full_name.getText());
                txtUsername = String.valueOf(user_name.getText());
                txtEmail = String.valueOf(e_mail.getText());
                txtPassword = String.valueOf(pass_word.getText());


                if (!full_name.equals("") && !user_name.equals("") && !e_mail.equals("") && !pass_word.equals("")) {

                    progress.setVisibility(View.VISIBLE);

                    //Start ProgressBar first (Set visibility VISIBLE)
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[4];
                            field[0] = "fullname";
                            field[1] = "email";
                            field[2] = "username";
                            field[3] = "password";
                            //Creating array for data
                            String[] data = new String[4];
                            data[0] = txtFullname;
                            data[1] = txtUsername;
                            data[2] = txtEmail;
                            data[3] = txtPassword;
                            PutData putData = new PutData("https://vivekwebsiteapi.000webhostapp.com/signup.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progress.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if(result.equals("Sign Up Success"))
                                    {
                                        Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(RegisterActivity.this, "Signup Failed", Toast.LENGTH_SHORT).show();
                                    }
                                    //End ProgressBar (Set visibility to GONE)
                                    Log.i("PutData", result);
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });

                }
            else {
                    Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
            }

        });

        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });


    }
}