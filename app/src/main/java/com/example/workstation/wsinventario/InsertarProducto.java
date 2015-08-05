package com.example.workstation.wsinventario;

/* Codigo*/

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import android.text.InputType;
//import android.widget.ArrayAdapter;
//import android.widget.ArrayAdapter;

public class InsertarProducto extends Activity {

    EditText id, nombre, precio, cantidad;
    TextView idCategoriaSel, idProveedorSel;
    String idCategoria, idProveedor;
    Spinner categoria;
    Spinner proveedor;
    Button guardar;
    SQLHelper sqlhelper;
    SQLiteDatabase db;
    int indiceCategoria = 0;
    final String BASEDEDATOS = "BD_Inventario.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_producto);

        id = (EditText) findViewById(R.id.id);
        nombre = (EditText) findViewById(R.id.nombre);
        precio = (EditText) findViewById(R.id.precio);
        cantidad = (EditText) findViewById(R.id.cantidad);
        guardar = (Button) findViewById(R.id.guardar);
        categoria = (Spinner) findViewById(R.id.categoria);
        idCategoriaSel = (TextView) findViewById(R.id.idcategoriasel);
        proveedor = (Spinner) findViewById(R.id.proveedor);
        idProveedorSel = (TextView) findViewById(R.id.idproveedorsel);

        //Bundle
        Bundle parametros = getIntent().getExtras();
        id.setText(parametros.getString("id"));
        nombre.setText(parametros.getString("nom"));
        precio.setText(parametros.getString("precio"));
        cantidad.setText(parametros.getString("cantidad"));
        idCategoria = parametros.getString("idCategoria");
        idProveedor = parametros.getString("idProveedor");
        //Bundle

        Log.e("JMMC", idCategoria);
        idCategoriaSel.setText(idCategoria);

        Log.e("JMMC", idProveedor);
        idProveedorSel.setText(idProveedor);

        id.setSelected(false);

        guardar.setText(parametros.getString("boton"));
        //Log.e("JMMC",idPais);

        sqlhelper = new SQLHelper(this);
        //sqlhelper = new SQLHelper(this, BASEDEDATOS, null, 1);

        guardar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                db = sqlhelper.getWritableDatabase();
                // TODO Auto-generated method stub
                boolean finActividad = false;
                Log.e("Valor idCategoria", idCategoria.toString());
                Log.e("Valor idProveedor", idProveedor.toString());
                if (guardar.getText().equals("Insertar")) {
                    id.setEnabled(true);
                    try {
                        db.execSQL(" INSERT INTO productos "
                                + " (_idProducto, nombre, precio, cantidad, idCategoria, idProveedor) "
                                + " VALUES ( "
                                + " '" + id.getText().toString() + "', "
                                + " '" + nombre.getText().toString() + "', "
                                + " '" + precio.getText().toString() + "', "
                                + " '" + cantidad.getText().toString() + "', "
                                + " '" + idCategoria.toString() + "', "
                                + " '" + idProveedor.toString() + "'"
                                + " ) ");
                        finActividad = true;
                    } catch (SQLiteConstraintException ex) {
                        Toast.makeText(InsertarProducto.this, "Indice probablemente repetido", Toast.LENGTH_LONG).show();
                        finActividad = false;
                    }
                } else if (guardar.getText().equals("Modificar")) {
                    id.setEnabled(false);
                    db.execSQL("UPDATE productos" +
                            " SET" +
                            " nombre = '" + nombre.getText().toString() + "'," +
                            " precio = '" + precio.getText().toString() + "'," +
                            " cantidad = '" + cantidad.getText().toString() + "'," +
                            " idCategoria = '" + idCategoria.toString() + "'," +
                            " idProveedor = '" + idProveedor.toString() + "'" +
                            " WHERE" +
                            " _idProducto='" + id.getText().toString() + "'");
                    finActividad = true;
                }
                if (finActividad) {
                    db.close();
                    finish();
                }
            }
        });

        Log.e("JMMC", "2");

        //Spinner Categoria
        categoria = (Spinner) this.findViewById(R.id.categoria);
        db = sqlhelper.getWritableDatabase();

        Cursor cur = db.rawQuery("SELECT _idCategoria AS _id, nombre FROM categoria", null);

        int[] categoriaId = new int[]{android.R.id.text1};
        String[] categoriaNombre = new String[]{"nombre"};

        System.out.println("SEGUIMIENTO DE INDICE:" + idCategoria);



        SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item
                , cur, categoriaNombre, categoriaId, 0); //indice donde debe iniciar el spinner seleccionado
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoria.setAdapter(mAdapter);

        categoria.setSelection(getIndex(categoria, idCategoria));
        db.close();
        Log.e("JMMC", "3");

        categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Long itemId = parent.getItemIdAtPosition(pos);
                Log.e("JMMC", "Listener=" + itemId);
                idCategoria = itemId + "";
                idCategoriaSel.setText(idCategoria);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Log.e("JMMC", "4");

        //Spinner Proveedor
        proveedor = (Spinner) this.findViewById(R.id.proveedor);
        db = sqlhelper.getWritableDatabase();

        Cursor cur2 = db.rawQuery("SELECT _idProveedor AS _id, nombre FROM proveedor", null);

        int[] proveedorId = new int[]{android.R.id.text1};
        String[] proveedorNombre = new String[]{"nombre"};

        System.out.println("SEGUIMIENTO DE INDICE:" + idProveedor);



        SimpleCursorAdapter mAdapter2 = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item
                , cur2, proveedorNombre, proveedorId, 0); //indice donde debe iniciar el spinner seleccionado
        mAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        proveedor.setAdapter(mAdapter2);

        proveedor.setSelection(getIndex(proveedor, idProveedor));
        db.close();
        Log.e("JMMC", "3");

        proveedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Long itemId = parent.getItemIdAtPosition(pos);
                Log.e("JMMC", "Listener=" + itemId);
                idProveedor = itemId + "";
                idProveedorSel.setText(idProveedor);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Log.e("JMMC", "4");
    }

    //private method of your class
    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            Log.e("POSICION","Posicion spinner: "+ spinner.getItemAtPosition(i).toString()+" strign: "+myString);
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                i=spinner.getCount();//will stop the loop, kind of break, by making condition false
            }
        }
        Log.e("POSICION: ",index + "");
        return index;
    }


}