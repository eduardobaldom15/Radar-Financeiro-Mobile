package com.example.radarfinanceiro;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.radarfinanceiro.models.PesquisadorRequest;
import com.example.radarfinanceiro.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        EditText nome = findViewById(R.id.etNome);
        EditText email = findViewById(R.id.etEmailCadastro);
        EditText senha = findViewById(R.id.etSenhaCadastro);
        EditText curso = findViewById(R.id.etCurso);
        EditText departamento = findViewById(R.id.etDepartamento);

        Button btnCadastrar = findViewById(R.id.btnCadastrar);
        Button btnEntrar = findViewById(R.id.btnEntrar);

        btnCadastrar.setOnClickListener(v -> {

            String nomeTexto = nome.getText().toString().trim();
            String emailTexto = email.getText().toString().trim();
            String senhaTexto = senha.getText().toString();
            String cursoTexto = curso.getText().toString().trim();
            String departamentoTexto = departamento.getText().toString().trim();

            PesquisadorRequest request =
                    new PesquisadorRequest(
                            nomeTexto,
                            emailTexto,
                            senhaTexto,
                            cursoTexto,
                            departamentoTexto
                    );

            Log.d("CADASTRO", "Enviando cadastro...");

            Call<Void> call =
                    RetrofitClient.getApiService(CadastroActivity.this).cadastrar(request);

            call.enqueue(new Callback<Void>() {

                @Override
                public void onResponse(
                        Call<Void> call,
                        Response<Void> response) {

                    if (response.isSuccessful()) {

                        Toast.makeText(
                                CadastroActivity.this,
                                "Cadastro realizado com sucesso!",
                                Toast.LENGTH_LONG
                        ).show();

                        Intent intent = new Intent(
                                CadastroActivity.this,
                                MainActivity.class
                        );

                        startActivity(intent);
                        finish();

                    } else {

                        Toast.makeText(
                                CadastroActivity.this,
                                "Erro ao cadastrar: "
                                        + response.code(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }

                @Override
                public void onFailure(
                        Call<Void> call,
                        Throwable t) {

                    t.printStackTrace();

                    Toast.makeText(
                            CadastroActivity.this,
                            "Erro: " + t.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            });
        });
        btnEntrar.setOnClickListener(v -> {
            Intent intent = new Intent(CadastroActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}