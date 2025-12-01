package com.banco.pruebatecnica;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParallelTest {

    @Test
    void testParallel() {
        // Ejecuta todos los features en la carpeta 'karate/features' en paralelo
        Results results = Runner.path("classpath:karate/features")
                .tags("~@ignore")   // excluye escenarios con @ignore
                .parallel(5);       // 5 hilos en paralelo

        // Verifica que no haya fallos
        assertEquals(0, results.getFailCount(), results.getErrorMessages());
    }

}