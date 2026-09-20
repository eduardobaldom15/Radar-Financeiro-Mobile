package com.example.radarfinanceiro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.radarfinanceiro.models.Despesa;
import com.example.radarfinanceiro.models.Projeto;
import com.example.radarfinanceiro.models.Receita;
import com.example.radarfinanceiro.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjetosActivity extends AppCompatActivity {
    private Button btnGerenciarProjetos;
    private double receitaTotal = 0;
    private double despesaOrcada = 0;
    private double despesaRealizada = 0;
    private int chamadasPendentes = 0;
    private TextView tvQuantidadeProjetos;
    private TextView tvReceitaTotal;
    private TextView tvDespesaOrcada;
    private TextView tvDespesaRealizada;
    private ProgressBar barraReceita;
    private TextView tvPercentualUtilizado;
    private double saldoOrcamentario = 0;
    private TextView tvSaldoOrcamentario;
    private View marcadorSaldo;
    private FrameLayout graficoSaldo;
    private Spinner spinnerProjetos;
    private List<Projeto> projetos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projetos);

        tvQuantidadeProjetos =
                findViewById(R.id.tvQuantidadeProjetos);

        tvReceitaTotal =
                findViewById(R.id.tvReceitaTotal);

        tvDespesaOrcada =
                findViewById(R.id.tvDespesaOrcada);

        tvDespesaRealizada =
                findViewById(R.id.tvDespesaRealizada);

        barraReceita =
                findViewById(R.id.barraReceita);

        tvPercentualUtilizado =
                findViewById(R.id.tvPercentualUtilizado);

        tvSaldoOrcamentario =
                findViewById(R.id.tvSaldoOrcamentario);

        graficoSaldo =
                findViewById(R.id.graficoSaldo);

        marcadorSaldo =
                findViewById(R.id.marcadorSaldo);

        spinnerProjetos =
                findViewById(R.id.spinnerProjetos);

        btnGerenciarProjetos =
                findViewById(R.id.btnGerenciarProjetos);

        btnGerenciarProjetos.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            ProjetosActivity.this,
                            GerenciarProjetosActivity.class
                    );

            startActivity(intent);
        });

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

                    projetos = response.body();

                    List<String> nomesProjetos = new ArrayList<>();


                    nomesProjetos.add("Todos os projetos");

                    for (Projeto projeto : projetos) {
                        nomesProjetos.add(projeto.getNome());
                    }

                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<>(
                                    ProjetosActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    nomesProjetos
                            );

                    adapter.setDropDownViewResource(
                            android.R.layout.simple_spinner_dropdown_item
                    );

                    spinnerProjetos.setAdapter(adapter);

                    spinnerProjetos.setOnItemSelectedListener(
                            new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(
                                        AdapterView<?> parent,
                                        View view,
                                        int position,
                                        long id) {

                                    if (position == 0) {

                                        carregarProjetosFinanceiros(projetos);

                                    } else {

                                        Projeto projetoSelecionado =
                                                projetos.get(position - 1);

                                        List<Projeto> projetoSelecionadoLista =
                                                new ArrayList<>();

                                        projetoSelecionadoLista.add(projetoSelecionado);

                                        carregarProjetosFinanceiros(
                                                projetoSelecionadoLista
                                        );
                                    }
                                }

                                @Override
                                public void onNothingSelected(
                                        AdapterView<?> parent) {
                                }
                            }
                    );

                    Toast.makeText(
                            ProjetosActivity.this,
                            "Projetos encontrados: " + projetos.size(),
                            Toast.LENGTH_LONG
                    ).show();

                    tvQuantidadeProjetos.setText(String.valueOf(projetos.size()));

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

    private void buscarReceitas(int projetoId) {

        Call<List<Receita>> call =
                RetrofitClient
                        .getApiService(ProjetosActivity.this)
                        .getReceitasPorProjeto(projetoId);

        call.enqueue(new Callback<List<Receita>>() {

            @Override
            public void onResponse(
                    Call<List<Receita>> call,
                    Response<List<Receita>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<Receita> receitas = response.body();

                    for (Receita receita : receitas) {
                        receitaTotal += receita.getValor();
                    }
                }

                chamadasPendentes--;

                verificarFinalizacao();
            }

            @Override
            public void onFailure(
                    Call<List<Receita>> call,
                    Throwable t) {

                chamadasPendentes--;

                verificarFinalizacao();
            }
        });
    }

    private void buscarDespesas(int projetoId) {

        Call<List<Despesa>> call =
                RetrofitClient
                        .getApiService(ProjetosActivity.this)
                        .getDespesasPorProjeto(projetoId);

        call.enqueue(new Callback<List<Despesa>>() {

            @Override
            public void onResponse(
                    Call<List<Despesa>> call,
                    Response<List<Despesa>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<Despesa> despesas = response.body();

                    for (Despesa despesa : despesas) {

                        despesaOrcada += despesa.getValorOrcado();

                        despesaRealizada += despesa.getValorRealizado();
                    }
                }

                chamadasPendentes--;

                verificarFinalizacao();
            }

            @Override
            public void onFailure(
                    Call<List<Despesa>> call,
                    Throwable t) {

                chamadasPendentes--;

                verificarFinalizacao();
            }
        });

    }

    private void verificarFinalizacao() {

        if (chamadasPendentes == 0) {

            NumberFormat formatoMoeda =
                    NumberFormat.getCurrencyInstance(
                            new Locale("pt", "BR")
                    );

            tvReceitaTotal.setText(
                    formatoMoeda.format(receitaTotal)
            );

            tvDespesaOrcada.setText(
                    formatoMoeda.format(despesaOrcada)
            );

            tvDespesaRealizada.setText(
                    formatoMoeda.format(despesaRealizada)
            );

            double percentualUtilizado = 0;

            if (receitaTotal > 0) {
                percentualUtilizado =
                        (despesaRealizada / receitaTotal) * 100;
            }
            int percentualInteiro =
                    (int) percentualUtilizado;

            barraReceita.setProgress(percentualInteiro);

            tvPercentualUtilizado.setText(
                    percentualInteiro + "% da receita utilizada"
            );

            saldoOrcamentario =
                    despesaOrcada - despesaRealizada;

            tvSaldoOrcamentario.setText(
                    formatoMoeda.format(saldoOrcamentario)
            );

            graficoSaldo.post(() -> {

                float largura = graficoSaldo.getWidth();

                float deslocamentoMaximo =
                        (largura / 2f) - 8f;

                float posicao = 0;

                if (despesaOrcada > 0) {

                    posicao = (float) (saldoOrcamentario / despesaOrcada);

                    if (posicao > 1) {
                        posicao = 1;
                    }

                    if (posicao < -1) {
                        posicao = -1;
                    }
                }

                marcadorSaldo.setTranslationX(
                        posicao * deslocamentoMaximo
                );
            });
        }
    }
    private void carregarProjetosFinanceiros(List<Projeto> projetosSelecionados) {
        receitaTotal = 0;
        despesaOrcada = 0;
        despesaRealizada = 0;

    chamadasPendentes =
            projetosSelecionados.size() * 2;

    for (Projeto projeto : projetosSelecionados) {

            int projetoId = projeto.getId();

            buscarReceitas(projetoId);
            buscarDespesas(projetoId);
        }
    }
}