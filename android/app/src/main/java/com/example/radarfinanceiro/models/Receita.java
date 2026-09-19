package com.example.radarfinanceiro.models;

public class Receita {

    private int id;
    private String tipo;
    private String origem;
    private String dataEntrada;
    private double valor;
    private int projetoId;

    public int getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getOrigem() {
        return origem;
    }

    public String getDataEntrada() {
        return dataEntrada;
    }

    public double getValor() {
        return valor;
    }

    public int getProjetoId() {
        return projetoId;
    }
}
