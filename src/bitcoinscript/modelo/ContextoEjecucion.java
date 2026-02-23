package bitcoinscript.modelo;


public class ContextoEjecucion {

    private final PilaScript pilaP;
    private final boolean modoTraza;
    private int paso;

    public ContextoEjecucion(boolean modoTraza) {
        this.pilaP     = new PilaScript();
        this.modoTraza = modoTraza;
        this.paso      = 0;
    }

    public PilaScript getPilaP() { return pilaP; }
    public boolean isModoTraza() { return modoTraza; }

   
    public void trazar(String nombreOp) {
        if (modoTraza) {
            paso++;
            System.out.printf("[Paso %2d] %-20s →  Pila: %s%n",
                    paso, nombreOp, pilaP.toString());
        }
    }

    public void trazar(String nombreOp, String extra) {
        if (modoTraza) {
            paso++;
            System.out.printf("[Paso %2d] %-20s →  Pila: %s   (%s)%n",
                    paso, nombreOp, pilaP.toString(), extra);
        }
    }
}