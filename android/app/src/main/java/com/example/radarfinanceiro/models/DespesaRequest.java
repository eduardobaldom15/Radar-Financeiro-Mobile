package com.example.radarfinanceiro.models;

public class DespesaRequest {
    private String categoria;
    private String tipo;
    private String nomeDespesa;
    private String descricao;
    private double valorUnitario;
    private double quantidade;
    private double valorOrcado;
    private int projetoId;

    public DespesaRequest(
            String categoria,
            String tipo,
            String nomeDespesa,
            String descricao,
            double valorUnitario,
            double quantidade,
            double valorOrcado,
            int projetoId
    ) {
        this.categoria = categoria;
        this.tipo = tipo;
        this.nomeDespesa = nomeDespesa;
        this.descricao = descricao;
        this.valorUnitario = valorUnitario;
        this.quantidade = quantidade;
        this.valorOrcado = valorOrcado;
        this.projetoId = projetoId;
    }
}
