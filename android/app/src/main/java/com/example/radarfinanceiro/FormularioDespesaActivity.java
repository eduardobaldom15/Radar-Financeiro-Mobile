package com.example.radarfinanceiro;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.radarfinanceiro.models.DespesaRequest;
import com.example.radarfinanceiro.models.Despesa;
import com.example.radarfinanceiro.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormularioDespesaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_despesa);

        Intent intent = getIntent();

        int projetoId =
                intent.getIntExtra("projetoId", -1);

        EditText etCategoriaDespesa =
                findViewById(R.id.etCategoriaDespesa);

        EditText etTipoDespesa =
                findViewById(R.id.etTipoDespesa);

        EditText etNomeDespesa =
                findViewById(R.id.etNomeDespesa);

        EditText etDescricaoDespesa =
                findViewById(R.id.etDescricaoDespesa);

        EditText etValorUnitarioDespesa =
                findViewById(R.id.etValorUnitarioDespesa);

        EditText etQuantidadeDespesa =
                findViewById(R.id.etQuantidadeDespesa);

        EditText etValorOrcadoDespesa =
                findViewById(R.id.etValorOrcadoDespesa);

        Button btnCadastrarDespesa =
                findViewById(R.id.btnCadastrarDespesa);

        btnCadastrarDespesa.setOnClickListener(v -> {

            String categoria =
                    etCategoriaDespesa.getText().toString();

            String tipo =
                    etTipoDespesa.getText().toString();

            String nomeDespesa =
                    etNomeDespesa.getText().toString();

            String descricao =
                    etDescricaoDespesa.getText().toString();

            double valorUnitario =
                    Double.parseDouble(
                            etValorUnitarioDespesa
                                    .getText()
                                    .toString()
                    );

            double quantidade =
                    Double.parseDouble(
                            etQuantidadeDespesa
                                    .getText()
                                    .toString()
                    );

            double valorOrcado =
                    Double.parseDouble(
                            etValorOrcadoDespesa
                                    .getText()
                                    .toString()
                    );

            DespesaRequest request =
                    new DespesaRequest(
                            categoria,
                            tipo,
                            nomeDespesa,
                            descricao,
                            valorUnitario,
                            quantidade,
                            valorOrcado,
                            projetoId
                    );

            Call<Despesa> call =
                    RetrofitClient
                            .getApiService(
                                    FormularioDespesaActivity.this
                            )
                            .criarDespesa(request);

            call.enqueue(new Callback<Despesa>() {

                @Override
                public void onResponse(
                        Call<Despesa> call,
                        Response<Despesa> response) {

                    if (response.isSuccessful()) {

                        Toast.makeText(
                                FormularioDespesaActivity.this,
                                "Despesa cadastrada com sucesso!",
                                Toast.LENGTH_LONG
                        ).show();

                        finish();

                    } else {

                        Toast.makeText(
                                FormularioDespesaActivity.this,
                                "Erro ao cadastrar: "
                                        + response.code(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }

                @Override
                public void onFailure(
                        Call<Despesa> call,
                        Throwable t) {

                    Toast.makeText(
                            FormularioDespesaActivity.this,
                            "Erro: " + t.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            });
        });
    }
}