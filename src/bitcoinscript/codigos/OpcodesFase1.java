package bitcoinscript.codigos;

import bitcoinscript.excepcion.ExcepcionScript;
import bitcoinscript.modelo.ContextoEjecucion;
import bitcoinscript.modelo.PilaScript;
import java.util.Arrays;


public class OpcodesFase1 {

   
    public static class OpDuplicar implements CodigoOperacion {
        @Override
        public void ejecutar(ContextoEjecucion ctx) {
            byte[] tope = ctx.getPilaP().verTope();
            ctx.getPilaP().empujar(tope.clone());
            ctx.trazar("OP_DUP");
        }
        @Override public String getNombre() { return "OP_DUP"; }
    }


    public static class OpEliminar implements CodigoOperacion {
        @Override
        public void ejecutar(ContextoEjecucion ctx) {
            ctx.getPilaP().sacar();
            ctx.trazar("OP_DROP");
        }
        @Override public String getNombre() { return "OP_DROP"; }
    }

    public static class OpIgual implements CodigoOperacion {
        @Override
        public void ejecutar(ContextoEjecucion ctx) {
            PilaScript pila = ctx.getPilaP();
            byte[] b = pila.sacar();
            byte[] a = pila.sacar();
            boolean iguales = Arrays.equals(a, b);
            pila.empujarBooleano(iguales);
            ctx.trazar("OP_EQUAL", iguales ? "iguales ✓" : "diferentes ✗");
        }
        @Override public String getNombre() { return "OP_EQUAL"; }
    }

  
    public static class OpIgualVerificar implements CodigoOperacion {
        @Override
        public void ejecutar(ContextoEjecucion ctx) {
            PilaScript pila = ctx.getPilaP();
            byte[] b = pila.sacar();
            byte[] a = pila.sacar();
            if (!Arrays.equals(a, b)) {
                throw new ExcepcionScript(
                    "OP_EQUALVERIFY falló: los hashes no coinciden", "OP_EQUALVERIFY");
            }
            ctx.trazar("OP_EQUALVERIFY", "hashes coinciden ✓");
        }
        @Override public String getNombre() { return "OP_EQUALVERIFY"; }
    }

    public static class OpHash160 implements CodigoOperacion {

        private final bitcoinscript.cripto.ServicioCriptografico cripto;

        public OpHash160(bitcoinscript.cripto.ServicioCriptografico cripto) {
            this.cripto = cripto;
        }

        @Override
        public void ejecutar(ContextoEjecucion ctx) {
            byte[] dato = ctx.getPilaP().sacar();
            byte[] hash = cripto.hash160(dato);
            ctx.getPilaP().empujar(hash);
            ctx.trazar("OP_HASH160");
        }
        @Override public String getNombre() { return "OP_HASH160"; }
    }


    public static class OpVerificarFirma implements CodigoOperacion {

        private final bitcoinscript.cripto.ServicioCriptografico cripto;

        public OpVerificarFirma(bitcoinscript.cripto.ServicioCriptografico cripto) {
            this.cripto = cripto;
        }

        @Override
        public void ejecutar(ContextoEjecucion ctx) {
            PilaScript pila     = ctx.getPilaP();
            byte[] clavePublica = pila.sacar();   // tope = clave pública
            byte[] firma        = pila.sacar();   // segundo = firma
            boolean valida      = cripto.verificarFirma(firma, clavePublica);
            pila.empujarBooleano(valida);
            ctx.trazar("OP_CHECKSIG", valida ? "firma válida ✓" : "firma inválida ✗");
        }
        @Override public String getNombre() { return "OP_CHECKSIG"; }
    }
}
