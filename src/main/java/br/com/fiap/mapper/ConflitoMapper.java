package br.com.fiap.mapper;

import br.com.fiap.exceptions.ConflitoException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ConflitoMapper implements ExceptionMapper<ConflitoException> {
    @Override
    public Response toResponse(ConflitoException e) {
        return Response.status(409)
                .entity(ErroResposta.de(e.getMessage(), 409))
                .build();
    }
}
