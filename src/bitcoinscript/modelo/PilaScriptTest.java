package bitcoinscript.modelo;

import bitcoinscript.excepcion.ExcepcionScript;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PilaScriptTest {

    private PilaScript pila;

    @BeforeEach
    void setUp() {
        pila = new PilaScript();
    }

    @Test
    void testEmpujarYSacarBasico() {
        pila.empujar(new byte[]{10});
        assertArrayEquals(new byte[]{10}, pila.sacar());
    }

    @Test
    void testCasoBordePilaVacia() {
        // Verifica que intentar sacar de una pila vacía lance ExcepcionScript
        assertThrows(ExcepcionScript.class, () -> {
            pila.sacar();
        });
    }

    @Test
    void testConversionTiposEntero() {
        pila.empujarEntero(256);
        assertEquals(256, pila.sacarEntero());
    }

    @Test
    void testConversionTiposBooleano() {
        pila.empujarBooleano(true);
        pila.empujarBooleano(false);
        
        assertFalse(pila.sacarBooleano()); // Saca el último (false)
        assertTrue(pila.sacarBooleano());  // Saca el primero (true)
    }
}
