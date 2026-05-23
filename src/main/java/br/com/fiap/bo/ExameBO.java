package br.com.fiap.bo;

import br.com.fiap.dao.ExameDAO;
import br.com.fiap.dto.ExameRequest;
import br.com.fiap.dto.ExameResponse;
import br.com.fiap.entities.Exame;
import br.com.fiap.exceptions.DadoInvalidoException;
import br.com.fiap.exceptions.PersistenciaException;
import br.com.fiap.exceptions.RecursoNaoEncontradoException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExameBO {

    public List<ExameResponse> listar() {
        ExameDAO dao = null;
        try {
            dao = new ExameDAO();
            List<ExameResponse> out = new ArrayList<>();
            for (Exame p : dao.selecionar()) out.add(toResponse(p));
            return out;
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao listar exames", e);
        } finally {
            fechar(dao);
        }
    }

    public ExameResponse buscarPorId(int id) {
        ExameDAO dao = null;
        try {
            dao = new ExameDAO();
            Exame p = dao.selecionarPorId(id);
            if (p == null) {
                throw new RecursoNaoEncontradoException("Exame " + id + " nao encontrado.");
            }
            return toResponse(p);
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao buscar exame", e);
        } finally {
            fechar(dao);
        }
    }

    public ExameResponse criar(ExameRequest req) {
        Exame p = fromRequest(req, 0);
        validar(p);
        ExameDAO dao = null;
        try {
            dao = new ExameDAO();
            dao.inserir(p);
            int novoId = dao.ultimoId();
            return toResponse(dao.selecionarPorId(novoId));
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao criar exame", e);
        } finally {
            fechar(dao);
        }
    }

    public ExameResponse atualizar(int id, ExameRequest req) {
        Exame p = fromRequest(req, id);
        validar(p);
        ExameDAO dao = null;
        try {
            dao = new ExameDAO();
            if (dao.selecionarPorId(id) == null) {
                throw new RecursoNaoEncontradoException("Exame " + id + " nao encontrado.");
            }
            dao.atualizar(p);
            return toResponse(dao.selecionarPorId(id));
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao atualizar exame", e);
        } finally {
            fechar(dao);
        }
    }

    public void deletar(int id) {
        ExameDAO dao = null;
        try {
            dao = new ExameDAO();
            if (dao.selecionarPorId(id) == null) {
                throw new RecursoNaoEncontradoException("Exame " + id + " nao encontrado.");
            }
            dao.deletar(id);
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao deletar exame", e);
        } finally {
            fechar(dao);
        }
    }

    private void validar(Exame e) {
        if (e.getTipo() == null || e.getTipo().isBlank() || e.getIdAtendimento() <= 0) {
            throw new DadoInvalidoException(
                "Exame invalido: 'tipo' e 'idAtendimento' sao obrigatorios.");
        }
    }

    private Exame fromRequest(ExameRequest r, int id) {
        return new Exame(id, r.tipo(), r.requisitos(), r.resultado(), r.idAtendimento());
    }

    private ExameResponse toResponse(Exame e) {
        return new ExameResponse(e.getIdExame(), e.getTipo(), e.getRequisitos(),
                e.getResultado(), e.getIdAtendimento());
    }

    private void fechar(ExameDAO dao) {
        if (dao != null && dao.minhaConexao != null) {
            try { dao.minhaConexao.close(); } catch (SQLException ignored) {}
        }
    }
}
