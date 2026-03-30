package bitcoinscript;

import bitcoinscript.cripto.ServicioCriptografico;
import bitcoinscript.interprete.InterpretadorScript;
import bitcoinscript.modelo.PilaScript;

public class Principal {

    public static void main(String[] args) {
        // Encendemos la traza a 'true' obligatoriamente para que en el video
        // el catedrático pueda ver cómo cambia la pila paso a paso.
        boolean traza = true; 
        InterpretadorScript interprete = new InterpretadorScript();

        System.out.println("==================================================================");
        System.out.println("      INTÉRPRETE BITCOIN SCRIPT - FASE 2 (DEMOSTRACIONES)         ");
        System.out.println("==================================================================");

        // 1. Demostración P2PKH (Correcto/Incorrecto)
        probarP2PKH(interprete, traza);

        // 2. Demostración de Control de Flujo (Condicionales)
        probarCondicional(interprete, traza);

        // 3. Demostración Opcional Avanzado (Multisig 2 de 3)
        probarMultisig(interprete, traza);
    }

    private static void probarP2PKH(InterpretadorScript interprete, boolean traza) {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║   Demo 1: P2PKH (Correcto e Incorrecto)          ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        ServicioCriptografico cripto = new ServicioCriptografico();
        String clavePublica = "miClavePublicaDePrueba";
        String firma        = "MOCKSIG_miClavePublicaDePrueba";

        byte[] hashClave = cripto.hash160(clavePublica.getBytes());
        String hashHex   = PilaScript.bytesAHex(hashClave).substring(2); // Quitar el '0x'

        String scriptSig    = firma + " " + clavePublica;
        String scriptPubKey = "OP_DUP OP_HASH160 <" + hashHex + "> OP_EQUALVERIFY OP_CHECKSIG";

        System.out.println(">> Escenario 1A: Transacción P2PKH Correcta");
        System.out.println("  scriptSig    : " + scriptSig);
        System.out.println("  scriptPubKey : " + scriptPubKey);
        interprete.validar(scriptSig, scriptPubKey, traza);

        System.out.println("\n>> Escenario 1B: Transacción P2PKH Incorrecta (Firma Inválida)");
        String scriptSigMalo = "FIRMA_FALSA_INVENTADA " + clavePublica;
        System.out.println("  scriptSig    : " + scriptSigMalo);
        interprete.validar(scriptSigMalo, scriptPubKey, traza);
    }

    private static void probarCondicional(InterpretadorScript interprete, boolean traza) {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║   Demo 2: Control de Flujo (OP_IF / OP_ELSE)     ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        // Mandamos OP_TRUE para que el intérprete entre a la suma y salte el ELSE
        String scriptSig = "OP_TRUE"; 
        String scriptPubKey = "OP_IF OP_10 OP_5 OP_ADD OP_ELSE OP_0 OP_ENDIF";

        System.out.println(">> Escenario: Evaluación de bloque condicional verdadero");
        System.out.println("  scriptSig    : " + scriptSig);
        System.out.println("  scriptPubKey : " + scriptPubKey);
        interprete.validar(scriptSig, scriptPubKey, traza);
    }

    private static void probarMultisig(InterpretadorScript interprete, boolean traza) {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║   Demo 3: Opcional Avanzado (Multisig 2 de 3)    ║");
        System.out.println("╚══════════════════════════════════════════════════╝\n");

        // OP_0 inicial para el bug histórico de Bitcoin, seguido de 2 firmas válidas
        String scriptSig = "OP_0 MOCKSIG_USUARIO1 MOCKSIG_USUARIO2"; 
        // Solicitamos que se cumplan 2 firmas de un total de 3 llaves públicas
        String scriptPubKey = "OP_2 LLAVE_PUB_1 LLAVE_PUB_2 LLAVE_PUB_3 OP_3 OP_CHECKMULTISIG";

        System.out.println(">> Escenario: Multifirma válida evaluando 2 firmas contra 3 llaves");
        System.out.println("  scriptSig    : " + scriptSig);
        System.out.println("  scriptPubKey : " + scriptPubKey);
        interprete.validar(scriptSig, scriptPubKey, traza);
    }
}
