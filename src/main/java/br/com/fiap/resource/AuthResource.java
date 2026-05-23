package br.com.fiap.resource;

import br.com.fiap.bo.AuthBO;
import br.com.fiap.dto.LoginRequest;
import br.com.fiap.dto.LoginResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final AuthBO bo = new AuthBO();

    @POST
    public LoginResponse entrar(LoginRequest req) {
        return bo.autenticar(req);
    }
}
