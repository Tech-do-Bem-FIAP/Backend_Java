package br.com.fiap.dto;

public record ExameResponse(int idExame, String tipo, String requisitos,
        String resultado, int idAtendimento) {}
