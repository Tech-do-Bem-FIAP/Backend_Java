package br.com.fiap.dto;

public record CampanhaRequest(String nome, String local,
        String dataInicio, String dataFim, int idColaborador) {}
