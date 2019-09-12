package com.gioidev.assignment403.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gioidev.assignment403.Adapters.PhotosAdatper;
import com.gioidev.assignment403.Models.Photo;
import com.gioidev.assignment403.Models.SearchResults;
import com.gioidev.assignment403.R;
import com.gioidev.assignment403.Unsplash;
import com.gioidev.assignment403.Webservice.ApiInterface;
import com.gioidev.assignment403.Webservice.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private final static String TAG = SearchActivity.class.getSimpleName();
    private Toolbar toolbarSearch;
    private EditText svSearch;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    private final String CLIENT_ID = "5aa734224267217d2efe6c46a7858d2ffe94672820f22934e25acb0f42c19afd";
    private Unsplash unsplash;

    private PhotosAdatper adatper;
    private List<Photo> photos = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        unsplash = new Unsplash(CLIENT_ID);

        svSearch = findViewById(R.id.svSearch);
        progressBar = findViewById(R.id.searchView);

        recyclerView = findViewById(R.id.fragment_photos_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adatper = new PhotosAdatper(SearchActivity.this,photos);
        recyclerView.setAdapter(adatper);

        getPhotos();
        svSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = svSearch.getText().toString();

                unsplash.searchPhotos(query, new Unsplash.OnSearchCompleteListener() {
                    @Override
                    public void onComplete(SearchResults results) {
                        Log.e("Photos", "Total Results Found " + results.getTotal());
                        List<Photo> photos = results.getResults();
                        adatper.setPhotos(photos);

                    }
                    @Override
                    public void onError(String error) {
                        Log.d("Unsplash", error);
                    }
                });
            }
        });

    }
    private void getPhotos(){
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<List<Photo>> call = apiInterface.getPhotos(1,500);
        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "Loading successfully, size: " + response.body().size());
                    for(Photo photo: response.body()){
                        photos.add(photo);
                        Log.e(TAG, photo.getUrl().getFull());

                    }
                    adatper.notifyDataSetChanged();

                }else{
                    Log.e(TAG, "Fail" + response.message());
                }
                showProgressBar(false);
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Log.d(TAG, "Fail: " + t.getMessage());
                showProgressBar(false);
            }
        });
    }
    private void showProgressBar(boolean isShow){
        if(isShow){
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SearchActivity.this,MainActivity.class));
    }
}
