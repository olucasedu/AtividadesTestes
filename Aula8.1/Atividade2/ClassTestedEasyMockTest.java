/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.atividadestestes;

import org.easymock.EasyMockSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author lucas
 */
public class ClassTestedEasyMockTest
        extends EasyMockSupport {

    private Collaborator collaboratorMock;
    private ClassTested classUnderTest;

    @BeforeEach
    public void setUp() {
        collaboratorMock =
                mock(Collaborator.class);

        classUnderTest =
                new ClassTested();

        classUnderTest.setListener(
                collaboratorMock
        );
    }

    @Test
    public void deveAvisarQueDocumentoFoiAdicionado() {

        // Registra a chamada esperada
        collaboratorMock.documentAdded(
                "Novo Documento"
        );

        // Coloca o Mock no modo de execução
        replayAll();

        // Executa o método da classe testada
        classUnderTest.addDocument(
                "Novo Documento",
                "Conteúdo do documento"
        );

        // Confirma que a chamada esperada aconteceu
        verifyAll();
    }
}
