package bitcoinscript.codigos;

import bitcoinscript.modelo.ContextoEjecucion;

public interface CodigoOperacion {
    void ejecutar(ContextoEjecucion ctx);
    String getNombre();
}
