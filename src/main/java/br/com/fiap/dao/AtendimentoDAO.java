package br.com.fiap.dao;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.entities.Atendimento;
import br.com.fiap.entities.Campanha;
import br.com.fiap.entities.Dentista;
import br.com.fiap.entities.Paciente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AtendimentoDAO {

    public Connection minhaConexao;

    public AtendimentoDAO() throws SQLException, ClassNotFoundException {
        this.minhaConexao = new ConexaoFactory().conexao();
    }

    public String inserir(Atendimento a) throws SQLException {
        String sql = "INSERT INTO T_ATENDIMENTO " +
                "(ID_ATENDIMENTO, DATA, TIPO, STATUS, OBSERVACOES, " +
                "T_PACIENTE_ID_PACIENTE, T_DENTISTA_ID_DENTISTA, ID_CAMPANHA) " +
                "VALUES (SEQ_ATENDIMENTO.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setTimestamp(1, new java.sql.Timestamp(a.getData().getTime()));
        stmt.setString(2, a.getTipo());
        stmt.setString(3, a.getStatus());
        stmt.setString(4, a.getObservacoes());
        stmt.setInt(5, a.getIdPaciente().getIdPaciente());
        stmt.setInt(6, a.getIdDentista().getIdDentista());
        stmt.setInt(7, a.getIdCampanha().getIdCampanha());
        stmt.execute();
        stmt.close();
        return "Atendimento cadastrado com sucesso!";
    }

    public String atualizar(Atendimento a) throws SQLException {
        String sql = "UPDATE T_ATENDIMENTO SET DATA=?, TIPO=?, STATUS=?, OBSERVACOES=?, " +
                "T_PACIENTE_ID_PACIENTE=?, T_DENTISTA_ID_DENTISTA=?, ID_CAMPANHA=? " +
                "WHERE ID_ATENDIMENTO=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setTimestamp(1, new java.sql.Timestamp(a.getData().getTime()));
        stmt.setString(2, a.getTipo());
        stmt.setString(3, a.getStatus());
        stmt.setString(4, a.getObservacoes());
        stmt.setInt(5, a.getIdPaciente().getIdPaciente());
        stmt.setInt(6, a.getIdDentista().getIdDentista());
        stmt.setInt(7, a.getIdCampanha().getIdCampanha());
        stmt.setInt(8, a.getIdAtendimento());
        stmt.executeUpdate();
        stmt.close();
        return "Atendimento atualizado com sucesso!";
    }

    public String deletar(int id) throws SQLException {
        String sql = "DELETE FROM T_ATENDIMENTO WHERE ID_ATENDIMENTO=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.execute();
        stmt.close();
        return "Atendimento deletado com sucesso!";
    }

    public ArrayList<Atendimento> selecionar() throws SQLException {
        ArrayList<Atendimento> lista = new ArrayList<>();
        String sql = baseSelect() + " ORDER BY A.DATA DESC";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            lista.add(mapearAtendimento(rs));
        }
        return lista;
    }

    public Atendimento selecionarPorId(int id) throws SQLException {
        Atendimento a = null;
        String sql = baseSelect() + " WHERE A.ID_ATENDIMENTO=?";
        PreparedStatement stmt = minhaConexao.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            a = mapearAtendimento(rs);
        }
        return a;
    }

    /** Retorna o último ID gerado pela sequência nesta sessão. */
    public int ultimoId() throws SQLException {
        PreparedStatement stmt =
                minhaConexao.prepareStatement("SELECT SEQ_ATENDIMENTO.CURRVAL FROM DUAL");
        ResultSet rs = stmt.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    private String baseSelect() {
        return "SELECT A.*, " +
                "P.NOME AS NOME_PACIENTE, P.EMAIL AS EMAIL_PACIENTE, P.DATA_NASC AS NASC_PACIENTE, " +
                "D.NOME AS NOME_DENTISTA, D.CRO, D.DISPONIBILIDADE AS DISP_DENTISTA, " +
                "C.NOME AS NOME_CAMPANHA, C.LOCAL " +
                "FROM T_ATENDIMENTO A " +
                "JOIN T_PACIENTE P ON A.T_PACIENTE_ID_PACIENTE = P.ID_PACIENTE " +
                "JOIN T_DENTISTA D ON A.T_DENTISTA_ID_DENTISTA = D.ID_DENTISTA " +
                "JOIN T_CAMPANHA C ON A.ID_CAMPANHA = C.ID_CAMPANHA";
    }

    private Atendimento mapearAtendimento(ResultSet rs) throws SQLException {
        Paciente p = new Paciente();
        p.setIdPaciente(rs.getInt("T_PACIENTE_ID_PACIENTE"));
        p.setNome(rs.getString("NOME_PACIENTE"));
        p.setEmail(rs.getString("EMAIL_PACIENTE"));
        p.setDataNasc(rs.getDate("NASC_PACIENTE"));

        Dentista d = new Dentista();
        d.setIdDentista(rs.getInt("T_DENTISTA_ID_DENTISTA"));
        d.setNome(rs.getString("NOME_DENTISTA"));
        d.setCro(rs.getString("CRO"));
        d.setDisponibilidade(rs.getInt("DISP_DENTISTA"));

        Campanha c = new Campanha();
        c.setIdCampanha(rs.getInt("ID_CAMPANHA"));
        c.setNome(rs.getString("NOME_CAMPANHA"));
        c.setLocal(rs.getString("LOCAL"));

        Atendimento a = new Atendimento();
        a.setIdAtendimento(rs.getInt("ID_ATENDIMENTO"));
        a.setIdPaciente(p);
        a.setIdDentista(d);
        a.setIdCampanha(c);
        a.setData(rs.getTimestamp("DATA"));
        a.setStatus(rs.getString("STATUS"));
        a.setTipo(rs.getString("TIPO"));
        a.setObservacoes(rs.getString("OBSERVACOES"));
        return a;
    }
}
