package br.com.fiap.dto;

public record PacienteRequest(
        String nome,
        String cpf,
        String dataNasc,
        String telefone,
        String email,
        int idDentista) {}
