package com.gioidev.assignment403.Webservice;

import com.gioidev.assignment403.Models.Collection;
import com.gioidev.assignment403.Models.Photo;
import com.gioidev.assignment403.Models.SearchResults;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("photos")
    Call<List<Photo>> getPhotos(@Query("page") Integer page, @Query("per_page") Integer perPage);

    @GET("photos")
    Call<List<Photo>> getPhoto(@Query("page") Integer page, @Query("per_page") Integer perPage,
                               @Query("order_by") String orderBy);
    @GET("collections/featured")
    Call<List<com.gioidev.assignment403.Models.Collection>> getCollections(@Query("page") Integer page, @Query("per_page") Integer perPage);

    @GET("collections/{id}")
    Call<Collection> getInformationOfCollection(@Path("id") int id);

    @GET("collections/{id}/photos")
    Call<List<Photo>> getPhotosOfCollection(@Path("id") int id,@Query("page") Integer page, @Query("per_page") Integer perPage);

    @GET("photos/{id}")
    Call<Photo> getPhoto(@Path("id") String id);

    @GET("photos/{id}")
    Call<List<Photo>> getPhotoImage(@Path("id") String id);

    @GET("search/photos")
    Call<SearchResults> searchPhotos(@Query("query") String query,
                                     @Query("page") Integer page,
                                     @Query("per_page") Integer perPage,
                                     @Query("orientation") String orientation);

    //get id photo
//    @GET("photos/{id}")
//    Call<Photo> getIDPhoto(@GET("id") String idphoto);
}
