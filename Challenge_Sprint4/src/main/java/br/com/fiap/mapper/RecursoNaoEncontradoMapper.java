package br.com.fiap.mapper;

import br.com.fiap.exceptions.RecursoNaoEncontradoException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RecursoNaoEncontradoMapper
        implements ExceptionMapper<RecursoNaoEncontradoException> {
    @Override
    public Response toResponse(RecursoNaoEncontradoException e) {
        return Response.status(404)
                .entity(ErroResposta.de(e.getMessage(), 404))
                .build();
    }
}
