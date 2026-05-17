package br.com.fiap.dto;

public record AtendimentoRequest(
        String data,
        String tipo,
        String status,
        String observacoes,
        int idPaciente,
        int idDentista,
        int idCampanha) {}
