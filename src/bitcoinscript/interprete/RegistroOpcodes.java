package bitcoinscript.interprete;

import bitcoinscript.codigos.CodigoOperacion;
import bitcoinscript.codigos.OpcodesFase1;
import bitcoinscript.cripto.ServicioCriptografico;
import bitcoinscript.excepcion.ExcepcionScript;

import java.util.HashMap;
import java.util.Map;

/**
 * Registro central de opcodes para Fase 1.
 *
 * Estructura: HashMap<String, CodigoOperacion>
 *   - Búsqueda en O(1) por nombre de opcode
 *   - Ideal para catálogo pequeño (~15 entradas en Fase 1)
 *
 * Opcodes registrados en Fase 1:
 *   OP_0, OP_1 .. OP_16   (literales numéricos)
 *   OP_DUP                (duplicar tope)
 *   OP_DROP               (eliminar tope)
 *   OP_EQUAL              (comparar igualdad)
 *   OP_EQUALVERIFY        (comparar o fallar)
 *   OP_HASH160            (hash criptográfico mock)
 *   OP_CHECKSIG           (firma mock)
 */
public class RegistroOpcodes {

    private final Map<String, CodigoOperacion> registro;

    public RegistroOpcodes() {
        registro = new HashMap<>();
        registrarFase1();
    }

    private void registrarFase1() {
        ServicioCriptografico cripto = new ServicioCriptografico();

      
        registro.put("OP_0",     ctx -> { ctx.getPilaP().empujar(new byte[0]); ctx.trazar("OP_0"); });
        registro.put("OP_FALSE", registro.get("OP_0"));

 
        for (int i = 1; i <= 16; i++) {
            final byte val = (byte) i;
            final String nombre = "OP_" + i;
            registro.put(nombre, ctx -> { ctx.getPilaP().empujar(new byte[]{val}); ctx.trazar(nombre); });
        }
        registro.put("OP_TRUE", registro.get("OP_1"));


        registro.put("OP_DUP",         new OpcodesFase1.OpDuplicar());
        registro.put("OP_DROP",        new OpcodesFase1.OpEliminar());


        registro.put("OP_EQUAL",       new OpcodesFase1.OpIgual());
        registro.put("OP_EQUALVERIFY", new OpcodesFase1.OpIgualVerificar());

  
        registro.put("OP_HASH160",     new OpcodesFase1.OpHash160(cripto));
        registro.put("OP_CHECKSIG",    new OpcodesFase1.OpVerificarFirma(cripto));
    }

    /**
     * Devuelve el opcode por nombre en O(1).
     * @throws ExcepcionScript si el opcode no existe en el registro.
     */
    public CodigoOperacion obtener(String nombre) {
        CodigoOperacion op = registro.get(nombre.toUpperCase());
        if (op == null) {
            throw new ExcepcionScript("Opcode no reconocido en Fase 1: " + nombre);
        }
        return op;
    }

    public boolean contiene(String nombre) {
        return registro.containsKey(nombre.toUpperCase());
    }
}
