package bitcoinscript.codigos;

import bitcoinscript.modelo.ContextoEjecucion;

/**
 * Interfaz que representa un opcode de Bitcoin Script.
 *
 * Cada opcode implementa esta interfaz y define su propia lógica.
 * Esto permite registrar todos los opcodes en un Map y ejecutarlos
 * de forma uniforme sin usar switch gigante.
 */
public interface CodigoOperacion {
    void ejecutar(ContextoEjecucion ctx);
    String getNombre();
}
