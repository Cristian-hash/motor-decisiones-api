package com.arquitectura.motor_decisiones.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EvaluacionServiceTest {


    @Test
    void pruebaAnatomiaBasica(){

        // 1.ARRANGE
        int numeroA = 2;
        int numeroB = 2;

        // 2.ACT
        int resultado;
        resultado= numeroA+numeroB;

        //3. ASSERT
        assertEquals(4,resultado);


    }

}
