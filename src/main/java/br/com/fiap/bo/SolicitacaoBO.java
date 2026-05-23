package br.com.fiap.bo;

import br.com.fiap.dao.ColaboradorDAO;
import br.com.fiap.dao.SolicitacaoDAO;
import br.com.fiap.dto.SolicitacaoRequest;
import br.com.fiap.dto.SolicitacaoResponse;
import br.com.fiap.dto.SolicitacaoReviewRequest;
import br.com.fiap.entities.Colaborador;
import br.com.fiap.entities.Solicitacao;
import br.com.fiap.exceptions.DadoInvalidoException;
import br.com.fiap.exceptions.PersistenciaException;
import br.com.fiap.exceptions.RecursoNaoEncontradoException;
import br.com.fiap.mapper.DataUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class SolicitacaoBO {

    private static final Set<String> STATUS_REVISAO = Set.of("aprovada", "rejeitada");
    private static final Set<String> CARGOS = Set.of(
            "Administrador", "Coordenador", "Auxiliar", "Estagiário");
    /** T_COLABORADOR.DISPONIBILIDADE é booleano (0/1): 1 = disponível. */
    private static final int DISPONIBILIDADE_DEFAULT = 1;

    public List<SolicitacaoResponse> listar() {
        SolicitacaoDAO dao = null;
        try {
            dao = new SolicitacaoDAO();
            List<SolicitacaoResponse> out = new ArrayList<>();
            for (Solicitacao s : dao.selecionar()) out.add(toResponse(s, null));
            return out;
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao listar solicitacoes", e);
        } finally {
            fechar(dao);
        }
    }

    public SolicitacaoResponse buscarPorId(int id) {
        SolicitacaoDAO dao = null;
        try {
            dao = new SolicitacaoDAO();
            Solicitacao s = dao.selecionarPorId(id);
            if (s == null) {
                throw new RecursoNaoEncontradoException("Solicitacao " + id + " nao encontrada.");
            }
            return toResponse(s, null);
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao buscar solicitacao", e);
        } finally {
            fechar(dao);
        }
    }

    public SolicitacaoResponse criar(SolicitacaoRequest req) {
        validar(req);
        Solicitacao s = new Solicitacao();
        s.setTipo(req.tipo().trim());
        s.setDescricao(req.descricao().trim());

        boolean externo = req.idSolicitante() == null || req.idSolicitante() <= 0;
        if (externo) {
            s.setIdSolicitante(null);
            s.setNomeExterno(req.nomeExterno().trim());
            s.setCpfExterno(
                    req.cpfExterno() != null ? req.cpfExterno().trim() : null);
            s.setEmailExterno(req.emailExterno().trim());
            s.setSenhaExterno(req.senhaExterno()); // sem trim (senha)
            s.setTelefoneExterno(
                    req.telefoneExterno() != null ? req.telefoneExterno().trim() : null);
        } else {
            s.setIdSolicitante(req.idSolicitante());
        }

        SolicitacaoDAO dao = null;
        try {
            dao = new SolicitacaoDAO();
            dao.inserir(s);
            int novoId = dao.ultimoId();
            return toResponse(dao.selecionarPorId(novoId), null);
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao criar solicitacao", e);
        } finally {
            fechar(dao);
        }
    }

    public SolicitacaoResponse revisar(int id, SolicitacaoReviewRequest req) {
        if (req.status() == null || !STATUS_REVISAO.contains(req.status().toLowerCase())) {
            throw new DadoInvalidoException("status deve ser 'aprovada' ou 'rejeitada'.");
        }
        String statusNovo = req.status().toLowerCase();

        // 1) Lê a solicitação atual e fecha a conexão imediatamente — o DB da FIAP
        // não gosta de conexões simultâneas dentro da mesma chamada.
        Solicitacao atual = buscarComConexaoEfemera(id);
        if (!"pendente".equalsIgnoreCase(atual.getStatus())) {
            throw new DadoInvalidoException(
                    "Solicitacao ja foi revisada (status atual: " + atual.getStatus() + ").");
        }
        boolean externa = atual.getIdSolicitante() == null
                && atual.getNomeExterno() != null;

        // 2) Aprovação externa: cria o colaborador antes (conexão própria, fechada no fim).
        Integer idColabCriado = null;
        if (statusNovo.equals("aprovada") && externa) {
            idColabCriado = criarColaboradorDeSolicitacao(atual, req.cargo());
        }

        // 3) Marca a solicitação como revisada.
        SolicitacaoDAO dao = null;
        try {
            dao = new SolicitacaoDAO();
            dao.revisar(id, statusNovo, req.idRevisor(),
                    req.comentario(), new Date());
            return toResponse(dao.selecionarPorId(id), idColabCriado);
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao revisar solicitacao", e);
        } finally {
            fechar(dao);
        }
    }

    private Solicitacao buscarComConexaoEfemera(int id) {
        SolicitacaoDAO dao = null;
        try {
            dao = new SolicitacaoDAO();
            Solicitacao s = dao.selecionarPorId(id);
            if (s == null) {
                throw new RecursoNaoEncontradoException("Solicitacao " + id + " nao encontrada.");
            }
            return s;
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao buscar solicitacao", e);
        } finally {
            fechar(dao);
        }
    }

    /** Cria T_COLABORADOR usando os dados externos da solicitação. */
    private Integer criarColaboradorDeSolicitacao(Solicitacao s, String cargo) {
        if (cargo == null || cargo.isBlank() || !CARGOS.contains(cargo)) {
            throw new DadoInvalidoException(
                    "Para aprovar pedido externo, informe um cargo valido "
                    + "(Administrador, Coordenador, Auxiliar ou Estagiário).");
        }
        if (s.getCpfExterno() == null || s.getCpfExterno().isBlank()) {
            throw new DadoInvalidoException(
                    "Solicitacao externa nao tem CPF; nao da pra criar o colaborador.");
        }
        Colaborador c = new Colaborador(0,
                s.getNomeExterno(),
                s.getCpfExterno(),
                s.getEmailExterno(),
                s.getSenhaExterno(),
                cargo,
                DISPONIBILIDADE_DEFAULT);
        if (!c.cpfValido()) {
            throw new DadoInvalidoException(
                    "CPF da solicitacao invalido (esperado 11 digitos).");
        }
        ColaboradorDAO dao = null;
        try {
            dao = new ColaboradorDAO();
            dao.inserir(c);
            return dao.ultimoId();
        } catch (SQLException e) {
            throw SqlConflictDetector.traduzir(e, "criar colaborador da solicitacao");
        } catch (ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao criar colaborador da solicitacao", e);
        } finally {
            if (dao != null && dao.minhaConexao != null) {
                try { dao.minhaConexao.close(); } catch (SQLException ignored) {}
            }
        }
    }

    public void deletar(int id) {
        SolicitacaoDAO dao = null;
        try {
            dao = new SolicitacaoDAO();
            Solicitacao atual = dao.selecionarPorId(id);
            if (atual == null) {
                throw new RecursoNaoEncontradoException("Solicitacao " + id + " nao encontrada.");
            }
            dao.deletar(id);
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao deletar solicitacao", e);
        } finally {
            fechar(dao);
        }
    }

    private void validar(SolicitacaoRequest req) {
        if (req.tipo() == null || req.tipo().isBlank()) {
            throw new DadoInvalidoException("'tipo' e obrigatorio.");
        }
        if (req.descricao() == null || req.descricao().isBlank()) {
            throw new DadoInvalidoException("'descricao' e obrigatoria.");
        }
        boolean temInterno = req.idSolicitante() != null && req.idSolicitante() > 0;
        boolean temExterno = req.nomeExterno() != null && !req.nomeExterno().isBlank();
        if (temInterno && temExterno) {
            throw new DadoInvalidoException(
                    "Envie 'idSolicitante' OU dados externos, nao ambos.");
        }
        if (!temInterno && !temExterno) {
            throw new DadoInvalidoException(
                    "Informe 'idSolicitante' ou os dados externos (nome/email/senha).");
        }
        if (temExterno) {
            if (req.emailExterno() == null || req.emailExterno().isBlank()) {
                throw new DadoInvalidoException("'emailExterno' e obrigatorio.");
            }
            if (req.senhaExterno() == null || req.senhaExterno().isBlank()) {
                throw new DadoInvalidoException("'senhaExterno' e obrigatorio.");
            }
        }
    }

    private SolicitacaoResponse toResponse(Solicitacao s, Integer idColaboradorCriado) {
        return new SolicitacaoResponse(
                s.getIdSolicitacao(),
                s.getIdSolicitante(),
                s.getNomeSolicitante(),
                s.getTipo(),
                s.getDescricao(),
                s.getStatus(),
                DataUtil.format(s.getDataSolicitacao()),
                s.getIdRevisor(),
                s.getNomeRevisor(),
                s.getDataRevisao() != null ? DataUtil.format(s.getDataRevisao()) : null,
                s.getComentarioRevisao(),
                s.getNomeExterno(),
                s.getCpfExterno(),
                s.getEmailExterno(),
                s.getSenhaExterno(),
                s.getTelefoneExterno(),
                idColaboradorCriado);
    }

    private void fechar(SolicitacaoDAO dao) {
        if (dao != null && dao.minhaConexao != null) {
            try { dao.minhaConexao.close(); } catch (SQLException ignored) {}
        }
    }
}
