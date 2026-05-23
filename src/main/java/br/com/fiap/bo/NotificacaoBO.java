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
import java.util.Date;
import java.util.List;

public class NotificacaoBO {

    private static final String DEFAULT_STATUS = "enviado";
    private static final String DEFAULT_CANAL  = "app";

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

    /**
     * Marca a notificação como lida no instante atual.
     * Só faz sentido para notificações col→den (dentista é quem lê).
     */
    public NotificacaoResponse marcarLida(int id) {
        NotificacaoDAO dao = null;
        try {
            dao = new NotificacaoDAO();
            Notificacao atual = dao.selecionarPorId(id);
            if (atual == null) {
                throw new RecursoNaoEncontradoException("Notificacao " + id + " nao encontrada.");
            }
            if (atual.getIdPaciente() != null) {
                throw new DadoInvalidoException(
                        "Notificacoes a pacientes nao tem estado de leitura.");
            }
            dao.marcarLida(id, new Date());
            return toResponse(dao.selecionarPorId(id));
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao marcar notificacao como lida", e);
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

    /**
     * Regras:
     *   - mensagem obrigatória
     *   - exatamente uma direção válida:
     *       col+den (paciente null)  → colaborador → dentista
     *       den+pac (colaborador null) → dentista → paciente
     */
    private void validar(Notificacao n) {
        if (n.getMensagem() == null || n.getMensagem().isBlank()) {
            throw new DadoInvalidoException("'mensagem' e obrigatoria.");
        }

        boolean hasCol = n.getIdColaborador() != null;
        boolean hasDen = n.getIdDentista()    != null;
        boolean hasPac = n.getIdPaciente()    != null;

        boolean colToDen = hasCol && hasDen && !hasPac;
        boolean denToPac = hasDen && hasPac && !hasCol;

        if (!colToDen && !denToPac) {
            throw new DadoInvalidoException(
                    "Direcao invalida. Use idColaborador+idDentista (col->den) " +
                    "ou idDentista+idPaciente (den->pac).");
        }
    }

    private Notificacao fromRequest(NotificacaoRequest r, int id) {
        Date dataEnvio = (r.dataEnvio() == null || r.dataEnvio().isBlank())
                ? new Date()
                : DataUtil.parse(r.dataEnvio());
        String status = (r.statusEnvio() == null || r.statusEnvio().isBlank())
                ? DEFAULT_STATUS
                : r.statusEnvio();
        String canal = (r.canal() == null || r.canal().isBlank())
                ? DEFAULT_CANAL
                : r.canal();
        return new Notificacao(id, r.mensagem(), dataEnvio, status, canal,
                r.idDentista(), r.idColaborador(), r.idPaciente(), null);
    }

    private NotificacaoResponse toResponse(Notificacao n) {
        return new NotificacaoResponse(
                n.getIdNotificacao(),
                n.getMensagem(),
                DataUtil.format(n.getDataEnvio()),
                n.getStatusEnvio(),
                n.getCanal(),
                n.getIdDentista(),
                n.getIdColaborador(),
                n.getIdPaciente(),
                DataUtil.formatDateTime(n.getDataLeitura()),
                n.getNomeDentista(),
                n.getNomeColaborador(),
                n.getNomePaciente());
    }

    private void fechar(NotificacaoDAO dao) {
        if (dao != null && dao.minhaConexao != null) {
            try { dao.minhaConexao.close(); } catch (SQLException ignored) {}
        }
    }
}
