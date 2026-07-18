/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.atividadestestes;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
/**
 *
 * @author lucas
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Execution(ExecutionMode.SAME_THREAD)

public class UsuarioTest {
    private Usuario usuarioDasRepeticoes;

    @BeforeAll
    void prepararTesteDeRepeticoes() {
        usuarioDasRepeticoes = new Usuario();
        usuarioDasRepeticoes.setSenha("senhaCorreta");
    }

    @BeforeEach
    void informarTesteAtual(
            TestInfo testInfo,
            TestReporter testReporter) {

        testReporter.publishEntry(
                "Teste em execução",
                testInfo.getDisplayName()
        );
    }

    @Test
    @DisplayName("Deve aceitar um nome válido")
    void deveAceitarNomeValido(TestReporter testReporter) {
        Usuario usuario = new Usuario();

        usuario.setNome("Lucas Eduardo");

        testReporter.publishEntry("Nome testado", usuario.getNome());

        assertEquals("Lucas Eduardo", usuario.getNome());
    }

    @Test
    @DisplayName("Deve rejeitar nome com números")
    void deveRejeitarNomeComNumeros() {
        Usuario usuario = new Usuario();

        assertThrows(
                IllegalArgumentException.class,
                () -> usuario.setNome("Lucas123")
        );
    }

    @Test
    @DisplayName("Deve rejeitar nome com caractere especial")
    void deveRejeitarNomeComCaractereEspecial() {
        Usuario usuario = new Usuario();

        assertThrows(
                IllegalArgumentException.class,
                () -> usuario.setNome("Lucas@Eduardo")
        );
    }

    @Test
    @DisplayName("Deve rejeitar nome nulo")
    void deveRejeitarNomeNulo() {
        Usuario usuario = new Usuario();

        assertThrows(
                IllegalArgumentException.class,
                () -> usuario.setNome(null)
        );
    }

    @Test
    @DisplayName("Deve rejeitar nome vazio")
    void deveRejeitarNomeVazio() {
        Usuario usuario = new Usuario();

        assertThrows(
                IllegalArgumentException.class,
                () -> usuario.setNome("")
        );
    }

    @Test
    @DisplayName("Deve aceitar um e-mail válido")
    void deveAceitarEmailValido(TestReporter testReporter) {
        Usuario usuario = new Usuario();

        usuario.setEmail("lucas.eduardo@ufu.br");

        testReporter.publishEntry("E-mail testado", usuario.getEmail());

        assertEquals("lucas.eduardo@ufu.br", usuario.getEmail());
    }

    @Test
    @DisplayName("Deve rejeitar e-mail sem arroba")
    void deveRejeitarEmailSemArroba() {
        Usuario usuario = new Usuario();

        assertThrows(
                IllegalArgumentException.class,
                () -> usuario.setEmail("lucas.eduardoufu.br")
        );
    }

    @Test
    @DisplayName("Deve rejeitar e-mail com caracteres acentuados")
    void deveRejeitarEmailComAcento() {
        Usuario usuario = new Usuario();

        assertThrows(
                IllegalArgumentException.class,
                () -> usuario.setEmail("lucás@dominio")
        );
    }

    @Test
    @DisplayName("Deve armazenar a senha como hash")
    void deveArmazenarSenhaComoHash() {
        Usuario usuario = new Usuario();

        usuario.setSenha("minhaSenha");

        assertNotEquals("minhaSenha", usuario.getSenhaHash());
    }

    @Test
    @DisplayName("Deve autenticar usando a senha correta")
    void deveAutenticarComSenhaCorreta(TestReporter testReporter) {
        Usuario usuario = new Usuario();
        usuario.setSenha("senha123");

        boolean autenticado = usuario.autenticar("senha123");

        testReporter.publishEntry(
                "Resultado da autenticação",
                String.valueOf(autenticado)
        );

        assertTrue(autenticado);
    }

    @Test
    @DisplayName("Deve retornar falso para senha incorreta")
    void deveRetornarFalsoParaSenhaIncorreta() {
        Usuario usuario = new Usuario();
        usuario.setSenha("senhaCorreta");

        assertFalse(usuario.autenticar("senhaErrada"));
    }

    @RepeatedTest(
            value = 4,
            name = "Tentativa {currentRepetition} de {totalRepetitions}"
    )
    void deveBloquearNaQuartaTentativa(
            RepetitionInfo repetitionInfo,
            TestReporter testReporter) {

        int repeticaoAtual =
                repetitionInfo.getCurrentRepetition();

        testReporter.publishEntry(
                "Número da tentativa",
                String.valueOf(repeticaoAtual)
        );

        if (repeticaoAtual <= 3) {
            assertFalse(
                    usuarioDasRepeticoes.autenticar("senhaErrada")
            );
        } else {
            assertThrows(
                    ExceededAttemptsException.class,
                    () -> usuarioDasRepeticoes.autenticar("senhaErrada")
            );
        }
    }

    @Test
    @DisplayName("Deve liberar o usuário depois de um minuto")
    void deveLiberarUsuarioDepoisDeUmMinuto(
            TestReporter testReporter) {

        RelogioMutavel relogio = new RelogioMutavel(
                Instant.parse("2026-07-18T12:00:00Z"),
                ZoneId.of("UTC")
        );

        Usuario usuario = new Usuario(relogio);
        usuario.setSenha("senhaCorreta");

        assertFalse(usuario.autenticar("errada1"));
        assertFalse(usuario.autenticar("errada2"));
        assertFalse(usuario.autenticar("errada3"));

        assertThrows(
                ExceededAttemptsException.class,
                () -> usuario.autenticar("errada4")
        );

        /*
         * Avança o relógio do teste sem precisar aguardar
         * um minuto de verdade.
         */
        relogio.avancar(Duration.ofMinutes(1));

        boolean autenticado =
                usuario.autenticar("senhaCorreta");

        testReporter.publishEntry(
                "Autenticação após o bloqueio",
                String.valueOf(autenticado)
        );

        assertTrue(autenticado);
    }

    /*
     * Relógio controlado utilizado somente nos testes.
     */
    private static class RelogioMutavel extends Clock {

        private Instant instanteAtual;
        private final ZoneId zona;

        RelogioMutavel(Instant instanteAtual, ZoneId zona) {
            this.instanteAtual = instanteAtual;
            this.zona = zona;
        }

        void avancar(Duration duracao) {
            instanteAtual = instanteAtual.plus(duracao);
        }

        @Override
        public ZoneId getZone() {
            return zona;
        }

        @Override
        public Clock withZone(ZoneId novaZona) {
            return new RelogioMutavel(instanteAtual, novaZona);
        }

        @Override
        public Instant instant() {
            return instanteAtual;
        }
    }
    
    
}
