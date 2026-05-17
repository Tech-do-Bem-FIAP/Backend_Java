package br.com.fiap.dto;

public record NotificacaoRequest(String mensagem, String dataEnvio,
        String statusEnvio, String canal, Integer idDentista, Integer idColaborador) {}
