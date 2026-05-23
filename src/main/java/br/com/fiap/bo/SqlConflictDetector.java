package br.com.fiap.bo;

import br.com.fiap.exceptions.ConflitoException;
import br.com.fiap.exceptions.PersistenciaException;

import java.sql.SQLException;

/**
 * Traduz ORA-00001 (unique constraint violated) em {@link ConflitoException} com mensagem
 * amigável a partir do nome da constraint contido no texto do erro do Oracle.
 * Quando não for ORA-00001, devolve uma {@link PersistenciaException}.
 */
final class SqlConflictDetector {

    private SqlConflictDetector() {}

    static RuntimeException traduzir(SQLException e, String operacao) {
        if (e.getErrorCode() == 1) {
            String msg = e.getMessage() != null ? e.getMessage().toUpperCase() : "";
            if (msg.contains("EMAIL_UN")) {
                return new ConflitoException("E-mail já cadastrado.");
            }
            if (msg.contains("CPF_UN")) {
                return new ConflitoException("CPF já cadastrado.");
            }
            if (msg.contains("CRO_UN")) {
                return new ConflitoException("CRO já cadastrado.");
            }
            return new ConflitoException("Registro duplicado.");
        }
        return new PersistenciaException("Erro ao " + operacao, e);
    }
}
