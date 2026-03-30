package bitcoinscript.interprete;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InterpretadorIntegracionTest {

    private InterpretadorScript interprete;

    @BeforeEach
    void setUp() {
        interprete = new InterpretadorScript();
    }

    @Test
    void testP2PKHValido() {
        String scriptSig = "MOCKSIG_miClave miClave";
        String scriptPubKey = "OP_DUP OP_HASH160 <miClave> OP_EQUALVERIFY OP_CHECKSIG"; // Asumiendo hash mockeado
        // Como el cripto es mockeado en su código, ajusten el hash hex de ser necesario.
        // Si el mock pasa directo con MOCKSIG_, debería retornar true.
        assertTrue(interprete.validar(scriptSig, scriptPubKey, false));
    }

    @Test
    void testCondicionalSimpleExito() {
        String scriptSig = "OP_TRUE";
        String scriptPubKey = "OP_IF OP_5 OP_ELSE OP_10 OP_ENDIF OP_5 OP_NUMEQUALVERIFY OP_TRUE";
        
        // Si entra al IF, deja 5. Luego 5 == 5 pasa el verify, y deja TRUE al final.
        assertTrue(interprete.validar(scriptSig, scriptPubKey, false));
    }

    @Test
    void testCondicionalAnidado() {
        // Estructura: IF (true) -> IF (false) -> OP_1 ELSE -> OP_2 ENDIF ELSE -> OP_3 ENDIF
        // Resultado esperado: Debe dejar OP_2 en la pila
        String scriptSig = "";
        String scriptPubKey = "OP_TRUE OP_IF " +
                              "  OP_FALSE OP_IF OP_1 OP_ELSE OP_2 OP_ENDIF " +
                              "OP_ELSE " +
                              "  OP_3 " +
                              "OP_ENDIF " +
                              "OP_2 OP_NUMEQUALVERIFY OP_TRUE";
        
        assertTrue(interprete.validar(scriptSig, scriptPubKey, false));
    }

    @Test
    void testErrorBloqueCondicionalSinCerrar() {
        String scriptSig = "";
        String scriptPubKey = "OP_TRUE OP_IF OP_1"; // Falta el OP_ENDIF
        
        assertFalse(interprete.validar(scriptSig, scriptPubKey, false));
    }

    @Test
    void testMultisigValido() {
        String scriptSig = "OP_0 MOCKSIG_1 MOCKSIG_2";
        String scriptPubKey = "OP_2 LLAVE_1 LLAVE_2 LLAVE_3 OP_3 OP_CHECKMULTISIG";
        
        assertTrue(interprete.validar(scriptSig, scriptPubKey, false));
    }
}
