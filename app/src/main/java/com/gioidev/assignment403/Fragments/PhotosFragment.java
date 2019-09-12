package com.gioidev.assignment403.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gioidev.assignment403.Adapters.PhotosAdatper;
import com.gioidev.assignment403.Models.Photo;
import com.gioidev.assignment403.R;
import com.gioidev.assignment403.Webservice.ApiInterface;
import com.gioidev.assignment403.Webservice.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotosFragment extends Fragment {
    private final static String TAG = PhotosFragment.class.getSimpleName();
    private ProgressBar progressBar;
    private RecyclerView photosRecyclerView;

    private PhotosAdatper photosAdatper;
    private List<Photo> photos = new ArrayList<>();




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photos, container, false);
        progressBar = view.findViewById(R.id.fragment_photos_processbar);
        photosRecyclerView = view.findViewById(R.id.fragment_photos_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        photosRecyclerView.setLayoutManager(linearLayoutManager);

        photosAdatper = new PhotosAdatper(getActivity(), photos);
        photosRecyclerView.setAdapter(photosAdatper);
        showProgressBar(true);
        getPhotos();
        return view;
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
                    photosAdatper.notifyDataSetChanged();

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
            photosRecyclerView.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            photosRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
