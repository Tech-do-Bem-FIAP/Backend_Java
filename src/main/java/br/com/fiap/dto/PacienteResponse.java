package br.com.fiap.dto;

public record PacienteResponse(
        int idPaciente,
        String nome,
        String cpf,
        String dataNasc,
        String telefone,
        String email,
        int idDentista,
        String cep,
        String logradouro,
        String bairro,
        String cidade,
        String uf,
        Double latitude,
        Double longitude) {}
