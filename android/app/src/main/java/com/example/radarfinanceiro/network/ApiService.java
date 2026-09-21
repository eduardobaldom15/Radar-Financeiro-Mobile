package com.example.radarfinanceiro.network;

import com.example.radarfinanceiro.models.LoginRequest;
import com.example.radarfinanceiro.models.LoginResponse;
import com.example.radarfinanceiro.models.PesquisadorRequest;
import com.example.radarfinanceiro.models.ProjetoRequest;

import com.example.radarfinanceiro.models.Projeto;
import java.util.List;
import retrofit2.http.GET;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;

import com.example.radarfinanceiro.models.Despesa;
import com.example.radarfinanceiro.models.Receita;

import retrofit2.http.Path;

public interface ApiService {

    @POST("api/Auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/Pesquisadores")
    Call<Void> cadastrar(@Body PesquisadorRequest request);

    @GET("api/Pesquisadores/me/projetos")
    Call<List<Projeto>> getMeusProjetos();

    @GET("api/Receitas/projeto/{id}")
    Call<List<Receita>> getReceitasPorProjeto(@Path("id") int id);

    @GET("api/Despesas/projeto/{id}")
    Call<List<Despesa>> getDespesasPorProjeto(@Path("id") int id);

    @POST("api/Projetos")
    Call<Projeto> criarProjeto(
            @Body ProjetoRequest projeto
    );
    @PUT("api/Projetos/{id}")
    Call<Projeto> atualizarProjeto(
            @Path("id") int id,
            @Body ProjetoRequest projeto
    );

    @DELETE("api/Projetos/{id}")
    Call<Void> excluirProjeto(
            @Path("id") int id
    );
}