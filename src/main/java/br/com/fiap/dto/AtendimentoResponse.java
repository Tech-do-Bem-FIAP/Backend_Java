package br.com.fiap.dto;

public record AtendimentoResponse(
        int idAtendimento,
        int idPaciente,
        String nomePaciente,
        int idDentista,
        String nomeDentista,
        int idCampanha,
        String nomeCampanha,
        String data,
        String status,
        String tipo,
        String observacoes) {}
