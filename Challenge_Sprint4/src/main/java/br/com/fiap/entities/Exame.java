package br.com.fiap.entities;

public class Exame {

    private int    idExame;
    private String tipo;
    private String requisitos;
    private String resultado;
    private int    idAtendimento; // FK obrigatória -> T_ATENDIMENTO

    public Exame() {
    }

    public Exame(int idExame, String tipo, String requisitos,
                 String resultado, int idAtendimento) {
        this.idExame       = idExame;
        this.tipo          = tipo;
        this.requisitos    = requisitos;
        this.resultado     = resultado;
        this.idAtendimento = idAtendimento;
    }

    // -------------------------------------------------------------------------
    // Lógica de Negócio
    // -------------------------------------------------------------------------

    /** Verifica se o exame já tem resultado lançado. */
    public boolean temResultado() {
        return this.resultado != null && !this.resultado.isBlank();
    }

    /** Verifica se o resultado do exame indica normalidade. */
    public boolean isResultadoNormal() {
        return temResultado()
                && (this.resultado.toLowerCase().contains("normal")
                 || this.resultado.toLowerCase().contains("sem altera")
                 || this.resultado.toLowerCase().contains("ok"));
    }

    /** Resultado lançado, porém fora do padrão de normalidade. */
    public boolean isResultadoAlterado() {
        return temResultado() && !isResultadoNormal();
    }

    /** Exige preparo prévio quando há requisitos cadastrados. */
    public boolean exigePreparo() {
        return this.requisitos != null && !this.requisitos.isBlank();
    }

    /**
     * Status do laudo, consumido pela tela de acompanhamento clínico.
     * Um exame ALTERADO dispara o encaminhamento do paciente.
     */
    public String statusLaudo() {
        if (!temResultado())     return "PENDENTE";
        if (isResultadoNormal()) return "NORMAL";
        return "ALTERADO";
    }

    /** Sinaliza que o paciente deve ser encaminhado para acompanhamento. */
    public boolean exigeEncaminhamento() {
        return isResultadoAlterado();
    }

    // -------------------------------------------------------------------------
    // Getters e Setters
    // -------------------------------------------------------------------------

    public int getIdExame() {
        return idExame;
    }

    public void setIdExame(int idExame) {
        this.idExame = idExame;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public int getIdAtendimento() {
        return idAtendimento;
    }

    public void setIdAtendimento(int idAtendimento) {
        this.idAtendimento = idAtendimento;
    }
}
