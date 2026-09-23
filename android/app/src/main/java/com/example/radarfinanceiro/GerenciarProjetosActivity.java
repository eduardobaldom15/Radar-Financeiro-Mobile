package com.example.radarfinanceiro;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

import com.example.radarfinanceiro.models.Projeto;
import com.example.radarfinanceiro.network.ApiService;
import com.example.radarfinanceiro.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GerenciarProjetosActivity extends AppCompatActivity {

    private LinearLayout containerProjetos;
    private Button btnCriarProjeto;
    private Button btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gerenciar_projetos);

        containerProjetos = findViewById(R.id.containerProjetos);

        btnCriarProjeto =
                findViewById(R.id.btnCriarProjeto);

        btnCriarProjeto.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            GerenciarProjetosActivity.this,
                            FormularioProjetoActivity.class
                    );

            startActivity(intent);
        });
        btnVoltar =
                findViewById(R.id.btnVoltar);

        btnVoltar.setOnClickListener(v -> {
            finish();
        });
    }

    private void carregarProjetos() {

        ApiService apiService =
                RetrofitClient.getApiService(
                        GerenciarProjetosActivity.this
                );

        apiService.getMeusProjetos().enqueue(
                new Callback<List<Projeto>>() {

                    @Override
                    public void onResponse(
                            Call<List<Projeto>> call,
                            Response<List<Projeto>> response) {

                        if (response.isSuccessful() && response.body() != null) {

                            List<Projeto> projetos = response.body();

                            containerProjetos.removeAllViews();

                            for (Projeto projeto : projetos) {

                                LinearLayout blocoProjeto =
                                        (LinearLayout) LayoutInflater.from(
                                                GerenciarProjetosActivity.this
                                        ).inflate(
                                                R.layout.item_projeto,
                                                containerProjetos,
                                                false
                                        );

                                TextView tvNomeProjeto =
                                        blocoProjeto.findViewById(
                                                R.id.tvNomeProjeto
                                        );

                                Button btnEditar =
                                        blocoProjeto.findViewById(
                                                R.id.btnEditar
                                        );

                                Button btnExcluir =
                                        blocoProjeto.findViewById(
                                                R.id.btnExcluir
                                        );

                                Button btnFinancas =
                                        blocoProjeto.findViewById(
                                                R.id.btnFinancas
                                        );

                                TextView tvDescricaoProjeto =
                                        blocoProjeto.findViewById(
                                                R.id.tvDescricaoProjeto
                                        );

                                TextView tvProgramaProjeto =
                                        blocoProjeto.findViewById(
                                                R.id.tvProgramaProjeto
                                        );

                                tvNomeProjeto.setText(
                                        projeto.getNome()
                                );

                                tvDescricaoProjeto.setText(
                                        projeto.getDescricao()
                                );

                                tvProgramaProjeto.setText(
                                        "Programa: " + projeto.getPrograma()
                                );

                                btnEditar.setOnClickListener(v -> {

                                    Intent intent =
                                            new Intent(
                                                    GerenciarProjetosActivity.this,
                                                    FormularioProjetoActivity.class
                                            );

                                    intent.putExtra("projetoId", projeto.getId());
                                    intent.putExtra("nome", projeto.getNome());
                                    intent.putExtra("descricao", projeto.getDescricao());
                                    intent.putExtra("programa", projeto.getPrograma());
                                    intent.putExtra("dataInicio", projeto.getDataInicio());
                                    intent.putExtra("dataFim", projeto.getDataFim());

                                    startActivity(intent);
                                });

                                btnFinancas.setOnClickListener(v -> {

                                    Intent intent =
                                            new Intent(
                                                    GerenciarProjetosActivity.this,
                                                    EditarFinancasActivity.class
                                            );

                                    intent.putExtra("projetoId", projeto.getId());
                                    intent.putExtra("nomeProjeto", projeto.getNome());

                                    startActivity(intent);
                                });

                                btnExcluir.setOnClickListener(v -> {

                                    new androidx.appcompat.app.AlertDialog.Builder(
                                            GerenciarProjetosActivity.this
                                    )
                                            .setTitle("Excluir projeto")
                                            .setMessage("Deseja realmente excluir este projeto?")
                                            .setPositiveButton("Excluir", (dialog, which) -> {

                                                ApiService apiService =
                                                        RetrofitClient.getApiService(
                                                                GerenciarProjetosActivity.this
                                                        );

                                                apiService.excluirProjeto(
                                                        projeto.getId()
                                                ).enqueue(
                                                        new Callback<Void>() {

                                                            @Override
                                                            public void onResponse(
                                                                    Call<Void> call,
                                                                    Response<Void> response) {

                                                                if (response.isSuccessful()) {

                                                                    Toast.makeText(
                                                                            GerenciarProjetosActivity.this,
                                                                            "Projeto excluído com sucesso!",
                                                                            Toast.LENGTH_SHORT
                                                                    ).show();

                                                                    carregarProjetos();

                                                                } else {

                                                                    Toast.makeText(
                                                                            GerenciarProjetosActivity.this,
                                                                            "Erro ao excluir projeto.",
                                                                            Toast.LENGTH_SHORT
                                                                    ).show();
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(
                                                                    Call<Void> call,
                                                                    Throwable t) {

                                                                Toast.makeText(
                                                                        GerenciarProjetosActivity.this,
                                                                        "Erro de conexão.",
                                                                        Toast.LENGTH_SHORT
                                                                ).show();
                                                            }
                                                        }
                                                );
                                            })
                                            .setNegativeButton("Cancelar", null)
                                            .show();
                                });

                                containerProjetos.addView(
                                        blocoProjeto
                                );
                            }

                        } else {

                            Toast.makeText(
                                    GerenciarProjetosActivity.this,
                                    "Erro ao carregar projetos.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<List<Projeto>> call,
                            Throwable t) {

                        Toast.makeText(
                                GerenciarProjetosActivity.this,
                                "Erro de conexão.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }
    @Override
    protected void onResume() {
        super.onResume();

        carregarProjetos();
    }
}