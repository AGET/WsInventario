package com.example.workstation.wsinventario;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class InsertarCategoria extends Activity {
    EditText id, nombre;
    Button guardar;
    SQLHelper sqlhelper;
    SQLiteDatabase db;
    final String BASEDEDATOS = "BD_Inventario.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_categoria);

        id = (EditText) findViewById(R.id.edtidcategoria);
        nombre = (EditText) findViewById(R.id.edtnombrecategoria);
        guardar = (Button) findViewById(R.id.btnguardarcategoria);
        //Bundle
        Bundle parametros = getIntent().getExtras();
        id.setText(parametros.getString("id"));
        nombre.setText(parametros.getString("nom"));
        //Bundle
        id.setSelected(false);

        guardar.setText(parametros.getString("boton"));

        sqlhelper = new SQLHelper(this);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = sqlhelper.getWritableDatabase();
                // TODO Auto-generated method stub
                boolean finActividad = false;
                if (guardar.getText().equals("Insertar")) {
                    id.setEnabled(true);
                    try {
                        db.execSQL(" INSERT INTO categoria "
                                + " (_idCategoria, nombre) "
                                + " VALUES ( "
                                + " '" + id.getText().toString() + "', "
                                + " '" + nombre.getText().toString() + "'"
                                + " ) ");
                        finActividad = true;
                    } catch (SQLiteConstraintException ex) {
                        Toast.makeText(InsertarCategoria.this, "Indice probablemente repetido", Toast.LENGTH_LONG).show();
                        finActividad = false;
                    }
                } else if (guardar.getText().equals("Modificar")) {
                    id.setEnabled(false);
                    db.execSQL("UPDATE categoria" +
                            " SET" +
                            " nombre = '" + nombre.getText().toString() + "'"
                            + " WHERE" +
                            " _idCategoria='" + id.getText().toString() + "'");
                    finActividad = true;
                }
                if (finActividad) {
                    db.close();
                    finish();
                }
            }
        });
        Log.e("JMMC", "2");
    }
}
