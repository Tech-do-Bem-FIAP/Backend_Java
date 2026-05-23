package br.com.fiap.dao;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.entities.Notificacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

public class NotificacaoDAO {

    public Connection minhaConexao;

    public NotificacaoDAO() throws SQLException, ClassNotFoundException {
        this.minhaConexao = new ConexaoFactory().conexao();
    }

    public String inserir(Notificacao n) throws SQLException {
        String sql = "INSERT INTO T_NOTIFICACAO " +
                "(ID_NOTIFICACAO, MENSAGEM, DATA_ENVIO, STATUS_ENVIO, CANAL, " +
                "T_DENTISTA_ID_DENTISTA, T_COLABORADOR_ID_COLABORADOR, " +
                "T_PACIENTE_ID_PACIENTE, DATA_LEITURA) " +
                "VALUES (SEQ_NOTIFICACAO.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setString(1, n.getMensagem());
        stmt.setDate(2, new java.sql.Date(n.getDataEnvio().getTime()));
        stmt.setString(3, n.getStatusEnvio());
        stmt.setString(4, n.getCanal());
        setNullableInt(stmt, 5, n.getIdDentista());
        setNullableInt(stmt, 6, n.getIdColaborador());
        setNullableInt(stmt, 7, n.getIdPaciente());
        setNullableTimestamp(stmt, 8, n.getDataLeitura());
        stmt.execute();
        stmt.close();
        return "Notificacao cadastrada com sucesso!";
    }

    public String atualizar(Notificacao n) throws SQLException {
        String sql = "UPDATE T_NOTIFICACAO SET MENSAGEM=?, DATA_ENVIO=?, STATUS_ENVIO=?, " +
                "CANAL=?, T_DENTISTA_ID_DENTISTA=?, T_COLABORADOR_ID_COLABORADOR=?, " +
                "T_PACIENTE_ID_PACIENTE=?, DATA_LEITURA=? WHERE ID_NOTIFICACAO=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setString(1, n.getMensagem());
        stmt.setDate(2, new java.sql.Date(n.getDataEnvio().getTime()));
        stmt.setString(3, n.getStatusEnvio());
        stmt.setString(4, n.getCanal());
        setNullableInt(stmt, 5, n.getIdDentista());
        setNullableInt(stmt, 6, n.getIdColaborador());
        setNullableInt(stmt, 7, n.getIdPaciente());
        setNullableTimestamp(stmt, 8, n.getDataLeitura());
        stmt.setInt(9, n.getIdNotificacao());
        stmt.executeUpdate();
        stmt.close();
        return "Notificacao atualizada com sucesso!";
    }

    /** Grava o timestamp de leitura. Retorna true se algum registro foi afetado. */
    public boolean marcarLida(int id, java.util.Date momento) throws SQLException {
        String sql = "UPDATE T_NOTIFICACAO SET DATA_LEITURA=? WHERE ID_NOTIFICACAO=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setTimestamp(1, new Timestamp(momento.getTime()));
        stmt.setInt(2, id);
        int rows = stmt.executeUpdate();
        stmt.close();
        return rows > 0;
    }

    public String deletar(int id) throws SQLException {
        String sql = "DELETE FROM T_NOTIFICACAO WHERE ID_NOTIFICACAO=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.execute();
        stmt.close();
        return "Notificacao deletada com sucesso!";
    }

    public ArrayList<Notificacao> selecionar() throws SQLException {
        ArrayList<Notificacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM T_NOTIFICACAO ORDER BY DATA_ENVIO DESC";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(mapear(rs));
        }
        return lista;
    }

    public Notificacao selecionarPorId(int id) throws SQLException {
        Notificacao n = null;
        String sql = "SELECT * FROM T_NOTIFICACAO WHERE ID_NOTIFICACAO=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            n = mapear(rs);
        }
        return n;
    }

    /** Retorna o último ID gerado pela sequência nesta sessão. */
    public int ultimoId() throws SQLException {
        PreparedStatement stmt =
                minhaConexao.prepareStatement("SELECT SEQ_NOTIFICACAO.CURRVAL FROM DUAL");
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    private void setNullableInt(PreparedStatement stmt, int idx, Integer value)
            throws SQLException {
        if (value != null) {
            stmt.setInt(idx, value);
        } else {
            stmt.setNull(idx, Types.INTEGER);
        }
    }

    private void setNullableTimestamp(PreparedStatement stmt, int idx, java.util.Date value)
            throws SQLException {
        if (value != null) {
            stmt.setTimestamp(idx, new Timestamp(value.getTime()));
        } else {
            stmt.setNull(idx, Types.TIMESTAMP);
        }
    }

    private Notificacao mapear(ResultSet rs) throws SQLException {
        Notificacao n = new Notificacao();
        n.setIdNotificacao(rs.getInt("ID_NOTIFICACAO"));
        n.setMensagem(rs.getString("MENSAGEM"));
        n.setDataEnvio(rs.getDate("DATA_ENVIO"));
        n.setStatusEnvio(rs.getString("STATUS_ENVIO"));
        n.setCanal(rs.getString("CANAL"));

        int idDent = rs.getInt("T_DENTISTA_ID_DENTISTA");
        n.setIdDentista(rs.wasNull() ? null : idDent);

        int idColab = rs.getInt("T_COLABORADOR_ID_COLABORADOR");
        n.setIdColaborador(rs.wasNull() ? null : idColab);

        int idPac = rs.getInt("T_PACIENTE_ID_PACIENTE");
        n.setIdPaciente(rs.wasNull() ? null : idPac);

        Timestamp ts = rs.getTimestamp("DATA_LEITURA");
        n.setDataLeitura(ts);

        return n;
    }
}
