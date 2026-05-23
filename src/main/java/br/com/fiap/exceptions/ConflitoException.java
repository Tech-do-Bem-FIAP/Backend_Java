package br.com.fiap.exceptions;

/** Violação de regra de unicidade ou outro conflito de estado. Mapeia para HTTP 409. */
public class ConflitoException extends RuntimeException {
    public ConflitoException(String mensagem) {
        super(mensagem);
    }
}
