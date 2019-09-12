package com.gioidev.assignment403.Interface;

public interface ILoadMore {
    void onLoadMore();
//    public void setFabWallpaper(){
//        if(photoBitmap != null){
//            if(Functions.setWallpaper(FullscreenPhotoActivity.this, photoBitmap)){
//                Toast.makeText(this, "Đặt ảnh nền thành công", Toast.LENGTH_LONG).show();
//            }else{
//                Toast.makeText(this, "Đặt ảnh nền thất bại", Toast.LENGTH_LONG).show();
//            }
//        }
//        fab_menu.close(true);
//    }
//    public void setFabFavorite(){
//        if(realmController.isPhotoExist(photo.getId())){
//            realmController.deletePhoto(photo);
//            activityFullscreenPhotoFabFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_favorite));
//            Toast.makeText(this, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
//        }else{
//            realmController.savePhoto(photo);
//            activityFullscreenPhotoFabFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_favorited));
//            Toast.makeText(this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
//        }
//        fab_menu.close(true);
//    }
//private void getPhoto(String photo){
//    Intent intent = getIntent();
//    final String photoId = intent.getStringExtra("photoIdInfo");
//    Log.e(TAG, "photoIdInfo:" + photoId);
//    RequestQueue queue = Volley.newRequestQueue(InfoActivity.this);
//    StringRequest stringRequest = new StringRequest(Request.Method.GET, photoId
//            , new Response.Listener<String>() {
//        @Override
//        public void onResponse(String response) {
//            Log.e(TAG, "onResponse: " + response );
//            try {
//                JSONObject jsonObject  = new JSONObject(response);
//
//                JSONObject jsonObjectCity = jsonObject.getJSONObject("user");
//                JSONArray jsonArray = jsonObject.getJSONArray("tags");
//
//
//                String name = jsonObjectCity.getString("name");
//                String instagram_username = jsonObjectCity.getString("instagram_username");
//                String location = jsonObjectCity.getString("location");
//                String width = jsonObject.getString("width");
//                String height = jsonObject.getString("height");
//                String color = jsonObject.getString("color");
//                String views = jsonObject.getString("views");
//                String likes = jsonObject.getString("likes");
//                String updated_at = jsonObject.getString("updated_at");
//
//                tvName.setText(name);
//                tvIstagram.setText(instagram_username);
//                tvLive.setText(location);
//                tvImageWeight.setText(width);
//                tvImageHeight.setText(height);
//                tvColor.setText(color);
//                tvView.setText(views);
//                tvDateSubmitted.setText(updated_at);
//                tvLike.setText(likes);
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }, new Response.ErrorListener() {
//        @Override
//        public void onErrorResponse(VolleyError error) {
//
//        }
//    });
//    queue.add(stringRequest);
//}
}

