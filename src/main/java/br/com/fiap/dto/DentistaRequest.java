package br.com.fiap.dto;

public record DentistaRequest(String nome, String cpf, String email, String senha,
        String cro, String especialidade, int disponibilidade, Integer idColaborador) {}
