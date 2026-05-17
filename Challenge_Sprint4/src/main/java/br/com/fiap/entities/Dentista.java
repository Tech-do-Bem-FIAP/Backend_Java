package br.com.fiap.entities;

public class Dentista {

    private int     idDentista;
    private String  nome;
    private String  cpf;
    private String  email;
    private String  senha;
    private String  cro;
    private String  especialidade;
    private int     disponibilidade;  // 0 = indisponível, 1 = disponível
    private Integer idColaborador;    // FK opcional -> T_COLABORADOR

    public Dentista() {
    }

    public Dentista(int idDentista, String nome, String cpf, String email,
                    String senha, String cro, String especialidade,
                    int disponibilidade, Integer idColaborador) {
        this.idDentista      = idDentista;
        this.nome            = nome;
        this.cpf             = cpf;
        this.email           = email;
        this.senha           = senha;
        this.cro             = cro;
        this.especialidade   = especialidade;
        this.disponibilidade = disponibilidade;
        this.idColaborador   = idColaborador;
    }

    // -------------------------------------------------------------------------
    // Lógica de Negócio
    // -------------------------------------------------------------------------

    /**
     * Valida o CRO no formato oficial do banco (CK_DENT_CRO):
     * 6 dígitos, hífen e UF com 2 letras maiúsculas. Ex.: "100001-SP".
     */
    public boolean validarCRO() {
        return this.cro != null && this.cro.matches("\\d{6}-[A-Z]{2}");
    }

    /** Disponível para novos atendimentos (coluna NUMBER(1)). */
    public boolean isDisponivel() {
        return this.disponibilidade == 1;
    }

    /** Valida o CPF (11 dígitos) — mesma regra da CHECK CK_DENT_CPF. */
    public boolean cpfValido() {
        return this.cpf != null && this.cpf.matches("\\d{11}");
    }

    /** Valida o formato do e-mail. */
    public boolean emailValido() {
        return this.email != null
                && this.email.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.-]+$");
    }

    /** Confere se a senha atende à política mínima de segurança. */
    public boolean senhaSegura() {
        return this.senha != null
                && this.senha.length() >= 6
                && this.senha.matches(".*\\d.*")
                && this.senha.matches(".*[A-Za-z].*");
    }

    /** Extrai a UF do CRO (ex.: "SP"), ou vazio se o CRO for inválido. */
    public String ufDoCro() {
        return validarCRO() ? this.cro.substring(7) : "";
    }

    /** CRO formatado para exibição. */
    public String croFormatado() {
        return validarCRO() ? "CRO " + this.cro : "CRO inválido";
    }

    /**
     * Regra de aptidão: o dentista só pode ser escalado se o CRO for
     * válido e ele estiver marcado como disponível.
     */
    public boolean aptoParaAtendimento() {
        return validarCRO() && isDisponivel();
    }

    // -------------------------------------------------------------------------
    // Getters e Setters
    // -------------------------------------------------------------------------

    public int getIdDentista() {
        return idDentista;
    }

    public void setIdDentista(int idDentista) {
        this.idDentista = idDentista;
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

    public String getCro() {
        return cro;
    }

    public void setCro(String cro) {
        this.cro = cro;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public int getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(int disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public Integer getIdColaborador() {
        return idColaborador;
    }

    public void setIdColaborador(Integer idColaborador) {
        this.idColaborador = idColaborador;
    }
}
