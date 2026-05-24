package br.com.fiap.resource;

import br.com.fiap.bo.RelatorioBO;
import br.com.fiap.dto.RelatorioResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/relatorios")
@Produces(MediaType.APPLICATION_JSON)
public class RelatorioResource {

    private final RelatorioBO bo = new RelatorioBO();

    @GET
    public RelatorioResponse gerar() {
        return bo.gerar();
    }
}
