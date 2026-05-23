package br.com.fiap.dto;

/**
 * Payload de criação/atualização de notificação.
 *
 * Direção é deduzida pelas FKs preenchidas:
 *   - idColaborador + idDentista (idPaciente null) → colaborador → dentista
 *   - idDentista    + idPaciente (idColaborador null) → dentista → paciente
 *
 * Campos técnicos (dataEnvio, statusEnvio, canal) são opcionais — quando
 * ausentes o BO aplica defaults (hoje, 'enviado', 'app').
 */
public record NotificacaoRequest(
        String mensagem,
        String dataEnvio,
        String statusEnvio,
        String canal,
        Integer idDentista,
        Integer idColaborador,
        Integer idPaciente) {}
