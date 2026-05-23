package br.com.fiap.dao;

import br.com.fiap.conexoes.ConexaoFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * DAO da tabela associativa FK_ATEND_CAMP (relacionamento N:M entre
 * T_CAMPANHA e T_ATENDIMENTO).
 */
public class CampanhaAtendimentoDAO {

    public Connection minhaConexao;

    public CampanhaAtendimentoDAO() throws SQLException, ClassNotFoundException {
        this.minhaConexao = new ConexaoFactory().conexao();
    }

    public String inserir(int idCampanha, int idAtendimento) throws SQLException {
        String sql = "INSERT INTO FK_ATEND_CAMP " +
                "(T_CAMPANHA_ID_CAMPANHA, T_ATENDIMENTO_ID_ATENDIMENTO) VALUES (?, ?)";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, idCampanha);
        stmt.setInt(2, idAtendimento);
        stmt.execute();
        stmt.close();
        return "Vinculo campanha-atendimento criado com sucesso!";
    }

    public String deletar(int idCampanha, int idAtendimento) throws SQLException {
        String sql = "DELETE FROM FK_ATEND_CAMP WHERE T_CAMPANHA_ID_CAMPANHA=? " +
                "AND T_ATENDIMENTO_ID_ATENDIMENTO=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, idCampanha);
        stmt.setInt(2, idAtendimento);
        stmt.execute();
        stmt.close();
        return "Vinculo campanha-atendimento removido com sucesso!";
    }

    public ArrayList<int[]> selecionar() throws SQLException {
        ArrayList<int[]> lista = new ArrayList<>();
        String sql = "SELECT T_CAMPANHA_ID_CAMPANHA, T_ATENDIMENTO_ID_ATENDIMENTO " +
                "FROM FK_ATEND_CAMP";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(new int[]{rs.getInt(1), rs.getInt(2)});
        }
        return lista;
    }
}
