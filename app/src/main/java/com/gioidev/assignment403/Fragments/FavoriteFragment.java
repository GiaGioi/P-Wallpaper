package com.gioidev.assignment403.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gioidev.assignment403.Adapters.PhotosAdatper;
import com.gioidev.assignment403.Models.Photo;
import com.gioidev.assignment403.R;
import com.gioidev.assignment403.Utils.RealmController;

import java.util.ArrayList;
import java.util.List;
public class FavoriteFragment extends Fragment {
    private TextView fragmentFavoriteNotification;



    RecyclerView fragmentFavoriteRecyclerview;
    TextView notification;

    private PhotosAdatper photosAdatper;
    private List<Photo> photos = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        fragmentFavoriteNotification = view.findViewById(R.id.fragment_favorite_notification);
        fragmentFavoriteRecyclerview = view.findViewById(R.id.fragment_favorite_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        fragmentFavoriteRecyclerview.setLayoutManager(linearLayoutManager);
        photosAdatper = new PhotosAdatper(getActivity(), photos);
        fragmentFavoriteRecyclerview.setAdapter(photosAdatper);
        Log.d("Favorite", "Favorite");
        getPhotos();
        return view;
    }

    private void getPhotos(){
        RealmController realmController = new RealmController();
        photos.addAll(realmController.getPhotos());
        if(photos.size() == 0){
            notification.setVisibility(View.VISIBLE);
            fragmentFavoriteRecyclerview.setVisibility(View.GONE);
        }else{
            photosAdatper.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
