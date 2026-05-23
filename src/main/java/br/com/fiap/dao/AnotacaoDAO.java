package br.com.fiap.dao;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.entities.Anotacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AnotacaoDAO {

    public Connection minhaConexao;

    public AnotacaoDAO() throws SQLException, ClassNotFoundException {
        this.minhaConexao = new ConexaoFactory().conexao();
    }

    public void inserir(Anotacao a) throws SQLException {
        String sql = "INSERT INTO T_ANOTACAO " +
                "(ID_ANOTACAO, TEXTO, DATA, AUTOR_TIPO, AUTOR_ID, SOBRE_TIPO, SOBRE_ID) " +
                "VALUES (SEQ_ANOTACAO.NEXTVAL, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setString(1, a.getTexto());
        stmt.setDate(2, new java.sql.Date(a.getData().getTime()));
        stmt.setString(3, a.getAutorTipo());
        stmt.setInt(4, a.getAutorId());
        stmt.setString(5, a.getSobreTipo());
        stmt.setInt(6, a.getSobreId());
        stmt.execute();
        stmt.close();
    }

    public void deletar(int id) throws SQLException {
        String sql = "DELETE FROM T_ANOTACAO WHERE ID_ANOTACAO=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.execute();
        stmt.close();
    }

    public ArrayList<Anotacao> selecionarPorSobre(String sobreTipo, int sobreId) throws SQLException {
        ArrayList<Anotacao> lista = new ArrayList<>();
        String sql = "SELECT * FROM T_ANOTACAO WHERE SOBRE_TIPO=? AND SOBRE_ID=? " +
                "ORDER BY DATA DESC, ID_ANOTACAO DESC";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setString(1, sobreTipo);
        stmt.setInt(2, sobreId);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) lista.add(mapear(rs));
        return lista;
    }

    public Anotacao selecionarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM T_ANOTACAO WHERE ID_ANOTACAO=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        return rs.next() ? mapear(rs) : null;
    }

    public int ultimoId() throws SQLException {
        PreparedStatement stmt =
                minhaConexao.prepareStatement("SELECT SEQ_ANOTACAO.CURRVAL FROM DUAL");
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    private Anotacao mapear(ResultSet rs) throws SQLException {
        Anotacao a = new Anotacao();
        a.setIdAnotacao(rs.getInt("ID_ANOTACAO"));
        a.setTexto(rs.getString("TEXTO"));
        a.setData(rs.getDate("DATA"));
        a.setAutorTipo(rs.getString("AUTOR_TIPO"));
        a.setAutorId(rs.getInt("AUTOR_ID"));
        a.setSobreTipo(rs.getString("SOBRE_TIPO"));
        a.setSobreId(rs.getInt("SOBRE_ID"));
        return a;
    }
}
