package br.com.fiap.entities;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Atendimento {

    private int      idAtendimento;
    private Paciente idPaciente;
    private Dentista idDentista;
    private Campanha idCampanha;
    private Date     data;
    private String   status;
    private String   tipo;
    private String   observacoes;

    public Atendimento() {
    }

    public Atendimento(int idAtendimento, Paciente idPaciente, Dentista idDentista,
                       Campanha idCampanha, Date data, String status,
                       String tipo, String observacoes) {
        this.idAtendimento = idAtendimento;
        this.idPaciente    = idPaciente;
        this.idDentista    = idDentista;
        this.idCampanha    = idCampanha;
        this.data          = data;
        this.status        = status;
        this.tipo          = tipo;
        this.observacoes   = observacoes;
    }

    // -------------------------------------------------------------------------
    // Lógica de Negócio
    // -------------------------------------------------------------------------

    /** Identifica atendimentos agendados para o futuro. */
    public boolean isFuturo() {
        return this.data != null && this.data.after(new Date());
    }

    /** Atendimento agendado (CK_ATEND_STATUS: 'agendado'). */
    public boolean isAgendado() {
        return "agendado".equalsIgnoreCase(this.status);
    }

    /** Atendimento já realizado (valor 'realizado' aceito pela CHECK). */
    public boolean isRealizado() {
        return "realizado".equalsIgnoreCase(this.status);
    }

    /** Mantido por compatibilidade — equivale a isRealizado(). */
    public boolean isConcluido() {
        return isRealizado();
    }

    /** Atendimento cancelado pelo paciente ou pela ONG. */
    public boolean isCancelado() {
        return "cancelado".equalsIgnoreCase(this.status);
    }

    /** Verifica se há dentista responsável vinculado ao atendimento. */
    public boolean temDentista() {
        return this.idDentista != null;
    }

    /** Dias restantes até o atendimento (negativo se já passou). */
    public long diasAteAtendimento() {
        if (this.data == null) return 0;
        long diff = this.data.getTime() - new Date().getTime();
        return TimeUnit.MILLISECONDS.toDays(diff);
    }

    /**
     * Atendimento de paciente menor de idade exige autorização de
     * um responsável antes da realização.
     */
    public boolean exigeAutorizacaoResponsavel() {
        return idPaciente != null && idPaciente.isMenorDeIdade();
    }

    /**
     * Regra central de agendamento: um atendimento só pode ser realizado
     * se não estiver cancelado/realizado, tiver um dentista apto vinculado
     * e a data não estiver no passado.
     */
    public boolean podeSerRealizado() {
        boolean dentistaApto = temDentista() && idDentista.aptoParaAtendimento();
        boolean dataValida   = this.data != null && diasAteAtendimento() >= 0;
        return dentistaApto && dataValida && !isCancelado() && !isRealizado();
    }

    /** Texto consolidado do estado do atendimento para a interface. */
    public String resumoStatus() {
        if (isCancelado()) return "CANCELADO";
        if (isRealizado()) return "REALIZADO";
        if (isFuturo())    return "AGENDADO (em " + diasAteAtendimento() + " dia(s))";
        return "PENDENTE";
    }

    // -------------------------------------------------------------------------
    // Getters e Setters
    // -------------------------------------------------------------------------

    public int getIdAtendimento() {
        return idAtendimento;
    }

    public void setIdAtendimento(int idAtendimento) {
        this.idAtendimento = idAtendimento;
    }

    public Paciente getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Paciente idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Dentista getIdDentista() {
        return idDentista;
    }

    public void setIdDentista(Dentista idDentista) {
        this.idDentista = idDentista;
    }

    public Campanha getIdCampanha() {
        return idCampanha;
    }

    public void setIdCampanha(Campanha idCampanha) {
        this.idCampanha = idCampanha;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
