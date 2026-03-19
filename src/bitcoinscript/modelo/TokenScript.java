package bitcoinscript.modelo;


public class TokenScript {

    public enum TipoToken { CODIGO_OP, DATO }

    private final TipoToken tipo;
    private final String codigoOp;
    private final byte[] dato;

    public TokenScript(String codigoOp) {
        this.tipo     = TipoToken.CODIGO_OP;
        this.codigoOp = codigoOp;
        this.dato     = null;
    }

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
