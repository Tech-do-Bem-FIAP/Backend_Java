package br.com.fiap.dto;

public record NotificacaoResponse(
        int idNotificacao,
        String mensagem,
        String dataEnvio,
        String statusEnvio,
        String canal,
        Integer idDentista,
        Integer idColaborador,
        Integer idPaciente,
        String dataLeitura) {}
