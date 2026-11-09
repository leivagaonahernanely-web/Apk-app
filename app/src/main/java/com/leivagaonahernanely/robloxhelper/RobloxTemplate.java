package com.leivagaonahernanely.robloxhelper;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Una plantilla de script Luau para Roblox Studio.
 * El código puede contener marcadores con el formato {{CLAVE}} que se
 * sustituyen por los valores que introduce el usuario.
 */
public class RobloxTemplate {

    public final String titulo;
    public final String descripcion;
    public final String ubicacion;
    public final String codigo;

    /** Clave de marcador -> valor por defecto. */
    public final LinkedHashMap<String, String> parametros;

    /**
     * @param clavesYDefectos pares alternos: clave, valor por defecto
     */
    public RobloxTemplate(String titulo, String descripcion, String ubicacion,
                          String codigo, String... clavesYDefectos) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.codigo = codigo;
        this.parametros = new LinkedHashMap<>();
        for (int i = 0; i + 1 < clavesYDefectos.length; i += 2) {
            parametros.put(clavesYDefectos[i], clavesYDefectos[i + 1]);
        }
    }

    public String generarCodigo(Map<String, String> valores) {
        String resultado = codigo;
        for (Map.Entry<String, String> entrada : valores.entrySet()) {
            resultado = resultado.replace("{{" + entrada.getKey() + "}}", entrada.getValue());
        }
        return resultado;
    }

    @Override
    public String toString() {
        return titulo;
    }
}
