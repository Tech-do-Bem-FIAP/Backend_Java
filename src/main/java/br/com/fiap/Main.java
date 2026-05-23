package br.com.fiap;

import br.com.fiap.conexoes.ConexaoFactory;
import br.com.fiap.main.DemonstracaoSistema;

import java.sql.Connection;

/**
 * Ponto de entrada da aplicação Tech do Bem.
 *
 * Testa a conexão com o banco Oracle e, em seguida, executa a
 * demonstração completa (lógica de negócio + ciclo CRUD).
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Iniciando Tech do Bem (Domain Driven Design - Java)...\n");

        try (Connection cn = new ConexaoFactory().conexao()) {
            System.out.println("Conexão com o banco de dados realizada com sucesso!\n");
        } catch (Exception e) {
            System.out.println("[AVISO] Sem conexão com o Oracle no momento "
                    + "(verifique a VPN da FIAP). A lógica de negócio será "
                    + "demonstrada mesmo assim.\n");
        }

        DemonstracaoSistema.main(args);
    }
}
