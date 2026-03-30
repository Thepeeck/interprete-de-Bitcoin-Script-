package bitcoinscript.codigos;

import bitcoinscript.excepcion.ExcepcionScript;
import bitcoinscript.modelo.ContextoEjecucion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OpcodesTest {

    private ContextoEjecucion ctx;

    @BeforeEach
    void setUp() {
        ctx = new ContextoEjecucion(false); // Sin traza para los tests
    }

    @Test
    void testOpAddExito() {
        ctx.getPilaP().empujarEntero(5);
        ctx.getPilaP().empujarEntero(10);
        
        new OpcodesAritmeticos.OpAdd().ejecutar(ctx);
        
        assertEquals(15, ctx.getPilaP().sacarEntero());
    }

    @Test
    void testOpVerifyFalla() {
        ctx.getPilaP().empujarBooleano(false); // Empujamos un falso
        
        // OP_VERIFY debe fallar si el tope es falso
        assertThrows(ExcepcionScript.class, () -> {
            new OpcodesControl.OpVerify().ejecutar(ctx);
        });
    }

    @Test
    void testOpNumEqualVerifyExito() {
        ctx.getPilaP().empujarEntero(42);
        ctx.getPilaP().empujarEntero(42);
        
        // No debe lanzar excepción
        assertDoesNotThrow(() -> {
            new OpcodesAritmeticos.OpNumEqualVerify().ejecutar(ctx);
        });
    }

    @Test
    void testOpSwap() {
        ctx.getPilaP().empujarEntero(1);
        ctx.getPilaP().empujarEntero(2);
        
        new OpcodesPila.OpSwap().ejecutar(ctx);
        
        assertEquals(1, ctx.getPilaP().sacarEntero());
        assertEquals(2, ctx.getPilaP().sacarEntero());
    }
}
