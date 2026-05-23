package br.com.fiap.dao;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.entities.Dentista;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

public class DentistaDAO {

    public Connection minhaConexao;

    public DentistaDAO() throws SQLException, ClassNotFoundException {
        this.minhaConexao = new ConexaoFactory().conexao();
    }

    public String inserir(Dentista d) throws SQLException {
        if (!d.validarCRO()) {
            return "Erro: CRO invalido. Use o formato 999999-UF (ex.: 100001-SP).";
        }
        if (!d.cpfValido()) {
            return "Erro: CPF invalido. Informe 11 digitos numericos.";
        }
        String sql = "INSERT INTO T_DENTISTA " +
                "(ID_DENTISTA, NOME, CPF, EMAIL, SENHA, CRO, ESPECIALIDADE, " +
                "DISPONIBILIDADE, T_COLABORADOR_ID_COLABORADOR) " +
                "VALUES (SEQ_DENTISTA.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setString(1, d.getNome());
        stmt.setString(2, d.getCpf());
        stmt.setString(3, d.getEmail());
        stmt.setString(4, d.getSenha());
        stmt.setString(5, d.getCro());
        stmt.setString(6, d.getEspecialidade());
        stmt.setInt(7, d.getDisponibilidade());
        if (d.getIdColaborador() != null) {
            stmt.setInt(8, d.getIdColaborador());
        } else {
            stmt.setNull(8, Types.INTEGER);
        }
        stmt.execute();
        stmt.close();
        return "Dentista cadastrado com sucesso!";
    }

    public String atualizar(Dentista d) throws SQLException {
        if (!d.validarCRO()) {
            return "Erro: CRO invalido. Use o formato 999999-UF (ex.: 100001-SP).";
        }
        String sql = "UPDATE T_DENTISTA SET NOME=?, CPF=?, EMAIL=?, SENHA=?, CRO=?, " +
                "ESPECIALIDADE=?, DISPONIBILIDADE=?, T_COLABORADOR_ID_COLABORADOR=? " +
                "WHERE ID_DENTISTA=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setString(1, d.getNome());
        stmt.setString(2, d.getCpf());
        stmt.setString(3, d.getEmail());
        stmt.setString(4, d.getSenha());
        stmt.setString(5, d.getCro());
        stmt.setString(6, d.getEspecialidade());
        stmt.setInt(7, d.getDisponibilidade());
        if (d.getIdColaborador() != null) {
            stmt.setInt(8, d.getIdColaborador());
        } else {
            stmt.setNull(8, Types.INTEGER);
        }
        stmt.setInt(9, d.getIdDentista());
        stmt.executeUpdate();
        stmt.close();
        return "Dentista atualizado com sucesso!";
    }

    public String deletar(int id) throws SQLException {
        String sql = "DELETE FROM T_DENTISTA WHERE ID_DENTISTA=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.execute();
        stmt.close();
        return "Dentista deletado com sucesso!";
    }

    public ArrayList<Dentista> selecionar() throws SQLException {
        ArrayList<Dentista> lista = new ArrayList<>();
        String sql = "SELECT * FROM T_DENTISTA ORDER BY NOME";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(mapear(rs));
        }
        return lista;
    }

    public Dentista selecionarPorId(int id) throws SQLException {
        Dentista d = null;
        String sql = "SELECT * FROM T_DENTISTA WHERE ID_DENTISTA=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            d = mapear(rs);
        }
        return d;
    }

    /** Retorna o último ID gerado pela sequência nesta sessão. */
    public int ultimoId() throws SQLException {
        PreparedStatement stmt =
                minhaConexao.prepareStatement("SELECT SEQ_DENTISTA.CURRVAL FROM DUAL");
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    private Dentista mapear(ResultSet rs) throws SQLException {
        Dentista d = new Dentista();
        d.setIdDentista(rs.getInt("ID_DENTISTA"));
        d.setNome(rs.getString("NOME"));
        d.setCpf(rs.getString("CPF"));
        d.setEmail(rs.getString("EMAIL"));
        d.setSenha(rs.getString("SENHA"));
        d.setCro(rs.getString("CRO"));
        d.setEspecialidade(rs.getString("ESPECIALIDADE"));
        d.setDisponibilidade(rs.getInt("DISPONIBILIDADE"));
        int idColab = rs.getInt("T_COLABORADOR_ID_COLABORADOR");
        d.setIdColaborador(rs.wasNull() ? null : idColab);
        return d;
    }
}
