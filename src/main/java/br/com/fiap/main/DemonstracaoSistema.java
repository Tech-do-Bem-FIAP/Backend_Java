package br.com.fiap.main;

import br.com.fiap.dao.AtendimentoDAO;
import br.com.fiap.dao.CampanhaAtendimentoDAO;
import br.com.fiap.dao.CampanhaDAO;
import br.com.fiap.dao.ColaboradorDAO;
import br.com.fiap.dao.DentistaDAO;
import br.com.fiap.dao.EnviaDAO;
import br.com.fiap.dao.ExameAtendimentoDAO;
import br.com.fiap.dao.ExameDAO;
import br.com.fiap.dao.NotificacaoDAO;
import br.com.fiap.dao.PacienteDAO;
import br.com.fiap.entities.Atendimento;
import br.com.fiap.entities.Campanha;
import br.com.fiap.entities.Colaborador;
import br.com.fiap.entities.Dentista;
import br.com.fiap.entities.Exame;
import br.com.fiap.entities.Notificacao;
import br.com.fiap.entities.Paciente;

import java.util.Calendar;
import java.util.Date;

/**
 * Classe de teste em totalidade do sistema Tech do Bem.
 *
 * O modelo de dados segue exatamente o DDL oficial do projeto
 * (Building_Relational_Database / "Tech do Bem - SQL - sprint 4.sql").
 *
 * PARTE 1: instancia objetos e imprime o RESULTADO de cada método de
 *          lógica de negócio.
 * PARTE 2: executa um ciclo CRUD completo no Oracle, respeitando todas
 *          as chaves estrangeiras e as 3 tabelas associativas (N:M).
 *
 * Falhas de conexão são tratadas com try/catch para que a lógica de
 * negócio continue sendo demonstrada mesmo sem acesso ao banco.
 */
public class DemonstracaoSistema {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println(" TECH DO BEM - DEMONSTRACAO DA LOGICA DE NEGOCIO E CRUD");
        System.out.println("============================================================\n");

        demonstrarLogicaDeNegocio();
        demonstrarCrudCompleto();

