package br.com.fiap.dto;

public record ColaboradorResponse(int idColaborador, String nome, String cpf,
        String email, String cargo, int disponibilidade) {}
