package br.com.fiap.resource;

import br.com.fiap.bo.AdminBO;
import br.com.fiap.dto.AdminRebuildRequest;
import br.com.fiap.dto.AdminRebuildResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Endpoints administrativos restritos ao usuario especial "ADMIN".
 *
 * <p>POST /api/admin/rebuild — Executa o script de reconstrucao do banco
 * embarcado em {@code src/main/resources/seed/rebuild.sql}. Apaga todas as
 * tabelas, recria o schema e carrega massa de dados realista. So pode ser
 * chamado com email="admin@admin.com", senha="admin" e confirmacao="RECONSTRUIR".
 */
@Path("/api/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AdminResource {

    private final AdminBO bo = new AdminBO();

    @POST
    @Path("/rebuild")
    public AdminRebuildResponse rebuild(AdminRebuildRequest req) {
        return bo.executarRebuild(req);
    }
}
