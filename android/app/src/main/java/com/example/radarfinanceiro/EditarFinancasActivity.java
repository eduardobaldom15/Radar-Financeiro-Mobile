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
import com.example.radarfinanceiro.models.Despesa;
import com.example.radarfinanceiro.network.RetrofitClient;

import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarFinancasActivity extends AppCompatActivity {

    private int projetoId;

    private TextView tvReceitaTotal;
    private TextView tvDespesaOrcada;
    private TextView tvDespesaRealizada;

    private String formatarMoeda(double valor) {
        NumberFormat formato =
                NumberFormat.getCurrencyInstance(
                        new Locale("pt", "BR")
                );

        return formato.format(valor);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_financas);

        Button btnVoltar =
                findViewById(R.id.btnVoltar);

        btnVoltar.setOnClickListener(v -> finish());

        Intent intent = getIntent();

        projetoId = intent.getIntExtra("projetoId", -1);
        String nomeProjeto = intent.getStringExtra("nomeProjeto");

        TextView tvNomeProjeto =
                findViewById(R.id.tvNomeProjeto);

        tvNomeProjeto.setText(nomeProjeto);

        tvReceitaTotal =
                findViewById(R.id.tvReceitaTotal);

        tvDespesaOrcada =
                findViewById(R.id.tvDespesaOrcada);

        tvDespesaRealizada =
                findViewById(R.id.tvDespesaRealizada);

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

        Button btnAdicionarDespesa =
                findViewById(R.id.btnAdicionarDespesa);

        btnAdicionarDespesa.setOnClickListener(v -> {

            Intent intentFormulario =
                    new Intent(
                            EditarFinancasActivity.this,
                            FormularioDespesaActivity.class
                    );

            intentFormulario.putExtra(
                    "projetoId",
                    projetoId
            );

            startActivity(intentFormulario);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        carregarDados();
    }

    private void carregarDados() {

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

                    double receitaTotal = 0;

                    for (Receita receita : receitas) {
                        receitaTotal += receita.getValor();
                    }

                    tvReceitaTotal.setText(
                            "Receita total: "
                                    + formatarMoeda(receitaTotal)
                    );

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

                                        carregarDados();

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
                                formatarMoeda(receita.getValor())
                                        + " - "
                                        + data
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

        Call<List<Despesa>> callDespesas =
                RetrofitClient
                        .getApiService(EditarFinancasActivity.this)
                        .getDespesasPorProjeto(projetoId);

        callDespesas.enqueue(new Callback<List<Despesa>>() {

            @Override
            public void onResponse(
                    Call<List<Despesa>> callDespesas,
                    Response<List<Despesa>> response) {

                if (response.isSuccessful()
                        && response.body() != null) {

                    List<Despesa> despesas = response.body();

                    double despesaOrcada = 0;
                    double despesaRealizada = 0;

                    for (Despesa despesa : despesas) {
                        despesaOrcada += despesa.getValorOrcado();
                        despesaRealizada += despesa.getValorRealizado();
                    }

                    tvDespesaOrcada.setText(
                            "Despesa orçada: "
                                    + formatarMoeda(despesaOrcada)
                    );

                    tvDespesaRealizada.setText(
                            "Despesa realizada: "
                                    + formatarMoeda(despesaRealizada)
                    );

                    LinearLayout containerDespesas =
                            findViewById(R.id.containerDespesas);

                    containerDespesas.removeAllViews();

                    for (Despesa despesa : despesas) {

                        LinearLayout itemDespesa =
                                (LinearLayout) getLayoutInflater().inflate(
                                        R.layout.item_despesa,
                                        containerDespesas,
                                        false
                                );

                        TextView tvNomeDespesa =
                                itemDespesa.findViewById(
                                        R.id.tvNomeDespesa
                                );

                        TextView tvValoresDespesa =
                                itemDespesa.findViewById(
                                        R.id.tvValoresDespesa
                                );

                        tvNomeDespesa.setText(
                                despesa.getNomeDespesa()
                        );

                        Button btnExcluirDespesa =
                                itemDespesa.findViewById(
                                        R.id.btnExcluirDespesa
                                );

                        tvValoresDespesa.setText(
                                "Orçado: "
                                        + formatarMoeda(despesa.getValorOrcado())
                                        + "\nRealizado: "
                                        + formatarMoeda(despesa.getValorRealizado())
                        );

                        containerDespesas.addView(itemDespesa);

                        btnExcluirDespesa.setOnClickListener(v -> {

                            Call<Void> callExcluir =
                                    RetrofitClient
                                            .getApiService(
                                                    EditarFinancasActivity.this
                                            )
                                            .excluirDespesa(despesa.getId());

                            callExcluir.enqueue(new Callback<Void>() {

                                @Override
                                public void onResponse(
                                        Call<Void> callExcluir,
                                        Response<Void> response) {

                                    if (response.isSuccessful()) {

                                        carregarDados();

                                        Toast.makeText(
                                                EditarFinancasActivity.this,
                                                "Despesa excluída com sucesso!",
                                                Toast.LENGTH_SHORT
                                        ).show();

                                    } else {

                                        Toast.makeText(
                                                EditarFinancasActivity.this,
                                                "Erro ao excluir: "
                                                        + response.code(),
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
                                            "Erro ao excluir despesa",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            });
                        });
                    }
                }
            }

            @Override
            public void onFailure(
                    Call<List<Despesa>> callDespesas,
                    Throwable t) {

                Toast.makeText(
                        EditarFinancasActivity.this,
                        "Erro ao carregar despesas",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}