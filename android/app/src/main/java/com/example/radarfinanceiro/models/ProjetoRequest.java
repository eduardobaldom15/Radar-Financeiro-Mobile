package com.example.radarfinanceiro.models;

public class ProjetoRequest {

    private String nome;
    private String descricao;
    private String dataInicio;
    private String dataFim;
    private String programa;

    public ProjetoRequest(
            String nome,
            String descricao,
            String dataInicio,
            String dataFim,
            String programa
    ) {
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.programa = programa;
    }
}
