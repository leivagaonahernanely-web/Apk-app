package com.leivagaonahernanely.robloxhelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final String PREFS = "ajustes";
    private static final String CLAVE_API = "api_key";

    private EditText entrada;
    private Button botonGenerar;
    private TextView estado;
    private TextView codigo;
    private String codigoActual = "";
    private boolean generando = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        entrada = findViewById(R.id.entrada);
        botonGenerar = findViewById(R.id.boton_generar);
        estado = findViewById(R.id.estado);
        codigo = findViewById(R.id.codigo);

        botonGenerar.setOnClickListener(v -> generar());

        findViewById(R.id.boton_copiar).setOnClickListener(v -> {
            if (codigoActual.isEmpty()) return;
            ClipboardManager portapapeles =
                    (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            portapapeles.setPrimaryClip(ClipData.newPlainText("script", codigoActual));
            Toast.makeText(this, R.string.copiado, Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.boton_compartir).setOnClickListener(v -> {
            if (codigoActual.isEmpty()) return;
            Intent envio = new Intent(Intent.ACTION_SEND);
            envio.setType("text/plain");
            envio.putExtra(Intent.EXTRA_TEXT, codigoActual);
            startActivity(Intent.createChooser(envio, getString(R.string.titulo_compartir)));
        });

        findViewById(R.id.boton_clave).setOnClickListener(v -> mostrarDialogoClave());
    }

    private void generar() {
        if (generando) return;

        String descripcion = entrada.getText().toString().trim();
        if (descripcion.isEmpty()) {
            estado.setText(R.string.error_sin_descripcion);
            return;
        }

        String clave = getSharedPreferences(PREFS, MODE_PRIVATE).getString(CLAVE_API, "");
        if (clave.isEmpty()) {
            estado.setText(R.string.error_sin_clave);
            mostrarDialogoClave();
            return;
        }

        generando = true;
        botonGenerar.setEnabled(false);
        estado.setText(R.string.generando);

        new Thread(() -> {
            try {
                String respuesta = ApiClient.generarCodigo(clave, descripcion);
                String limpio = CodeCleaner.limpiar(respuesta);
                runOnUiThread(() -> mostrarResultado(limpio));
            } catch (Exception e) {
                String mensaje = e.getMessage() != null ? e.getMessage() : e.toString();
                runOnUiThread(() -> mostrarError(mensaje));
            }
        }).start();
    }

    private void mostrarResultado(String texto) {
        generando = false;
        botonGenerar.setEnabled(true);
        codigoActual = texto;
        codigo.setText(texto);
        estado.setText("");
    }

    private void mostrarError(String mensaje) {
        generando = false;
        botonGenerar.setEnabled(true);
        estado.setText(getString(R.string.error_generico, mensaje));
    }

    private void mostrarDialogoClave() {
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        EditText campo = new EditText(this);
        campo.setHint(R.string.hint_clave);
        campo.setSingleLine(true);
        campo.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        campo.setText(prefs.getString(CLAVE_API, ""));

        new AlertDialog.Builder(this)
                .setTitle(R.string.titulo_clave)
                .setMessage(R.string.explicacion_clave)
                .setView(campo)
                .setPositiveButton(R.string.guardar, (dialogo, which) ->
                        prefs.edit().putString(CLAVE_API, campo.getText().toString().trim()).apply())
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }
}
