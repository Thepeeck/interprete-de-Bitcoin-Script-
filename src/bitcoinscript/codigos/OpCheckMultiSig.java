package bitcoinscript.codigos;

import bitcoinscript.cripto.ServicioCriptografico;
import bitcoinscript.modelo.ContextoEjecucion;
import bitcoinscript.modelo.PilaScript;
import java.util.ArrayList;
import java.util.List;

public class OpCheckMultiSig implements CodigoOperacion {
    private final ServicioCriptografico cripto;
    
    public OpCheckMultiSig(ServicioCriptografico cripto) { 
        this.cripto = cripto; 
    }

    @Override
    public void ejecutar(ContextoEjecucion ctx) {
        PilaScript pila = ctx.getPilaP();
        
        int nLlaves = pila.sacarEntero();
        List<byte[]> llavesPublicas = new ArrayList<>();
        for (int i = 0; i < nLlaves; i++) llavesPublicas.add(pila.sacar());
        
        int mFirmas = pila.sacarEntero();
        List<byte[]> firmas = new ArrayList<>();
        for (int i = 0; i < mFirmas; i++) firmas.add(pila.sacar());
        
        pila.sacar(); // Descartar elemento histórico dummy (Bug de Bitcoin)
        
        boolean valido = (mFirmas <= nLlaves); // Mock
        pila.empujarBooleano(valido);
        ctx.trazar("OP_CHECKMULTISIG", valido ? "Multifirma válida ✓" : "Inválida ✗");
    }
    
    @Override public String getNombre() { return "OP_CHECKMULTISIG"; }
}
