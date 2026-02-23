package bitcoinscript.modelo;

/**
 * Representa un único token dentro de un Bitcoin Script.
 * Puede ser un opcode (ej: OP_DUP) o un dato en bruto (ej: 0xdeadbeef).
 */
public class TokenScript {

    public enum TipoToken { CODIGO_OP, DATO }

    private final TipoToken tipo;
    private final String codigoOp;
    private final byte[] dato;

    /** Token de tipo opcode. */
    public TokenScript(String codigoOp) {
        this.tipo     = TipoToken.CODIGO_OP;
        this.codigoOp = codigoOp;
        this.dato     = null;
    }

    /** Token de tipo dato. */
    public TokenScript(byte[] dato) {
        this.tipo     = TipoToken.DATO;
        this.codigoOp = null;
        this.dato     = dato;
    }

    public TipoToken getTipo()  { return tipo; }
    public boolean esOpcode()   { return tipo == TipoToken.CODIGO_OP; }
    public boolean esDato()     { return tipo == TipoToken.DATO; }
    public String getCodigoOp() { return codigoOp; }
    public byte[] getDato()     { return dato; }

    @Override
    public String toString() {
        return esOpcode() ? codigoOp : "<" + PilaScript.bytesAHex(dato) + ">";
    }
}
