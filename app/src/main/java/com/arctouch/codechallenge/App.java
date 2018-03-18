package com.arctouch.codechallenge;

import android.app.Application;

import com.arctouch.codechallenge.api.TmdbApi;

import io.reactivex.annotations.NonNull;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by Daniel Ulrik on 17/03/2018.
 */
public class App extends Application {
    private static TmdbApi api;

    @NonNull
    public static TmdbApi getApi() {
        if (api == null) {
            api = new Retrofit.Builder()
                    .baseUrl(TmdbApi.URL)
                    .client(new OkHttpClient.Builder().build())
                    .addConverterFactory(MoshiConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                    .create(TmdbApi.class);
        }
        return api;
    }
}
