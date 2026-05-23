package br.com.fiap.mapper;

public record ErroResposta(String erro, int status, String timestamp) {
    public static ErroResposta de(String erro, int status) {
        return new ErroResposta(erro, status, java.time.OffsetDateTime.now().toString());
    }
}
