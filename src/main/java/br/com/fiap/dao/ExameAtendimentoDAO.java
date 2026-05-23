package br.com.fiap.dao;

import br.com.fiap.conexoes.ConexaoFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * DAO da tabela associativa FK_EXAME_ATEND (relacionamento N:M entre
 * T_ATENDIMENTO e T_EXAME).
 */
public class ExameAtendimentoDAO {

    public Connection minhaConexao;

    public ExameAtendimentoDAO() throws SQLException, ClassNotFoundException {
        this.minhaConexao = new ConexaoFactory().conexao();
    }

    public String inserir(int idAtendimento, int idExame) throws SQLException {
        String sql = "INSERT INTO FK_EXAME_ATEND " +
                "(T_ATENDIMENTO_ID_ATENDIMENTO, T_EXAME_ID_EXAME) VALUES (?, ?)";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, idAtendimento);
        stmt.setInt(2, idExame);
        stmt.execute();
        stmt.close();
        return "Vinculo exame-atendimento criado com sucesso!";
    }

    public String deletar(int idAtendimento, int idExame) throws SQLException {
        String sql = "DELETE FROM FK_EXAME_ATEND WHERE T_ATENDIMENTO_ID_ATENDIMENTO=? " +
                "AND T_EXAME_ID_EXAME=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, idAtendimento);
        stmt.setInt(2, idExame);
        stmt.execute();
        stmt.close();
        return "Vinculo exame-atendimento removido com sucesso!";
    }

    public ArrayList<int[]> selecionar() throws SQLException {
        ArrayList<int[]> lista = new ArrayList<>();
        String sql = "SELECT T_ATENDIMENTO_ID_ATENDIMENTO, T_EXAME_ID_EXAME " +
                "FROM FK_EXAME_ATEND";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(new int[]{rs.getInt(1), rs.getInt(2)});
        }
        return lista;
    }
}
