package com.example.radarfinanceiro.network;

import com.example.radarfinanceiro.models.LoginRequest;
import com.example.radarfinanceiro.models.LoginResponse;
import com.example.radarfinanceiro.models.PesquisadorRequest;
import com.example.radarfinanceiro.models.ProjetoRequest;
import com.example.radarfinanceiro.models.Projeto;
import com.example.radarfinanceiro.models.Receita;
import com.example.radarfinanceiro.models.ReceitaRequest;
import com.example.radarfinanceiro.models.Despesa;
import com.example.radarfinanceiro.models.DespesaRequest;

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

    @GET("api/Receitas/projeto/{id}")
    Call<List<Receita>> getReceitasPorProjeto(@Path("id") int id);

    @POST("api/Receitas")
    Call<Receita> criarReceita(
            @Body ReceitaRequest receita);

    @DELETE("api/Receitas/{id}")
    Call<Void> excluirReceita(@Path("id") int id);

    @GET("api/Despesas/projeto/{id}")
    Call<List<Despesa>> getDespesasPorProjeto(@Path("id") int id);

    @POST("api/Despesas")
    Call<Despesa> criarDespesa(
            @Body DespesaRequest despesa);

    @DELETE("api/Despesas/{id}")
    Call<Void> excluirDespesa(@Path("id") int id);
}