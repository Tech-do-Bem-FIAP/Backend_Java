package br.com.fiap.resource;

import br.com.fiap.bo.ColaboradorBO;
import br.com.fiap.dto.ColaboradorRequest;
import br.com.fiap.dto.ColaboradorResponse;
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

@Path("/api/colaboradores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ColaboradorResource {

    private final ColaboradorBO bo = new ColaboradorBO();

    @GET
    public List<ColaboradorResponse> listar() {
        return bo.listar();
    }

    @GET
    @Path("/{id}")
    public ColaboradorResponse buscar(@PathParam("id") int id) {
        return bo.buscarPorId(id);
    }

    @POST
    public Response criar(ColaboradorRequest req) {
        ColaboradorResponse criado = bo.criar(req);
        return Response.status(201)
                .header("Location", "/api/colaboradores/" + criado.idColaborador())
                .entity(criado).build();
    }

    @PUT
    @Path("/{id}")
    public ColaboradorResponse atualizar(@PathParam("id") int id, ColaboradorRequest req) {
        return bo.atualizar(id, req);
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") int id) {
        bo.deletar(id);
        return Response.noContent().build();
    }
}