        System.out.println("\n============================================================");
        System.out.println(" FIM DA DEMONSTRACAO");
        System.out.println("============================================================");
    }

    // -------------------------------------------------------------------------
    // PARTE 1 — Lógica de negócio (sem banco de dados)
    // -------------------------------------------------------------------------

    private static void demonstrarLogicaDeNegocio() {
        System.out.println(">> PARTE 1: Lógica de negócio das entidades\n");

        Paciente crianca = new Paciente(1, "Ana Souza", "33333333300",
                dataDeNascimento(2018, Calendar.MARCH, 10),
                "(11) 99999-1234", "ana.souza@email.com", 1);
        Paciente adolescente = new Paciente(2, "Pedro Lima", null,
                dataDeNascimento(2009, Calendar.JULY, 2),
                "1133334444", "pedro@@invalido", 1);

        System.out.println("[Paciente] " + crianca.getNome()
                + " | idade=" + crianca.calcularIdade()
                + " | faixa=" + crianca.classificacaoEtaria()
                + " | prioridadeFila=" + crianca.prioridadeNaFila()
                + " | menorDeIdade=" + crianca.isMenorDeIdade()
                + " | cpfValido=" + crianca.cpfValido()
                + " | emailValido=" + crianca.emailValido()
                + " | cadastroCompleto=" + crianca.cadastroCompleto());
        System.out.println("[Paciente] " + adolescente.getNome()
                + " | faixa=" + adolescente.classificacaoEtaria()
                + " | prioridadeFila=" + adolescente.prioridadeNaFila()
                + " | cpfValido=" + adolescente.cpfValido()
                + " | emailValido=" + adolescente.emailValido()
                + " | cadastroCompleto=" + adolescente.cadastroCompleto());

        Dentista dentista = new Dentista(1, "Dra. Marina Lima", "22222222299",
                "marina.lima@email.com", "Senha123", "100099-SP", "Ortodontia", 1, 1);
        Dentista dentInvalido = new Dentista(2, "Dr. X", "123",
                "x@email.com", "123", "1234", "Clinico", 0, null);

        System.out.println("\n[Dentista] " + dentista.getNome()
                + " | " + dentista.croFormatado()
                + " | uf=" + dentista.ufDoCro()
                + " | croValido=" + dentista.validarCRO()
                + " | disponivel=" + dentista.isDisponivel()
                + " | senhaSegura=" + dentista.senhaSegura()
                + " | aptoParaAtendimento=" + dentista.aptoParaAtendimento());
        System.out.println("[Dentista] " + dentInvalido.getNome()
                + " | croValido=" + dentInvalido.validarCRO()
                + " | cpfValido=" + dentInvalido.cpfValido()
                + " | aptoParaAtendimento=" + dentInvalido.aptoParaAtendimento());

        Colaborador coord = new Colaborador(1, "Bruno Alves", "44444444401",
                "bruno@email.com", "Abc12345", "Coordenador", 1);
        Colaborador estag = new Colaborador(2, "Carla Dias", "44444444402",
                "carla@email.com", "Xyz98765", "Estagiário", 0);

        System.out.println("\n[Colaborador] " + coord.getNome()
                + " | nivelAcesso=" + coord.nivelAcesso()
                + " | gestor=" + coord.isGestor()
                + " | podeAutorizarCampanha=" + coord.podeAutorizarCampanha());
        System.out.println("[Colaborador] " + estag.getNome()
                + " | nivelAcesso=" + estag.nivelAcesso()
                + " | gestor=" + estag.isGestor()
                + " | podeAutorizarCampanha=" + estag.podeAutorizarCampanha());

        Campanha campanha = new Campanha(1, "Sorria Mais", "EMEF Vila Nova",
                dataRelativa(-2), dataRelativa(5), 1);
        System.out.println("\n[Campanha] " + campanha.getNome()
                + " | periodoValido=" + campanha.periodoValido()
                + " | duracaoEmDias=" + campanha.duracaoEmDias()
                + " | situacao=" + campanha.situacao()
                + " | vigente=" + campanha.isVigente());

        Exame exNormal = new Exame(1, "Radiografia", "Jejum de 4h", "Resultado normal", 1);
        Exame exAlterado = new Exame(2, "Periograma", "", "Lesao periapical detectada", 1);
        Exame exPendente = new Exame(3, "Raio-X panoramico", "Remover proteses", null, 1);
        System.out.println("\n[Exame] " + exNormal.getTipo()
                + " | status=" + exNormal.statusLaudo()
                + " | exigePreparo=" + exNormal.exigePreparo());
        System.out.println("[Exame] " + exAlterado.getTipo()
                + " | status=" + exAlterado.statusLaudo()
                + " | exigeEncaminhamento=" + exAlterado.exigeEncaminhamento());
        System.out.println("[Exame] " + exPendente.getTipo()
                + " | status=" + exPendente.statusLaudo());

        Notificacao notif = new Notificacao(1,
                "Lembrete: seu atendimento na campanha Sorria Mais e amanha as 14h.",
                new Date(), "falhou", "sms", 1, 1);
        System.out.println("\n[Notificacao] resumo=\"" + notif.resumo() + "\""
                + " | canalValido=" + notif.canalValido()
                + " | falha=" + notif.isFalha()
                + " | podeReenviar=" + notif.podeReenviar());

        Atendimento at = new Atendimento(1, crianca, dentista, campanha,
                dataRelativa(3), "agendado", "Limpeza", "Primeira consulta");
        System.out.println("\n[Atendimento] paciente=" + at.getIdPaciente().getNome()
                + " | resumoStatus=" + at.resumoStatus()
                + " | diasAte=" + at.diasAteAtendimento()
                + " | exigeAutorizacaoResponsavel=" + at.exigeAutorizacaoResponsavel()
                + " | podeSerRealizado=" + at.podeSerRealizado());
    }

    // -------------------------------------------------------------------------
    // PARTE 2 — CRUD completo no Oracle (schema oficial + tabelas N:M)
    // -------------------------------------------------------------------------

    private static void demonstrarCrudCompleto() {
        System.out.println("\n>> PARTE 2: Ciclo CRUD completo no banco Oracle\n");
        try {
            ColaboradorDAO colabDAO = new ColaboradorDAO();
            DentistaDAO    dentDAO  = new DentistaDAO();
            PacienteDAO    pacDAO   = new PacienteDAO();
            CampanhaDAO    campDAO  = new CampanhaDAO();
            AtendimentoDAO atendDAO = new AtendimentoDAO();
            ExameDAO       exameDAO = new ExameDAO();
            NotificacaoDAO notifDAO = new NotificacaoDAO();
            EnviaDAO              enviaDAO   = new EnviaDAO();
            CampanhaAtendimentoDAO campAtDAO = new CampanhaAtendimentoDAO();
            ExameAtendimentoDAO    exAtDAO   = new ExameAtendimentoDAO();

            String suf = String.valueOf(System.currentTimeMillis() % 100000);

            // CREATE — respeitando a ordem das FKs
            Colaborador colab = new Colaborador(0, "Colaborador Demo", "90000" + pad(suf),
                    "colab.demo" + suf + "@techdobem.org", "Demo12", "Coordenador", 1);
            System.out.println("CREATE  -> " + colabDAO.inserir(colab));
            int idColab = colabDAO.ultimoId();

            Dentista dent = new Dentista(0, "Dentista Demo", "91000" + pad(suf),
                    "dent.demo" + suf + "@techdobem.org", "Demo12", "100" + pad3(suf) + "-SP",
                    "Clinico Geral", 1, idColab);
            System.out.println("CREATE  -> " + dentDAO.inserir(dent));
            int idDent = dentDAO.ultimoId();

            Paciente pac = new Paciente(0, "Paciente Demo", "92000" + pad(suf),
                    dataDeNascimento(2000, Calendar.JANUARY, 15),
                    "11988887777", "pac.demo" + suf + "@techdobem.org", idDent);
            System.out.println("CREATE  -> " + pacDAO.inserir(pac));
            int idPac = pacDAO.ultimoId();

            Campanha camp = new Campanha(0, "Campanha Demo", "Posto Central",
                    dataRelativa(0), dataRelativa(10), idColab);
            System.out.println("CREATE  -> " + campDAO.inserir(camp));
            int idCamp = campDAO.ultimoId();

            Atendimento at = new Atendimento(0,
                    pacDAO.selecionarPorId(idPac),
                    dentDAO.selecionarPorId(idDent),
                    campDAO.selecionarPorId(idCamp),
                    dataRelativa(2), "agendado", "Avaliacao", "Atendimento de demonstracao");
            System.out.println("CREATE  -> " + atendDAO.inserir(at));
            int idAtend = atendDAO.ultimoId();

            Exame ex = new Exame(0, "Radiografia", "Sem preparo", "Sem alteracoes", idAtend);
            System.out.println("CREATE  -> " + exameDAO.inserir(ex));
            int idExame = exameDAO.ultimoId();

            Notificacao nt = new Notificacao(0, "Notificacao de demonstracao",
                    new Date(), "pendente", "email", idDent, idColab);
            System.out.println("CREATE  -> " + notifDAO.inserir(nt));
            int idNotif = notifDAO.ultimoId();

            // CREATE — tabelas associativas (N:M)
            System.out.println("CREATE  -> " + campAtDAO.inserir(idCamp, idAtend));
            System.out.println("CREATE  -> " + exAtDAO.inserir(idAtend, idExame));
            System.out.println("CREATE  -> " + enviaDAO.inserir(idNotif, idPac));

            // READ
            Paciente lido = pacDAO.selecionarPorId(idPac);
            System.out.println("READ    -> Paciente: " + lido.getNome()
                    + " | dentista vinculado=" + lido.getIdDentista());
            Atendimento aLido = atendDAO.selecionarPorId(idAtend);
            System.out.println("READ    -> Atendimento: " + aLido.getTipo()
                    + " | paciente=" + aLido.getIdPaciente().getNome()
                    + " | podeSerRealizado=" + aLido.podeSerRealizado());

            // UPDATE
            lido.setTelefone("11955554444");
            System.out.println("UPDATE  -> " + pacDAO.atualizar(lido));
            aLido.setStatus("realizado");
            System.out.println("UPDATE  -> " + atendDAO.atualizar(aLido)
                    + " (status agora: " + atendDAO.selecionarPorId(idAtend).resumoStatus() + ")");

            // DELETE — ordem inversa (associativas primeiro, depois as FKs)
            System.out.println("DELETE  -> " + enviaDAO.deletar(idNotif, idPac));
            System.out.println("DELETE  -> " + exAtDAO.deletar(idAtend, idExame));
            System.out.println("DELETE  -> " + campAtDAO.deletar(idCamp, idAtend));
            System.out.println("DELETE  -> " + notifDAO.deletar(idNotif));
            System.out.println("DELETE  -> " + exameDAO.deletar(idExame));
            System.out.println("DELETE  -> " + atendDAO.deletar(idAtend));
            System.out.println("DELETE  -> " + campDAO.deletar(idCamp));
            System.out.println("DELETE  -> " + pacDAO.deletar(idPac));
            System.out.println("DELETE  -> " + dentDAO.deletar(idDent));
            System.out.println("DELETE  -> " + colabDAO.deletar(idColab));

            System.out.println("\nCRUD concluido com sucesso (schema oficial + tabelas N:M)!");
        } catch (Exception e) {
            System.out.println("[AVISO] Nao foi possivel executar o CRUD no banco.");
            System.out.println("        Verifique a VPN/rede da FIAP e se o DDL oficial");
            System.out.println("        (Tech do Bem - SQL - sprint 4.sql) foi executado.");
            System.out.println("        Detalhe: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Métodos utilitários (funções com parâmetro e retorno)
    // -------------------------------------------------------------------------

    private static Date dataDeNascimento(int ano, int mes, int dia) {
        Calendar cal = Calendar.getInstance();
        cal.set(ano, mes, dia, 0, 0, 0);
        return cal.getTime();
    }

    private static Date dataRelativa(int dias) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, dias);
        return cal.getTime();
    }

    /** Garante 6 dígitos para compor um CPF de teste (11 dígitos). */
    private static String pad(String s) {
        String base = (s + "000000");
        return base.substring(0, 6);
    }

    /** Garante 3 dígitos para compor o CRO de teste (formato 999999-UF). */
    private static String pad3(String s) {
        String base = (s + "000");
        return base.substring(0, 3);
    }
}
