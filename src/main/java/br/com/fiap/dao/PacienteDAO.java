package br.com.fiap.dao;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.entities.Paciente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

public class PacienteDAO {

    public Connection minhaConexao;

    public PacienteDAO() throws SQLException, ClassNotFoundException {
        this.minhaConexao = new ConexaoFactory().conexao();
    }

    public String inserir(Paciente p) throws SQLException {
        if (!p.cpfValido()) {
            return "Erro: CPF invalido. Quando informado, deve ter 11 digitos.";
        }
        String sql = "INSERT INTO T_PACIENTE " +
                "(ID_PACIENTE, NOME, CPF, DATA_NASC, TELEFONE, EMAIL, ID_DENTISTA) " +
                "VALUES (SEQ_PACIENTE.NEXTVAL, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setString(1, p.getNome());
        if (p.getCpf() != null) {
            stmt.setString(2, p.getCpf());
        } else {
            stmt.setNull(2, Types.VARCHAR);
        }
        stmt.setDate(3, new java.sql.Date(p.getDataNasc().getTime()));
        stmt.setString(4, p.getTelefone());
        stmt.setString(5, p.getEmail());
        stmt.setInt(6, p.getIdDentista());
        stmt.execute();
        stmt.close();
        return "Paciente cadastrado com sucesso!";
    }

    public String atualizar(Paciente p) throws SQLException {
        String sql = "UPDATE T_PACIENTE SET NOME=?, CPF=?, DATA_NASC=?, TELEFONE=?, " +
                "EMAIL=?, ID_DENTISTA=? WHERE ID_PACIENTE=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setString(1, p.getNome());
        if (p.getCpf() != null) {
            stmt.setString(2, p.getCpf());
        } else {
            stmt.setNull(2, Types.VARCHAR);
        }
        stmt.setDate(3, new java.sql.Date(p.getDataNasc().getTime()));
        stmt.setString(4, p.getTelefone());
        stmt.setString(5, p.getEmail());
        stmt.setInt(6, p.getIdDentista());
        stmt.setInt(7, p.getIdPaciente());
        stmt.executeUpdate();
        stmt.close();
        return "Paciente atualizado com sucesso!";
    }

    public String deletar(int id) throws SQLException {
        String sql = "DELETE FROM T_PACIENTE WHERE ID_PACIENTE=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.execute();
        stmt.close();
        return "Paciente deletado com sucesso!";
    }

    public ArrayList<Paciente> selecionar() throws SQLException {
        ArrayList<Paciente> lista = new ArrayList<>();
        String sql = "SELECT * FROM T_PACIENTE ORDER BY NOME";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(mapear(rs));
        }
        return lista;
    }

    public Paciente selecionarPorId(int id) throws SQLException {
        Paciente p = null;
        String sql = "SELECT * FROM T_PACIENTE WHERE ID_PACIENTE=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            p = mapear(rs);
        }
        return p;
    }

    /** Retorna o último ID gerado pela sequência nesta sessão. */
    public int ultimoId() throws SQLException {
        PreparedStatement stmt =
                minhaConexao.prepareStatement("SELECT SEQ_PACIENTE.CURRVAL FROM DUAL");
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    private Paciente mapear(ResultSet rs) throws SQLException {
        Paciente p = new Paciente();
        p.setIdPaciente(rs.getInt("ID_PACIENTE"));
        p.setNome(rs.getString("NOME"));
        p.setCpf(rs.getString("CPF"));
        p.setDataNasc(rs.getDate("DATA_NASC"));
        p.setTelefone(rs.getString("TELEFONE"));
        p.setEmail(rs.getString("EMAIL"));
        p.setIdDentista(rs.getInt("ID_DENTISTA"));
        return p;
    }
}
