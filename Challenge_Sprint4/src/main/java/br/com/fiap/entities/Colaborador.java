package br.com.fiap.entities;

public class Colaborador {

    private int    idColaborador;
    private String nome;
    private String cpf;
    private String email;
    private String senha;
    private String cargo;
    private int    disponibilidade; // 0 = indisponível, 1 = disponível

    public Colaborador() {
    }

    public Colaborador(int idColaborador, String nome, String cpf, String email,
                       String senha, String cargo, int disponibilidade) {
        this.idColaborador   = idColaborador;
        this.nome            = nome;
        this.cpf             = cpf;
        this.email           = email;
        this.senha           = senha;
        this.cargo           = cargo;
        this.disponibilidade = disponibilidade;
    }

    // -------------------------------------------------------------------------
    // Lógica de Negócio
    // -------------------------------------------------------------------------

    /** Disponível para receber tarefas (coerente com a coluna NUMBER(1)). */
    public boolean isDisponivel() {
        return this.disponibilidade == 1;
    }

    /** Valida o CPF (11 dígitos) — mesma regra da CHECK CK_COLAB_CPF. */
    public boolean cpfValido() {
        return this.cpf != null && this.cpf.matches("\\d{11}");
    }

    /** Valida o formato do e-mail. */
    public boolean emailValido() {
        return this.email != null
                && this.email.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.-]+$");
    }

    /**
     * Nível de acesso derivado do cargo. Os cargos seguem exatamente os
     * valores aceitos pela CHECK CK_COLAB_CARGO do banco.
     */
    public int nivelAcesso() {
        if (cargo == null) return 0;
        switch (cargo.trim().toLowerCase()) {
            case "administrador": return 4;
            case "coordenador":   return 3;
            case "auxiliar":      return 2;
            case "estagiário":
            case "estagiario":    return 1;
            default:              return 0;
        }
    }

    /** Cargos de gestão (Coordenador/Administrador) podem aprovar campanhas. */
    public boolean isGestor() {
        return nivelAcesso() >= 3;
    }

    /** Regra: só um gestor disponível pode autorizar uma nova campanha. */
    public boolean podeAutorizarCampanha() {
        return isGestor() && isDisponivel();
    }

    // -------------------------------------------------------------------------
    // Getters e Setters
    // -------------------------------------------------------------------------

    public int getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(int idColaborador) {
        this.idColaborador = idColaborador;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public int getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(int disponibilidade) {
        this.disponibilidade = disponibilidade;
    }
}
