package br.com.fiap.bo;

import br.com.fiap.dao.DentistaDAO;
import br.com.fiap.dto.DentistaRequest;
import br.com.fiap.dto.DentistaResponse;
import br.com.fiap.entities.Dentista;
import br.com.fiap.exceptions.DadoInvalidoException;
import br.com.fiap.exceptions.PersistenciaException;
import br.com.fiap.exceptions.RecursoNaoEncontradoException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DentistaBO {

    public List<DentistaResponse> listar() {
        DentistaDAO dao = null;
        try {
            dao = new DentistaDAO();
            List<DentistaResponse> out = new ArrayList<>();
            for (Dentista p : dao.selecionar()) out.add(toResponse(p));
            return out;
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao listar dentistas", e);
        } finally {
            fechar(dao);
        }
    }

    public DentistaResponse buscarPorId(int id) {
        DentistaDAO dao = null;
        try {
            dao = new DentistaDAO();
            Dentista p = dao.selecionarPorId(id);
            if (p == null) {
                throw new RecursoNaoEncontradoException("Dentista " + id + " nao encontrado.");
            }
            return toResponse(p);
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao buscar dentista", e);
        } finally {
            fechar(dao);
        }
    }

    public DentistaResponse criar(DentistaRequest req) {
        Dentista p = fromRequest(req, 0);
        validar(p);
        DentistaDAO dao = null;
        try {
            dao = new DentistaDAO();
            dao.inserir(p);
            int novoId = dao.ultimoId();
            return toResponse(dao.selecionarPorId(novoId));
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao criar dentista", e);
        } finally {
            fechar(dao);
        }
    }

    public DentistaResponse atualizar(int id, DentistaRequest req) {
        Dentista p = fromRequest(req, id);
        validar(p);
        DentistaDAO dao = null;
        try {
            dao = new DentistaDAO();
            Dentista atual = dao.selecionarPorId(id);
            if (atual == null) {
                throw new RecursoNaoEncontradoException("Dentista " + id + " nao encontrado.");
            }
            // Preserva senha atual quando o cliente não envia senha nova.
            if (p.getSenha() == null || p.getSenha().isBlank()) {
                p.setSenha(atual.getSenha());
            }
            dao.atualizar(p);
            return toResponse(dao.selecionarPorId(id));
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao atualizar dentista", e);
        } finally {
            fechar(dao);
        }
    }

    public void deletar(int id) {
        DentistaDAO dao = null;
        try {
            dao = new DentistaDAO();
            if (dao.selecionarPorId(id) == null) {
                throw new RecursoNaoEncontradoException("Dentista " + id + " nao encontrado.");
            }
            dao.deletar(id);
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao deletar dentista", e);
        } finally {
            fechar(dao);
        }
    }

    private void validar(Dentista d) {
        if (!d.validarCRO()) {
            throw new DadoInvalidoException(
                "Dentista invalido: CRO deve seguir o formato 000000-UF.");
        }
    }

    private Dentista fromRequest(DentistaRequest r, int id) {
        return new Dentista(id, r.nome(), r.cpf(), r.email(), r.senha(),
                r.cro(), r.especialidade(), r.disponibilidade(), r.idColaborador());
    }

    private DentistaResponse toResponse(Dentista d) {
        return new DentistaResponse(d.getIdDentista(), d.getNome(), d.getCpf(),
                d.getEmail(), d.getCro(), d.getEspecialidade(),
                d.getDisponibilidade(), d.getIdColaborador());
    }

    private void fechar(DentistaDAO dao) {
        if (dao != null && dao.minhaConexao != null) {
            try { dao.minhaConexao.close(); } catch (SQLException ignored) {}
        }
    }
}
