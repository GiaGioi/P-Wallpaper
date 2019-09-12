package com.gioidev.assignment403.Activities;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.gioidev.assignment403.Models.Photo;
import com.gioidev.assignment403.R;
import com.gioidev.assignment403.Utils.Functions;
import com.gioidev.assignment403.Utils.RealmController;
import com.gioidev.assignment403.Webservice.ApiInterface;
import com.gioidev.assignment403.Webservice.ServiceGenerator;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.ByteArrayOutputStream;
import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FullscreenPhotoActivity extends AppCompatActivity {
    private final String TAG = FullscreenPhotoActivity.class.getSimpleName();
    private ImageView fullscreen_photo_photo;
    private CircleImageView userAvatar;
    private TextView username;
    private FloatingActionMenu fab_menu;
    private FloatingActionButton activityFullscreenPhotoFabFavorite;
    private FloatingActionButton activityFullscreenPhotoFabSetWallpaper,shareImage;
    private FloatingActionButton downloadImage;
    private LinearLayout click;
    DownloadManager downloadManager;


    private Bitmap photoBitmap;
    private RealmController realmController;
    private Photo photo = new Photo();
    String photoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_photo);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Get Information of Photo

        Intent intent = getIntent();
        photoId = intent.getStringExtra("photoId");
        Log.e(TAG, "PhotoId" + photoId );
        getPhoto(photoId);

        shareImage = findViewById(R.id.shareImage);
        click = findViewById(R.id.click);
        fullscreen_photo_photo = findViewById(R.id.fullscreen_photo_photo);
        userAvatar = findViewById(R.id.activity_fullscreen_photo_user_avatar);
        username = findViewById(R.id.activity_fullscreen_photo_username);
        downloadImage = findViewById(R.id.downloadImage);
        fab_menu = findViewById(R.id.fab_menu);
        activityFullscreenPhotoFabFavorite = findViewById(R.id.activity_fullscreen_photo_fab_favorite);
        activityFullscreenPhotoFabSetWallpaper = findViewById(R.id.activity_fullscreen_photo_fab_set_wallpaper);

        realmController = new RealmController();
        if(realmController.isPhotoExist(photoId)){
            activityFullscreenPhotoFabFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_favorited));
        }

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FullscreenPhotoActivity.this, InfoActivity.class);
                String url = "https://api.unsplash.com/photos/"+photoId+"/?client_id=5aa734224267217d2efe6c46a7858d2ffe94672820f22934e25acb0f42c19afd";
                Log.e(TAG, "PhotoIdInfo:" + url);
                intent.putExtra("photoIdInfo", url);
                startActivity(intent);
            }
        });
        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri bmpUri = getImageUri(FullscreenPhotoActivity.this,photoBitmap);

                    // Construct a ShareIntent with link to image
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                    shareIntent.setType("image/*");
                    // Launch sharing dialog for image
                    Log.e(TAG, "shareImage: "+bmpUri );
                    startActivity(Intent.createChooser(shareIntent, "Share Image"));

            }
        });
        activityFullscreenPhotoFabSetWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFabWallpaper();
            }
        });
        activityFullscreenPhotoFabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFabFavorite();
            }
        });
        downloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImage();
            }
        });

    }

    private void getPhoto(final String photoId){

        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<Photo> call = apiInterface.getPhoto(photoId);
        call.enqueue(new Callback<Photo>() {
            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                if(response.isSuccessful()){
                    photo = response.body();
                    Log.e(TAG, "onResponse: " + photo );
                    updateUI(photo);
                }
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {
                Toast.makeText(FullscreenPhotoActivity.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateUI(final Photo photo){
        try{
            username.setText(photo.getUser().getUsername());
            Glide.with(FullscreenPhotoActivity.this)
                    .load(photo.getUser().getProfileImage().getSmall())
                    .into(userAvatar);

            Glide
                    .with(FullscreenPhotoActivity.this)
                    .asBitmap()
                    .load(photo.getUrl().getRegular())
                    .centerCrop()
                    .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            fullscreen_photo_photo.setImageBitmap(resource);
                            photoBitmap = resource;
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void saveImage() {
        ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class);
        Call<Photo> call = apiInterface.getPhoto(photoId);
        call.enqueue(new Callback<Photo>() {
            private static final String DIR_NAME = "Download";

            @Override
            public void onResponse(Call<Photo> call, Response<Photo> response) {
                if(response.isSuccessful()){
                    Photo photo = response.body();
                    if (photo != null) {
                        String url = photo.getUrl().getFull();
                        Log.e(TAG, "Haha: " + url);
//                        Uri uri = Uri.parse(url);
//                        DownloadManager.Request request = new DownloadManager.Request(uri);
//                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                        Long reference = downloadManager.enqueue(request);
//                        Picasso.get()
//                                .load(url)
//                                .into(new com.squareup.picasso.Target() {
//                                    @Override
//                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//
//                                        try {
//                                            File mydie = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/Camera");
//                                            if (!mydie.exists()) {
//                                                mydie.mkdirs();
//                                            }
//                                            FileOutputStream fileOutputStream = new FileOutputStream(new File(mydie, new Date().toString() + ".jpg"));
//                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
//                                            fileOutputStream.flush();
//                                            fileOutputStream.close();
//                                            Toast.makeText(getApplicationContext(), "Save", Toast.LENGTH_LONG).show();
//                                        } catch (FileNotFoundException e) {
//                                            e.printStackTrace();
//                                        } catch (IOException e2) {
//                                            e2.printStackTrace();
//                                        }
//
//                                    }
//
//                                    @Override
//                                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//
//                                    }
//
//                                    @Override
//                                    public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//                                    }
//                                });

                        //Download Image
                        String filename = "filename.jpg";
                        String downloadUrlOfImage = photo.getUrl().getFull();
                        File direct =
                                new File(Environment
                                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                        .getAbsolutePath() + "/" + DIR_NAME + "/");


                        if (!direct.exists()) {
                            direct.mkdir();
                            Log.d(TAG, "dir created for first time");
                        }

                        DownloadManager dm = (DownloadManager) getApplication().getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri downloadUri = Uri.parse(downloadUrlOfImage);
                        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                                .setAllowedOverRoaming(false)
                                .setTitle(filename)
                                .setMimeType("image/jpeg")
                                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                                        File.separator + DIR_NAME + File.separator + filename);

                        dm.enqueue(request);


                    }

                    Log.e(TAG, "onResponse: " + photo );
                }
            }

            @Override
            public void onFailure(Call<Photo> call, Throwable t) {
                Toast.makeText(FullscreenPhotoActivity.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//        String urll = ;
    }
    public void setFabWallpaper(){
        if(photoBitmap != null){
            if(Functions.setWallpaper(FullscreenPhotoActivity.this, photoBitmap)){
                Toast.makeText(this, "Đặt ảnh nền thành công", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Đặt ảnh nền thất bại", Toast.LENGTH_LONG).show();
            }
        }
        fab_menu.close(true);
    }
    public void setFabFavorite(){
        if(realmController.isPhotoExist(photo.getId())){
            realmController.deletePhoto(photo);
            activityFullscreenPhotoFabFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_favorite));
            Toast.makeText(this, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
        }else{
            realmController.savePhoto(photo);
            activityFullscreenPhotoFabFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_favorited));
            Toast.makeText(this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
        }
        fab_menu.close(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
