package br.com.fiap.bo;

import br.com.fiap.dao.AtendimentoDAO;
import br.com.fiap.dto.AtendimentoRequest;
import br.com.fiap.dto.AtendimentoResponse;
import br.com.fiap.entities.Atendimento;
import br.com.fiap.entities.Campanha;
import br.com.fiap.entities.Dentista;
import br.com.fiap.entities.Paciente;
import br.com.fiap.exceptions.DadoInvalidoException;
import br.com.fiap.exceptions.PersistenciaException;
import br.com.fiap.exceptions.RecursoNaoEncontradoException;
import br.com.fiap.mapper.DataUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AtendimentoBO {

    public List<AtendimentoResponse> listar() {
        AtendimentoDAO dao = null;
        try {
            dao = new AtendimentoDAO();
            List<AtendimentoResponse> out = new ArrayList<>();
            for (Atendimento a : dao.selecionar()) out.add(toResponse(a));
            return out;
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao listar atendimentos", e);
        } finally {
            fechar(dao);
        }
    }

    public AtendimentoResponse buscarPorId(int id) {
        AtendimentoDAO dao = null;
        try {
            dao = new AtendimentoDAO();
            Atendimento a = dao.selecionarPorId(id);
            if (a == null) {
                throw new RecursoNaoEncontradoException("Atendimento " + id + " nao encontrado.");
            }
            return toResponse(a);
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao buscar atendimento", e);
        } finally {
            fechar(dao);
        }
    }

    public AtendimentoResponse criar(AtendimentoRequest req) {
        Atendimento a = fromRequest(req, 0);
        validar(a);
        AtendimentoDAO dao = null;
        try {
            dao = new AtendimentoDAO();
            dao.inserir(a);
            int novoId = dao.ultimoId();
            return toResponse(dao.selecionarPorId(novoId));
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao criar atendimento", e);
        } finally {
            fechar(dao);
        }
    }

    public AtendimentoResponse atualizar(int id, AtendimentoRequest req) {
        Atendimento a = fromRequest(req, id);
        validar(a);
        AtendimentoDAO dao = null;
        try {
            dao = new AtendimentoDAO();
            if (dao.selecionarPorId(id) == null) {
                throw new RecursoNaoEncontradoException("Atendimento " + id + " nao encontrado.");
            }
            dao.atualizar(a);
            return toResponse(dao.selecionarPorId(id));
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao atualizar atendimento", e);
        } finally {
            fechar(dao);
        }
    }

    public void deletar(int id) {
        AtendimentoDAO dao = null;
        try {
            dao = new AtendimentoDAO();
            if (dao.selecionarPorId(id) == null) {
                throw new RecursoNaoEncontradoException("Atendimento " + id + " nao encontrado.");
            }
            dao.deletar(id);
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao deletar atendimento", e);
        } finally {
            fechar(dao);
        }
    }

    private void validar(Atendimento a) {
        if (a.getData() == null || a.getStatus() == null || a.getStatus().isBlank()
                || a.getIdPaciente() == null || a.getIdPaciente().getIdPaciente() <= 0
                || a.getIdDentista() == null || a.getIdDentista().getIdDentista() <= 0
                || a.getIdCampanha() == null || a.getIdCampanha().getIdCampanha() <= 0) {
            throw new DadoInvalidoException(
                "Atendimento invalido: data, status, idPaciente, idDentista "
                + "e idCampanha sao obrigatorios.");
        }
    }

    private Atendimento fromRequest(AtendimentoRequest r, int id) {
        Paciente p = new Paciente(); p.setIdPaciente(r.idPaciente());
        Dentista d = new Dentista(); d.setIdDentista(r.idDentista());
        Campanha c = new Campanha(); c.setIdCampanha(r.idCampanha());
        Atendimento a = new Atendimento();
        a.setIdAtendimento(id);
        a.setIdPaciente(p);
        a.setIdDentista(d);
        a.setIdCampanha(c);
        a.setData(DataUtil.parseDateOrDateTime(r.data()));
        // Constraint CK_ATEND_STATUS exige lowercase ('agendado'/'realizado'/'cancelado').
        a.setStatus(r.status() == null ? null : r.status().toLowerCase());
        a.setTipo(r.tipo());
        a.setObservacoes(r.observacoes());
        return a;
    }

    private AtendimentoResponse toResponse(Atendimento a) {
        return new AtendimentoResponse(
                a.getIdAtendimento(),
                a.getIdPaciente().getIdPaciente(), a.getIdPaciente().getNome(),
                a.getIdDentista().getIdDentista(), a.getIdDentista().getNome(),
                a.getIdCampanha().getIdCampanha(), a.getIdCampanha().getNome(),
                DataUtil.formatDateTime(a.getData()), a.getStatus(), a.getTipo(),
                a.getObservacoes());
    }

    private void fechar(AtendimentoDAO dao) {
        if (dao != null && dao.minhaConexao != null) {
            try { dao.minhaConexao.close(); } catch (SQLException ignored) {}
        }
    }
}
