package br.com.fiap.dao;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.entities.Colaborador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ColaboradorDAO {

    public Connection minhaConexao;

    public ColaboradorDAO() throws SQLException, ClassNotFoundException {
        this.minhaConexao = new ConexaoFactory().conexao();
    }

    public String inserir(Colaborador c) throws SQLException {
        if (!c.cpfValido()) {
            return "Erro: CPF invalido. Informe 11 digitos numericos.";
        }
        String sql = "INSERT INTO T_COLABORADOR " +
                "(ID_COLABORADOR, NOME, CPF, EMAIL, SENHA, CARGO, DISPONIBILIDADE) " +
                "VALUES (SEQ_COLABORADOR.NEXTVAL, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setString(1, c.getNome());
        stmt.setString(2, c.getCpf());
        stmt.setString(3, c.getEmail());
        stmt.setString(4, c.getSenha());
        stmt.setString(5, c.getCargo());
        stmt.setInt(6, c.getDisponibilidade());
        stmt.execute();
        stmt.close();
        return "Colaborador cadastrado com sucesso!";
    }

    public String atualizar(Colaborador c) throws SQLException {
        String sql = "UPDATE T_COLABORADOR SET NOME=?, CPF=?, EMAIL=?, SENHA=?, " +
                "CARGO=?, DISPONIBILIDADE=? WHERE ID_COLABORADOR=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setString(1, c.getNome());
        stmt.setString(2, c.getCpf());
        stmt.setString(3, c.getEmail());
        stmt.setString(4, c.getSenha());
        stmt.setString(5, c.getCargo());
        stmt.setInt(6, c.getDisponibilidade());
        stmt.setInt(7, c.getIdColaborador());
        stmt.executeUpdate();
        stmt.close();
        return "Colaborador atualizado com sucesso!";
    }

    public String deletar(int id) throws SQLException {
        String sql = "DELETE FROM T_COLABORADOR WHERE ID_COLABORADOR=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.execute();
        stmt.close();
        return "Colaborador deletado com sucesso!";
    }

    public ArrayList<Colaborador> selecionar() throws SQLException {
        ArrayList<Colaborador> lista = new ArrayList<>();
        String sql = "SELECT * FROM T_COLABORADOR ORDER BY NOME";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(mapear(rs));
        }
        return lista;
    }

    public Colaborador selecionarPorId(int id) throws SQLException {
        Colaborador c = null;
        String sql = "SELECT * FROM T_COLABORADOR WHERE ID_COLABORADOR=?";
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
                minhaConexao.prepareStatement("SELECT SEQ_COLABORADOR.CURRVAL FROM DUAL");
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    private Colaborador mapear(ResultSet rs) throws SQLException {
        Colaborador c = new Colaborador();
        c.setIdColaborador(rs.getInt("ID_COLABORADOR"));
        c.setNome(rs.getString("NOME"));
        c.setCpf(rs.getString("CPF"));
        c.setEmail(rs.getString("EMAIL"));
        c.setSenha(rs.getString("SENHA"));
        c.setCargo(rs.getString("CARGO"));
        c.setDisponibilidade(rs.getInt("DISPONIBILIDADE"));
        return c;
    }
}
