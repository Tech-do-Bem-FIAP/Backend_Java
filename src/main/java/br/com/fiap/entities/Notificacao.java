package br.com.fiap.entities;

import java.util.Date;

public class Notificacao {

    private int     idNotificacao;
    private String  mensagem;
    private Date    dataEnvio;
    private String  statusEnvio;
    private String  canal;
    private Integer idDentista;    // FK opcional -> T_DENTISTA
    private Integer idColaborador; // FK opcional -> T_COLABORADOR

    public Notificacao() {
    }

    public Notificacao(int idNotificacao, String mensagem, Date dataEnvio,
                       String statusEnvio, String canal,
                       Integer idDentista, Integer idColaborador) {
        this.idNotificacao = idNotificacao;
        this.mensagem      = mensagem;
        this.dataEnvio     = dataEnvio;
        this.statusEnvio   = statusEnvio;
        this.canal         = canal;
        this.idDentista    = idDentista;
        this.idColaborador = idColaborador;
    }

    // -------------------------------------------------------------------------
    // Lógica de Negócio
    // -------------------------------------------------------------------------

    /** A notificação foi entregue com sucesso (CK_NOTI_STATUS: 'enviado'). */
    public boolean isEnviada() {
        return "enviado".equalsIgnoreCase(this.statusEnvio);
    }

    /** Ainda na fila, aguardando envio ('pendente'). */
    public boolean isPendente() {
        return "pendente".equalsIgnoreCase(this.statusEnvio);
    }

    /** O último envio falhou ('falhou' — valor aceito pela CHECK do banco). */
    public boolean isFalha() {
        return "falhou".equalsIgnoreCase(this.statusEnvio);
    }

    /** O canal informado é um dos suportados pelo sistema. */
    public boolean canalValido() {
        if (canal == null) return false;
        String c = canal.trim().toLowerCase();
        return c.equals("email") || c.equals("sms")
                || c.equals("push") || c.equals("app");
    }

    /**
     * Regra de reenvio: só faz sentido reenviar quando o status é 'falhou'
     * e o canal configurado é válido.
     */
    public boolean podeReenviar() {
        return isFalha() && canalValido();
    }

    /** Prévia da mensagem (até 30 caracteres) para listagens. */
    public String resumo() {
        if (mensagem == null) return "";
        return mensagem.length() <= 30
                ? mensagem
                : mensagem.substring(0, 30) + "...";
    }

    // -------------------------------------------------------------------------
    // Getters e Setters
    // -------------------------------------------------------------------------

    public int getIdNotificacao() {
        return idNotificacao;
    }

    public void setIdNotificacao(int idNotificacao) {
        this.idNotificacao = idNotificacao;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Date getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(Date dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public String getStatusEnvio() {
        return statusEnvio;
    }

    public void setStatusEnvio(String statusEnvio) {
        this.statusEnvio = statusEnvio;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public Integer getIdDentista() {
        return idDentista;
    }

    public void setIdDentista(Integer idDentista) {
        this.idDentista = idDentista;
    }

    public Integer getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(Integer idColaborador) {
        this.idColaborador = idColaborador;
    }
}
