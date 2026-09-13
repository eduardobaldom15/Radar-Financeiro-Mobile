package com.example.radarfinanceiro.models;

public class PesquisadorRequest {

    private String nome;
    private String email;
    private String senha;
    private String curso;
    private String departamento;

    public PesquisadorRequest(
            String nome,
            String email,
            String senha,
            String curso,
            String departamento) {

        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.curso = curso;
        this.departamento = departamento;
    }
}