package br.com.fiap.mapper;

import br.com.fiap.exceptions.DadoInvalidoException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DadoInvalidoMapper implements ExceptionMapper<DadoInvalidoException> {
    @Override
    public Response toResponse(DadoInvalidoException e) {
        return Response.status(400)
                .entity(ErroResposta.de(e.getMessage(), 400))
                .build();
    }
}
