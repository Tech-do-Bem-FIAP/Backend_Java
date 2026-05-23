package br.com.fiap.dto;

/**
 * Resposta do login.
 * - role: "colaborador" ou "dentista"
 * - cargo: presente só quando role="colaborador"
 *   (Administrador / Coordenador / Auxiliar / Estagiário)
 */
public record LoginResponse(String role, int id, String nome, String cargo) {}
