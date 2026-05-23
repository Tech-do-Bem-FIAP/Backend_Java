package br.com.fiap.resource;

import br.com.fiap.bo.CampanhaBO;
import br.com.fiap.dto.CampanhaRequest;
import br.com.fiap.dto.CampanhaResponse;
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

@Path("/api/campanhas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CampanhaResource {

    private final CampanhaBO bo = new CampanhaBO();

    @GET
    public List<CampanhaResponse> listar() {
        return bo.listar();
    }

    @GET
    @Path("/{id}")
    public CampanhaResponse buscar(@PathParam("id") int id) {
        return bo.buscarPorId(id);
    }

    @POST
    public Response criar(CampanhaRequest req) {
        CampanhaResponse criado = bo.criar(req);
        return Response.status(201)
                .header("Location", "/api/campanhas/" + criado.idCampanha())
                .entity(criado).build();
    }

    @PUT
    @Path("/{id}")
    public CampanhaResponse atualizar(@PathParam("id") int id, CampanhaRequest req) {
        return bo.atualizar(id, req);
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") int id) {
        bo.deletar(id);
        return Response.noContent().build();
    }
}
