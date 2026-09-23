package com.example.radarfinanceiro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Toast;

import com.example.radarfinanceiro.models.Receita;
import com.example.radarfinanceiro.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarFinancasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_financas);

        Intent intent = getIntent();

        int projetoId = intent.getIntExtra("projetoId", -1);
        String nomeProjeto = intent.getStringExtra("nomeProjeto");

        TextView tvNomeProjeto =
                findViewById(R.id.tvNomeProjeto);

        tvNomeProjeto.setText(nomeProjeto);

        Button btnAdicionarReceita =
                findViewById(R.id.btnAdicionarReceita);

        btnAdicionarReceita.setOnClickListener(v -> {

            Intent intentFormulario =
                    new Intent(
                            EditarFinancasActivity.this,
                            FormularioReceitaActivity.class
                    );

            intentFormulario.putExtra(
                    "projetoId",
                    projetoId
            );

            startActivity(intentFormulario);
        });

        tvNomeProjeto.setText(nomeProjeto);

        Call<List<Receita>> call =
                RetrofitClient
                        .getApiService(EditarFinancasActivity.this)
                        .getReceitasPorProjeto(projetoId);

        call.enqueue(new Callback<List<Receita>>() {

            @Override
            public void onResponse(
                    Call<List<Receita>> call,
                    Response<List<Receita>> response) {

                if (response.isSuccessful()
                        && response.body() != null) {

                    List<Receita> receitas = response.body();

                    LinearLayout containerReceitas =
                            findViewById(R.id.containerReceitas);

                    containerReceitas.removeAllViews();

                    for (Receita receita : receitas) {

                        LinearLayout itemReceita =
                                (LinearLayout) getLayoutInflater().inflate(
                                        R.layout.item_receita,
                                        containerReceitas,
                                        false
                                );

                        TextView tvOrigemReceita =
                                itemReceita.findViewById(
                                        R.id.tvOrigemReceita
                                );

                        TextView tvValorDataReceita =
                                itemReceita.findViewById(
                                        R.id.tvValorDataReceita
                                );

                        Button btnExcluirReceita =
                                itemReceita.findViewById(
                                        R.id.btnExcluirReceita
                                );

                        btnExcluirReceita.setOnClickListener(v -> {

                            Call<Void> callExcluir =
                                    RetrofitClient
                                            .getApiService(
                                                    EditarFinancasActivity.this
                                            )
                                            .excluirReceita(receita.getId());

                            callExcluir.enqueue(new Callback<Void>() {

                                @Override
                                public void onResponse(
                                        Call<Void> callExcluir,
                                        Response<Void> response) {

                                    if (response.isSuccessful()) {

                                        containerReceitas.removeView(itemReceita);

                                        Toast.makeText(
                                                EditarFinancasActivity.this,
                                                "Receita excluída com sucesso!",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                    } else {

                                        Toast.makeText(
                                                EditarFinancasActivity.this,
                                                "Erro ao excluir: " + response.code(),
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    }
                                }

                                @Override
                                public void onFailure(
                                        Call<Void> callExcluir,
                                        Throwable t) {

                                    Toast.makeText(
                                            EditarFinancasActivity.this,
                                            "Erro ao excluir receita",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            });
                        });

                        tvOrigemReceita.setText(
                                receita.getOrigem()
                        );

                        String data = receita.getDataEntrada();

                        if (data != null && data.length() >= 10) {
                            data = data.substring(0, 10);
                        }

                        String[] partes = data.split("-");

                        if (partes.length == 3) {
                            data = partes[2] + "/"
                                    + partes[1] + "/"
                                    + partes[0];
                        }

                        tvValorDataReceita.setText(
                                getString(
                                        R.string.receita_valor_data,
                                        receita.getValor(),
                                        data
                                )
                        );

                        containerReceitas.addView(itemReceita);
                    }
                }
            }

            @Override
            public void onFailure(
                    Call<List<Receita>> call,
                    Throwable t) {

                Toast.makeText(
                        EditarFinancasActivity.this,
                        "Erro ao carregar receitas",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}