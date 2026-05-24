package br.com.fiap.dto;

/**
 * Requisicao do endpoint admin para reconstrucao do banco.
 * Exige credenciais ADMIN/ADMIN e confirmacao explicita "RECONSTRUIR".
 */
public record AdminRebuildRequest(String email, String senha, String confirmacao) {}
