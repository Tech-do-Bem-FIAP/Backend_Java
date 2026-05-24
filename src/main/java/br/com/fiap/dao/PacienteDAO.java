package br.com.fiap.dao;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.entities.Paciente;

import java.math.BigDecimal;
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
                "(ID_PACIENTE, NOME, CPF, DATA_NASC, TELEFONE, EMAIL, ID_DENTISTA, " +
                " CEP, LOGRADOURO, BAIRRO, CIDADE, UF, LATITUDE, LONGITUDE) " +
                "VALUES (SEQ_PACIENTE.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
        setStringOrNull(stmt,  7, p.getCep());
        setStringOrNull(stmt,  8, p.getLogradouro());
        setStringOrNull(stmt,  9, p.getBairro());
        setStringOrNull(stmt, 10, p.getCidade());
        setStringOrNull(stmt, 11, p.getUf());
        setDoubleOrNull(stmt, 12, p.getLatitude());
        setDoubleOrNull(stmt, 13, p.getLongitude());
        stmt.execute();
        stmt.close();
        return "Paciente cadastrado com sucesso!";
    }

    public String atualizar(Paciente p) throws SQLException {
        String sql = "UPDATE T_PACIENTE SET NOME=?, CPF=?, DATA_NASC=?, TELEFONE=?, " +
                "EMAIL=?, ID_DENTISTA=?, CEP=?, LOGRADOURO=?, BAIRRO=?, CIDADE=?, " +
                "UF=?, LATITUDE=?, LONGITUDE=? WHERE ID_PACIENTE=?";
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
        setStringOrNull(stmt,  7, p.getCep());
        setStringOrNull(stmt,  8, p.getLogradouro());
        setStringOrNull(stmt,  9, p.getBairro());
        setStringOrNull(stmt, 10, p.getCidade());
        setStringOrNull(stmt, 11, p.getUf());
        setDoubleOrNull(stmt, 12, p.getLatitude());
        setDoubleOrNull(stmt, 13, p.getLongitude());
        stmt.setInt(14, p.getIdPaciente());
        stmt.executeUpdate();
        stmt.close();
        return "Paciente atualizado com sucesso!";
    }

    private static void setStringOrNull(PreparedStatement stmt, int idx, String value) throws SQLException {
        if (value == null) {
            stmt.setNull(idx, Types.VARCHAR);
        } else {
            stmt.setString(idx, value);
        }
    }

    private static void setDoubleOrNull(PreparedStatement stmt, int idx, Double value) throws SQLException {
        if (value == null) {
            stmt.setNull(idx, Types.DOUBLE);
        } else {
            stmt.setDouble(idx, value);
        }
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
        p.setCep(rs.getString("CEP"));
        p.setLogradouro(rs.getString("LOGRADOURO"));
        p.setBairro(rs.getString("BAIRRO"));
        p.setCidade(rs.getString("CIDADE"));
        p.setUf(rs.getString("UF"));
        // CRITICO: rs.getDouble retorna 0.0 quando o valor eh NULL.
        // Usamos BigDecimal para preservar NULL em latitude/longitude.
        BigDecimal lat = rs.getBigDecimal("LATITUDE");
        BigDecimal lng = rs.getBigDecimal("LONGITUDE");
        p.setLatitude(lat  != null ? lat.doubleValue()  : null);
        p.setLongitude(lng != null ? lng.doubleValue() : null);
        return p;
    }
}
