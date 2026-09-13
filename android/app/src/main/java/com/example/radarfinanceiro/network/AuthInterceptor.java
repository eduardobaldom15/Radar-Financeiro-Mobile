package com.example.radarfinanceiro.network;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        SharedPreferences preferences =
                context.getSharedPreferences(
                        "RadarFinanceiro",
                        Context.MODE_PRIVATE
                );

        String token = preferences.getString("token", null);

        Request request = chain.request()
                .newBuilder()
                .addHeader(
                        "Authorization",
                        "Bearer " + token
                )
                .build();

        return chain.proceed(request);
    }
}