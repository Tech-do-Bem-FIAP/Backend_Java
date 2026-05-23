package br.com.fiap.dto;

/** Aprovação/rejeição. status deve ser 'aprovada' ou 'rejeitada'. */
public record SolicitacaoReviewRequest(String status, int idRevisor, String comentario) {}
