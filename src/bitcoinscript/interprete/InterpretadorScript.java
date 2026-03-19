package bitcoinscript.interprete;

import bitcoinscript.excepcion.ExcepcionScript;
import bitcoinscript.modelo.ContextoEjecucion;
import bitcoinscript.modelo.PilaScript;
import bitcoinscript.modelo.TokenScript;

import java.util.List;


public class InterpretadorScript {

    private final RegistroOpcodes registro;
    private final AnalizadorScript analizador;

    public InterpretadorScript() {
        this.registro   = new RegistroOpcodes();
        this.analizador = new AnalizadorScript();
    }


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

    private void ejecutar(List<TokenScript> tokens, ContextoEjecucion ctx) {
        for (TokenScript token : tokens) {
            if (token.esDato()) {
                ctx.getPilaP().empujar(token.getDato());
                ctx.trazar("PUSH", "→ " + PilaScript.bytesAHex(token.getDato()));
            } else {
                registro.obtener(token.getCodigoOp()).ejecutar(ctx);
            }
        }
    }
}
