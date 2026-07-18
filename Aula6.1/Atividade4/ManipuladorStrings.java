/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.atividadestestes;

import java.util.Locale;

public class ManipuladorStrings {

    public String inverter(String texto) {
        validarString(texto);

        return new StringBuilder(texto)
                .reverse()
                .toString();
    }

    public int contarOcorrencias(String texto, char caractere) {
        validarString(texto);

        int quantidade = 0;

        for (int i = 0; i < texto.length(); i++) {
            if (texto.charAt(i) == caractere) {
                quantidade++;
            }
        }

        return quantidade;
    }

    public boolean ehPalindromo(String texto) {
        validarString(texto);

        String textoTratado = texto
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^\\p{L}\\p{N}]", "");

        String textoInvertido = new StringBuilder(textoTratado)
                .reverse()
                .toString();

        return textoTratado.equals(textoInvertido);
    }

    public String converterParaMaiusculas(String texto) {
        validarString(texto);

        return texto.toUpperCase(Locale.ROOT);
    }

    public String converterParaMinusculas(String texto) {
        validarString(texto);

        return texto.toLowerCase(Locale.ROOT);
    }

    private void validarString(String texto) {
        if (texto == null) {
            throw new IllegalArgumentException(
                    "A string não pode ser nula."
            );
        }
    }
}
