/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.atividadestestes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * @author lucas
 */
public class UsuarioParametrizadoTest {
    
    @ParameterizedTest
    @ValueSource(strings = {
        "senhaErrada",
        "123456",
        "admin"
    })
    void deveRecusarSenhasIncorretas(String tentativa) {
        Usuario usuario = new Usuario();
        usuario.setSenha("senhaCorreta");

        assertFalse(usuario.autenticar(tentativa));
    }

    /*
     * Testa os algoritmos usando ValueSource.
     */
    @ParameterizedTest
    @ValueSource(strings = {
        "SHA-256",
        "MD5",
        "SHA-1"
    })
    void deveAutenticarComValueSource(String algoritmo) {
        Usuario usuario = new Usuario();

        usuario.setSenha("senha123", algoritmo);

        assertTrue(
                usuario.autenticar("senha123", algoritmo)
        );
    }

    /*
     * Testa os algoritmos usando EnumSource.
     */
    @ParameterizedTest
    @EnumSource(Algoritmo.class)
    void deveAutenticarComEnumSource(
            Algoritmo algoritmo) {

        Usuario usuario = new Usuario();

        usuario.setSenha(
                "senha123",
                algoritmo.getNome()
        );

        assertTrue(
                usuario.autenticar(
                        "senha123",
                        algoritmo.getNome()
                )
        );
    }

    /*
     * Testa várias informações diretamente no código.
     */
    @ParameterizedTest
    @CsvSource({
        "SHA-256, senha123, senha123, true",
        "MD5, senha123, senhaErrada, false",
        "SHA-1, abc123, abc123, true",
        "SHA-256, teste123, errada, false"
    })
    void deveAutenticarComCsvSource(
            String algoritmo,
            String senhaCadastrada,
            String tentativa,
            boolean resultadoEsperado) {

        Usuario usuario = new Usuario();

        usuario.setSenha(
                senhaCadastrada,
                algoritmo
        );

        boolean resultado =
                usuario.autenticar(
                        tentativa,
                        algoritmo
                );

        assertEquals(resultadoEsperado, resultado);
    }

    /*
     * Lê os dados do arquivo dados-autenticacao.csv.
     */
    @ParameterizedTest
    @CsvFileSource(
            resources = "/dados-autenticacao.csv",
            numLinesToSkip = 1
    )
    void deveAutenticarComCsvFileSource(
            String algoritmo,
            String senhaCadastrada,
            String tentativa,
            boolean resultadoEsperado) {

        Usuario usuario = new Usuario();

        usuario.setSenha(
                senhaCadastrada,
                algoritmo
        );

        boolean resultado =
                usuario.autenticar(
                        tentativa,
                        algoritmo
                );

        assertEquals(resultadoEsperado, resultado);
    }

    /*
     * Verifica o lançamento da exceção para algoritmo inválido.
     */
    @Test
    void deveLancarExcecaoParaAlgoritmoDesconhecido() {
        Usuario usuario = new Usuario();
        usuario.setSenha("senha123");

        assertThrows(
                IllegalArgumentException.class,
                () -> usuario.autenticar(
                        "senha123",
                        "ALGORITMO-INEXISTENTE"
                )
        );
    }

    enum Algoritmo {

        SHA_256("SHA-256"),
        MD5("MD5"),
        SHA_1("SHA-1");

        private final String nome;

        Algoritmo(String nome) {
            this.nome = nome;
        }

        public String getNome() {
            return nome;
        }
    }
    
    
}
