package br.com.fiap.entities;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Campanha {

    private int    idCampanha;
    private String nome;
    private String local;
    private Date   dataInicio;
    private Date   dataFim;
    private int    idColaborador; // FK obrigatória -> T_COLABORADOR

    public Campanha() {
    }

    public Campanha(int idCampanha, String nome, String local,
                    Date dataInicio, Date dataFim, int idColaborador) {
        this.idCampanha    = idCampanha;
        this.nome          = nome;
        this.local         = local;
        this.dataInicio    = dataInicio;
        this.dataFim       = dataFim;
        this.idColaborador = idColaborador;
    }

    // -------------------------------------------------------------------------
    // Lógica de Negócio
    // -------------------------------------------------------------------------

    /** Período válido quando a data fim não é anterior à início (CK_CAMP_DATAS). */
    public boolean periodoValido() {
        return dataInicio != null && dataFim != null
                && !dataFim.before(dataInicio);
    }

    /** Duração planejada da campanha em dias (inclusive). */
    public long duracaoEmDias() {
        if (!periodoValido()) return 0;
        long diff = dataFim.getTime() - dataInicio.getTime();
        return TimeUnit.MILLISECONDS.toDays(diff) + 1;
    }

    /** A campanha está em andamento na data de hoje. */
    public boolean isVigente() {
        if (!periodoValido()) return false;
        Date hoje = new Date();
        return !hoje.before(dataInicio) && !hoje.after(dataFim);
    }

    /** A campanha já foi encerrada (data fim no passado). */
    public boolean isEncerrada() {
        return dataFim != null && dataFim.before(new Date());
    }

    /** A campanha ainda não começou. */
    public boolean isFutura() {
        return dataInicio != null && dataInicio.after(new Date());
    }

    /** Texto descritivo do estado atual da campanha. */
    public String situacao() {
        if (!periodoValido()) return "PERIODO_INVALIDO";
        if (isFutura())       return "AGENDADA";
        if (isVigente())      return "EM_ANDAMENTO";
        return "ENCERRADA";
    }

    // -------------------------------------------------------------------------
    // Getters e Setters
    // -------------------------------------------------------------------------

    public int getIdCampanha() {
        return idCampanha;
    }

    public void setIdCampanha(int idCampanha) {
        this.idCampanha = idCampanha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public int getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(int idColaborador) {
        this.idColaborador = idColaborador;
    }
}
