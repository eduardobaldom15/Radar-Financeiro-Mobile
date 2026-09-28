package com.example.radarfinanceiro.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.content.Context;

import okhttp3.OkHttpClient;

public class RetrofitClient {

    private static final String BASE_URL =
            "http://10.0.2.2:5279/";

    private static Retrofit retrofit;

    public static Retrofit getInstance(Context context) {

        if (retrofit == null) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(
                            new AuthInterceptor(context)
                    )
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(
                            GsonConverterFactory.create()
                    )
                    .build();
        }

        return retrofit;
    }

    public static ApiService getApiService(Context context) {
        return getInstance(context).create(ApiService.class);
    }
}