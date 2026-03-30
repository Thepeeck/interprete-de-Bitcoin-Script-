package bitcoinscript.interprete;

import bitcoinscript.modelo.TokenScript;
import java.util.ArrayList;
import java.util.List;

public class AnalizadorScript {

    /**
     * Convierte una cadena de texto de Bitcoin Script en una lista de Tokens ejecutables.
     */
    public List<TokenScript> analizar(String script) {
        List<TokenScript> tokens = new ArrayList<>();
        
        if (script == null || script.trim().isEmpty()) {
            return tokens;
        }

        // Separar el texto por espacios en blanco
        String[] partes = script.trim().split("\\s+");
        
        for (String parte : partes) {
            // Si empieza con "OP_", es un Código de Operación
            if (parte.toUpperCase().startsWith("OP_")) {
                tokens.add(new TokenScript(parte.toUpperCase()));
            } 
            // Si está entre < >, es un hash hexadecimal (como <a1b2>)
            else if (parte.startsWith("<") && parte.endsWith(">")) {
                String hex = parte.substring(1, parte.length() - 1);
                tokens.add(new TokenScript(hexABytes(hex)));
            } 
            // Si no es ninguno de los anteriores, es texto plano (como "MOCKSIG_miClave")
            else {
                tokens.add(new TokenScript(parte.getBytes()));
            }
        }
        
        return tokens;
    }

    /**
     * Método auxiliar para convertir un texto Hexadecimal a un arreglo de bytes.
     */
    private byte[] hexABytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                                 + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }
}
