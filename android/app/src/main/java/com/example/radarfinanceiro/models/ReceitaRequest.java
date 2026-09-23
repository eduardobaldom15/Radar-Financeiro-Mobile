package com.example.radarfinanceiro.models;

public class ReceitaRequest {

    private String tipo;
    private String origem;
    private double valor;
    private int projetoId;

    public ReceitaRequest(
            String tipo,
            String origem,
            double valor,
            int projetoId
    ) {
        this.tipo = tipo;
        this.origem = origem;
        this.valor = valor;
        this.projetoId = projetoId;
    }
}
