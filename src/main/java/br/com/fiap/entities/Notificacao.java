package br.com.fiap.entities;

import java.util.Date;

public class Notificacao {

    private int     idNotificacao;
    private String  mensagem;
    private Date    dataEnvio;
    private String  statusEnvio;
    private String  canal;
    private Integer idDentista;
    private Integer idColaborador;
    private Integer idPaciente;
    private Date    dataLeitura;

    public Notificacao() {
    }

    public Notificacao(int idNotificacao, String mensagem, Date dataEnvio,
                       String statusEnvio, String canal,
                       Integer idDentista, Integer idColaborador) {
        this(idNotificacao, mensagem, dataEnvio, statusEnvio, canal,
                idDentista, idColaborador, null, null);
    }

    public Notificacao(int idNotificacao, String mensagem, Date dataEnvio,
                       String statusEnvio, String canal,
                       Integer idDentista, Integer idColaborador,
                       Integer idPaciente, Date dataLeitura) {
        this.idNotificacao = idNotificacao;
        this.mensagem      = mensagem;
        this.dataEnvio     = dataEnvio;
        this.statusEnvio   = statusEnvio;
        this.canal         = canal;
        this.idDentista    = idDentista;
        this.idColaborador = idColaborador;
        this.idPaciente    = idPaciente;
        this.dataLeitura   = dataLeitura;
    }

    // -------------------------------------------------------------------------
    // Lógica de Negócio
    // -------------------------------------------------------------------------

    public boolean isEnviada() {
        return "enviado".equalsIgnoreCase(this.statusEnvio);
    }

    public boolean isPendente() {
        return "pendente".equalsIgnoreCase(this.statusEnvio);
    }

    public boolean isFalha() {
        return "falhou".equalsIgnoreCase(this.statusEnvio);
    }

    public boolean canalValido() {
        if (canal == null) return false;
        String c = canal.trim().toLowerCase();
        return c.equals("email") || c.equals("sms")
                || c.equals("push") || c.equals("app");
    }

    public boolean podeReenviar() {
        return isFalha() && canalValido();
    }

    public String resumo() {
        if (mensagem == null) return "";
        return mensagem.length() <= 30
                ? mensagem
                : mensagem.substring(0, 30) + "...";
    }

    public boolean foiLida() {
        return dataLeitura != null;
    }

    // -------------------------------------------------------------------------
    // Getters e Setters
    // -------------------------------------------------------------------------

    public int getIdNotificacao() { return idNotificacao; }
    public void setIdNotificacao(int idNotificacao) { this.idNotificacao = idNotificacao; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public Date getDataEnvio() { return dataEnvio; }
    public void setDataEnvio(Date dataEnvio) { this.dataEnvio = dataEnvio; }

    public String getStatusEnvio() { return statusEnvio; }
    public void setStatusEnvio(String statusEnvio) { this.statusEnvio = statusEnvio; }

    public String getCanal() { return canal; }
    public void setCanal(String canal) { this.canal = canal; }

    public Integer getIdDentista() { return idDentista; }
    public void setIdDentista(Integer idDentista) { this.idDentista = idDentista; }

    public Integer getIdColaborador() { return idColaborador; }
    public void setIdColaborador(Integer idColaborador) { this.idColaborador = idColaborador; }

    public Integer getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Integer idPaciente) { this.idPaciente = idPaciente; }

    public Date getDataLeitura() { return dataLeitura; }
    public void setDataLeitura(Date dataLeitura) { this.dataLeitura = dataLeitura; }
}
