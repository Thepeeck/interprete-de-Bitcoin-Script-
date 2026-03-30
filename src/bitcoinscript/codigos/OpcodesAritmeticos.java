package bitcoinscript.codigos;

import bitcoinscript.excepcion.ExcepcionScript;
import bitcoinscript.modelo.ContextoEjecucion;

public class OpcodesAritmeticos {

    public static class OpAdd implements CodigoOperacion {
        @Override public void ejecutar(ContextoEjecucion ctx) {
            int b = ctx.getPilaP().sacarEntero();
            int a = ctx.getPilaP().sacarEntero();
            ctx.getPilaP().empujarEntero(a + b);
            ctx.trazar("OP_ADD", (a + b) + "");
        }
        @Override public String getNombre() { return "OP_ADD"; }
    }

    public static class OpSub implements CodigoOperacion {
        @Override public void ejecutar(ContextoEjecucion ctx) {
            int b = ctx.getPilaP().sacarEntero();
            int a = ctx.getPilaP().sacarEntero();
            ctx.getPilaP().empujarEntero(a - b);
            ctx.trazar("OP_SUB", (a - b) + "");
        }
        @Override public String getNombre() { return "OP_SUB"; }
    }

    public static class OpLessThan implements CodigoOperacion {
        @Override public void ejecutar(ContextoEjecucion ctx) {
            int b = ctx.getPilaP().sacarEntero();
            int a = ctx.getPilaP().sacarEntero();
            ctx.getPilaP().empujarBooleano(a < b);
            ctx.trazar("OP_LESSTHAN");
        }
        @Override public String getNombre() { return "OP_LESSTHAN"; }
    }

    public static class OpGreaterThan implements CodigoOperacion {
        @Override public void ejecutar(ContextoEjecucion ctx) {
            int b = ctx.getPilaP().sacarEntero();
            int a = ctx.getPilaP().sacarEntero();
            ctx.getPilaP().empujarBooleano(a > b);
            ctx.trazar("OP_GREATERTHAN");
        }
        @Override public String getNombre() { return "OP_GREATERTHAN"; }
    }

    public static class OpBoolAnd implements CodigoOperacion {
        @Override public void ejecutar(ContextoEjecucion ctx) {
            boolean b = ctx.getPilaP().sacarBooleano();
            boolean a = ctx.getPilaP().sacarBooleano();
            ctx.getPilaP().empujarBooleano(a && b);
            ctx.trazar("OP_BOOLAND");
        }
        @Override public String getNombre() { return "OP_BOOLAND"; }
    }

    public static class OpNumEqualVerify implements CodigoOperacion {
        @Override public void ejecutar(ContextoEjecucion ctx) {
            int b = ctx.getPilaP().sacarEntero();
            int a = ctx.getPilaP().sacarEntero();
            if (a != b) throw new ExcepcionScript("OP_NUMEQUALVERIFY falló.");
            ctx.trazar("OP_NUMEQUALVERIFY", "Superado ✓");
        }
        @Override public String getNombre() { return "OP_NUMEQUALVERIFY"; }
    }
    
    public static class OpNot implements CodigoOperacion {
        @Override public void ejecutar(ContextoEjecucion ctx) {
            int a = ctx.getPilaP().sacarEntero();
            ctx.getPilaP().empujarEntero(a == 0 ? 1 : 0);
            ctx.trazar("OP_NOT");
        }
        @Override public String getNombre() { return "OP_NOT"; }
    }
}