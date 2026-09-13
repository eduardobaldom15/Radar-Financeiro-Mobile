package com.example.radarfinanceiro.network;

import com.example.radarfinanceiro.models.LoginRequest;
import com.example.radarfinanceiro.models.LoginResponse;
import com.example.radarfinanceiro.models.PesquisadorRequest;

import com.example.radarfinanceiro.models.Projeto;
import java.util.List;
import retrofit2.http.GET;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("api/Auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/Pesquisadores")
    Call<Void> cadastrar(@Body PesquisadorRequest request);

    @GET("api/Pesquisadores/me/projetos")
    Call<List<Projeto>> getMeusProjetos();
}