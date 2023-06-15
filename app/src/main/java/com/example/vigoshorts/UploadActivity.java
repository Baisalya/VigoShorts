package com.example.vigoshorts;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {

    private ImageView selectBtn;
    private EditText videoTitle,videoDescription;
    private Button uploadBtn;
    private String insertUrl="https://vivekwebsiteapi.000webhostapp.com/video_upload.php";
    private Uri uri;
    private String encodedVideo;
    private ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading Video..");
        progressDialog.setMessage("Please Wait..");
        progressDialog.setCancelable(false);

        selectBtn=findViewById(R.id.selectBtn);
        videoTitle=findViewById(R.id.videoTitle);
        videoDescription=findViewById(R.id.videoDescription);

        uploadBtn=findViewById(R.id.uploadBtn);

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dexter.withContext(UploadActivity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                Intent intent=new Intent();
                                intent.setType("video/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent,1234);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                                Toast.makeText(UploadActivity.this, "please give permission to read files", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();

                            }
                        }).check();
            }

        });



        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!videoTitle.getText().toString().isEmpty() && !videoDescription.getText().toString().isEmpty()){
                    if(!encodedVideo.isEmpty())
                    {
                        uploadData(encodedVideo);
                    }
                    else{
                        Toast.makeText(UploadActivity.this, "select video to upload", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    videoTitle.setError("please enter title");
                    videoDescription.setError("enter video description");
                }
            }

            private void uploadData(String encodedVideo) {
                progressDialog.show();
                StringRequest request=new StringRequest(Request.Method.POST, insertUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(UploadActivity.this, response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        startActivity(new Intent(UploadActivity.this, HomeActivity.class));
                        finish();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UploadActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String,String> map= new HashMap<>();
                        map.put("video",encodedVideo);
                        map.put("title",videoTitle.getText().toString());
                        map.put("description",videoDescription.getText().toString());

                        return map;
                    }
                };
                RequestQueue requestQueue= Volley.newRequestQueue(UploadActivity.this);
                requestQueue.add(request);

            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1234 && resultCode==RESULT_OK);{
            if(data!=null)
            {
                uri=data.getData();
                encodedVideo= android.util.Base64.encodeToString(UploadHelper.getFileDataFromDrawable(UploadActivity.this,uri), Base64.DEFAULT);

            }
        }
    }
}
