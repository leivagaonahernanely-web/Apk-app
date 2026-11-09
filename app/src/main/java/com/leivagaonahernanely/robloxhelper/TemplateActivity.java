package com.leivagaonahernanely.robloxhelper;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedHashMap;
import java.util.Map;

public class TemplateActivity extends Activity {

    public static final String EXTRA_INDICE = "indice";

    private RobloxTemplate plantilla;
    private final Map<String, EditText> campos = new LinkedHashMap<>();
    private TextView vistaCodigo;
    private String codigoActual = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);

        int indice = getIntent().getIntExtra(EXTRA_INDICE, 0);
        plantilla = Templates.TODAS.get(indice);

        ((TextView) findViewById(R.id.titulo)).setText(plantilla.titulo);
        ((TextView) findViewById(R.id.descripcion)).setText(plantilla.descripcion);
        ((TextView) findViewById(R.id.ubicacion))
                .setText(getString(R.string.formato_ubicacion, plantilla.ubicacion));
        vistaCodigo = findViewById(R.id.codigo);

        LinearLayout contenedor = findViewById(R.id.contenedor_parametros);
        if (plantilla.parametros.isEmpty()) {
            findViewById(R.id.etiqueta_parametros).setVisibility(View.GONE);
            contenedor.setVisibility(View.GONE);
        } else {
            for (Map.Entry<String, String> parametro : plantilla.parametros.entrySet()) {
                TextView etiqueta = new TextView(this);
                etiqueta.setText(parametro.getKey().replace('_', ' ').toLowerCase());
                etiqueta.setPadding(0, 16, 0, 0);
                contenedor.addView(etiqueta);

                EditText campo = new EditText(this);
                campo.setText(parametro.getValue());
                campo.setSingleLine(true);
                campo.setInputType(InputType.TYPE_CLASS_TEXT);
                contenedor.addView(campo);

                campos.put(parametro.getKey(), campo);
            }
        }

        findViewById(R.id.boton_actualizar).setOnClickListener(v -> actualizarCodigo());

        findViewById(R.id.boton_copiar).setOnClickListener(v -> {
            ClipboardManager portapapeles =
                    (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            portapapeles.setPrimaryClip(ClipData.newPlainText(plantilla.titulo, codigoActual));
            Toast.makeText(this, R.string.copiado, Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.boton_compartir).setOnClickListener(v -> {
            Intent envio = new Intent(Intent.ACTION_SEND);
            envio.setType("text/plain");
            envio.putExtra(Intent.EXTRA_SUBJECT, plantilla.titulo);
            envio.putExtra(Intent.EXTRA_TEXT, codigoActual);
            startActivity(Intent.createChooser(envio, getString(R.string.titulo_compartir)));
        });

        actualizarCodigo();
    }

    private void actualizarCodigo() {
        Map<String, String> valores = new LinkedHashMap<>();
        for (Map.Entry<String, EditText> campo : campos.entrySet()) {
            valores.put(campo.getKey(), campo.getValue().getText().toString().trim());
        }
        codigoActual = plantilla.generarCodigo(valores);
        vistaCodigo.setText(codigoActual);
    }
}
