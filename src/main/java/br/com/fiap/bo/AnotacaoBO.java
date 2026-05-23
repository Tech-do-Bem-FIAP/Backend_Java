package br.com.fiap.bo;

import br.com.fiap.dao.AnotacaoDAO;
import br.com.fiap.dto.AnotacaoRequest;
import br.com.fiap.dto.AnotacaoResponse;
import br.com.fiap.entities.Anotacao;
import br.com.fiap.exceptions.DadoInvalidoException;
import br.com.fiap.exceptions.PersistenciaException;
import br.com.fiap.exceptions.RecursoNaoEncontradoException;
import br.com.fiap.mapper.DataUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class AnotacaoBO {

    private static final Set<String> AUTOR_TIPOS = Set.of("dentista", "colaborador");
    private static final Set<String> SOBRE_TIPOS = Set.of("dentista", "paciente");

    public List<AnotacaoResponse> listarPorSobre(String sobreTipo, int sobreId) {
        validarSobreTipo(sobreTipo);
        AnotacaoDAO dao = null;
        try {
            dao = new AnotacaoDAO();
            List<AnotacaoResponse> out = new ArrayList<>();
            for (Anotacao a : dao.selecionarPorSobre(sobreTipo, sobreId)) {
                out.add(toResponse(a));
            }
            return out;
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao listar anotacoes", e);
        } finally {
            fechar(dao);
        }
    }

    public AnotacaoResponse criar(AnotacaoRequest req) {
        Anotacao a = fromRequest(req, 0);
        validar(a);
        AnotacaoDAO dao = null;
        try {
            dao = new AnotacaoDAO();
            dao.inserir(a);
            int novoId = dao.ultimoId();
            return toResponse(dao.selecionarPorId(novoId));
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao criar anotacao", e);
        } finally {
            fechar(dao);
        }
    }

    public void deletar(int id) {
        AnotacaoDAO dao = null;
        try {
            dao = new AnotacaoDAO();
            if (dao.selecionarPorId(id) == null) {
                throw new RecursoNaoEncontradoException("Anotacao " + id + " nao encontrada.");
            }
            dao.deletar(id);
        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Erro ao deletar anotacao", e);
        } finally {
            fechar(dao);
        }
    }

    private void validar(Anotacao a) {
        if (a.getTexto() == null || a.getTexto().isBlank()) {
            throw new DadoInvalidoException("'texto' e obrigatorio.");
        }
        validarAutorTipo(a.getAutorTipo());
        validarSobreTipo(a.getSobreTipo());
    }

    private void validarAutorTipo(String t) {
        if (t == null || !AUTOR_TIPOS.contains(t.toLowerCase())) {
            throw new DadoInvalidoException("autorTipo deve ser 'dentista' ou 'colaborador'.");
        }
    }

    private void validarSobreTipo(String t) {
        if (t == null || !SOBRE_TIPOS.contains(t.toLowerCase())) {
            throw new DadoInvalidoException("sobreTipo deve ser 'dentista' ou 'paciente'.");
        }
    }

    private Anotacao fromRequest(AnotacaoRequest r, int id) {
        Date data = (r.data() == null || r.data().isBlank())
                ? new Date()
                : DataUtil.parse(r.data());
        return new Anotacao(id, r.texto(), data,
                r.autorTipo() == null ? null : r.autorTipo().toLowerCase(),
                r.autorId(),
                r.sobreTipo() == null ? null : r.sobreTipo().toLowerCase(),
                r.sobreId());
    }

    private AnotacaoResponse toResponse(Anotacao a) {
        return new AnotacaoResponse(
                a.getIdAnotacao(),
                a.getTexto(),
                DataUtil.format(a.getData()),
                a.getAutorTipo(),
                a.getAutorId(),
                a.getSobreTipo(),
                a.getSobreId());
    }

    private void fechar(AnotacaoDAO dao) {
        if (dao != null && dao.minhaConexao != null) {
            try { dao.minhaConexao.close(); } catch (SQLException ignored) {}
        }
    }
}
