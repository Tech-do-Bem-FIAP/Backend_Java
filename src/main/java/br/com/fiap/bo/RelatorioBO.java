package br.com.fiap.bo;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.dto.RelatorioResponse;
import br.com.fiap.dto.RelatorioResponse.AtendimentoDetalhado;
import br.com.fiap.dto.RelatorioResponse.AtendimentoPorStatus;
import br.com.fiap.dto.RelatorioResponse.DentistaAcimaMedia;
import br.com.fiap.dto.RelatorioResponse.IdadeStats;
import br.com.fiap.dto.RelatorioResponse.TopDentista;
import br.com.fiap.exceptions.PersistenciaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Executa os 5 relatorios da rubrica Sprint 4 e empacota em um snapshot unico.
 * As consultas espelham as 5 do {@code seed/rebuild.sql} (linhas 866-942).
 */
public class RelatorioBO {

    public RelatorioResponse gerar() {
        try (Connection conn = new ConexaoFactory().conexao()) {
            return new RelatorioResponse(
                    rankingDentistas(conn),
                    idadeStats(conn),
                    atendimentosPorStatus(conn),
                    dentistasAcimaMedia(conn),
                    ultimosAtendimentos(conn));
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Falha ao gerar relatorios", e);
        }
    }

    private List<TopDentista> rankingDentistas(Connection c) throws SQLException {
        String sql = "SELECT D.NOME, D.ESPECIALIDADE, COUNT(P.ID_PACIENTE) "
                + "FROM T_DENTISTA D "
                + "LEFT JOIN T_PACIENTE P ON P.ID_DENTISTA = D.ID_DENTISTA "
                + "GROUP BY D.NOME, D.ESPECIALIDADE "
                + "ORDER BY COUNT(P.ID_PACIENTE) DESC, D.NOME "
                + "FETCH FIRST 10 ROWS ONLY";
        List<TopDentista> out = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new TopDentista(rs.getString(1), rs.getString(2), rs.getInt(3)));
            }
        }
        return out;
    }

    private IdadeStats idadeStats(Connection c) throws SQLException {
        String sql = "SELECT ROUND(AVG(MONTHS_BETWEEN(SYSDATE, DATA_NASC) / 12), 1), "
                + "       COUNT(*), "
                + "       MIN(TRUNC(MONTHS_BETWEEN(SYSDATE, DATA_NASC) / 12)), "
                + "       MAX(TRUNC(MONTHS_BETWEEN(SYSDATE, DATA_NASC) / 12)) "
                + "FROM T_PACIENTE";
        try (PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new IdadeStats(rs.getDouble(1), rs.getInt(2), rs.getInt(3), rs.getInt(4));
            }
        }
        return new IdadeStats(0, 0, 0, 0);
    }

    private List<AtendimentoPorStatus> atendimentosPorStatus(Connection c) throws SQLException {
        String sql = "SELECT STATUS, COUNT(*), "
                + "       TO_CHAR(MIN(DATA), 'YYYY-MM-DD'), "
                + "       TO_CHAR(MAX(DATA), 'YYYY-MM-DD') "
                + "FROM T_ATENDIMENTO "
                + "GROUP BY STATUS "
                + "HAVING COUNT(*) > 0 "
                + "ORDER BY COUNT(*) DESC";
        List<AtendimentoPorStatus> out = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new AtendimentoPorStatus(
                        rs.getString(1), rs.getInt(2), rs.getString(3), rs.getString(4)));
            }
        }
        return out;
    }

    private List<DentistaAcimaMedia> dentistasAcimaMedia(Connection c) throws SQLException {
        String sql = "SELECT D.ID_DENTISTA, D.NOME, "
                + "       (SELECT COUNT(*) FROM T_PACIENTE P WHERE P.ID_DENTISTA = D.ID_DENTISTA) "
                + "FROM T_DENTISTA D "
                + "WHERE  (SELECT COUNT(*) FROM T_PACIENTE P WHERE P.ID_DENTISTA = D.ID_DENTISTA) "
                + "     > (SELECT AVG(QTD) FROM (SELECT COUNT(*) AS QTD FROM T_PACIENTE GROUP BY ID_DENTISTA)) "
                + "ORDER BY 3 DESC";
        List<DentistaAcimaMedia> out = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new DentistaAcimaMedia(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
        }
        return out;
    }

    private List<AtendimentoDetalhado> ultimosAtendimentos(Connection c) throws SQLException {
        String sql = "SELECT A.ID_ATENDIMENTO, TO_CHAR(A.DATA, 'YYYY-MM-DD'), A.TIPO, A.STATUS, "
                + "       P.NOME, D.NOME, D.ESPECIALIDADE, C.NOME "
                + "FROM T_ATENDIMENTO A "
                + "INNER JOIN T_PACIENTE P ON A.T_PACIENTE_ID_PACIENTE = P.ID_PACIENTE "
                + "INNER JOIN T_DENTISTA D ON A.T_DENTISTA_ID_DENTISTA = D.ID_DENTISTA "
                + "INNER JOIN T_CAMPANHA C ON A.ID_CAMPANHA            = C.ID_CAMPANHA "
                + "ORDER BY A.DATA DESC "
                + "FETCH FIRST 20 ROWS ONLY";
        List<AtendimentoDetalhado> out = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new AtendimentoDetalhado(
                        rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
            }
        }
        return out;
    }
}
