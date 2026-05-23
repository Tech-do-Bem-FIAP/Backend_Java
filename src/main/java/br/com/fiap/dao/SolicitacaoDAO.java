package br.com.fiap.dao;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.entities.Solicitacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;

public class SolicitacaoDAO {

    public Connection minhaConexao;

    public SolicitacaoDAO() throws SQLException, ClassNotFoundException {
        this.minhaConexao = new ConexaoFactory().conexao();
    }

    public void inserir(Solicitacao s) throws SQLException {
        String sql = "INSERT INTO T_SOLICITACAO " +
                "(ID_SOLICITACAO, T_COLABORADOR_ID_SOLICITANTE, TIPO, DESCRICAO, " +
                "STATUS, DATA_SOLICITACAO, " +
                "NOME_EXTERNO, CPF_EXTERNO, EMAIL_EXTERNO, SENHA_EXTERNO, TELEFONE_EXTERNO) " +
                "VALUES (SEQ_SOLICITACAO.NEXTVAL, ?, ?, ?, 'pendente', SYSTIMESTAMP, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        setNullableInt(stmt, 1, s.getIdSolicitante());
        stmt.setString(2, s.getTipo());
        stmt.setString(3, s.getDescricao());
        setNullableString(stmt, 4, s.getNomeExterno());
        setNullableString(stmt, 5, s.getCpfExterno());
        setNullableString(stmt, 6, s.getEmailExterno());
        setNullableString(stmt, 7, s.getSenhaExterno());
        setNullableString(stmt, 8, s.getTelefoneExterno());
        stmt.execute();
        stmt.close();
    }

    public boolean revisar(int id, String status, int idRevisor, String comentario,
                           Date momento) throws SQLException {
        String sql = "UPDATE T_SOLICITACAO SET STATUS=?, T_COLABORADOR_ID_REVISOR=?, " +
                "DATA_REVISAO=?, COMENTARIO_REVISAO=? WHERE ID_SOLICITACAO=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setString(1, status);
        stmt.setInt(2, idRevisor);
        stmt.setTimestamp(3, new Timestamp(momento.getTime()));
        setNullableString(stmt, 4, comentario);
        stmt.setInt(5, id);
        int rows = stmt.executeUpdate();
        stmt.close();
        return rows > 0;
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM T_SOLICITACAO WHERE ID_SOLICITACAO=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.execute();
        stmt.close();
    }

    public ArrayList<Solicitacao> selecionar() throws SQLException {
        ArrayList<Solicitacao> lista = new ArrayList<>();
        String sql = baseSelect() + " ORDER BY S.DATA_SOLICITACAO DESC";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) lista.add(mapear(rs));
        return lista;
    }

    public Solicitacao selecionarPorId(int id) throws SQLException {
        String sql = baseSelect() + " WHERE S.ID_SOLICITACAO=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        return rs.next() ? mapear(rs) : null;
    }

    public int ultimoId() throws SQLException {
        PreparedStatement stmt =
                minhaConexao.prepareStatement("SELECT SEQ_SOLICITACAO.CURRVAL FROM DUAL");
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    private String baseSelect() {
        return "SELECT S.*, " +
                "SOL.NOME AS NOME_SOLICITANTE, " +
                "REV.NOME AS NOME_REVISOR " +
                "FROM T_SOLICITACAO S " +
                "LEFT JOIN T_COLABORADOR SOL ON S.T_COLABORADOR_ID_SOLICITANTE = SOL.ID_COLABORADOR " +
                "LEFT JOIN T_COLABORADOR REV ON S.T_COLABORADOR_ID_REVISOR = REV.ID_COLABORADOR";
    }

    private void setNullableInt(PreparedStatement stmt, int idx, Integer value)
            throws SQLException {
        if (value != null) stmt.setInt(idx, value);
        else stmt.setNull(idx, Types.INTEGER);
    }

    private void setNullableString(PreparedStatement stmt, int idx, String value)
            throws SQLException {
        if (value != null && !value.isBlank()) stmt.setString(idx, value);
        else stmt.setNull(idx, Types.VARCHAR);
    }

    private Solicitacao mapear(ResultSet rs) throws SQLException {
        Solicitacao s = new Solicitacao();
        s.setIdSolicitacao(rs.getInt("ID_SOLICITACAO"));

        int idSol = rs.getInt("T_COLABORADOR_ID_SOLICITANTE");
        s.setIdSolicitante(rs.wasNull() ? null : idSol);

        s.setTipo(rs.getString("TIPO"));
        s.setDescricao(rs.getString("DESCRICAO"));
        s.setStatus(rs.getString("STATUS"));
        s.setDataSolicitacao(rs.getTimestamp("DATA_SOLICITACAO"));

        int idRev = rs.getInt("T_COLABORADOR_ID_REVISOR");
        s.setIdRevisor(rs.wasNull() ? null : idRev);

        Timestamp tsRev = rs.getTimestamp("DATA_REVISAO");
        s.setDataRevisao(tsRev);

        s.setComentarioRevisao(rs.getString("COMENTARIO_REVISAO"));

        s.setNomeExterno(rs.getString("NOME_EXTERNO"));
        s.setCpfExterno(rs.getString("CPF_EXTERNO"));
        s.setEmailExterno(rs.getString("EMAIL_EXTERNO"));
        s.setSenhaExterno(rs.getString("SENHA_EXTERNO"));
        s.setTelefoneExterno(rs.getString("TELEFONE_EXTERNO"));

        try {
            s.setNomeSolicitante(rs.getString("NOME_SOLICITANTE"));
            s.setNomeRevisor(rs.getString("NOME_REVISOR"));
        } catch (SQLException ignored) {}

        return s;
    }
}
