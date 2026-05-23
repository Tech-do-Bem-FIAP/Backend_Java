package br.com.fiap.dto;

/**
 * Aprovação/rejeição. status deve ser 'aprovada' ou 'rejeitada'.
 * Em aprovações de solicitação externa, {@code cargo} é exigido para criar o colaborador.
 */
public record SolicitacaoReviewRequest(
        String status,
        int idRevisor,
        String comentario,
        String cargo) {}
