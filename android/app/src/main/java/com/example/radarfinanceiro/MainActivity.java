package com.example.radarfinanceiro;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.radarfinanceiro.models.LoginRequest;
import com.example.radarfinanceiro.models.LoginResponse;
import com.example.radarfinanceiro.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText email = findViewById(R.id.etEmail);
        EditText senha = findViewById(R.id.etSenha);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {

            String emailTexto = email.getText().toString().trim();
            String senhaTexto = senha.getText().toString();

            LoginRequest request =
                    new LoginRequest(emailTexto, senhaTexto);

            Call<LoginResponse> call =
                    RetrofitClient.getApiService().login(request);

            call.enqueue(new Callback<LoginResponse>() {

                @Override
                public void onResponse(
                        Call<LoginResponse> call,
                        Response<LoginResponse> response) {

                    if (response.isSuccessful()
                            && response.body() != null) {

                        String token = response.body().getToken();

                        Toast.makeText(
                                MainActivity.this,
                                "Login realizado com sucesso!",
                                Toast.LENGTH_LONG
                        ).show();

                    } else if (response.code() == 401) {

                        Toast.makeText(
                                MainActivity.this,
                                "Usuário ou senha inválidos",
                                Toast.LENGTH_LONG
                        ).show();

                    } else {

                        Toast.makeText(
                                MainActivity.this,
                                "Erro no servidor: "
                                        + response.code(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }

                @Override
                public void onFailure(
                        Call<LoginResponse> call,
                        Throwable t) {

                    t.printStackTrace();

                    Toast.makeText(
                            MainActivity.this,
                            "Erro: " + t.getClass().getSimpleName()
                                    + "\n" + t.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            });
        });
    }
}