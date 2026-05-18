package br.com.fiap.bo;

import br.com.fiap.dao.ColaboradorDAO;
import br.com.fiap.dao.DentistaDAO;
import br.com.fiap.dto.LoginRequest;
import br.com.fiap.dto.LoginResponse;
import br.com.fiap.entities.Colaborador;
import br.com.fiap.entities.Dentista;
import br.com.fiap.exceptions.DadoInvalidoException;
import br.com.fiap.exceptions.PersistenciaException;

import java.sql.SQLException;

public class AuthBO {

    public LoginResponse autenticar(LoginRequest req) {
        if (req == null || req.email() == null || req.senha() == null
                || req.email().isBlank() || req.senha().isBlank()) {
            throw new DadoInvalidoException("Email e senha sao obrigatorios.");
        }

        Colaborador c = buscarColaborador(req.email(), req.senha());
        if (c != null) {
            return new LoginResponse("colaborador", c.getIdColaborador(), c.getNome());
        }

        Dentista d = buscarDentista(req.email(), req.senha());
        if (d != null) {
            return new LoginResponse("dentista", d.getIdDentista(), d.getNome());
        }

        throw new DadoInvalidoException("Email ou senha invalidos.");
    }

    private Colaborador buscarColaborador(String email, String senha) {
        ColaboradorDAO dao = null;
        try {
            dao = new ColaboradorDAO();
            for (Colaborador c : dao.selecionar()) {
                if (email.equalsIgnoreCase(c.getEmail()) && senha.equals(c.getSenha())) {
                    return c;
                }
            }
            return null;
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao autenticar colaborador", e);
        } finally {
            fechar(dao);
        }
    }

    private Dentista buscarDentista(String email, String senha) {
        DentistaDAO dao = null;
        try {
            dao = new DentistaDAO();
            for (Dentista d : dao.selecionar()) {
                if (email.equalsIgnoreCase(d.getEmail()) && senha.equals(d.getSenha())) {
                    return d;
                }
            }
            return null;
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao autenticar dentista", e);
        } finally {
            fechar(dao);
        }
    }

    private void fechar(ColaboradorDAO dao) {
        if (dao != null && dao.minhaConexao != null) {
            try { dao.minhaConexao.close(); } catch (SQLException ignored) {}
        }
    }

    private void fechar(DentistaDAO dao) {
        if (dao != null && dao.minhaConexao != null) {
            try { dao.minhaConexao.close(); } catch (SQLException ignored) {}
        }
    }
}
