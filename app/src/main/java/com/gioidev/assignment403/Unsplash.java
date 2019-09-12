package com.gioidev.assignment403;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gioidev.assignment403.Models.SearchResults;
import com.gioidev.assignment403.Utils.Constants;
import com.gioidev.assignment403.Webservice.ApiInterface;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Unsplash {

    private static final String BASE_URL = "https://api.unsplash.com/";

    private ApiInterface photosApiService;
    private String TAG = "Unsplash";

    public Unsplash(String clientId) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("Authorization", "Client-ID " + Constants.APPLICATION_ID)
                                .build();
                        return chain.proceed(request);
                    }
                });
        OkHttpClient okHttpClient = okHttpClientBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        photosApiService = retrofit.create(ApiInterface.class);
    }



    public void searchPhotos(@NonNull String query, OnSearchCompleteListener listener) {
        searchPhotos(query, 1, 200, null, listener);
    }

    public void searchPhotos(@NonNull String query, @Nullable Integer page, @Nullable Integer perPage, @Nullable String orientation, OnSearchCompleteListener listener) {
        Call<SearchResults> call = photosApiService.searchPhotos(query, page, perPage, orientation);
        call.enqueue(getSearchResultsCallback(listener));
    }



    // CALLBACKS




    private Callback<SearchResults> getSearchResultsCallback(final OnSearchCompleteListener listener) {
        return new UnsplashCallback<SearchResults>() {
            @Override
            void onComplete(SearchResults response) {
                listener.onComplete(response);
            }

            @Override
            void onError(Call<SearchResults> call, String message) {
                listener.onError(message);
            }
        };
    }

    private abstract class UnsplashCallback<T> implements Callback<T> {

        abstract void onComplete(T response);

        abstract void onError(Call<T> call, String message);

        @Override
        public void onResponse(Call<T> call, Response<T> response) {
            int statusCode = response.code();
            Log.d(TAG, "Status Code = " + statusCode);
            if (statusCode == 200) {
                onComplete(response.body());
            } else if (statusCode >= 400) {
                onError(call, String.valueOf(statusCode));

                if (statusCode == 401) {
                    Log.d(TAG, "Unauthorized, Check your client Id");
                }
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            onError(call, t.getMessage());
        }
    }


    public interface OnSearchCompleteListener {
        void onComplete(SearchResults results);

        void onError(String error);
    }

}
