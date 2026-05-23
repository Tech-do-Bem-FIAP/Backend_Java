package br.com.fiap.dto;

public record ColaboradorRequest(String nome, String cpf, String email,
        String senha, String cargo, int disponibilidade) {}
