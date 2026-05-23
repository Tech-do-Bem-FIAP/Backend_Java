package br.com.fiap.bo;

import br.com.fiap.dao.NotificacaoDAO;
import br.com.fiap.dto.NotificacaoRequest;
import br.com.fiap.dto.NotificacaoResponse;
import br.com.fiap.entities.Notificacao;
import br.com.fiap.exceptions.DadoInvalidoException;
import br.com.fiap.exceptions.PersistenciaException;
import br.com.fiap.exceptions.RecursoNaoEncontradoException;
import br.com.fiap.mapper.DataUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificacaoBO {

    public List<NotificacaoResponse> listar() {
        NotificacaoDAO dao = null;
        try {
            dao = new NotificacaoDAO();
            List<NotificacaoResponse> out = new ArrayList<>();
            for (Notificacao p : dao.selecionar()) out.add(toResponse(p));
            return out;
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao listar notificacoes", e);
        } finally {
            fechar(dao);
        }
    }

    public NotificacaoResponse buscarPorId(int id) {
        NotificacaoDAO dao = null;
        try {
            dao = new NotificacaoDAO();
            Notificacao p = dao.selecionarPorId(id);
            if (p == null) {
                throw new RecursoNaoEncontradoException("Notificacao " + id + " nao encontrada.");
            }
            return toResponse(p);
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao buscar notificacao", e);
        } finally {
            fechar(dao);
        }
    }

    public NotificacaoResponse criar(NotificacaoRequest req) {
        Notificacao p = fromRequest(req, 0);
        validar(p);
        NotificacaoDAO dao = null;
        try {
            dao = new NotificacaoDAO();
            dao.inserir(p);
            int novoId = dao.ultimoId();
            return toResponse(dao.selecionarPorId(novoId));
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao criar notificacao", e);
        } finally {
            fechar(dao);
        }
    }

    public NotificacaoResponse atualizar(int id, NotificacaoRequest req) {
        Notificacao p = fromRequest(req, id);
        validar(p);
        NotificacaoDAO dao = null;
        try {
            dao = new NotificacaoDAO();
            if (dao.selecionarPorId(id) == null) {
                throw new RecursoNaoEncontradoException("Notificacao " + id + " nao encontrada.");
            }
            dao.atualizar(p);
            return toResponse(dao.selecionarPorId(id));
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao atualizar notificacao", e);
        } finally {
            fechar(dao);
        }
    }

    public void deletar(int id) {
        NotificacaoDAO dao = null;
        try {
            dao = new NotificacaoDAO();
            if (dao.selecionarPorId(id) == null) {
                throw new RecursoNaoEncontradoException("Notificacao " + id + " nao encontrada.");
            }
            dao.deletar(id);
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao deletar notificacao", e);
        } finally {
            fechar(dao);
        }
    }

    private void validar(Notificacao n) {
        if (n.getMensagem() == null || n.getMensagem().isBlank()) {
            throw new DadoInvalidoException("Notificacao invalida: 'mensagem' e obrigatoria.");
        }
    }

    private Notificacao fromRequest(NotificacaoRequest r, int id) {
        return new Notificacao(id, r.mensagem(), DataUtil.parse(r.dataEnvio()),
                r.statusEnvio(), r.canal(), r.idDentista(), r.idColaborador());
    }

    private NotificacaoResponse toResponse(Notificacao n) {
        return new NotificacaoResponse(n.getIdNotificacao(), n.getMensagem(),
                DataUtil.format(n.getDataEnvio()), n.getStatusEnvio(), n.getCanal(),
                n.getIdDentista(), n.getIdColaborador());
    }

    private void fechar(NotificacaoDAO dao) {
        if (dao != null && dao.minhaConexao != null) {
            try { dao.minhaConexao.close(); } catch (SQLException ignored) {}
        }
    }
}
