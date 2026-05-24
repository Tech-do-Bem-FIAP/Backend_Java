package br.com.fiap.bo;

import br.com.fiap.dao.PacienteDAO;
import br.com.fiap.dto.PacienteRequest;
import br.com.fiap.dto.PacienteResponse;
import br.com.fiap.entities.Paciente;
import br.com.fiap.exceptions.DadoInvalidoException;
import br.com.fiap.exceptions.PersistenciaException;
import br.com.fiap.exceptions.RecursoNaoEncontradoException;
import br.com.fiap.mapper.DataUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacienteBO {

    public List<PacienteResponse> listar() {
        PacienteDAO dao = null;
        try {
            dao = new PacienteDAO();
            List<PacienteResponse> out = new ArrayList<>();
            for (Paciente p : dao.selecionar()) out.add(toResponse(p));
            return out;
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao listar pacientes", e);
        } finally {
            fechar(dao);
        }
    }

    public PacienteResponse buscarPorId(int id) {
        PacienteDAO dao = null;
        try {
            dao = new PacienteDAO();
            Paciente p = dao.selecionarPorId(id);
            if (p == null) {
                throw new RecursoNaoEncontradoException("Paciente " + id + " nao encontrado.");
            }
            return toResponse(p);
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao buscar paciente", e);
        } finally {
            fechar(dao);
        }
    }

    public PacienteResponse criar(PacienteRequest req) {
        Paciente p = fromRequest(req, 0);
        validar(p);
        PacienteDAO dao = null;
        try {
            dao = new PacienteDAO();
            dao.inserir(p);
            int novoId = dao.ultimoId();
            return toResponse(dao.selecionarPorId(novoId));
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao criar paciente", e);
        } finally {
            fechar(dao);
        }
    }

    public PacienteResponse atualizar(int id, PacienteRequest req) {
        Paciente p = fromRequest(req, id);
        validar(p);
        PacienteDAO dao = null;
        try {
            dao = new PacienteDAO();
            if (dao.selecionarPorId(id) == null) {
                throw new RecursoNaoEncontradoException("Paciente " + id + " nao encontrado.");
            }
            dao.atualizar(p);
            return toResponse(dao.selecionarPorId(id));
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao atualizar paciente", e);
        } finally {
            fechar(dao);
        }
    }

    public void deletar(int id) {
        PacienteDAO dao = null;
        try {
            dao = new PacienteDAO();
            if (dao.selecionarPorId(id) == null) {
                throw new RecursoNaoEncontradoException("Paciente " + id + " nao encontrado.");
            }
            dao.deletar(id);
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao deletar paciente", e);
        } finally {
            fechar(dao);
        }
    }

    private void validar(Paciente p) {
        if (!p.cadastroCompleto()) {
            throw new DadoInvalidoException(
                "Paciente invalido: verifique nome, data de nascimento, "
                + "email, telefone, CPF e idDentista.");
        }
    }

    private Paciente fromRequest(PacienteRequest r, int id) {
        return new Paciente(id, r.nome(), r.cpf(), DataUtil.parse(r.dataNasc()),
                r.telefone(), r.email(), r.idDentista(),
                r.cep(), r.logradouro(), r.bairro(),
                r.cidade(), r.uf(),
                r.latitude(), r.longitude());
    }

    public PacienteResponse toResponse(Paciente p) {
        return new PacienteResponse(p.getIdPaciente(), p.getNome(), p.getCpf(),
                DataUtil.format(p.getDataNasc()), p.getTelefone(),
                p.getEmail(), p.getIdDentista(),
                p.getCep(), p.getLogradouro(), p.getBairro(),
                p.getCidade(), p.getUf(),
                p.getLatitude(), p.getLongitude());
    }

    /**
     * Retorna apenas pacientes com latitude E longitude preenchidas.
     * Util para o mapa de geocoding na UI.
     */
    public List<Paciente> listarComGeo() {
        PacienteDAO dao = null;
        try {
            dao = new PacienteDAO();
            List<Paciente> out = new ArrayList<>();
            for (Paciente p : dao.selecionar()) {
                if (p.getLatitude() != null && p.getLongitude() != null) {
                    out.add(p);
                }
            }
            return out;
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao listar pacientes com geo", e);
        } finally {
            fechar(dao);
        }
    }

    private void fechar(PacienteDAO dao) {
        if (dao != null && dao.minhaConexao != null) {
            try { dao.minhaConexao.close(); } catch (SQLException ignored) {}
        }
    }
}
