package com.example.radarfinanceiro.network;

import com.example.radarfinanceiro.models.LoginRequest;
import com.example.radarfinanceiro.models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("api/Auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}