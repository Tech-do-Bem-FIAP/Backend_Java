package br.com.fiap.resource;

import br.com.fiap.bo.SolicitacaoBO;
import br.com.fiap.dto.SolicitacaoRequest;
import br.com.fiap.dto.SolicitacaoResponse;
import br.com.fiap.dto.SolicitacaoReviewRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/api/solicitacoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SolicitacaoResource {

    private final SolicitacaoBO bo = new SolicitacaoBO();

    @GET
    public List<SolicitacaoResponse> listar() {
        return bo.listar();
    }

    @GET
    @Path("/{id}")
    public SolicitacaoResponse buscar(@PathParam("id") int id) {
        return bo.buscarPorId(id);
    }

    @POST
    public Response criar(SolicitacaoRequest req) {
        SolicitacaoResponse criado = bo.criar(req);
        return Response.status(201)
                .header("Location", "/api/solicitacoes/" + criado.idSolicitacao())
                .entity(criado).build();
    }

    @PATCH
    @Path("/{id}/revisao")
    public SolicitacaoResponse revisar(@PathParam("id") int id, SolicitacaoReviewRequest req) {
        return bo.revisar(id, req);
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") int id) {
        bo.deletar(id);
        return Response.noContent().build();
    }
}
