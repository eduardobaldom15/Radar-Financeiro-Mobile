package com.example.radarfinanceiro.models;

public class Despesa {

    private int id;
    private String categoria;
    private String tipo;
    private String nomeDespesa;
    private String descricao;
    private double valorUnitario;
    private double quantidade;
    private double valorOrcado;
    private double valorRealizado;
    private int projetoId;

    public int getId() {
        return id;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNomeDespesa() {
        return nomeDespesa;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public double getValorOrcado() {
        return valorOrcado;
    }

    public double getValorRealizado() {
        return valorRealizado;
    }

    public int getProjetoId() {
        return projetoId;
    }
}