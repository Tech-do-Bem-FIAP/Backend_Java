package br.com.fiap.entities;

import java.util.Date;

public class Solicitacao {

    private int     idSolicitacao;
    /** Null quando a solicitação vem de alguém ainda não cadastrado (pedido de cadastro). */
    private Integer idSolicitante;
    private String  tipo;
    private String  descricao;
    private String  status;          // 'pendente' | 'aprovada' | 'rejeitada'
    private Date    dataSolicitacao;
    private Integer idRevisor;       // null enquanto pendente
    private Date    dataRevisao;     // null enquanto pendente
    private String  comentarioRevisao;

    // Solicitante externo (presente quando idSolicitante é null).
    private String nomeExterno;
    private String cpfExterno;
    private String emailExterno;
    private String senhaExterno;
    private String telefoneExterno;

    private String nomeSolicitante;
    private String nomeRevisor;

    public Solicitacao() {}

    public int getIdSolicitacao() { return idSolicitacao; }
    public void setIdSolicitacao(int v) { this.idSolicitacao = v; }

    public Integer getIdSolicitante() { return idSolicitante; }
    public void setIdSolicitante(Integer v) { this.idSolicitante = v; }

    public String getTipo() { return tipo; }
    public void setTipo(String v) { this.tipo = v; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String v) { this.descricao = v; }

    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }

    public Date getDataSolicitacao() { return dataSolicitacao; }
    public void setDataSolicitacao(Date v) { this.dataSolicitacao = v; }

    public Integer getIdRevisor() { return idRevisor; }
    public void setIdRevisor(Integer v) { this.idRevisor = v; }

    public Date getDataRevisao() { return dataRevisao; }
    public void setDataRevisao(Date v) { this.dataRevisao = v; }

    public String getComentarioRevisao() { return comentarioRevisao; }
    public void setComentarioRevisao(String v) { this.comentarioRevisao = v; }

    public String getNomeExterno() { return nomeExterno; }
    public void setNomeExterno(String v) { this.nomeExterno = v; }

    public String getCpfExterno() { return cpfExterno; }
    public void setCpfExterno(String v) { this.cpfExterno = v; }

    public String getEmailExterno() { return emailExterno; }
    public void setEmailExterno(String v) { this.emailExterno = v; }

    public String getSenhaExterno() { return senhaExterno; }
    public void setSenhaExterno(String v) { this.senhaExterno = v; }

    public String getTelefoneExterno() { return telefoneExterno; }
    public void setTelefoneExterno(String v) { this.telefoneExterno = v; }

    public String getNomeSolicitante() { return nomeSolicitante; }
    public void setNomeSolicitante(String v) { this.nomeSolicitante = v; }

    public String getNomeRevisor() { return nomeRevisor; }
    public void setNomeRevisor(String v) { this.nomeRevisor = v; }
}
