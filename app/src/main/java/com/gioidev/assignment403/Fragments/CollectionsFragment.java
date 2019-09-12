package com.gioidev.assignment403.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gioidev.assignment403.Adapters.CollectionsAdapter;
import com.gioidev.assignment403.Models.Collection;
import com.gioidev.assignment403.R;
import com.gioidev.assignment403.Utils.Functions;
import com.gioidev.assignment403.Webservice.ApiInterface;
import com.gioidev.assignment403.Webservice.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionsFragment extends Fragment {
    private final String TAG = CollectionsFragment.class.getSimpleName();


    GridView fragmentCollectionsGridview;
    ProgressBar fragmentCollectionsProcessbar;

    private List<Collection> collections = new ArrayList<>();
    private CollectionsAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collections, container, false);
        fragmentCollectionsProcessbar = view.findViewById(R.id.fragment_collections_processbar);
        fragmentCollectionsGridview = view.findViewById(R.id.fragment_collections_gridview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        adapter = new CollectionsAdapter(getActivity(), collections);
        fragmentCollectionsGridview.setAdapter(adapter);

        fragmentCollectionsGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Collection collection = collections.get(i);
                Log.d("Collection id: ", collection.getId() + "");
                Bundle bundle = new Bundle();
                bundle.putInt("collectionId", collection.getId());
                CollectionFragment collectionFragment = new CollectionFragment();
                collectionFragment.setArguments(bundle);
                Functions.changeMainFragment(getActivity(), collectionFragment);
            }
        });

        getCollections();
        showProgressBar(true);
        return view;
    }
//    public void onItemClick(int position){
//        Collection collection = collections.get(position);
//        Log.d(TAG, collection.getId() + "");
//        Bundle bundle = new Bundle();
//        bundle.putInt("collectionId", collection.getId());
//        CollectionFragment collectionFragment = new CollectionFragment();
//        collectionFragment.setArguments(bundle);
//        Functions.changeMainFragmentWithBack(getActivity(), collectionFragment);
//    }

    private void getCollections(){
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<List<Collection>> call = apiInterface.getCollections(1,100);
        call.enqueue(new Callback<List<Collection>>() {
            @Override
            public void onResponse(Call<List<Collection>> call, Response<List<Collection>> response) {
                if(response.isSuccessful()){
                    for(Collection collection: response.body()){
                        collections.add(collection);
                    }
                    adapter.notifyDataSetChanged();
                    Log.e("Me no", "size " + collections.size());
                }else{
                    Log.d(TAG, "fail " + response.message());
                }
                showProgressBar(false);
            }

            @Override
            public void onFailure(Call<List<Collection>> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                showProgressBar(false);
            }
        });
    }
    private void showProgressBar(boolean isShow){
        if(isShow){
            fragmentCollectionsProcessbar.setVisibility(View.VISIBLE);
            fragmentCollectionsGridview.setVisibility(View.GONE);
        }else{
            fragmentCollectionsProcessbar.setVisibility(View.GONE);
            fragmentCollectionsGridview.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

