package com.example.radarfinanceiro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.radarfinanceiro.models.Receita;
import com.example.radarfinanceiro.models.ReceitaRequest;
import com.example.radarfinanceiro.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormularioReceitaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_receita);

        Intent intent = getIntent();

        int projetoId =
                intent.getIntExtra("projetoId", -1);

        EditText etTipoReceita =
                findViewById(R.id.etTipoReceita);

        EditText etOrigemReceita =
                findViewById(R.id.etOrigemReceita);

        EditText etValorReceita =
                findViewById(R.id.etValorReceita);

        Button btnCadastrarReceita =
                findViewById(R.id.btnCadastrarReceita);

        btnCadastrarReceita.setOnClickListener(v -> {

            String tipo =
                    etTipoReceita.getText().toString();

            String origem =
                    etOrigemReceita.getText().toString();

            double valor =
                    Double.parseDouble(
                            etValorReceita
                                    .getText()
                                    .toString()
                    );

            ReceitaRequest request =
                    new ReceitaRequest(
                            tipo,
                            origem,
                            valor,
                            projetoId
                    );

            Call<Receita> call =
                    RetrofitClient
                            .getApiService(
                                    FormularioReceitaActivity.this
                            )
                            .criarReceita(request);

            call.enqueue(new Callback<Receita>() {

                @Override
                public void onResponse(
                        Call<Receita> call,
                        Response<Receita> response) {

                    if (response.isSuccessful()) {

                        Toast.makeText(
                                FormularioReceitaActivity.this,
                                "Receita cadastrada com sucesso!",
                                Toast.LENGTH_LONG
                        ).show();

                        finish();

                    } else {

                        Toast.makeText(
                                FormularioReceitaActivity.this,
                                "Erro ao cadastrar: "
                                        + response.code(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }

                @Override
                public void onFailure(
                        Call<Receita> call,
                        Throwable t) {

                    Toast.makeText(
                            FormularioReceitaActivity.this,
                            "Erro: " + t.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            });
        });
    }
}