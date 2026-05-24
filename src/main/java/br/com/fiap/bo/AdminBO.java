package br.com.fiap.bo;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.dto.AdminRebuildRequest;
import br.com.fiap.dto.AdminRebuildResponse;
import br.com.fiap.exceptions.DadoInvalidoException;
import br.com.fiap.exceptions.PersistenciaException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Executa o script de reconstrucao do banco (seed/rebuild.sql).
 *
 * <p>O script contem:
 * <ul>
 *   <li>Um bloco PL/SQL anonimo (BEGIN ... END; seguido de '/') que dropa
 *       tabelas e sequences toleravelmente.</li>
 *   <li>CREATE SEQUENCE, CREATE TABLE, ALTER TABLE, INSERT, UPDATE, DELETE,
 *       COMMIT — separados por ';' no final da linha.</li>
 *   <li>5 SELECTs ao final (relatorios da rubrica) que devem ser PULADOS.</li>
 * </ul>
 *
 * <p>Algoritmo do parser:
 * <ol>
 *   <li>Le o arquivo linha a linha.</li>
 *   <li>Remove comentarios de linha ({@code --...}).</li>
 *   <li>Linhas em branco sao ignoradas (a nao ser dentro de um bloco PL/SQL).</li>
 *   <li>Se a linha (apos trim) comeca com {@code BEGIN} ou {@code DECLARE} e nao
 *       estamos ja num statement, entra no modo PL/SQL. Acumula tudo ate
 *       encontrar uma linha cujo conteudo e exatamente {@code /} (slash sozinho).
 *       Esse e UM statement.</li>
 *   <li>Caso contrario, acumula linhas ate encontrar uma terminando em {@code ;}.
 *       Esse e UM statement (sem o ';' final).</li>
 *   <li>SELECTs (linha do statement comeca com SELECT) sao pulados.</li>
 * </ol>
 */
public class AdminBO {

    private static final String SQL_RESOURCE = "/seed/rebuild.sql";

    /** Codigos Oracle toleraveis durante DROP em tabela/sequence inexistente. */
    private static final int ORA_TABELA_NAO_EXISTE   = 942;
    private static final int ORA_SEQUENCE_NAO_EXISTE = 2289;

