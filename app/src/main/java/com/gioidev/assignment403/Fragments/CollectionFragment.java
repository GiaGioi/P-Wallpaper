package com.gioidev.assignment403.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gioidev.assignment403.Adapters.PhotosAdatper;
import com.gioidev.assignment403.Models.Collection;
import com.gioidev.assignment403.Models.Photo;
import com.gioidev.assignment403.R;
import com.gioidev.assignment403.Webservice.ApiInterface;
import com.gioidev.assignment403.Webservice.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionFragment extends Fragment {
    private final String TAG = CollectionFragment.class.getSimpleName();
    private ProgressBar progressBar;
    private RecyclerView photosRecyclerView;
    private LinearLayout fragmentCollectionInformation;
    private TextView title;
    private TextView fragmentCollectionDescription;
    private CircleImageView userAvatar;
    private TextView username;
    TextView description;


    private PhotosAdatper photosAdatper;
    private List<Photo> photos = new ArrayList<>();
    @Nullable
    @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);
        // RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        progressBar = view.findViewById(R.id.collection_processbar);
        photosRecyclerView = view.findViewById(R.id.collection_recyclerview);

        photosRecyclerView.setLayoutManager(linearLayoutManager);
        photosAdatper = new PhotosAdatper(getActivity(), photos);
        photosRecyclerView.setAdapter(photosAdatper);
        Bundle bundle = getArguments();
        int collectionId = bundle.getInt("collectionId");

        showProgressBar(true);
        getInformationOfCollection(collectionId);
        getPhotosOfCollection(collectionId);
        return view;
    }

    private void getInformationOfCollection(final int collectionId){
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<Collection> call = apiInterface.getInformationOfCollection(collectionId);
        call.enqueue(new Callback<Collection>() {
            @Override
            public void onResponse(Call<Collection> call, Response<Collection> response) {
                if(response.isSuccessful()){
                    Collection collection = response.body();
                    Log.e(TAG, "onResponse: " + collection);
//                    CharSequence cs = collection.getTitle();
//                    String s2 = cs.toString();
//                    title.setText(s2);
//
//                    CharSequence des = collection.getDescription();
//                    String des2 = des.toString();
//                    description.setText(des2);
//
//                    CharSequence user = collection.getUser().getUsername();
//                    String user2 = user.toString();
//                    username.setText(user2);
//
//                    Glide
//                            .with(getActivity())
//                            .load(collection.getUser().getProfileImage().getSmall())
//                            .into(userAvatar);

                }else{
                    Log.d(TAG, "Fail" + response.message());
                }
            }
            @Override
            public void onFailure(Call<Collection> call, Throwable t) {
                Log.d(TAG, "Fail: " + t.getMessage());
            }
        });
    }


    private void getPhotosOfCollection(int collectionId){
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<List<Photo>> call = apiInterface.getPhotosOfCollection(collectionId,1,200);
        call.enqueue(new Callback<List<Photo>>() {
            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "Loading successfully, size: " + response.body().size());
                    for(Photo photo: response.body()){
                        photos.add(photo);
                        Log.d(TAG, photo.getUrl().getFull());
                    }
                    photosAdatper.notifyDataSetChanged();

                }else{
                    Log.d(TAG, "Fail" + response.message());
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
