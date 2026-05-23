package br.com.fiap.dto;

public record DentistaResponse(int idDentista, String nome, String cpf, String email,
        String cro, String especialidade, int disponibilidade, Integer idColaborador) {}
