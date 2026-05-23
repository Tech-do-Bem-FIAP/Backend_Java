package br.com.fiap.entities;

import java.util.Date;

public class Anotacao {

    private int    idAnotacao;
    private String texto;
    private Date   data;
    private String autorTipo;
    private int    autorId;
    private String sobreTipo;
    private int    sobreId;

    public Anotacao() {}

    public Anotacao(int idAnotacao, String texto, Date data,
                    String autorTipo, int autorId,
                    String sobreTipo, int sobreId) {
        this.idAnotacao = idAnotacao;
        this.texto      = texto;
        this.data       = data;
        this.autorTipo  = autorTipo;
        this.autorId    = autorId;
        this.sobreTipo  = sobreTipo;
        this.sobreId    = sobreId;
    }

    public int getIdAnotacao() { return idAnotacao; }
    public void setIdAnotacao(int idAnotacao) { this.idAnotacao = idAnotacao; }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }

    public String getAutorTipo() { return autorTipo; }
    public void setAutorTipo(String autorTipo) { this.autorTipo = autorTipo; }

    public int getAutorId() { return autorId; }
    public void setAutorId(int autorId) { this.autorId = autorId; }

    public String getSobreTipo() { return sobreTipo; }
    public void setSobreTipo(String sobreTipo) { this.sobreTipo = sobreTipo; }

    public int getSobreId() { return sobreId; }
    public void setSobreId(int sobreId) { this.sobreId = sobreId; }
}
