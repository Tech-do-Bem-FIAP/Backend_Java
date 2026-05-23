package br.com.fiap.mapper;

import br.com.fiap.exceptions.PersistenciaException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class PersistenciaMapper implements ExceptionMapper<PersistenciaException> {
    @Override
    public Response toResponse(PersistenciaException e) {
        return Response.status(500)
                .entity(ErroResposta.de(
                        "Falha ao acessar o banco de dados (verifique a VPN da FIAP).",
                        500))
                .build();
    }
}
