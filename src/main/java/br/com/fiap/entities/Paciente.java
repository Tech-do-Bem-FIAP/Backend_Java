package br.com.fiap.entities;

import java.util.Calendar;
import java.util.Date;

public class Paciente {

    private int    idPaciente;
    private String nome;
    private String cpf;        // opcional no banco (UNIQUE)
    private Date   dataNasc;
    private String telefone;
    private String email;
    private int    idDentista; // FK obrigatória -> T_DENTISTA

    public Paciente() {
    }

    public Paciente(int idPaciente, String nome, String cpf, Date dataNasc,
                    String telefone, String email, int idDentista) {
        this.idPaciente = idPaciente;
        this.nome       = nome;
        this.cpf        = cpf;
        this.dataNasc   = dataNasc;
        this.telefone   = telefone;
        this.email      = email;
        this.idDentista = idDentista;
    }

    // -------------------------------------------------------------------------
    // Lógica de Negócio
    // -------------------------------------------------------------------------

    /** Calcula a idade real do paciente em anos completos. */
    public int calcularIdade() {
        if (this.dataNasc == null) return 0;
        Calendar nascimento = Calendar.getInstance();
        nascimento.setTime(this.dataNasc);
        Calendar hoje = Calendar.getInstance();
        int idade = hoje.get(Calendar.YEAR) - nascimento.get(Calendar.YEAR);
        if (hoje.get(Calendar.DAY_OF_YEAR) < nascimento.get(Calendar.DAY_OF_YEAR)) {
            idade--;
        }
        return idade;
    }

    /** Quando verdadeiro, o atendimento exige autorização de um responsável. */
    public boolean isMenorDeIdade() {
        return calcularIdade() < 18;
    }

    /**
     * Classifica o paciente por faixa etária. A ONG atende crianças e
     * adolescentes; adultos são exceção.
     */
    public String classificacaoEtaria() {
        int idade = calcularIdade();
        if (idade <= 0)  return "INDEFINIDA";
        if (idade < 12)  return "CRIANCA";
        if (idade < 18)  return "ADOLESCENTE";
        return "ADULTO";
    }

    /** Prioridade na fila (1 = maior prioridade). Crianças primeiro. */
    public int prioridadeNaFila() {
        String faixa = classificacaoEtaria();
        if (faixa.equals("CRIANCA"))     return 1;
        if (faixa.equals("ADOLESCENTE")) return 2;
        return 3;
    }

    /** CPF é opcional; se informado deve ter 11 dígitos (CK_PAC_CPF). */
    public boolean cpfValido() {
        return this.cpf == null || this.cpf.matches("\\d{11}");
    }

    /** Valida o formato do e-mail informado. */
    public boolean emailValido() {
        return this.email != null
                && this.email.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.-]+$");
    }

    /** Valida o telefone (10 ou 11 dígitos, ignorando a máscara). */
    public boolean telefoneValido() {
        if (this.telefone == null) return false;
        String digitos = this.telefone.replaceAll("\\D", "");
        return digitos.length() == 10 || digitos.length() == 11;
    }

    /** Indica se o cadastro está apto a gerar atendimentos. */
    public boolean cadastroCompleto() {
        return nome != null && !nome.isBlank()
                && dataNasc != null
                && emailValido()
                && telefoneValido()
                && cpfValido()
                && idDentista > 0;
    }

    // -------------------------------------------------------------------------
    // Getters e Setters
    // -------------------------------------------------------------------------

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
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

    public Date getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(Date dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdDentista() {
        return idDentista;
    }

    public void setIdDentista(int idDentista) {
        this.idDentista = idDentista;
    }
}
