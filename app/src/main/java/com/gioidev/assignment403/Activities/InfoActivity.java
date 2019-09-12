package com.gioidev.assignment403.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gioidev.assignment403.Models.Photo;
import com.gioidev.assignment403.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InfoActivity extends AppCompatActivity {
    private final String TAG = InfoActivity.class.getSimpleName();
    private Photo photo = new Photo();
    private Toolbar toolbarAboutUs;
    private TextView tvName;
    private TextView tvIstagram;
    private TextView tvLive;
    private TextView tvImageWeight;
    private TextView tvImageHeight;
    private TextView tvColor;
    private TextView tvDateSubmitted;
    private TextView tvView,tvLike;



    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        toolbarAboutUs = findViewById(R.id.toolbarAboutUs);
        tvName = findViewById(R.id.tvName);
        tvIstagram = findViewById(R.id.tvIstagram);
        tvLive = findViewById(R.id.tvLive);
        tvImageWeight = findViewById(R.id.tvImageSizeWeight);
        tvImageHeight = findViewById(R.id.tvImageSizeHeight);
        tvColor = findViewById(R.id.tvColor);
        tvDateSubmitted = findViewById(R.id.tvDateSubmitted);
        tvView = findViewById(R.id.tvView);
        tvLike = findViewById(R.id.tvLike);
        setSupportActionBar(toolbarAboutUs);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Get Information of Photo
        Intent intent = getIntent();
        final String photoId = intent.getStringExtra("photoIdInfo");
        Log.e(TAG, "photoIdInfo:" + photoId);
        getPhoto(photoId);



    }
    private void getPhoto(String photo){
        Intent intent = getIntent();
        final String photoId = intent.getStringExtra("photoIdInfo");
        Log.e(TAG, "photoIdInfo:" + photoId);
        RequestQueue queue = Volley.newRequestQueue(InfoActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, photoId
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "onResponse: " + response );
                try {
                    JSONObject jsonObject  = new JSONObject(response);

                    JSONObject jsonObjectCity = jsonObject.getJSONObject("user");
                    JSONArray jsonArray = jsonObject.getJSONArray("tags");


                    String name = jsonObjectCity.getString("name");
                    String instagram_username = jsonObjectCity.getString("instagram_username");
                    String location = jsonObjectCity.getString("location");
                    String width = jsonObject.getString("width");
                    String height = jsonObject.getString("height");
                    String color = jsonObject.getString("color");
                    String views = jsonObject.getString("views");
                    String likes = jsonObject.getString("likes");
                    String updated_at = jsonObject.getString("updated_at");

                    tvName.setText(name);
                    tvIstagram.setText(instagram_username);
                    tvLive.setText(location);
                    tvImageWeight.setText(width);
                    tvImageHeight.setText(height);
                    tvColor.setText(color);
                    tvView.setText(views);
                    tvDateSubmitted.setText(updated_at);
                    tvLike.setText(likes);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }
}
