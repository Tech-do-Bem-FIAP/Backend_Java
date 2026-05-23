package br.com.fiap.dto;

public record AnotacaoResponse(
        int    idAnotacao,
        String texto,
        String data,
        String autorTipo,
        int    autorId,
        String sobreTipo,
        int    sobreId) {}
