/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.atividadestestes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de manipulação de strings")
public class ManipuladorStringsTeste {

    private ManipuladorStrings manipulador;

    @BeforeEach
    public void prepararTestes() {
        manipulador = new ManipuladorStrings();
    }

    @Test
    @DisplayName("Deve inverter uma string")
    public void testInverterString() {
        String resultado = manipulador.inverter("Java");

        assertEquals("avaJ", resultado);
    }

    @Test
    @DisplayName("Deve inverter uma string com caracteres especiais")
    public void testInverterCaracteresEspeciais() {
        String resultado = manipulador.inverter("Olá!");

        assertEquals("!álO", resultado);
    }

    @Test
    @DisplayName("Deve contar as ocorrências de um caractere")
    public void testContarOcorrencias() {
        int resultado = manipulador.contarOcorrencias(
                "banana",
                'a'
        );

        assertEquals(3, resultado);
    }

    @Test
    @DisplayName("Deve retornar zero quando o caractere não existir")
    public void testCaractereInexistente() {
        int resultado = manipulador.contarOcorrencias(
                "banana",
                'x'
        );

        assertEquals(0, resultado);
    }

    @Test
    @DisplayName("Deve identificar uma palavra palíndroma")
    public void testPalindromo() {
        assertTrue(manipulador.ehPalindromo("arara"));
    }

    @Test
    @DisplayName("Deve identificar uma frase palíndroma")
    public void testPalindromoComEspacos() {
        assertTrue(
                manipulador.ehPalindromo("A sacada da casa")
        );
    }

    @Test
    @DisplayName("Deve identificar uma string que não é palíndroma")
    public void testNaoPalindromo() {
        assertFalse(manipulador.ehPalindromo("Java"));
    }

    @Test
    @DisplayName("Deve converter para maiúsculas")
    public void testConverterParaMaiusculas() {
        String resultado =
                manipulador.converterParaMaiusculas("JUnit");

        assertEquals("JUNIT", resultado);
    }

    @Test
    @DisplayName("Deve converter para minúsculas")
    public void testConverterParaMinusculas() {
        String resultado =
                manipulador.converterParaMinusculas("JAVA");

        assertEquals("java", resultado);
    }

    @Test
    @DisplayName("Deve trabalhar com string vazia")
    public void testStringVazia() {
        assertAll(
                () -> assertEquals(
                        "",
                        manipulador.inverter("")
                ),

                () -> assertEquals(
                        0,
                        manipulador.contarOcorrencias("", 'a')
                ),

                () -> assertTrue(
                        manipulador.ehPalindromo("")
                ),

                () -> assertEquals(
                        "",
                        manipulador.converterParaMaiusculas("")
                )
        );
    }

    @Test
    @DisplayName("Deve lançar exceção para string nula")
    public void testStringNula() {
        assertAll(
                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> manipulador.inverter(null)
                ),

                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> manipulador.contarOcorrencias(null, 'a')
                ),

                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> manipulador.ehPalindromo(null)
                ),

                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> manipulador.converterParaMaiusculas(null)
                ),

                () -> assertThrows(
                        IllegalArgumentException.class,
                        () -> manipulador.converterParaMinusculas(null)
                )
        );
    }

    @Test
    @DisplayName("Deve trabalhar com string curta")
    public void testStringCurta() {
        assertEquals("a", manipulador.inverter("a"));
    }

    @Test
    @DisplayName("Deve trabalhar com string longa")
    public void testStringLonga() {
        String texto =
                "Esta é uma string longa utilizada para realizar testes";

        String resultado = manipulador.inverter(texto);

        assertEquals(
                new StringBuilder(texto).reverse().toString(),
                resultado
        );
    }
}


