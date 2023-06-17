package com.example.vigoshorts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.vigoshorts.search.SearchActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {
    private ArrayList<VideoItems> videoItems = new ArrayList<>();
    private ImageView addNewVideo;
    private ViewPager2 viewPager2;
    boolean granted = false;

    private Uri uri;

    private ImageView disk;
    String apiUrl = "https://vivekwebsiteapi.000webhostapp.com/getData.php/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        ImageButton serachbtn=findViewById(R.id.serach_btn);
        ImageButton PROFILEBTN=findViewById(R.id.PROFILE_BTN);
        serachbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        PROFILEBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this, MyProfileActivity.class);
                startActivity(intent);

            }
        });
        addNewVideo = findViewById(R.id.imageView2);

        viewPager2 = findViewById(R.id.viewPager);

        addNewVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, UploadActivity.class));
                finish();
            }
        });

        if (permissionCheck()) {

            SharedPreferences preferences = getSharedPreferences("app", MODE_PRIVATE);
            String uri = preferences.getString("uri", "no");

            DocumentFile fileDoc = DocumentFile.fromTreeUri(HomeActivity.this, Uri.parse(uri));


            StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String fetchSuccess = jsonObject.getString("on_success");
                        JSONArray jsonArray = jsonObject.getJSONArray("response");

                        if (fetchSuccess.matches("1")) {
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject object = jsonArray.getJSONObject(a);

                                String id = object.getString("id");
                                String title = object.getString("title");
                                String description = object.getString("description");
                                String video = "https://vivekwebsiteapi.000webhostapp.com/videos/" + object.getString("video_name");

                                VideoItems Items = new VideoItems(id, title, description, video);
                                videoItems.add(Items);
                                viewPager2 = findViewById(R.id.viewPager);

                                VideoAdapter videoAdapter = new VideoAdapter(videoItems, HomeActivity.this, fileDoc, viewPager2);
                                viewPager2.setAdapter(videoAdapter);


                            }
                        } else {
                            Toast.makeText(HomeActivity.this, "Server Error!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }
        else {
            Toast.makeText(this, "Please Give Permission to access Storage!", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean permissionCheck() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            SharedPreferences preferences = getSharedPreferences("app", MODE_PRIVATE);
            String uri = preferences.getString("uri", "no");


            if (uri.matches("no")) {

                granted = false;
                openDirectory();
            } else {
                granted = true;
            }
        }

            else {
                Dexter.withContext(HomeActivity.this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                    granted = true;

                                } else {
                                    granted = false;
                                }

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                            }
                        });
            }
        return granted;
        }


    public void openDirectory() {

        String path = Environment.getExternalStorageDirectory() + "/DCIM";
        File file = new File(path);
        String startDir = null, secondDir, finalDirPath;

        if (file.exists()) {
            startDir = "DCIM";  // use %2F for "/"
        }

        StorageManager sm = (StorageManager) getSystemService(Context.STORAGE_SERVICE);

        Intent intent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            intent = sm.getPrimaryStorageVolume().createOpenDocumentTreeIntent();

            Uri uri = intent.getParcelableExtra("android.provider.extra.INITIAL_URI");

            String scheme = uri.toString();

            Log.d("TAG", "INITIAL_URI scheme: " + scheme);

            scheme = scheme.replace("/root/", "/document/");

            finalDirPath = scheme + "%3A" + startDir;

            uri = Uri.parse(finalDirPath);

            intent.putExtra("android.provider.extra.INITIAL_URI", uri);

            Log.d("TAG", "uri: " + uri.toString());

            try {
                startActivityForResult(intent, 1234);
            } catch (ActivityNotFoundException ignored) {

            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 1234 && resultCode == RESULT_OK) {

            Uri treeUri = data.getData();

            getContentResolver().takePersistableUriPermission(treeUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

            SharedPreferences preferences = getSharedPreferences("app", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("uri", String.valueOf(treeUri));
            editor.apply();

            DocumentFile fileDoc = DocumentFile.fromTreeUri(HomeActivity.this, treeUri);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, apiUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String fetchSuccess = jsonObject.getString("on_success");
                        JSONArray jsonArray = jsonObject.getJSONArray("response");

                        if (fetchSuccess.matches("1")) {
                            for (int a = 0; a < jsonArray.length(); a++) {
                                JSONObject object = jsonArray.getJSONObject(a);

                                String id = object.getString("id");
                                String title = object.getString("title");
                                String description = object.getString("description");
                                String video = "https://vivekwebsiteapi.000webhostapp.com/videos/" + object.getString("video_name");

                                VideoItems Items = new VideoItems(id, title, description, video);
                                videoItems.add(Items);
                                viewPager2 = findViewById(R.id.viewPager);

                                VideoAdapter videoAdapter = new VideoAdapter(videoItems, HomeActivity.this, fileDoc , viewPager2);
                                viewPager2.setAdapter(videoAdapter);


                            }
                        } else {
                            Toast.makeText(HomeActivity.this, "Server Error!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }
}
