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


public class InsertarProveedor extends Activity {

    EditText id, nombre, apPaterno, apMaterno, telefono, email;
    Button guardar;
    SQLHelper sqlhelper;
    SQLiteDatabase db;
    final String BASEDEDATOS = "BD_Inventario.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_proveedor);

        id = (EditText) findViewById(R.id.edtidproveedor);
        nombre = (EditText) findViewById(R.id.edtnombreproveedor);
        apPaterno = (EditText) findViewById(R.id.edtappaternoproveedor);
        apMaterno = (EditText) findViewById(R.id.edtapmaternoproveedor);
        telefono = (EditText) findViewById(R.id.edttelefonoproveedor);
        email = (EditText) findViewById(R.id.edtemailproveedor);
        guardar = (Button) findViewById(R.id.btnguardarproveedor);

        //Bundle
        Bundle parametros = getIntent().getExtras();
        id.setText(parametros.getString("id"));
        nombre.setText(parametros.getString("nom"));
        apPaterno.setText(parametros.getString("apPaterno"));
        apMaterno.setText(parametros.getString("apMaterno"));
        telefono.setText(parametros.getString("telefono"));
        email.setText(parametros.getString("email"));
        //Bundle

        id.setSelected(false);

        guardar.setText(parametros.getString("boton"));
        //Log.e("JMMC",idPais);

        sqlhelper = new SQLHelper(this);
        //sqlhelper = new SQLHelper(this, BASEDEDATOS, null, 1);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = sqlhelper.getWritableDatabase();
                // TODO Auto-generated method stub
                boolean finActividad = false;

                if (guardar.getText().equals("Insertar")) {
                    id.setEnabled(true);
                    try {
                        db.execSQL(" INSERT INTO proveedor "
                                + " (_idProveedor, nombre, apPaterno, apMaterno, telefono, email) "
                                + " VALUES ( "
                                + " '" + id.getText().toString() + "', "
                                + " '" + nombre.getText().toString() + "', "
                                + " '" + apPaterno.getText().toString() + "', "
                                + " '" + apMaterno.getText().toString() + "', "
                                + " '" + telefono.getText().toString() + "', "
                                + " '" + email.getText().toString() + "'"
                                + " ) ");
                        finActividad = true;
                    } catch (SQLiteConstraintException ex) {
                        Toast.makeText(InsertarProveedor.this, "Indice probablemente repetido", Toast.LENGTH_LONG).show();
                        finActividad = false;
                    }
                } else if (guardar.getText().equals("Modificar")) {
                    id.setEnabled(false);
                    db.execSQL("UPDATE proveedor" +
                            " SET" +
                            " nombre = '" + nombre.getText().toString() + "'," +
                            " apPaterno = '" + apPaterno.getText().toString() + "'," +
                            " apMaterno = '" + apMaterno.getText().toString() + "'," +
                            " telefono = '" + telefono.getText().toString() + "'," +
                            " email = '" + email.getText().toString() + "'" +
                            " WHERE" +
                            " _idProveedor='" + id.getText().toString() + "'");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_insertar_proveedor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
