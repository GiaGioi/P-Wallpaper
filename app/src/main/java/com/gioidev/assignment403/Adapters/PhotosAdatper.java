package com.gioidev.assignment403.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gioidev.assignment403.Activities.FullscreenPhotoActivity;
import com.gioidev.assignment403.Activities.InfoActivity;
import com.gioidev.assignment403.Models.Photo;
import com.gioidev.assignment403.R;
import com.gioidev.assignment403.Utils.SquareImage;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PhotosAdatper extends RecyclerView.Adapter<PhotosAdatper.ViewHolder> {
    private final String TAG = PhotosAdatper.class.getSimpleName();
    private List<Photo> photos;
    private Context context;

    public PhotosAdatper(Context context, List<Photo> photos){
        this.photos = photos;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private FrameLayout itemPhotoLayout;
        private SquareImage itemPhotoPhoto;
        private CircleImageView itemPhotoUserAvatar;
        private TextView itemPhotoUsername;

        FrameLayout frameLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            itemPhotoLayout = itemView.findViewById(R.id.item_photo_layout);
            itemPhotoPhoto = itemView.findViewById(R.id.item_photo_photo);
            itemPhotoUserAvatar = itemView.findViewById(R.id.item_photo_user_avatar);
            itemPhotoUsername = itemView.findViewById(R.id.item_photo_username);

            itemPhotoPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleOnClick();
                }
            });
        }
        public void handleOnClick(){
            int position = getAdapterPosition();
            String photoId = photos.get(position).getId();
            Intent intent = new Intent(context, FullscreenPhotoActivity.class);
            intent.putExtra("photoId", photoId);
            Log.e(TAG, "photoId:" + photoId);
            context.startActivity(intent);
        }
        public void info(){
            int position = getAdapterPosition();
            String photoId = photos.get(position).getId();
            Intent intent = new Intent(context, InfoActivity.class);
            Log.e(TAG, "photoIdInfo:" + photoId);
            String url = "https://api.unsplash.com/photos/"+photoId+"/?client_id=5aa734224267217d2efe6c46a7858d2ffe94672820f22934e25acb0f42c19afd";
            intent.putExtra("photoIdInfo", url);

        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Photo photo = photos.get(position);
        holder.itemPhotoUsername.setText(photo.getUser().getUsername());
        Glide
                .with(context)
                .load(photo.getUrl().getRegular())
                .placeholder(R.drawable.placeholder)
                .override(600, 600)
                .into(holder.itemPhotoPhoto);

        Glide
                .with(context)
                .load(photo.getUser().getProfileImage().getSmall())
                .into(holder.itemPhotoUserAvatar);

    }
    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }
}
