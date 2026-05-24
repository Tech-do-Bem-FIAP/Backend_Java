package br.com.fiap.resource;

import br.com.fiap.bo.PacienteBO;
import br.com.fiap.dto.PacienteRequest;
import br.com.fiap.dto.PacienteResponse;
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
import java.util.stream.Collectors;

@Path("/api/pacientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PacienteResource {

    private final PacienteBO bo = new PacienteBO();

    @GET
    public List<PacienteResponse> listar() {
        return bo.listar();
    }

    @GET
    @Path("/geo")
    public List<PacienteResponse> listarComGeo() {
        return bo.listarComGeo().stream()
                .map(bo::toResponse)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public PacienteResponse buscar(@PathParam("id") int id) {
        return bo.buscarPorId(id);
    }

    @POST
    public Response criar(PacienteRequest req) {
        PacienteResponse criado = bo.criar(req);
        return Response.status(201)
                .header("Location", "/api/pacientes/" + criado.idPaciente())
                .entity(criado).build();
    }

    @PUT
    @Path("/{id}")
    public PacienteResponse atualizar(@PathParam("id") int id, PacienteRequest req) {
        return bo.atualizar(id, req);
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") int id) {
        bo.deletar(id);
        return Response.noContent().build();
    }
}
