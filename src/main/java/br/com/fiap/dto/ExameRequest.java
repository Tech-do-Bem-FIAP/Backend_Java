package br.com.fiap.dto;

public record ExameRequest(String tipo, String requisitos,
        String resultado, int idAtendimento) {}
