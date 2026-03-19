package bitcoinscript;

import bitcoinscript.cripto.ServicioCriptografico;
import bitcoinscript.interprete.InterpretadorScript;
import bitcoinscript.modelo.PilaScript;


public class Principal {

    public static void main(String[] args) {
        boolean traza = false;
        for (String arg : args) {
            if (arg.equals("--traza") || arg.equals("--trace")) traza = true;
        }
        probarP2PKH(traza);
    }

    private static void probarP2PKH(boolean traza) {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║   Intérprete Bitcoin Script — Fase 1 — P2PKH    ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println();

        InterpretadorScript interprete = new InterpretadorScript();
        ServicioCriptografico cripto   = new ServicioCriptografico();

        // ── Datos de prueba ──────────────────────────────────────

        String clavePublica = "miClavePublicaDePrueba";
        String firma        = "MOCKSIG_miClavePublicaDePrueba";

        byte[] hashClave = cripto.hash160(clavePublica.getBytes());
        String hashHex   = PilaScript.bytesAHex(hashClave).substring(2);

        String scriptSig    = firma + " " + clavePublica;
        String scriptPubKey = "OP_DUP OP_HASH160 <" + hashHex + "> OP_EQUALVERIFY OP_CHECKSIG";

        System.out.println("  scriptSig    : " + scriptSig);
        System.out.println("  scriptPubKey : " + scriptPubKey);
        System.out.println();

        boolean resultado = interprete.validar(scriptSig, scriptPubKey, traza);

        System.out.println();
        System.out.println("══════════════════════════════════════════════════");
        System.out.println("  Resultado final: " + (resultado ? "✓ VÁLIDO" : "✗ INVÁLIDO"));
        System.out.println("══════════════════════════════════════════════════");

        System.out.println();
        System.out.println("── Prueba negativa (firma incorrecta) ──────────────");
        String scriptSigMalo = "FIRMA_INCORRECTA " + clavePublica;
        boolean resultadoMalo = interprete.validar(scriptSigMalo, scriptPubKey, traza);
        System.out.println("  Resultado: " + (resultadoMalo ? "✓ VÁLIDO" : "✗ INVÁLIDO (esperado)"));
    }
}
