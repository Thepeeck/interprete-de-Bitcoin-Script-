package bitcoinscript.interprete;

import bitcoinscript.codigos.*;
import bitcoinscript.cripto.ServicioCriptografico;
import bitcoinscript.excepcion.ExcepcionScript;
import bitcoinscript.modelo.ContextoEjecucion;
import java.util.HashMap;
import java.util.Map;

public class RegistroOpcodes {

    private final Map<String, CodigoOperacion> registro;

    public RegistroOpcodes() {
        registro = new HashMap<>();
        registrarFase1();
        registrarFase2(); // Nuevo llamado
    }

private void registrarFase1() {
        ServicioCriptografico cripto = new ServicioCriptografico();

        // Corrección: Usar clases anónimas en lugar de lambdas
        registro.put("OP_0", new CodigoOperacion() {
            @Override
            public void ejecutar(ContextoEjecucion ctx) { 
                ctx.getPilaP().empujar(new byte[0]); 
                ctx.trazar("OP_0"); 
            }
            @Override
            public String getNombre() { return "OP_0"; }
        });
        
        registro.put("OP_FALSE", registro.get("OP_0"));

        for (int i = 1; i <= 16; i++) {
            final byte val = (byte) i;
            final String nombre = "OP_" + i;
            registro.put(nombre, new CodigoOperacion() {
                @Override
                public void ejecutar(ContextoEjecucion ctx) { 
                    ctx.getPilaP().empujar(new byte[]{val}); 
                    ctx.trazar(nombre); 
                }
                @Override
                public String getNombre() { return nombre; }
            });
        }
        registro.put("OP_TRUE", registro.get("OP_1"));

        registro.put("OP_DUP", new OpcodesFase1.OpDuplicar());
        registro.put("OP_DROP", new OpcodesFase1.OpEliminar());
        registro.put("OP_EQUAL", new OpcodesFase1.OpIgual());
        registro.put("OP_EQUALVERIFY", new OpcodesFase1.OpIgualVerificar());
        registro.put("OP_HASH160", new OpcodesFase1.OpHash160(cripto));
        registro.put("OP_CHECKSIG", new OpcodesFase1.OpVerificarFirma(cripto));
    }

    private void registrarFase2() {
        ServicioCriptografico cripto = new ServicioCriptografico();

        // Pila
        registro.put("OP_SWAP", new OpcodesPila.OpSwap());
        registro.put("OP_OVER", new OpcodesPila.OpOver());

        // Aritmética y Lógica
        registro.put("OP_ADD", new OpcodesAritmeticos.OpAdd());
        registro.put("OP_SUB", new OpcodesAritmeticos.OpSub());
        registro.put("OP_LESSTHAN", new OpcodesAritmeticos.OpLessThan());
        registro.put("OP_GREATERTHAN", new OpcodesAritmeticos.OpGreaterThan());
        registro.put("OP_BOOLAND", new OpcodesAritmeticos.OpBoolAnd());
        registro.put("OP_NUMEQUALVERIFY", new OpcodesAritmeticos.OpNumEqualVerify());
        registro.put("OP_NOT", new OpcodesAritmeticos.OpNot());

        // Control
        registro.put("OP_IF", new OpcodesControl.OpIf());
        registro.put("OP_NOTIF", new OpcodesControl.OpNotIf());
        registro.put("OP_ELSE", new OpcodesControl.OpElse());
        registro.put("OP_ENDIF", new OpcodesControl.OpEndIf());
        registro.put("OP_VERIFY", new OpcodesControl.OpVerify());
        registro.put("OP_RETURN", new OpcodesControl.OpReturn());

        // Avanzado
        registro.put("OP_CHECKMULTISIG", new OpCheckMultiSig(cripto));
    }

    public CodigoOperacion obtener(String nombre) {
        CodigoOperacion op = registro.get(nombre.toUpperCase());
        if (op == null) throw new ExcepcionScript("Opcode no reconocido: " + nombre);
        return op;
    }

    public boolean contiene(String nombre) {
        return registro.containsKey(nombre.toUpperCase());
    }
}
