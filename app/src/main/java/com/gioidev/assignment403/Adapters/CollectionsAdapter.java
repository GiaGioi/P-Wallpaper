package com.gioidev.assignment403.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gioidev.assignment403.Models.Collection;
import com.gioidev.assignment403.R;
import com.gioidev.assignment403.Utils.SquareImage;

import java.util.List;

public class CollectionsAdapter extends BaseAdapter {

    private List<Collection> collections;
    private Context context;
    public CollectionsAdapter(Context context, List<Collection> collections){
        this.collections = collections;
        this.context = context;
    }

    @Override
    public int getCount() {
        return collections.size();
    }

    @Override
    public Object getItem(int i) {
        return collections.get(i);
    }

    @Override
    public long getItemId(int i) {
        return collections.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_collection, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        Collection collection = (Collection) collections.get(i);

        if(collection.getTitle() != null){
            Log.d("Title", collection.getTitle());
            holder.itemCollectionTitle.setText(collection.getTitle());

        }
        holder.itemCollectionTotalPhotos.setText(String.valueOf(collection.getTotalPhotos()) + " photos");
        Glide
                .with(context)
                .load(collection.getCoverPhoto().getUrl().getRegular())
                .into(holder.itemCollectionPhoto);
        return view;

    }
    static class ViewHolder{
        private SquareImage itemCollectionPhoto;
        private TextView itemCollectionTitle;
        private TextView itemCollectionTotalPhotos;

        public ViewHolder(View view) {
            itemCollectionPhoto = view.findViewById(R.id.item_collection_photo);
            itemCollectionTitle = view.findViewById(R.id.item_collection_title);
            itemCollectionTotalPhotos = view.findViewById(R.id.item_collection_total_photos);


        }
    }
}
