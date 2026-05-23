package br.com.fiap.dto;

public record AnotacaoRequest(
        String texto,
        String data,
        String autorTipo,
        int    autorId,
        String sobreTipo,
        int    sobreId) {}
