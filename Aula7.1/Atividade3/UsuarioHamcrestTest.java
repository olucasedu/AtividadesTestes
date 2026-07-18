/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.atividadestestes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 *
 * @author lucas
 */
public class UsuarioHamcrestTest {
    
    @Test
    void deveAceitarNomeValido() {
        Usuario usuario = new Usuario();

        usuario.setNome("Lucas Eduardo");

        assertThat(usuario.getNome(), is("Lucas Eduardo"));
    }

    @Test
    void deveAceitarEmailValido() {
        Usuario usuario = new Usuario();

        usuario.setEmail("lucas.eduardo@ufu.br");

        assertThat(
                usuario.getEmail(),
                is(equalTo("lucas.eduardo@ufu.br"))
        );
    }

    @Test
    void deveArmazenarSenhaComoHash() {
        Usuario usuario = new Usuario();

        usuario.setSenha("minhaSenha");

        assertThat(
                usuario.getSenhaHash(),
                allOf(
                        notNullValue(),
                        not(emptyString()),
                        not(equalTo("minhaSenha"))
                )
        );
    }

    @Test
    void deveAutenticarComSenhaCorreta() {
        Usuario usuario = new Usuario();
        usuario.setSenha("senha123");

        boolean resultado = usuario.autenticar("senha123");

        assertThat(resultado, is(true));
    }

    @Test
    void deveRecusarSenhaIncorreta() {
        Usuario usuario = new Usuario();
        usuario.setSenha("senhaCorreta");

        boolean resultado = usuario.autenticar("senhaErrada");

        assertThat(resultado, is(false));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "SHA-256",
        "MD5",
        "SHA-1"
    })
    void deveAutenticarComDiferentesAlgoritmos(String algoritmo) {
        Usuario usuario = new Usuario();

        usuario.setSenha("senha123", algoritmo);

        assertThat(
                usuario.autenticar("senha123", algoritmo),
                is(true)
        );

        assertThat(
                usuario.getAlgoritmoHash(),
                is(algoritmo)
        );
    }

    @ParameterizedTest
    @CsvSource({
        "SHA-256, senha123, senha123, true",
        "MD5, senha123, senhaErrada, false",
        "SHA-1, abc123, abc123, true"
    })
    void deveTestarDadosComCsvSource(
            String algoritmo,
            String senhaCadastrada,
            String tentativa,
            boolean resultadoEsperado) {

        Usuario usuario = new Usuario();
        usuario.setSenha(senhaCadastrada, algoritmo);

        boolean resultado =
                usuario.autenticar(tentativa, algoritmo);

        assertThat(resultado, is(resultadoEsperado));
    }

    @Test
    void deveRejeitarNomeComNumeros() {
        Usuario usuario = new Usuario();

        assertThat(
                usuario.validarNome("Lucas123"),
                is(false)
        );
    }

    @Test
    void deveLancarExcecaoParaAlgoritmoDesconhecido() {
        Usuario usuario = new Usuario();
        usuario.setSenha("senha123");

        // AssertThrows continua mais apropriado para exceções.
        assertThrows(
                IllegalArgumentException.class,
                () -> usuario.autenticar(
                        "senha123",
                        "ALGORITMO-INEXISTENTE"
                )
        );
    }
    
    
}
