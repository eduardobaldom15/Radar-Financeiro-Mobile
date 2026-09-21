package com.example.radarfinanceiro;
import com.example.radarfinanceiro.models.Projeto;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.app.DatePickerDialog;

import java.util.Calendar;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Toast;

import com.example.radarfinanceiro.models.ProjetoRequest;
import com.example.radarfinanceiro.network.ApiService;
import com.example.radarfinanceiro.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormularioProjetoActivity extends AppCompatActivity {

    private TextView tvTituloFormulario;
    private EditText edtNome;
    private EditText edtPrograma;
    private EditText edtDescricao;
    private EditText edtDataInicio;
    private EditText edtDataFim;
    private Button btnSalvarProjeto;
    private Button btnCancelar;
    private int projetoId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_formulario_projeto);

        tvTituloFormulario =
                findViewById(R.id.tvTituloFormulario);

        edtNome =
                findViewById(R.id.edtNome);

        edtPrograma =
                findViewById(R.id.edtPrograma);

        edtDescricao =
                findViewById(R.id.edtDescricao);

        edtDataInicio =
                findViewById(R.id.edtDataInicio);

        edtDataFim =
                findViewById(R.id.edtDataFim);

        btnSalvarProjeto =
                findViewById(R.id.btnSalvarProjeto);

        btnCancelar =
                findViewById(R.id.btnCancelar);

        btnCancelar.setOnClickListener(v -> {
            finish();
        });

        if (getIntent().hasExtra("projetoId")) {

            projetoId =
                    getIntent().getIntExtra(
                            "projetoId",
                            -1
                    );

            tvTituloFormulario.setText("Editar Projeto");

            edtNome.setText(
                    getIntent().getStringExtra("nome")
            );

            edtPrograma.setText(
                    getIntent().getStringExtra("programa")
            );

            edtDescricao.setText(
                    getIntent().getStringExtra("descricao")
            );

            edtDataInicio.setText(
                    converterDataParaExibicao(
                            getIntent().getStringExtra("dataInicio")
                    )
            );

            edtDataFim.setText(
                    converterDataParaExibicao(
                            getIntent().getStringExtra("dataFim")
                    )
            );
        }

        edtDataInicio.setOnClickListener(v -> {

            selecionarData(edtDataInicio);

        });

        edtDataFim.setOnClickListener(v -> {

            selecionarData(edtDataFim);

        });
        btnSalvarProjeto.setOnClickListener(v -> {

            String nome =
                    edtNome.getText().toString();

            String programa =
                    edtPrograma.getText().toString();

            String descricao =
                    edtDescricao.getText().toString();

            String dataInicio =
                    converterData(
                            edtDataInicio.getText().toString()
                    );

            String dataFim =
                    converterData(
                            edtDataFim.getText().toString()
                    );

            ProjetoRequest projetoRequest =
                    new ProjetoRequest(
                            nome,
                            descricao,
                            dataInicio,
                            dataFim,
                            programa
                    );

            ApiService apiService =
                    RetrofitClient.getApiService(
                            FormularioProjetoActivity.this
                    );

            Call<Projeto> chamada;

            if (projetoId == -1) {

                chamada =
                        apiService.criarProjeto(projetoRequest);

            } else {

                chamada =
                        apiService.atualizarProjeto(
                                projetoId,
                                projetoRequest
                        );
            }

            chamada.enqueue(
                    new Callback<Projeto>() {

                        @Override
                        public void onResponse(
                                Call<Projeto> call,
                                Response<Projeto> response) {

                            if (response.isSuccessful()) {

                                Toast.makeText(
                                        FormularioProjetoActivity.this,
                                        projetoId == -1
                                                ? "Projeto criado com sucesso!"
                                                : "Projeto atualizado com sucesso!",
                                        Toast.LENGTH_SHORT
                                ).show();

                                finish();

                            } else {

                                Toast.makeText(
                                        FormularioProjetoActivity.this,
                                        "Erro ao criar projeto.",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }

                        @Override
                        public void onFailure(
                                Call<Projeto> call,
                                Throwable t) {

                            Toast.makeText(
                                    FormularioProjetoActivity.this,
                                    "Erro de conexão.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
            );
        });
    }
    private void selecionarData(EditText campoData) {

        Calendar calendario =
                Calendar.getInstance();

        int ano =
                calendario.get(Calendar.YEAR);

        int mes =
                calendario.get(Calendar.MONTH);

        int dia =
                calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(
                        this,
                        (view, year, month, dayOfMonth) -> {

                            String data =
                                    String.format(
                                            Locale.getDefault(),
                                            "%02d/%02d/%04d",
                                            dayOfMonth,
                                            month + 1,
                                            year
                                    );

                            campoData.setText(data);
                        },
                        ano,
                        mes,
                        dia
                );

        datePickerDialog.show();
    }
    private String converterData(String data) {

        String[] partes =
                data.split("/");

        String dia = partes[0];
        String mes = partes[1];
        String ano = partes[2];

        return ano + "-" + mes + "-" + dia + "T00:00:00";
    }
    private String converterDataParaExibicao(String data) {

        String[] partes =
                data.substring(0, 10).split("-");

        String ano = partes[0];
        String mes = partes[1];
        String dia = partes[2];

        return dia + "/" + mes + "/" + ano;
    }
}
