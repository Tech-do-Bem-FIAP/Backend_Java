package br.com.fiap.resource;

import br.com.fiap.bo.ExameBO;
import br.com.fiap.dto.ExameRequest;
import br.com.fiap.dto.ExameResponse;
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

@Path("/api/exames")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExameResource {

    private final ExameBO bo = new ExameBO();

    @GET
    public List<ExameResponse> listar() {
        return bo.listar();
    }

    @GET
    @Path("/{id}")
    public ExameResponse buscar(@PathParam("id") int id) {
        return bo.buscarPorId(id);
    }

    @POST
    public Response criar(ExameRequest req) {
        ExameResponse criado = bo.criar(req);
        return Response.status(201)
                .header("Location", "/api/exames/" + criado.idExame())
                .entity(criado).build();
    }

    @PUT
    @Path("/{id}")
    public ExameResponse atualizar(@PathParam("id") int id, ExameRequest req) {
        return bo.atualizar(id, req);
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") int id) {
        bo.deletar(id);
        return Response.noContent().build();
    }
}
