package br.com.fiap.resource;

import br.com.fiap.bo.NotificacaoBO;
import br.com.fiap.dto.NotificacaoRequest;
import br.com.fiap.dto.NotificacaoResponse;
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

@Path("/api/notificacoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotificacaoResource {

    private final NotificacaoBO bo = new NotificacaoBO();

    @GET
    public List<NotificacaoResponse> listar() {
        return bo.listar();
    }

    @GET
    @Path("/{id}")
    public NotificacaoResponse buscar(@PathParam("id") int id) {
        return bo.buscarPorId(id);
    }

    @POST
    public Response criar(NotificacaoRequest req) {
        NotificacaoResponse criado = bo.criar(req);
        return Response.status(201)
                .header("Location", "/api/notificacoes/" + criado.idNotificacao())
                .entity(criado).build();
    }

    @PUT
    @Path("/{id}")
    public NotificacaoResponse atualizar(@PathParam("id") int id, NotificacaoRequest req) {
        return bo.atualizar(id, req);
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") int id) {
        bo.deletar(id);
        return Response.noContent().build();
    }
}
