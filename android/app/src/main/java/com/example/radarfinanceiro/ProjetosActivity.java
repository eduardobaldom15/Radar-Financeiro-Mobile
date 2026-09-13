package com.example.radarfinanceiro;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.radarfinanceiro.models.Projeto;
import com.example.radarfinanceiro.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjetosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projetos);

        Call<List<Projeto>> call =
                RetrofitClient
                        .getApiService(ProjetosActivity.this)
                        .getMeusProjetos();

        call.enqueue(new Callback<List<Projeto>>() {

            @Override
            public void onResponse(
                    Call<List<Projeto>> call,
                    Response<List<Projeto>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<Projeto> projetos = response.body();

                    Toast.makeText(
                            ProjetosActivity.this,
                            "Projetos encontrados: " + projetos.size(),
                            Toast.LENGTH_LONG
                    ).show();

                } else {

                    Toast.makeText(
                            ProjetosActivity.this,
                            "Erro ao buscar projetos: " + response.code(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<List<Projeto>> call,
                    Throwable t) {

                Toast.makeText(
                        ProjetosActivity.this,
                        "Erro: " + t.getMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}