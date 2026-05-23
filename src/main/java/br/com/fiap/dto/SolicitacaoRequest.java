package br.com.fiap.dto;

/**
 * Solicitação interna: enviar {@code idSolicitante} + tipo + descrição.
 * Solicitação externa (pedido de cadastro vindo da tela de login):
 * deixar {@code idSolicitante} null e enviar nome/cpf/email/senha externos.
 */
public record SolicitacaoRequest(
        Integer idSolicitante,
        String tipo,
        String descricao,
        String nomeExterno,
        String cpfExterno,
        String emailExterno,
        String senhaExterno,
        String telefoneExterno) {}
