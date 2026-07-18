/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.atividadestestes;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.util.Base64;
import java.util.Objects;

/**
 *
 * @author lucas
 */
public class Usuario {
    
    private static final int MAXIMO_TENTATIVAS = 3;
    private static final long JANELA_TENTATIVAS_MS = 30_000;
    private static final long TEMPO_BLOQUEIO_MS = 60_000;

    private String nome;
    private String email;
    private String senhaHash;
    private String algoritmoHash;

    private int tentativasFalhas;
    private long inicioDasTentativas = -1;
    private long bloqueadoAte = -1;

    private final Clock clock;

    public Usuario() {
        this(Clock.systemDefaultZone());
    }

    Usuario(Clock clock) {
        this.clock = Objects.requireNonNull(clock);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (!validarNome(nome)) {
            throw new IllegalArgumentException("Nome inválido.");
        }

        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!validarEmail(email)) {
            throw new IllegalArgumentException("E-mail inválido.");
        }

        this.email = email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public String getAlgoritmoHash() {
        return algoritmoHash;
    }

    public boolean validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return false;
        }

        return nome.matches("\\p{L}+(?: \\p{L}+)*");
    }

    public boolean validarEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }

        return email.matches(
                "[A-Za-z0-9._]+@[A-Za-z0-9]+(?:\\.[A-Za-z0-9]+)*"
        );
    }

    /*
     * Usa SHA-256 automaticamente.
     */
    public void setSenha(String senha) {
        setSenha(senha, "SHA-256");
    }

    /*
     * Sobrecarga que permite escolher o algoritmo.
     */
    public void setSenha(String senha, String algoritmo) {
        validarCampoObrigatorio(senha, "Senha");

        String algoritmoValidado = validarAlgoritmo(algoritmo);

        algoritmoHash = algoritmoValidado;
        senhaHash = gerarHash(senha, algoritmoValidado);
    }

    /*
     * Usa o algoritmo que foi escolhido no cadastro da senha.
     */
    public boolean autenticar(String senha) {
        if (senhaHash == null || algoritmoHash == null) {
            throw new IllegalStateException(
                    "Nenhuma senha foi cadastrada."
            );
        }

        return autenticar(senha, algoritmoHash);
    }

    /*
     * Sobrecarga que recebe o algoritmo.
     */
    public boolean autenticar(String senha, String algoritmo) {
        validarCampoObrigatorio(senha, "Senha");

        if (senhaHash == null || algoritmoHash == null) {
            throw new IllegalStateException(
                    "Nenhuma senha foi cadastrada."
            );
        }

        String algoritmoValidado = validarAlgoritmo(algoritmo);
        long agora = clock.millis();

        if (bloqueadoAte != -1 && agora < bloqueadoAte) {
            throw new ExceededAttemptsException(
                    "Usuário bloqueado. Aguarde um minuto."
            );
        }

        if (bloqueadoAte != -1 && agora >= bloqueadoAte) {
            bloqueadoAte = -1;
            limparTentativas();
        }

        boolean senhaCorreta =
                algoritmoHash.equals(algoritmoValidado)
                && senhaHash.equals(
                        gerarHash(senha, algoritmoValidado)
                );

        if (senhaCorreta) {
            limparTentativas();
            bloqueadoAte = -1;
            return true;
        }

        registrarTentativaFalha(agora);
        return false;
    }

    private void registrarTentativaFalha(long agora) {
        if (inicioDasTentativas == -1
                || agora - inicioDasTentativas
                >= JANELA_TENTATIVAS_MS) {

            inicioDasTentativas = agora;
            tentativasFalhas = 1;

        } else {
            tentativasFalhas++;
        }

        if (tentativasFalhas > MAXIMO_TENTATIVAS) {
            bloqueadoAte = agora + TEMPO_BLOQUEIO_MS;

            throw new ExceededAttemptsException(
                    "Limite de tentativas excedido."
            );
        }
    }

    private void limparTentativas() {
        tentativasFalhas = 0;
        inicioDasTentativas = -1;
    }

    private void validarCampoObrigatorio(
            String valor,
            String campo) {

        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                    campo + " não pode ser nulo ou vazio."
            );
        }
    }

    private String validarAlgoritmo(String algoritmo) {
        if (algoritmo == null || algoritmo.isBlank()) {
            throw new IllegalArgumentException(
                    "O algoritmo não pode ser nulo ou vazio."
            );
        }

        if (!algoritmo.equals("SHA-256")
                && !algoritmo.equals("MD5")
                && !algoritmo.equals("SHA-1")) {

            throw new IllegalArgumentException(
                    "Algoritmo desconhecido: " + algoritmo
            );
        }

        return algoritmo;
    }

    private String gerarHash(
            String senha,
            String algoritmo) {

        try {
            MessageDigest digest =
                    MessageDigest.getInstance(algoritmo);

            byte[] hashBytes = digest.digest(
                    senha.getBytes(StandardCharsets.UTF_8)
            );

            return Base64.getEncoder()
                    .encodeToString(hashBytes);

        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalArgumentException(
                    "Algoritmo desconhecido: " + algoritmo,
                    exception
            );
        }
    }

    
}
