package br.com.fiap.dao;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.entities.Campanha;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CampanhaDAO {

    public Connection minhaConexao;

    public CampanhaDAO() throws SQLException, ClassNotFoundException {
        this.minhaConexao = new ConexaoFactory().conexao();
    }

    public String inserir(Campanha c) throws SQLException {
        if (!c.periodoValido()) {
            return "Erro: a data fim nao pode ser anterior a data inicio.";
        }
        String sql = "INSERT INTO T_CAMPANHA " +
                "(ID_CAMPANHA, NOME, LOCAL, DATA_INICIO, DATA_FIM, T_COLABORADOR_ID_COLABORADOR) " +
                "VALUES (SEQ_CAMPANHA.NEXTVAL, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setString(1, c.getNome());
        stmt.setString(2, c.getLocal());
        stmt.setDate(3, new java.sql.Date(c.getDataInicio().getTime()));
        stmt.setDate(4, new java.sql.Date(c.getDataFim().getTime()));
        stmt.setInt(5, c.getIdColaborador());
        stmt.execute();
        stmt.close();
        return "Campanha cadastrada com sucesso!";
    }

    public String atualizar(Campanha c) throws SQLException {
        String sql = "UPDATE T_CAMPANHA SET NOME=?, LOCAL=?, DATA_INICIO=?, DATA_FIM=?, " +
                "T_COLABORADOR_ID_COLABORADOR=? WHERE ID_CAMPANHA=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setString(1, c.getNome());
        stmt.setString(2, c.getLocal());
        stmt.setDate(3, new java.sql.Date(c.getDataInicio().getTime()));
        stmt.setDate(4, new java.sql.Date(c.getDataFim().getTime()));
        stmt.setInt(5, c.getIdColaborador());
        stmt.setInt(6, c.getIdCampanha());
        stmt.executeUpdate();
        stmt.close();
        return "Campanha atualizada com sucesso!";
    }

    public String deletar(int id) throws SQLException {
        String sql = "DELETE FROM T_CAMPANHA WHERE ID_CAMPANHA=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.execute();
        stmt.close();
        return "Campanha deletada com sucesso!";
    }

    public ArrayList<Campanha> selecionar() throws SQLException {
        ArrayList<Campanha> lista = new ArrayList<>();
        String sql = "SELECT * FROM T_CAMPANHA ORDER BY NOME";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(mapear(rs));
        }
        return lista;
    }

    public Campanha selecionarPorId(int id) throws SQLException {
        Campanha c = null;
        String sql = "SELECT * FROM T_CAMPANHA WHERE ID_CAMPANHA=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            c = mapear(rs);
        }
        return c;
    }

    /** Retorna o último ID gerado pela sequência nesta sessão. */
    public int ultimoId() throws SQLException {
        PreparedStatement stmt =
                minhaConexao.prepareStatement("SELECT SEQ_CAMPANHA.CURRVAL FROM DUAL");
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    private Campanha mapear(ResultSet rs) throws SQLException {
        Campanha c = new Campanha();
        c.setIdCampanha(rs.getInt("ID_CAMPANHA"));
        c.setNome(rs.getString("NOME"));
        c.setLocal(rs.getString("LOCAL"));
        c.setDataInicio(rs.getDate("DATA_INICIO"));
        c.setDataFim(rs.getDate("DATA_FIM"));
        c.setIdColaborador(rs.getInt("T_COLABORADOR_ID_COLABORADOR"));
        return c;
    }
}
