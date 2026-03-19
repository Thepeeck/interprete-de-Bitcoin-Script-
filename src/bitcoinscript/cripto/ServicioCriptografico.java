package bitcoinscript.cripto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ServicioCriptografico {

    public byte[] sha256(byte[] datos) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(datos);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 no disponible", e);
        }
    }


    public byte[] hash160(byte[] datos) {
        byte[] primerHash  = sha256(datos);
        byte[] segundoHash = sha256(primerHash);
        byte[] resultado   = new byte[20];
        System.arraycopy(segundoHash, 0, resultado, 0, 20);
        return resultado;
    }

    public boolean verificarFirma(byte[] firma, byte[] clavePublica) {
        if (firma == null || clavePublica == null) return false;
        return new String(firma).startsWith("MOCKSIG");
    }
}
