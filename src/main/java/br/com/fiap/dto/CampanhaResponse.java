package br.com.fiap.dto;

public record CampanhaResponse(int idCampanha, String nome, String local,
        String dataInicio, String dataFim, int idColaborador) {}
