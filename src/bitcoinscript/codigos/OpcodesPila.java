package bitcoinscript.codigos;

import bitcoinscript.modelo.ContextoEjecucion;

public class OpcodesPila {

    public static class OpSwap implements CodigoOperacion {
        @Override public void ejecutar(ContextoEjecucion ctx) {
            byte[] b = ctx.getPilaP().sacar();
            byte[] a = ctx.getPilaP().sacar();
            ctx.getPilaP().empujar(b);
            ctx.getPilaP().empujar(a);
            ctx.trazar("OP_SWAP");
        }
        @Override public String getNombre() { return "OP_SWAP"; }
    }

    public static class OpOver implements CodigoOperacion {
        @Override public void ejecutar(ContextoEjecucion ctx) {
            byte[] b = ctx.getPilaP().sacar();
            byte[] a = ctx.getPilaP().sacar();
            ctx.getPilaP().empujar(a);
            ctx.getPilaP().empujar(b);
            ctx.getPilaP().empujar(a.clone());
            ctx.trazar("OP_OVER");
        }
        @Override public String getNombre() { return "OP_OVER"; }
    }
}