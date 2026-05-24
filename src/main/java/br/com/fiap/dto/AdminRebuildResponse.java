package br.com.fiap.dto;

import java.util.List;

/**
 * Resultado da reconstrucao do banco.
 * - sucesso: true quando nao houve erros bloqueantes
 * - statementsExecutados: contagem de statements executados com sucesso
 * - statementsPulados: contagem de SELECTs (relatorios) ignorados
 * - erros: mensagens de erros toleraveis (ex: DROP em tabela inexistente)
 */
public record AdminRebuildResponse(
        boolean sucesso,
        int statementsExecutados,
        int statementsPulados,
        List<String> erros
) {}
