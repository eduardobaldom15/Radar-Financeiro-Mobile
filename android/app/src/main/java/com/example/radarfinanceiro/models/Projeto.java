package com.example.radarfinanceiro.models;

public class Projeto {

    private int id;
    private String nome;
    private String descricao;
    private String dataInicio;
    private String dataFim;
    private int duracaoEmDias;
    private String programa;
    private int pesquisadorId;

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public int getDuracaoEmDias() {
        return duracaoEmDias;
    }

    public String getPrograma() {
        return programa;
    }

    public int getPesquisadorId() {
        return pesquisadorId;
    }
}
