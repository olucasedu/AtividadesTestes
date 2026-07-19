/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.atividadestestes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 *
 * @author lucas
 */
@ExtendWith(MockitoExtension.class)
public class ClassTestedMockitoTest {

    @Mock
    private Collaborator collaboratorMock;

    private ClassTested classUnderTest;

    @BeforeEach
    public void setUp() {
        classUnderTest = new ClassTested();
        classUnderTest.setListener(collaboratorMock);
    }

    @Test
    public void deveAvisarQueDocumentoFoiAdicionado() {

        classUnderTest.addDocument(
                "Novo Documento",
                "Conteúdo do documento"
        );

        verify(collaboratorMock)
                .documentAdded("Novo Documento");
    }

    @Test
    public void deveRemoverDocumentoComVotoFavoravel() {

        classUnderTest.addDocument(
                "Relatório",
                "Conteúdo do relatório"
        );

        when(collaboratorMock.voteForRemoval("Relatório"))
                .thenReturn((byte) 1);

        boolean resultado =
                classUnderTest.removeDocument("Relatório");

        assertTrue(resultado);

        verify(collaboratorMock)
                .voteForRemoval("Relatório");

        verify(collaboratorMock)
                .documentRemoved("Relatório");
    }

    @Test
    public void deveAvisarTresAlteracoesDoDocumento() {

        classUnderTest.addDocument(
                "Documento",
                "Versão 1"
        );

        classUnderTest.addDocument(
                "Documento",
                "Versão 2"
        );

        classUnderTest.addDocument(
                "Documento",
                "Versão 3"
        );

        classUnderTest.addDocument(
                "Documento",
                "Versão 4"
        );

        verify(
                collaboratorMock,
                times(3)
        ).documentChanged("Documento");
    }
}
