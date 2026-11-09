package com.leivagaonahernanely.robloxhelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Llama a la API de Gemini (generateContent) para generar código Luau.
 * Usa solo clases del SDK de Android, sin librerías externas.
 */
public final class ApiClient {

    /** Modelo que genera el código. Cámbialo aquí si quieres otro. */
    public static final String MODELO = "gemini-3.5-flash-lite";

    private static final String URL_BASE =
            "https://generativelanguage.googleapis.com/v1beta/models/";

    private static final String SISTEMA =
            "Eres un experto en Roblox Studio y en el lenguaje Luau. "
            + "El usuario describe en español el script que necesita. "
            + "Responde SOLO con el código Luau completo, listo para pegar en Roblox Studio, "
            + "con comentarios breves en español. "
            + "En la primera línea, como comentario, indica dónde va el script "
            + "(por ejemplo: Script en ServerScriptService, LocalScript en StarterPlayerScripts, "
            + "Script dentro de una Part). "
            + "No añadas explicaciones fuera del código. "
            + "Usa APIs modernas como task.wait y task.delay. "
            + "Solo crea scripts para juegos propios; no crees herramientas para explotar otros juegos.";

    private ApiClient() {
    }

    public static String generarCodigo(String claveApi, String descripcion) throws Exception {
        JSONObject cuerpo = new JSONObject();

        JSONObject instruccionSistema = new JSONObject();
        instruccionSistema.put("parts", new JSONArray()
                .put(new JSONObject().put("text", SISTEMA)));
        cuerpo.put("systemInstruction", instruccionSistema);

        JSONObject mensaje = new JSONObject();
        mensaje.put("role", "user");
        mensaje.put("parts", new JSONArray()
                .put(new JSONObject().put("text", descripcion)));
        cuerpo.put("contents", new JSONArray().put(mensaje));

        JSONObject configuracion = new JSONObject();
        configuracion.put("maxOutputTokens", 2048);
        cuerpo.put("generationConfig", configuracion);

        HttpURLConnection con = (HttpURLConnection) new URL(URL_BASE + MODELO + ":generateContent")
                .openConnection();
        try {
            con.setRequestMethod("POST");
            con.setConnectTimeout(20000);
            con.setReadTimeout(120000);
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("x-goog-api-key", claveApi);

            try (OutputStream salida = con.getOutputStream()) {
                salida.write(cuerpo.toString().getBytes(StandardCharsets.UTF_8));
            }

            int codigo = con.getResponseCode();
            InputStream entrada = codigo >= 400 ? con.getErrorStream() : con.getInputStream();
            String texto = leer(entrada);

            JSONObject json = new JSONObject(texto);
            if (codigo >= 400) {
                JSONObject error = json.optJSONObject("error");
                String mensaje2 = error != null ? error.optString("message", texto) : texto;
                throw new IOException("HTTP " + codigo + ": " + mensaje2);
            }

            JSONArray candidatos = json.optJSONArray("candidates");
            if (candidatos == null || candidatos.length() == 0) {
                throw new IOException("La respuesta no contiene candidatos");
            }
            JSONArray partes = candidatos.getJSONObject(0)
                    .optJSONObject("content") != null
                    ? candidatos.getJSONObject(0).getJSONObject("content").optJSONArray("parts")
                    : null;

            StringBuilder resultado = new StringBuilder();
            if (partes != null) {
                for (int i = 0; i < partes.length(); i++) {
                    resultado.append(partes.getJSONObject(i).optString("text", ""));
                }
            }
            if (resultado.length() == 0) {
                throw new IOException("La respuesta no contiene texto");
            }
            return resultado.toString();
        } finally {
            con.disconnect();
        }
    }

    private static String leer(InputStream entrada) throws IOException {
        if (entrada == null) {
            return "";
        }
        try (InputStream in = entrada) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] datos = new byte[4096];
            int leidos;
            while ((leidos = in.read(datos)) != -1) {
                buffer.write(datos, 0, leidos);
            }
            return buffer.toString(StandardCharsets.UTF_8.name());
        }
    }
}