    public AdminRebuildResponse executarRebuild(AdminRebuildRequest req) {
        validarCredenciais(req);

        String script = carregarScript();
        List<String> statements = parse(script);

        Connection conn = null;
        int executados = 0;
        int pulados = 0;
        List<String> erros = new ArrayList<>();

        try {
            conn = new ConexaoFactory().conexao();
            conn.setAutoCommit(false);

            for (String stmt : statements) {
                String trimmed = stmt.trim();
                if (trimmed.isEmpty()) continue;

                // Pula SELECTs (relatorios) — nao alteram dados, sao puramente
                // demonstrativos do script original.
                if (comecaCom(trimmed, "SELECT")) {
                    pulados++;
                    continue;
                }

                try (Statement s = conn.createStatement()) {
                    s.execute(trimmed);
                    executados++;
                } catch (SQLException e) {
                    int code = e.getErrorCode();
                    // Tolerar DROPs de objetos inexistentes (mesmo o bloco PL/SQL
                    // ja faz isso internamente; isto e seguranca extra).
                    if (code == ORA_TABELA_NAO_EXISTE || code == ORA_SEQUENCE_NAO_EXISTE) {
                        erros.add("Aviso (ORA-" + String.format("%05d", code) + "): " + e.getMessage().trim());
                        continue;
                    }
                    // Erro real: registra mas segue (rebuild parcial e melhor
                    // do que rebuild zero — o estudante consegue inspecionar).
                    erros.add("Erro: " + e.getMessage().trim()
                            + " | statement: " + resumo(trimmed));
                }
            }

            conn.commit();
            // Sucesso = nao houve "Erro:" (apenas avisos toleraveis).
            boolean sucesso = erros.stream().noneMatch(s -> s.startsWith("Erro:"));
            return new AdminRebuildResponse(sucesso, executados, pulados, erros);

        } catch (SQLException | ClassNotFoundException e) {
            throw new PersistenciaException("Falha ao conectar para reconstruir o banco", e);
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException ignored) {}
            }
        }
    }

    // ─── Validacao / IO ──────────────────────────────────────────────────────

    private void validarCredenciais(AdminRebuildRequest req) {
        if (req == null
                || !"ADMIN".equals(req.email())
                || !"ADMIN".equals(req.senha())
                || !"RECONSTRUIR".equals(req.confirmacao())) {
            throw new DadoInvalidoException(
                    "Credenciais admin invalidas ou confirmacao incorreta.");
        }
    }

    private String carregarScript() {
        try (InputStream in = getClass().getResourceAsStream(SQL_RESOURCE)) {
            if (in == null) {
                throw new PersistenciaException(
                        "Recurso " + SQL_RESOURCE + " nao encontrado no classpath",
                        new IOException("resource not found"));
            }
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(in, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            }
            return sb.toString();
        } catch (IOException e) {
            throw new PersistenciaException("Erro lendo " + SQL_RESOURCE, e);
        }
    }

    // ─── Parser ──────────────────────────────────────────────────────────────

    /**
     * Quebra o script em statements executaveis (1 statement por entrada).
     * Detecta blocos PL/SQL ({@code BEGIN...END;} terminados por linha {@code /})
     * e os mantem como uma unica unidade.
     */
    static List<String> parse(String script) {
        List<String> out = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inPlSql = false;

        String[] lines = script.split("\n", -1);
        for (String raw : lines) {
            String line = stripLineComment(raw);

            if (!inPlSql) {
                String trimmed = line.trim();
                // Linha vazia entre statements: ignorar
                if (trimmed.isEmpty() && current.length() == 0) continue;

                // Inicio de bloco PL/SQL (apenas no comeco de um statement novo)
                if (current.length() == 0
                        && (comecaCom(trimmed, "BEGIN") || comecaCom(trimmed, "DECLARE"))) {
                    inPlSql = true;
                    current.append(line).append('\n');
                    continue;
                }

                // Statement normal: acumula linhas. Termina quando a linha
                // (apos remover whitespace) acaba com ';'.
                current.append(line).append('\n');
                String acc = current.toString().trim();
                if (acc.endsWith(";")) {
                    // Remove o ';' final
                    String stmt = acc.substring(0, acc.length() - 1).trim();
                    if (!stmt.isEmpty()) out.add(stmt);
                    current.setLength(0);
                }
            } else {
                // Dentro de bloco PL/SQL: linha exatamente "/" termina o bloco
                if (line.trim().equals("/")) {
                    String stmt = current.toString().trim();
                    if (!stmt.isEmpty()) out.add(stmt);
                    current.setLength(0);
                    inPlSql = false;
                } else {
                    current.append(line).append('\n');
                }
            }
        }

        // Statement remanescente sem ';' final (raro mas possivel)
        String tail = current.toString().trim();
        if (!tail.isEmpty()) out.add(tail);

        return out;
    }

    /**
     * Remove comentarios de linha ({@code --...}) do conteudo.
     * Cuidado: nao trata strings literais com '--' dentro (no script atual
     * nao ha esse caso, entao a simplificacao e segura).
     */
    private static String stripLineComment(String line) {
        int idx = line.indexOf("--");
        if (idx < 0) return line;
        return line.substring(0, idx);
    }

    private static boolean comecaCom(String trimmed, String prefixo) {
        if (trimmed.length() < prefixo.length()) return false;
        String head = trimmed.substring(0, prefixo.length());
        return head.equalsIgnoreCase(prefixo);
    }

    private static String resumo(String stmt) {
        String oneLine = stmt.replaceAll("\\s+", " ");
        return oneLine.length() > 120 ? oneLine.substring(0, 120) + "..." : oneLine;
    }
}
