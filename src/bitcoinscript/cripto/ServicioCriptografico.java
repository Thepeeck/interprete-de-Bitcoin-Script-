package bitcoinscript.cripto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Servicio criptográfico para Fase 1.
 *
 * HASH160:
 *   En Bitcoin real = RIPEMD160(SHA256(dato)).
 *   En Fase 1 (mock) = primeros 20 bytes de SHA256(SHA256(dato)).
 *   RIPEMD160 no está disponible en Java estándar sin BouncyCastle,
 *   por eso se simula para fines educativos.
 *
 * CHECKSIG:
 *   En Bitcoin real usa ECDSA sobre secp256k1.
 *   En Fase 1 (mock): la firma es válida si empieza con "MOCKSIG".
 */
public class ServicioCriptografico {

    /** SHA256 real usando java.security. */
    public byte[] sha256(byte[] datos) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(datos);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 no disponible", e);
        }
    }

    /**
     * HASH160 simulado para Fase 1.
     * Devuelve los primeros 20 bytes de SHA256(SHA256(datos)).
     */
    public byte[] hash160(byte[] datos) {
        byte[] primerHash  = sha256(datos);
        byte[] segundoHash = sha256(primerHash);
        byte[] resultado   = new byte[20];
        System.arraycopy(segundoHash, 0, resultado, 0, 20);
        return resultado;
    }

    /**
     * Verificación de firma simulada (mock) para Fase 1.
     * Una firma es válida si su representación en texto comienza con "MOCKSIG".
     *
     * En fases siguientes esto será reemplazado por ECDSA real.
     */
    public boolean verificarFirma(byte[] firma, byte[] clavePublica) {
        if (firma == null || clavePublica == null) return false;
        return new String(firma).startsWith("MOCKSIG");
    }
}
