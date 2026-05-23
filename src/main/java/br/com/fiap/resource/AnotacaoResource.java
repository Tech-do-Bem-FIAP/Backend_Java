package br.com.fiap.resource;

import br.com.fiap.bo.AnotacaoBO;
import br.com.fiap.dto.AnotacaoRequest;
import br.com.fiap.dto.AnotacaoResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/anotacoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AnotacaoResource {

    private final AnotacaoBO bo = new AnotacaoBO();

    /** Listagem é sempre escopada por destinatário: ?sobreTipo=X&sobreId=Y. */
    @GET
    public List<AnotacaoResponse> listar(@QueryParam("sobreTipo") String sobreTipo,
                                         @QueryParam("sobreId") int sobreId) {
        return bo.listarPorSobre(sobreTipo, sobreId);
    }

    @POST
    public Response criar(AnotacaoRequest req) {
        AnotacaoResponse criado = bo.criar(req);
        return Response.status(201)
                .header("Location", "/api/anotacoes/" + criado.idAnotacao())
                .entity(criado).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") int id) {
        bo.deletar(id);
        return Response.noContent().build();
    }
}
