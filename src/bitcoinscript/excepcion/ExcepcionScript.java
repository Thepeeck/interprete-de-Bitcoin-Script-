package bitcoinscript.excepcion;

/**
 * Excepción lanzada cuando ocurre un error durante la ejecución del script.
 */
public class ExcepcionScript extends RuntimeException {

    private final String codigoOperacion;

    public ExcepcionScript(String mensaje) {
        super(mensaje);
        this.codigoOperacion = null;
    }

    public ExcepcionScript(String mensaje, String codigoOperacion) {
        super(mensaje + " [opcode: " + codigoOperacion + "]");
        this.codigoOperacion = codigoOperacion;
    }

    public String getCodigoOperacion() {
        return codigoOperacion;
    }
}
