package br.com.fiap.dto;

import java.util.List;

/**
 * Snapshot consolidado dos 5 relatorios da rubrica Sprint 4 (Banco de Dados).
 * Renderizado em /admin/relatorios no frontend para o cargo Administrador.
 */
public record RelatorioResponse(
        List<TopDentista> rankingDentistas,
        IdadeStats idadeStats,
        List<AtendimentoPorStatus> atendimentosPorStatus,
        List<DentistaAcimaMedia> dentistasAcimaMedia,
        List<AtendimentoDetalhado> ultimosAtendimentos
) {
    public record TopDentista(String nome, String especialidade, int qtdPacientes) {}
    public record IdadeStats(double idadeMedia, int total, int maisNovo, int maisVelho) {}
    public record AtendimentoPorStatus(String status, int quantidade, String primeiroAtendimento, String ultimoAtendimento) {}
    public record DentistaAcimaMedia(int id, String nome, int qtdPacientes) {}
    public record AtendimentoDetalhado(int id, String data, String tipo, String status, String paciente, String dentista, String especialidade, String campanha) {}
}
