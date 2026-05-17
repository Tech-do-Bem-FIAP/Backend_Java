package br.com.fiap.resource;

import br.com.fiap.bo.DentistaBO;
import br.com.fiap.dto.DentistaRequest;
import br.com.fiap.dto.DentistaResponse;
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

@Path("/api/dentistas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DentistaResource {

    private final DentistaBO bo = new DentistaBO();

    @GET
    public List<DentistaResponse> listar() {
        return bo.listar();
    }

    @GET
    @Path("/{id}")
    public DentistaResponse buscar(@PathParam("id") int id) {
        return bo.buscarPorId(id);
    }

    @POST
    public Response criar(DentistaRequest req) {
        DentistaResponse criado = bo.criar(req);
        return Response.status(201)
                .header("Location", "/api/dentistas/" + criado.idDentista())
                .entity(criado).build();
    }

    @PUT
    @Path("/{id}")
    public DentistaResponse atualizar(@PathParam("id") int id, DentistaRequest req) {
        return bo.atualizar(id, req);
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") int id) {
        bo.deletar(id);
        return Response.noContent().build();
    }
}
