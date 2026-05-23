package br.com.fiap.dto;

public record SolicitacaoResponse(
        int idSolicitacao,
        int idSolicitante,
        String nomeSolicitante,
        String tipo,
        String descricao,
        String status,
        String dataSolicitacao,
        Integer idRevisor,
        String nomeRevisor,
        String dataRevisao,
        String comentarioRevisao) {}
