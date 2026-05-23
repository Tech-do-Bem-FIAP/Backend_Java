package br.com.fiap.resource;

import br.com.fiap.bo.AtendimentoBO;
import br.com.fiap.dto.AtendimentoRequest;
import br.com.fiap.dto.AtendimentoResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/atendimentos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AtendimentoResource {

    private final AtendimentoBO bo = new AtendimentoBO();

    @GET
    public List<AtendimentoResponse> listar() {
        return bo.listar();
    }

    @GET
    @Path("/{id}")
    public AtendimentoResponse buscar(@PathParam("id") int id) {
        return bo.buscarPorId(id);
    }

    @POST
    public Response criar(AtendimentoRequest req) {
        AtendimentoResponse criado = bo.criar(req);
        return Response.status(201)
                .header("Location", "/api/atendimentos/" + criado.idAtendimento())
                .entity(criado).build();
    }

    @PUT
    @Path("/{id}")
    public AtendimentoResponse atualizar(@PathParam("id") int id, AtendimentoRequest req) {
        return bo.atualizar(id, req);
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") int id) {
        bo.deletar(id);
        return Response.noContent().build();
    }
}
