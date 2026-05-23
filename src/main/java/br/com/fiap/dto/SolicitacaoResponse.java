package br.com.fiap.dto;

public record SolicitacaoResponse(
        int idSolicitacao,
        Integer idSolicitante,
        String nomeSolicitante,
        String tipo,
        String descricao,
        String status,
        String dataSolicitacao,
        Integer idRevisor,
        String nomeRevisor,
        String dataRevisao,
        String comentarioRevisao,
        String nomeExterno,
        String cpfExterno,
        String emailExterno,
        String senhaExterno,
        String telefoneExterno,
        /** Quando uma solicitação externa é aprovada, o colaborador recém-criado. */
        Integer idColaboradorCriado) {}
