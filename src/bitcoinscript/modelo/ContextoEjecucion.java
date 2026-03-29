package bitcoinscript.modelo;

import bitcoinscript.excepcion.ExcepcionScript;
import java.util.ArrayDeque;
import java.util.Deque;

public class ContextoEjecucion {
    private final PilaScript pilaP;
    private final boolean modoTraza;
    private int paso;
    private final Deque<Boolean> pilaDeControl;

    public ContextoEjecucion(boolean modoTraza) {
        this.pilaP = new PilaScript();
        this.modoTraza = modoTraza;
        this.paso = 0;
        this.pilaDeControl = new ArrayDeque<>();
    }

    public PilaScript getPilaP() { return pilaP; }
    public boolean isModoTraza() { return modoTraza; }

    public boolean debeEjecutarInstruccionNormal() {
        for (Boolean estadoActivo : pilaDeControl) {
            if (!estadoActivo) return false;
        }
        return true;
    }

    public void entrarBloqueIf(boolean condicion) { pilaDeControl.push(condicion); }

    public void cambiarBloqueElse() {
        if (pilaDeControl.isEmpty()) throw new ExcepcionScript("OP_ELSE sin OP_IF previo.");
        boolean estadoActual = pilaDeControl.pop();
        pilaDeControl.push(!estadoActual);
    }

    public void cerrarBloqueIf() {
        if (pilaDeControl.isEmpty()) throw new ExcepcionScript("OP_ENDIF sin OP_IF previo.");
        pilaDeControl.pop();
    }

    public void trazar(String nombreOp) {
        if (modoTraza) {
            paso++;
            System.out.printf("[Paso %2d] %-20s →  Pila: %s%n", paso, nombreOp, pilaP.toString());
        }
    }

    public void trazar(String nombreOp, String extra) {
        if (modoTraza) {
            paso++;
            System.out.printf("[Paso %2d] %-20s →  Pila: %s   (%s)%n", paso, nombreOp, pilaP.toString(), extra);
        }
    }
}
