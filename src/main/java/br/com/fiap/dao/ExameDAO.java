package br.com.fiap.dao;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.entities.Exame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ExameDAO {

    public Connection minhaConexao;

    public ExameDAO() throws SQLException, ClassNotFoundException {
        this.minhaConexao = new ConexaoFactory().conexao();
    }

    public String inserir(Exame e) throws SQLException {
        String sql = "INSERT INTO T_EXAME " +
                "(ID_EXAME, TIPO, REQUISITOS, RESULTADO, ID_ATENDIMENTO) " +
                "VALUES (SEQ_EXAME.NEXTVAL, ?, ?, ?, ?)";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setString(1, e.getTipo());
        stmt.setString(2, e.getRequisitos());
        stmt.setString(3, e.getResultado());
        stmt.setInt(4, e.getIdAtendimento());
        stmt.execute();
        stmt.close();
        return "Exame cadastrado com sucesso!";
    }

    public String atualizar(Exame e) throws SQLException {
        String sql = "UPDATE T_EXAME SET TIPO=?, REQUISITOS=?, RESULTADO=?, " +
                "ID_ATENDIMENTO=? WHERE ID_EXAME=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setString(1, e.getTipo());
        stmt.setString(2, e.getRequisitos());
        stmt.setString(3, e.getResultado());
        stmt.setInt(4, e.getIdAtendimento());
        stmt.setInt(5, e.getIdExame());
        stmt.executeUpdate();
        stmt.close();
        return "Exame atualizado com sucesso!";
    }

    public String deletar(int id) throws SQLException {
        String sql = "DELETE FROM T_EXAME WHERE ID_EXAME=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.execute();
        stmt.close();
        return "Exame deletado com sucesso!";
    }

    public ArrayList<Exame> selecionar() throws SQLException {
        ArrayList<Exame> lista = new ArrayList<>();
        String sql = "SELECT * FROM T_EXAME ORDER BY TIPO";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(mapear(rs));
        }
        return lista;
    }

    public Exame selecionarPorId(int id) throws SQLException {
        Exame e = null;
        String sql = "SELECT * FROM T_EXAME WHERE ID_EXAME=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            e = mapear(rs);
        }
        return e;
    }

    /** Retorna o último ID gerado pela sequência nesta sessão. */
    public int ultimoId() throws SQLException {
        PreparedStatement stmt =
                minhaConexao.prepareStatement("SELECT SEQ_EXAME.CURRVAL FROM DUAL");
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    private Exame mapear(ResultSet rs) throws SQLException {
        Exame e = new Exame();
        e.setIdExame(rs.getInt("ID_EXAME"));
        e.setTipo(rs.getString("TIPO"));
        e.setRequisitos(rs.getString("REQUISITOS"));
        e.setResultado(rs.getString("RESULTADO"));
        e.setIdAtendimento(rs.getInt("ID_ATENDIMENTO"));
        return e;
    }
}
