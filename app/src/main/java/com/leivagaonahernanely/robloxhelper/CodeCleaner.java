package com.leivagaonahernanely.robloxhelper;

/**
 * Extrae el código de la respuesta del modelo. Si el modelo envuelve el
 * código en bloques ```, se quedan solo las líneas que hay dentro.
 */
public final class CodeCleaner {

    private CodeCleaner() {
    }

    public static String limpiar(String texto) {
        if (texto == null) {
            return "";
        }
        if (!texto.contains("```")) {
            return texto.trim();
        }
        StringBuilder codigo = new StringBuilder();
        boolean dentro = false;
        for (String linea : texto.split("\n", -1)) {
            if (linea.trim().startsWith("```")) {
                dentro = !dentro;
                continue;
            }
            if (dentro) {
                codigo.append(linea).append('\n');
            }
        }
        return codigo.length() == 0 ? texto.trim() : codigo.toString().trim();
    }
}
