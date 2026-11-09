package com.leivagaonahernanely.robloxhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lista = findViewById(R.id.lista_plantillas);

        ArrayAdapter<RobloxTemplate> adaptador =
                new ArrayAdapter<RobloxTemplate>(this, android.R.layout.simple_list_item_2, Templates.TODAS) {
                    @Override
                    public View getView(int posicion, View convertView, ViewGroup padre) {
                        View vista = super.getView(posicion, convertView, padre);
                        RobloxTemplate plantilla = getItem(posicion);
                        ((TextView) vista.findViewById(android.R.id.text1)).setText(plantilla.titulo);
                        ((TextView) vista.findViewById(android.R.id.text2)).setText(plantilla.descripcion);
                        return vista;
                    }
                };

        lista.setAdapter(adaptador);
        lista.setOnItemClickListener((padre, vista, posicion, id) -> {
            Intent intent = new Intent(this, TemplateActivity.class);
            intent.putExtra(TemplateActivity.EXTRA_INDICE, posicion);
            startActivity(intent);
        });
    }
}
