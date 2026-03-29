package bitcoinscript.modelo;

import bitcoinscript.excepcion.ExcepcionScript;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class PilaScript {
    private final Deque<byte[]> pila;
    public PilaScript() { this.pila = new ArrayDeque<>(); }

    public void empujar(byte[] valor) {
        if (valor == null) throw new ExcepcionScript("No se puede empujar null");
        pila.push(valor);
    }
    public void empujarBooleano(boolean valor) { empujar(valor ? new byte[]{1} : new byte[]{0}); }
    public byte[] sacar() {
        if (pila.isEmpty()) throw new ExcepcionScript("Pila vacía");
        return pila.pop();
    }
    public byte[] verTope() {
        if (pila.isEmpty()) throw new ExcepcionScript("Pila vacía");
        return pila.peek();
    }
    public boolean sacarBooleano() { return decodificarBooleano(sacar()); }
    public boolean verTopeBooleano() { return decodificarBooleano(verTope()); }
    public int tamanio() { return pila.size(); }
    public boolean estaVacia() { return pila.isEmpty(); }
    public List<byte[]> aLista() { return new ArrayList<>(pila); }

    public static boolean decodificarBooleano(byte[] datos) {
        if (datos == null || datos.length == 0) return false;
        for (byte b : datos) if (b != 0) return true;
        return false;
    }
    public static String bytesAHex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) return "0x(vacío)";
        StringBuilder sb = new StringBuilder("0x");
        for (byte b : bytes) sb.append(String.format("%02x", b & 0xFF));
        return sb.toString();
    }

    @Override
    public String toString() {
        if (pila.isEmpty()) return "[ ]";
        StringBuilder sb = new StringBuilder("[ ");
        List<byte[]> elementos = aLista();
        for (int i = 0; i < elementos.size(); i++) {
            sb.append(intentarTexto(elementos.get(i)));
            if (i < elementos.size() - 1) sb.append("  |  ");
        }
        sb.append(" ]");
        return sb.toString();
    }

    private String intentarTexto(byte[] bytes) {
        if (bytes.length == 0) return "0x(vacío)";
        if (bytes.length == 1) return "0x" + String.format("%02x", bytes[0] & 0xFF);
        for (byte b : bytes) if (b < 32 || b > 126) return bytesAHex(bytes);
        return "\"" + new String(bytes) + "\"";
    }

    // --- AGREGADOS FASE 2 ---
    public int sacarEntero() { return bytesAEntero(sacar()); }
    public void empujarEntero(int valor) { empujar(enteroABytes(valor)); }

    public static int bytesAEntero(byte[] datos) {
        if (datos == null || datos.length == 0) return 0;
        int res = 0;
        for (byte b : datos) res = (res << 8) | (b & 0xFF);
        return res;
    }
    public static byte[] enteroABytes(int valor) {
        if (valor == 0) return new byte[0];
        if (valor > 0 && valor <= 16) return new byte[]{(byte) valor};
        return java.nio.ByteBuffer.allocate(4).putInt(valor).array();
    }
}
