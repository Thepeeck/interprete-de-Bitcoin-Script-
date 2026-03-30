package bitcoinscript.codigos;

import bitcoinscript.excepcion.ExcepcionScript;
import bitcoinscript.modelo.ContextoEjecucion;

public class OpcodesControl {

    public static class OpIf implements CodigoOperacion {
        @Override public void ejecutar(ContextoEjecucion ctx) {
            if (ctx.debeEjecutarInstruccionNormal()) {
                boolean cond = ctx.getPilaP().sacarBooleano();
                ctx.entrarBloqueIf(cond);
                ctx.trazar("OP_IF", cond ? "Rama VERDADERA" : "Rama FALSA (Saltando)");
            } else {
                ctx.entrarBloqueIf(false);
                ctx.trazar("OP_IF (Omitido)", "Anidado");
            }
        }
        @Override public String getNombre() { return "OP_IF"; }
    }

    public static class OpNotIf implements CodigoOperacion {
        @Override public void ejecutar(ContextoEjecucion ctx) {
            if (ctx.debeEjecutarInstruccionNormal()) {
                boolean cond = !ctx.getPilaP().sacarBooleano();
                ctx.entrarBloqueIf(cond);
                ctx.trazar("OP_NOTIF");
            } else {
                ctx.entrarBloqueIf(false);
                ctx.trazar("OP_NOTIF (Omitido)");
            }
        }
        @Override public String getNombre() { return "OP_NOTIF"; }
    }

    public static class OpElse implements CodigoOperacion {
        @Override public void ejecutar(ContextoEjecucion ctx) {
            ctx.cambiarBloqueElse();
            ctx.trazar("OP_ELSE", "Cambiando rama");
        }
        @Override public String getNombre() { return "OP_ELSE"; }
    }

    public static class OpEndIf implements CodigoOperacion {
        @Override public void ejecutar(ContextoEjecucion ctx) {
            ctx.cerrarBloqueIf();
            ctx.trazar("OP_ENDIF", "Cierre de bloque");
        }
        @Override public String getNombre() { return "OP_ENDIF"; }
    }

    public static class OpReturn implements CodigoOperacion {
        @Override public void ejecutar(ContextoEjecucion ctx) {
            throw new ExcepcionScript("OP_RETURN encontrado. Terminando.");
        }
        @Override public String getNombre() { return "OP_RETURN"; }
    }
    
    public static class OpVerify implements CodigoOperacion {
        @Override public void ejecutar(ContextoEjecucion ctx) {
            if (!ctx.getPilaP().sacarBooleano()) throw new ExcepcionScript("OP_VERIFY falló.");
            ctx.trazar("OP_VERIFY", "Superado ✓");
        }
        @Override public String getNombre() { return "OP_VERIFY"; }
    }
}
