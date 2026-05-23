package br.com.fiap.dao;

import br.com.fiap.conexoes.ConexaoFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * DAO da tabela associativa ENVIA (relacionamento N:M entre
 * T_NOTIFICACAO e T_PACIENTE).
 */
public class EnviaDAO {

    public Connection minhaConexao;

    public EnviaDAO() throws SQLException, ClassNotFoundException {
        this.minhaConexao = new ConexaoFactory().conexao();
    }

    public String inserir(int idNotificacao, int idPaciente) throws SQLException {
        String sql = "INSERT INTO Envia " +
                "(T_NOTIFICACAO_ID_NOTIFICACAO, T_PACIENTE_ID_PACIENTE) VALUES (?, ?)";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, idNotificacao);
        stmt.setInt(2, idPaciente);
        stmt.execute();
        stmt.close();
        return "Vinculo notificacao-paciente criado com sucesso!";
    }

    public String deletar(int idNotificacao, int idPaciente) throws SQLException {
        String sql = "DELETE FROM Envia WHERE T_NOTIFICACAO_ID_NOTIFICACAO=? " +
                "AND T_PACIENTE_ID_PACIENTE=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, idNotificacao);
        stmt.setInt(2, idPaciente);
        stmt.execute();
        stmt.close();
        return "Vinculo notificacao-paciente removido com sucesso!";
    }

    public ArrayList<int[]> selecionar() throws SQLException {
        ArrayList<int[]> lista = new ArrayList<>();
        String sql = "SELECT T_NOTIFICACAO_ID_NOTIFICACAO, T_PACIENTE_ID_PACIENTE FROM Envia";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(new int[]{rs.getInt(1), rs.getInt(2)});
        }
        return lista;
    }
}
