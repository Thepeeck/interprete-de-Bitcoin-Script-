package bitcoinscript.interprete;

import bitcoinscript.excepcion.ExcepcionScript;
import bitcoinscript.modelo.ContextoEjecucion;
import bitcoinscript.modelo.PilaScript;
import bitcoinscript.modelo.TokenScript;

import java.util.List;

/**
 * Intérprete de Bitcoin Script — Fase 1.
 *
 * Flujo de validación:
 *   1. Ejecutar scriptSig  → empuja firma y clave pública a la pila
 *   2. Ejecutar scriptPubKey → verifica los datos usando opcodes
 *   3. Resultado válido si:
 *      - Ningún opcode lanzó ExcepcionScript
 *      - La pila no está vacía
 *      - El tope de la pila es verdadero (distinto de cero)
 */
public class InterpretadorScript {

    private final RegistroOpcodes registro;
    private final AnalizadorScript analizador;

    public InterpretadorScript() {
        this.registro   = new RegistroOpcodes();
        this.analizador = new AnalizadorScript();
    }

    /**
     * Valida una transacción combinando scriptSig y scriptPubKey.
     *
     * @param scriptSig    script de desbloqueo  (ej: "MOCKSIG miClave")
     * @param scriptPubKey script de bloqueo     (ej: "OP_DUP OP_HASH160 <hash> OP_EQUALVERIFY OP_CHECKSIG")
     * @param traza        true para imprimir la pila paso a paso
     * @return true si la transacción es válida
     */
    public boolean validar(String scriptSig, String scriptPubKey, boolean traza) {
        List<TokenScript> tokensDesbloqueo = analizador.analizar(scriptSig);
        List<TokenScript> tokensBloqueo    = analizador.analizar(scriptPubKey);

        ContextoEjecucion ctx = new ContextoEjecucion(traza);

        try {
            if (traza) System.out.println("── scriptSig (desbloqueo) ──────────────────────");
            ejecutar(tokensDesbloqueo, ctx);

            if (traza) System.out.println("── scriptPubKey (bloqueo) ──────────────────────");
            ejecutar(tokensBloqueo, ctx);

        } catch (ExcepcionScript e) {
            if (traza) System.out.println("  ✗ ERROR: " + e.getMessage());
            return false;
        }

        // Verificación final
        PilaScript pila = ctx.getPilaP();
        if (pila.estaVacia()) {
            if (traza) System.out.println("  ✗ La pila está vacía al finalizar");
            return false;
        }

        boolean resultado = pila.verTopeBooleano();
        if (traza) {
            System.out.println("── Resultado ───────────────────────────────────");
            System.out.println("  Pila final: " + pila);
            System.out.println("  " + (resultado ? "✓ VÁLIDO" : "✗ INVÁLIDO"));
        }
        return resultado;
    }

    /** Ejecuta una lista de tokens sobre el contexto dado. */
    private void ejecutar(List<TokenScript> tokens, ContextoEjecucion ctx) {
        for (TokenScript token : tokens) {
            if (token.esDato()) {
                // Es un dato: simplemente empujarlo a la pila
                ctx.getPilaP().empujar(token.getDato());
                ctx.trazar("PUSH", "→ " + PilaScript.bytesAHex(token.getDato()));
            } else {
                // Es un opcode: buscarlo en el registro y ejecutarlo
                registro.obtener(token.getCodigoOp()).ejecutar(ctx);
            }
        }
    }
}
