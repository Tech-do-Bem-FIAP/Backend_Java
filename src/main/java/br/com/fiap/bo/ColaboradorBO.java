package br.com.fiap.bo;

import br.com.fiap.dao.ColaboradorDAO;
import br.com.fiap.dto.ColaboradorRequest;
import br.com.fiap.dto.ColaboradorResponse;
import br.com.fiap.entities.Colaborador;
import br.com.fiap.exceptions.DadoInvalidoException;
import br.com.fiap.exceptions.PersistenciaException;
import br.com.fiap.exceptions.RecursoNaoEncontradoException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ColaboradorBO {

    public List<ColaboradorResponse> listar() {
        ColaboradorDAO dao = null;
        try {
            dao = new ColaboradorDAO();
            List<ColaboradorResponse> out = new ArrayList<>();
            for (Colaborador p : dao.selecionar()) out.add(toResponse(p));
            return out;
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao listar colaboradores", e);
        } finally {
            fechar(dao);
        }
    }

    public ColaboradorResponse buscarPorId(int id) {
        ColaboradorDAO dao = null;
        try {
            dao = new ColaboradorDAO();
            Colaborador p = dao.selecionarPorId(id);
            if (p == null) {
                throw new RecursoNaoEncontradoException("Colaborador " + id + " nao encontrado.");
            }
            return toResponse(p);
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao buscar colaborador", e);
        } finally {
            fechar(dao);
        }
    }

    public ColaboradorResponse criar(ColaboradorRequest req) {
        Colaborador p = fromRequest(req, 0);
        validar(p);
        ColaboradorDAO dao = null;
        try {
            dao = new ColaboradorDAO();
            dao.inserir(p);
            int novoId = dao.ultimoId();
            return toResponse(dao.selecionarPorId(novoId));
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao criar colaborador", e);
        } finally {
            fechar(dao);
        }
    }

    public ColaboradorResponse atualizar(int id, ColaboradorRequest req) {
        Colaborador p = fromRequest(req, id);
        validar(p);
        ColaboradorDAO dao = null;
        try {
            dao = new ColaboradorDAO();
            Colaborador atual = dao.selecionarPorId(id);
            if (atual == null) {
                throw new RecursoNaoEncontradoException("Colaborador " + id + " nao encontrado.");
            }
            // Preserva senha atual quando o cliente não envia senha nova.
            if (p.getSenha() == null || p.getSenha().isBlank()) {
                p.setSenha(atual.getSenha());
            }
            dao.atualizar(p);
            return toResponse(dao.selecionarPorId(id));
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao atualizar colaborador", e);
        } finally {
            fechar(dao);
        }
    }

    public void deletar(int id) {
        ColaboradorDAO dao = null;
        try {
            dao = new ColaboradorDAO();
            if (dao.selecionarPorId(id) == null) {
                throw new RecursoNaoEncontradoException("Colaborador " + id + " nao encontrado.");
            }
            dao.deletar(id);
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao deletar colaborador", e);
        } finally {
            fechar(dao);
        }
    }

    private void validar(Colaborador c) {
        if (!c.cpfValido()) {
            throw new DadoInvalidoException("Colaborador invalido: CPF deve ter 11 digitos.");
        }
    }

    private Colaborador fromRequest(ColaboradorRequest r, int id) {
        return new Colaborador(id, r.nome(), r.cpf(), r.email(),
                r.senha(), r.cargo(), r.disponibilidade());
    }

    private ColaboradorResponse toResponse(Colaborador c) {
        return new ColaboradorResponse(c.getIdColaborador(), c.getNome(), c.getCpf(),
                c.getEmail(), c.getCargo(), c.getDisponibilidade());
    }

    private void fechar(ColaboradorDAO dao) {
        if (dao != null && dao.minhaConexao != null) {
            try { dao.minhaConexao.close(); } catch (SQLException ignored) {}
        }
    }
}
