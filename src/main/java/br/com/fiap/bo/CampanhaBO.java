package br.com.fiap.bo;

import br.com.fiap.dao.CampanhaDAO;
import br.com.fiap.dto.CampanhaRequest;
import br.com.fiap.dto.CampanhaResponse;
import br.com.fiap.entities.Campanha;
import br.com.fiap.exceptions.DadoInvalidoException;
import br.com.fiap.exceptions.PersistenciaException;
import br.com.fiap.exceptions.RecursoNaoEncontradoException;
import br.com.fiap.mapper.DataUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CampanhaBO {

    public List<CampanhaResponse> listar() {
        CampanhaDAO dao = null;
        try {
            dao = new CampanhaDAO();
            List<CampanhaResponse> out = new ArrayList<>();
            for (Campanha p : dao.selecionar()) out.add(toResponse(p));
            return out;
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao listar campanhas", e);
        } finally {
            fechar(dao);
        }
    }

    public CampanhaResponse buscarPorId(int id) {
        CampanhaDAO dao = null;
        try {
            dao = new CampanhaDAO();
            Campanha p = dao.selecionarPorId(id);
            if (p == null) {
                throw new RecursoNaoEncontradoException("Campanha " + id + " nao encontrada.");
            }
            return toResponse(p);
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao buscar campanha", e);
        } finally {
            fechar(dao);
        }
    }

    public CampanhaResponse criar(CampanhaRequest req) {
        Campanha p = fromRequest(req, 0);
        validar(p);
        CampanhaDAO dao = null;
        try {
            dao = new CampanhaDAO();
            dao.inserir(p);
            int novoId = dao.ultimoId();
            return toResponse(dao.selecionarPorId(novoId));
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao criar campanha", e);
        } finally {
            fechar(dao);
        }
    }

    public CampanhaResponse atualizar(int id, CampanhaRequest req) {
        Campanha p = fromRequest(req, id);
        validar(p);
        CampanhaDAO dao = null;
        try {
            dao = new CampanhaDAO();
            if (dao.selecionarPorId(id) == null) {
                throw new RecursoNaoEncontradoException("Campanha " + id + " nao encontrada.");
            }
            dao.atualizar(p);
            return toResponse(dao.selecionarPorId(id));
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao atualizar campanha", e);
        } finally {
            fechar(dao);
        }
    }

    public void deletar(int id) {
        CampanhaDAO dao = null;
        try {
            dao = new CampanhaDAO();
            if (dao.selecionarPorId(id) == null) {
                throw new RecursoNaoEncontradoException("Campanha " + id + " nao encontrada.");
            }
            dao.deletar(id);
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao deletar campanha", e);
        } finally {
            fechar(dao);
        }
    }

    private void validar(Campanha c) {
        if (!c.periodoValido()) {
            throw new DadoInvalidoException(
                "Campanha invalida: dataFim nao pode ser anterior a dataInicio.");
        }
    }

    private Campanha fromRequest(CampanhaRequest r, int id) {
        return new Campanha(id, r.nome(), r.local(),
                DataUtil.parse(r.dataInicio()), DataUtil.parse(r.dataFim()),
                r.idColaborador());
    }

    private CampanhaResponse toResponse(Campanha c) {
        return new CampanhaResponse(c.getIdCampanha(), c.getNome(), c.getLocal(),
                DataUtil.format(c.getDataInicio()), DataUtil.format(c.getDataFim()),
                c.getIdColaborador());
    }

    private void fechar(CampanhaDAO dao) {
        if (dao != null && dao.minhaConexao != null) {
            try { dao.minhaConexao.close(); } catch (SQLException ignored) {}
        }
    }
}
