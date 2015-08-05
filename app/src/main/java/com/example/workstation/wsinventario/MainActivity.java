package com.example.workstation.wsinventario;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

public class MainActivity extends Activity {

    ListView lstv;
    String namespace = "http://tempuri.org/";
    //String url = "http://10.0.2.2/WSAgenda/Service1.asmx";			//Local
    String url = "http://192.168.1.76/Service1.asmx";            //Local
//	String url = "http://192.168.43.103/WSAgenda/Service1.asmx";	//Intranet
//    String url = "http://192.168.2.59/WSAgenda/Service1.asmx";	//Intranet

    SQLHelper sqlhelper;
    SQLiteDatabase db;
    HttpTransportSE transporte;
    SoapObject request;
    SoapSerializationEnvelope sobre;
    SoapPrimitive resultado;
    int indice = 1;

    String idProducto = null;
    String nombreProducto = null;
    String precioProducto = null;
    String cantidadProducto = null;
    String idCategoriaProducto = null;
    String idProveedorProducto = null;

    String idCategoria = null;
    String nombreCategoria = null;

    String idProveedor = null;
    String nombreProveedor = null;
    String apPaternoProveedor = null;
    String apMaternoProveedor = null;
    String telefonoProveedor = null;
    String emailProveedor = null;

    String idTabla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //lstv = (ListView) findViewById(R.id.lstv);

        Resources res = getResources();

        TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("Productos");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Productos", res.getDrawable(android.R.drawable.ic_menu_mapmode));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("Categoria");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Categoria", res.getDrawable(android.R.drawable.ic_menu_agenda));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("Proveedores");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Proveedores", res.getDrawable(android.R.drawable.ic_menu_info_details));
        tabs.addTab(spec);

        tabs.setCurrentTab(0);

        lstv = null;
        //adaptador = null;
        lstv = (ListView) findViewById(R.id.lista1);
        indice = 1;
        actualiza();
        lstv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(
                    AdapterView<?> arg0,
                    View arg1,
                    int posicion,
                    long arg3) {
                // TODO Auto-generated method stub
                lanzarAlertaProductos("", "", "", "", idCategoria, idProveedor);
            }
        });

        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Log.i("AndroidTabsDemo", "Pulsada pestaña: " + tabId);
                //lstv = null;
                if (tabId == "Productos") {
                  //  lstv = null;
                    lstv = (ListView) findViewById(R.id.lista1);
                    indice = 1;
                    actualiza();
                    lstv.setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(
                                AdapterView<?> arg0,
                                View arg1,
                                int posicion,
                                long arg3) {
                            // TODO Auto-generated method stub
                           /* String valor = (String) lstv.getItemAtPosition(posicion);
                            StringTokenizer st = new StringTokenizer(valor, "-");
                            String clave = st.nextToken();
                            String nombre = st.nextToken();
                            String precio = st.nextToken();
                            String cantidad  = st.nextToken();
                            String idCategoria = st.nextToken();
                            String idProveedor = st.nextToken();
                            lanzarAlertaProductos(clave, nombre, precio, cantidad, idCategoria, idProveedor);*/
                            lanzarAlertaProductos("", "", "", "", idCategoria, idProveedor);
                        }
                    });
                }
                if (tabId == "Categoria") {
                    //lstv = null;
                    lstv = (ListView) findViewById(R.id.lista2);
                    indice = 2;
                    Log.e("INDICE: ", indice + "");
                    actualiza();
                    lstv.setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(
                                AdapterView<?> arg0,
                                View arg1,
                                int posicion,
                                long arg3) {
                            // TODO Auto-generated method stub
                            Log.e("LANZARCATEGORIA", "-2");
                            String valor = (String) lstv.getItemAtPosition(posicion);
                            StringTokenizer st = new StringTokenizer(valor, "-");
                            String clave = st.nextToken();
                            String nombre = st.nextToken();
                            Log.e("LANZARCATEGORIA", "-1");
                            lanzarAlertaCategoria(clave, nombre);
                            Log.e("LANZARCATEGORIA", "0");
                        }
                    });
                }
                if (tabId == "Proveedores") {
                    ///lstv = null;
                    lstv = (ListView) findViewById(R.id.lista3);
                    indice = 3;
                    actualiza();
                    lstv.setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(
                                AdapterView<?> arg0,
                                View arg1,
                                int posicion,
                                long arg3) {
                            // TODO Auto-generated method stub
                            String valor = (String) lstv.getItemAtPosition(posicion);
                            StringTokenizer st = new StringTokenizer(valor, "-");
                            String clave = st.nextToken();
                            String nombre = st.nextToken();
                            String apPaterno = st.nextToken();
                            String apMaterno = st.nextToken();
                            String telefono = st.nextToken();
                            String email = st.nextToken();
                            lanzarAlertaProveedor(clave, nombre, apPaterno, apMaterno, telefono, email);
                        }
                    });
                }
            }
        });
    }


    public void lanzarAlertaProductos(final String cve, final String nom, final String precio,
                                      final String cantidad, final String idCategoria, final String idProveedor) {
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Que deseas realizar con el Contacto");
        //dialog.setMessage(nom);
        dialog.setMessage(nombreProducto);
        dialog.setCancelable(true);
        dialog.setPositiveButton(
                "Eliminar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        borrar(cve, 1);
                    }
                });
        dialog.setNegativeButton("Modificar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Intent intent = new Intent(
                                getApplicationContext(),
                                InsertarProducto.class);
                        intent.putExtra("id", idProducto);
                        intent.putExtra("nom", nombreProducto);
                        intent.putExtra("precio", precioProducto);
                        intent.putExtra("cantidad", cantidadProducto);
                        intent.putExtra("idCategoria", idCategoriaProducto);
                        intent.putExtra("idProveedor", idProveedorProducto);
                        intent.putExtra("boton", "Modificar");
                        startActivity(intent);
                    }
                });
        dialog.show();
    }

    public void lanzarAlertaCategoria(final String cve, final String nom) {
        Log.e("LANZARCATEGORIA", "1");
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Que deseas realizar con el Dato");
        dialog.setMessage(nom);
        dialog.setCancelable(true);
        dialog.setPositiveButton(
                "Eliminar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        borrar(cve, 2);
                        Log.e("LANZARCATEGORIA", "2");
                    }
                });
        dialog.setNegativeButton("Modificar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Intent intent = new Intent(
                                getApplicationContext(),
                                InsertarCategoria.class);
                        intent.putExtra("id", cve);
                        intent.putExtra("nom", nom);
                        intent.putExtra("boton", "Modificar");
                        startActivity(intent);
                        Log.e("LANZARCATEGORIA", "3");
                    }
                });
        Log.e("LANZARCATEGORIA", "4");
        dialog.show();
        Log.e("LANZARCATEGORIA", "5");
    }


    public void lanzarAlertaProveedor(final String cve, final String nom, final String apPaterno,
                                      final String apMaterno, final String telefono, final String email) {
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("Que deseas realizar con el Dato");
        dialog.setMessage(nom);
        dialog.setCancelable(true);
        dialog.setPositiveButton(
                "Eliminar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        borrar(cve, 3);
                    }
                });
        dialog.setNegativeButton("Modificar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        Intent intent = new Intent(
                                getApplicationContext(),
                                InsertarProveedor.class);
                        intent.putExtra("id", cve);
                        intent.putExtra("nom", nom);
                        intent.putExtra("apPaterno", apPaterno);
                        intent.putExtra("apMaterno", apMaterno);
                        intent.putExtra("telefono", telefono);
                        intent.putExtra("email", email);
                        intent.putExtra("boton", "Modificar");
                        startActivity(intent);
                    }
                });
        dialog.show();
    }

    protected void borrar(String id, int indice) {
        sqlhelper = new SQLHelper(this);
        db = sqlhelper.getWritableDatabase();
        if (indice == 1) {
            db.execSQL("delete from productos where _idProducto ='" + id + "'");
        } else if (indice == 2) {
            db.execSQL("delete from categoria where _idCategoria ='" + id + "'");
        } else if (indice == 3) {
            db.execSQL("delete from proveedor where _idProveedor ='" + id + "'");
        }

        db.close();
        actualiza();
    }

    protected void actualiza() {
        sqlhelper = new SQLHelper(this);
        db = sqlhelper.getWritableDatabase();
        Cursor c = null;
        ArrayList<String> arreglo = null;

        if (indice == 1) {
            c = db.rawQuery(
                    "SELECT "
                            + "pts._idProducto, "
                            + "pts.nombre, "
                            + "pts.precio, "
                            + "pts.cantidad, "
                            + "ct.nombre, "
                            + "pv.nombre "
                            + " FROM productos AS pts "
                            + " INNER JOIN categoria AS ct "
                            + " ON pts.idCategoria = ct._idCategoria "
                            + " INNER JOIN proveedor AS pv "
                            + " ON pts.idProveedor = pv._idProveedor", null);

            Log.e("cantidad", c.getColumnCount() + " contador" + c.getCount());
            if (c.moveToFirst()) {
                arreglo = new ArrayList<String>(c.getCount());
                do {
                    idProducto = c.getString(0);
                    nombreProducto = c.getString(1);
                    precioProducto = c.getString(2);
                    cantidadProducto = c.getString(3);
                    idCategoriaProducto = c.getString(4);
                    idProveedorProducto = c.getString(5);
                    arreglo.add(idProducto + " - " + nombreProducto + "   -   $" + precioProducto + " \nCantidad:  " + cantidadProducto + "\nCategoria:  " + idCategoriaProducto + " \nProveedor:  " + idProveedorProducto);
                    //arreglo.add(id + " - " + nom + " - " + precio + " - " + cantidad + " - " + rfCategoria + " - ");
                } while (c.moveToNext());
            } else {
                arreglo = new ArrayList<String>(1);
                arreglo.add("Sin Datos");
            }
        } else if (indice == 2) {
            c = db.rawQuery("SELECT * FROM categoria ", null);


            Log.e("cantidad", c.getColumnCount() + " contador" + c.getCount());
            if (c.moveToFirst()) {
                arreglo =
                        new ArrayList<String>(c.getCount());
                do {
                    idCategoria = c.getString(0);
                    nombreCategoria = c.getString(1);
                    arreglo.add(idCategoria + "-" + nombreCategoria);
                } while (c.moveToNext());
            } else {
                arreglo = new ArrayList<String>(1);
                arreglo.add("Sin Datos");
            }
        } else if (indice == 3) {
            c = db.rawQuery("SELECT * FROM proveedor ", null);

            Log.e("cantidad", c.getColumnCount() + " contador" + c.getCount());
            if (c.moveToFirst()) {
                arreglo = new ArrayList<String>(c.getCount());
                do {
                    idProveedor = c.getString(0);
                    nombreProveedor= c.getString(1);
                    apPaternoProveedor = c.getString(2);
                    apMaternoProveedor = c.getString(3);
                    telefonoProveedor = c.getString(4);
                    emailProveedor = c.getString(5);
                    arreglo.add(idProveedor + "-" + nombreProveedor + "-" + apPaternoProveedor + "-"
                            + apMaternoProveedor + "-" + telefonoProveedor + "-" + emailProveedor);
                } while (c.moveToNext());
            } else {
                arreglo = new ArrayList<String>(1);
                arreglo.add("Sin Datos");
            }
        }

//		Cursor c = db.rawQuery("select c.id, c.nombre, c.telefono, c.correo, c.pais from contacto c",null);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_list_item_1,
                        arreglo
                );
        lstv.setAdapter(adapter);
        //} //
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insertar:
                if (indice == 1) {
                    Intent intent = new Intent(getApplicationContext(), InsertarProducto.class);
                    intent.putExtra("indice", 1);
                    intent.putExtra("id", "");
                    intent.putExtra("nom", "");
                    intent.putExtra("precio", "");
                    intent.putExtra("cantidad", "");
                    intent.putExtra("idCategoria", "");
                    intent.putExtra("idProveedor", "");
                    intent.putExtra("boton", "Insertar");
                    startActivity(intent);
                } else if (indice == 2) {
                    Intent intent = new Intent(getApplicationContext(), InsertarCategoria.class);
                    intent.putExtra("indice", 2);
                    intent.putExtra("id", "");
                    intent.putExtra("nom", "");
                    intent.putExtra("boton", "Insertar");
                    startActivity(intent);
                } else if (indice == 3) {
                    Intent intent = new Intent(getApplicationContext(), InsertarProveedor.class);
                    intent.putExtra("indice", 3);
                    intent.putExtra("id", "");
                    intent.putExtra("nom", "");
                    intent.putExtra("apPaterno", "");
                    intent.putExtra("apMaterno", "");
                    intent.putExtra("telefono", "");
                    intent.putExtra("email", "");
                    intent.putExtra("boton", "Insertar");
                    startActivity(intent);
                }

                break;
            case R.id.actualizar:
                actualiza();
                break;
            case R.id.descargarCatalogo:
                descargarCatalogo();
                break;
            case R.id.cargarDatos:
                cargarDatos();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void descargarCatalogo() {
        sqlhelper = new SQLHelper(this);
        db = sqlhelper.getWritableDatabase();
        db.execSQL("DELETE FROM categoria");
        db.close();
        Log.e("AGET", "CATEGORIA");
        Log.e("JMMC", "SOAP-1");
        String accionSoap1 = "http://tempuri.org/consultaCategoria";
        String metodo1 = "consultaCategoria";
        String cadena1 = "";
        try {
            Log.e("JMMC", "SOAP-2");
            request = new SoapObject(namespace, metodo1);
            Log.e("JMMC", "SOAP-3");
            sobre = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            Log.e("JMMC", "SOAP-4");
            sobre.dotNet = true;
            Log.e("JMMC", "SOAP-5");
            sobre.setOutputSoapObject(request);
            Log.e("JMMC", "SOAP-6");

            // Habilitar la comunicacion con el
            // Web Services desde el Activity Principal
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            transporte = new HttpTransportSE(url);
            Log.e("JMMC", "SOAP-7");
            transporte.call(accionSoap1, sobre);
            Log.e("JMMC", "SOAP-8");
            resultado = (SoapPrimitive) sobre.getResponse();
            Log.e("JMMC", "SOAP-9");
            cadena1 = resultado.toString();
            Log.e("JMMC", "SOAP-10");
            //Toast.makeText(this, cadena, Toast.LENGTH_LONG).show();
            StringTokenizer st1 = new StringTokenizer(cadena1, "*");
            db = sqlhelper.getWritableDatabase();

            while (st1.hasMoreElements()) {
                Log.e("JMMC", "SOAP-11");
                StringTokenizer st2 = new StringTokenizer(st1.nextToken(), ",");
                Log.e("JMMC", "SOAP-12");
                String id = st2.nextToken().toString();
                String nombre = st2.nextToken().toString();
                Log.e("JMMC", "SOAP-13-" + id + "-" + nombre);
                db.execSQL("insert into categoria (_idCategoria, nombre) "
                        + "values ('" + id + "','" + nombre + "')");
            }

            db.close();
            Toast.makeText(this, "Datos del Catalogo \n Descargados (Categoria)", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("JMMC", "SOAP-" + e.toString());
        }

        /*    PROVEEDORES   */

        db = sqlhelper.getWritableDatabase();
        db.execSQL("DELETE FROM proveedor");
        db.close();
        Log.e("AGET", "PROVEEDOR");
        Log.e("JMMC", "SOAP-1");
        String accionSoap2 = "http://tempuri.org/consultaProveedores";
        String metodo2 = "consultaProveedores";
        String cadena2 = "";
        try {
            Log.e("JMMC", "SOAP-2");
            request = new SoapObject(namespace, metodo2);
            Log.e("JMMC", "SOAP-3");
            sobre = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            Log.e("JMMC", "SOAP-4");
            sobre.dotNet = true;
            Log.e("JMMC", "SOAP-5");
            sobre.setOutputSoapObject(request);
            Log.e("JMMC", "SOAP-6");

            // Habilitar la comunicacion con el
            // Web Services desde el Activity Principal
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            transporte = new HttpTransportSE(url);
            Log.e("JMMC", "SOAP-7");
            transporte.call(accionSoap2, sobre);
            Log.e("JMMC", "SOAP-8");
            resultado = (SoapPrimitive) sobre.getResponse();
            Log.e("JMMC", "SOAP-9");
            cadena2 = resultado.toString();
            Log.e("JMMC", "SOAP-10");
            //Toast.makeText(this, cadena, Toast.LENGTH_LONG).show();
            StringTokenizer st1 = new StringTokenizer(cadena2, "*");
            db = sqlhelper.getWritableDatabase();

            while (st1.hasMoreElements()) {
                Log.e("JMMC", "SOAP-11");
                StringTokenizer st2 = new StringTokenizer(st1.nextToken(), ",");
                Log.e("JMMC", "SOAP-12");
                String id = st2.nextToken().toString();
                String nombre = st2.nextToken().toString();
                String apPaterno = st2.nextToken().toString();
                String apMaterno = st2.nextToken().toString();
                String telefono = st2.nextToken().toString();
                String email = st2.nextToken().toString();
                Log.e("JMMC", "SOAP-13-" + id + "-" + nombre + "-" + apPaterno + "-" + apMaterno
                        + "-" + telefono + "-" + email);
                db.execSQL("insert into proveedor (_idProveedor, nombre, apPaterno, apMaterno, telefono, email) "
                        + "values ('" + id + "','" + nombre + "','" + apPaterno + "','" + apMaterno + "','" + telefono + "','" + email + "')");
            }

            db.close();
            Toast.makeText(this, "Datos del Catalogo \n Descargados (Proveedores)", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("JMMC", "SOAP-" + e.toString());
        }


        /* PRODUCTOS */

        db = sqlhelper.getWritableDatabase();
        db.execSQL("DELETE FROM productos");
        db.close();
        Log.e("AGET", "PRODUCTOS");
        Log.e("JMMC", "SOAP-1");
        String accionSoap3 = "http://tempuri.org/consultaProductos";
        String metodo3 = "consultaProductos";
        String cadena3 = "";
        try {
            Log.e("JMMC", "SOAP-2");
            request = new SoapObject(namespace, metodo3);
            Log.e("JMMC", "SOAP-3");
            sobre = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            Log.e("JMMC", "SOAP-4");
            sobre.dotNet = true;
            Log.e("JMMC", "SOAP-5");
            sobre.setOutputSoapObject(request);
            Log.e("JMMC", "SOAP-6");
            // Habilitar la comunicacion con el
            // Web Services desde el Activity Principal
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            transporte = new HttpTransportSE(url);
            Log.e("JMMC", "SOAP-7");
            transporte.call(accionSoap3, sobre);
            Log.e("JMMC", "SOAP-8");
            resultado = (SoapPrimitive) sobre.getResponse();
            Log.e("JMMC", "SOAP-9");
            cadena3 = resultado.toString();
            Log.e("JMMC", "SOAP-10");
            //Toast.makeText(this, cadena, Toast.LENGTH_LONG).show();
            StringTokenizer st1 = new StringTokenizer(cadena3, "*");
            db = sqlhelper.getWritableDatabase();

            while (st1.hasMoreElements()) {
                Log.e("JMMC", "SOAP-11");
                StringTokenizer st2 = new StringTokenizer(st1.nextToken(), ",");
                Log.e("JMMC", "SOAP-12");
                String id = st2.nextToken().toString();
                String nombre = st2.nextToken().toString();
                String precio = st2.nextToken().toString();
                String cantidad = st2.nextToken().toString();
                String idCategoria = st2.nextToken().toString();
                String idProveedor = st2.nextToken().toString();
                Log.e("JMMC", "SOAP-13-" + id + "-" + nombre + "-" + precio + "-" + cantidad
                        + "-" + idCategoria + "-" + idProveedor);
                db.execSQL("insert into productos (_idProducto, nombre,precio,cantidad,idCategoria,idProveedor) "
                        + "values ('" + id + "','" + nombre + "','" + precio + "','" + cantidad + "','" + idCategoria + "','" + idProveedor + "')");
            }

            db.close();
            Toast.makeText(this, "Datos del Catalogo \n Descargados (Productos)", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("JMMC", "SOAP-" + e.toString());
        }

    }

    protected void cargarDatos() {
        sqlhelper = new SQLHelper(this);

        String accionSoap1 = "http://tempuri.org/insertarCategoria";
        String metodo1 = "insertarCategoria";
        String cadena1 = "";
        String id1 = "";
        try {
            db = sqlhelper.getWritableDatabase();
            Cursor c = db.rawQuery("select * from categoria", null);
            if (c.moveToFirst()) {
                do {
                    id1 = c.getString(0);
                    String nom = c.getString(1);

//					Log.e("JMMC","SOAP-2b-"+id+"-"+nom+"-"+tel+"-"+mail+"-"+pais);

                    request = new SoapObject(namespace, metodo1);
                    Log.e("JMMC", "SOAP-3b");
                    Log.e("INSERTADO",id1+"");
                    request.addProperty("id", id1);
                    request.addProperty("nombre", nom);

                    Log.e("INSERTADO", Integer.valueOf(id1) + "");

                    sobre = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    Log.e("JMMC", "SOAP-4b");
                    sobre.dotNet = true;
                    Log.e("JMMC", "SOAP-5b");
                    sobre.setOutputSoapObject(request);
                    Log.e("JMMC", "SOAP-6b");

                    // Habilitar la comunicacion con el Web Services desde el Activity Principal
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    transporte = new HttpTransportSE(url);
                    Log.e("JMMC", "SOAP-7b");
                    transporte.call(accionSoap1, sobre);
                    Log.e("JMMC", "SOAP-8b");
                    resultado = (SoapPrimitive) sobre.getResponse();
                    Log.e("JMMC", "SOAP-9b");
                    cadena1 = resultado.toString();
                    Log.e("JMMC", "SOAP-10b");
                } while (c.moveToNext());
                Toast.makeText(this, "Datos Cargados", Toast.LENGTH_LONG).show();
            }
            db.close();
        } catch (Exception e) {
            Log.e("JMMC", "SOAP-b-" + e.toString());
            Toast.makeText(this, "Rebice el indice: " + id1, Toast.LENGTH_LONG).show();
        }

        /* PROVEEDORES */


       String accionSoap2 = "http://tempuri.org/insertarProveedor";
        String metodo2 = "insertarProveedor";
        String cadena2 = "";
        String id2 = "",nombre,apPaterno,apMaterno,telefono,email;
        try {
            db = sqlhelper.getWritableDatabase();
            Cursor c = db.rawQuery("select * from proveedor", null);
            if (c.moveToFirst()) {
                do {
                    id2 = c.getString(0);
                    nombre = c.getString(1);
                    apPaterno = c.getString(2);
                    apMaterno = c.getString(3);
                    telefono = c.getString(4);
                    email = c.getString(5);

//					Log.e("JMMC","SOAP-2b-"+id+"-"+nom+"-"+tel+"-"+mail+"-"+pais);

                    request = new SoapObject(namespace, metodo2);
                    Log.e("JMMC", "SOAP-3b");
                    request.addProperty("id", id2);
                    request.addProperty("nombre", nombre);
                    request.addProperty("apPaterno", apPaterno);
                    request.addProperty("apMaterno", apMaterno);
                    request.addProperty("telefono", telefono);
                    request.addProperty("email", email);

                    sobre = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    Log.e("JMMC", "SOAP-4b");
                    sobre.dotNet = true;
                    Log.e("JMMC", "SOAP-5b");
                    sobre.setOutputSoapObject(request);
                    Log.e("JMMC", "SOAP-6b");

                    // Habilitar la comunicacion con el Web Services desde el Activity Principal
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    transporte = new HttpTransportSE(url);
                    Log.e("JMMC", "SOAP-7b");
                    transporte.call(accionSoap2, sobre);
                    Log.e("JMMC", "SOAP-8b");
                    resultado = (SoapPrimitive) sobre.getResponse();
                    Log.e("JMMC", "SOAP-9b");
                    cadena2 = resultado.toString();
                    Log.e("JMMC", "SOAP-10b");
                } while (c.moveToNext());
                Toast.makeText(this, "Datos Cargados", Toast.LENGTH_LONG).show();
            }
            db.close();
        } catch (Exception e) {
            Log.e("JMMC", "SOAP-b-" + e.toString());
            Toast.makeText(this, "Rebice el indice: " + id2, Toast.LENGTH_LONG).show();
        }


        /* PRODUCTOS */


        String accionSoap3 = "http://tempuri.org/insertarProducto";
        String metodo3 = "insertarProducto";
        String cadena3 = "";
        String id3 = "",nombreProducto,precio,cantidad,idCategoria,idProveedor;
        try {
            db = sqlhelper.getWritableDatabase();
            Cursor c = db.rawQuery("select * from productos", null);
            if (c.moveToFirst()) {
                do {
                    id3 = c.getString(0);
                     nombreProducto = c.getString(1);
                    precio = c.getString(2);
                    cantidad = c.getString(3);
                    idCategoria = c.getString(4);
                    idProveedor = c.getString(5);

                    request = new SoapObject(namespace, metodo3);
                    Log.e("JMMC", "SOAP-3b");
                    request.addProperty("id", id3);
                    request.addProperty("nombre", nombreProducto);
                    request.addProperty("precio", precio);
                    request.addProperty("cantidad", cantidad);
                    request.addProperty("idCategoria", idCategoria);
                    request.addProperty("idProveedor", idProveedor);

                    sobre = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    Log.e("JMMC", "SOAP-4b");
                    sobre.dotNet = true;
                    Log.e("JMMC", "SOAP-5b");
                    sobre.setOutputSoapObject(request);
                    Log.e("JMMC", "SOAP-6b");

                    // Habilitar la comunicacion con el Web Services desde el Activity Principal
                    StrictMode.ThreadPolicy policy =
                            new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    transporte = new HttpTransportSE(url);
                    Log.e("JMMC", "SOAP-7b");
                    transporte.call(accionSoap3, sobre);
                    Log.e("JMMC", "SOAP-8b");
                    resultado = (SoapPrimitive) sobre.getResponse();
                    Log.e("JMMC", "SOAP-9b");
                    cadena3 = resultado.toString();
                    Log.e("JMMC", "SOAP-10b");
                } while (c.moveToNext());
                Toast.makeText(this, "Datos Cargados", Toast.LENGTH_LONG).show();
            }
            db.close();
        } catch (Exception e) {
            Log.e("JMMC", "SOAP-b-" + e.toString());
            Toast.makeText(this, "Rebice el indice: " + id3, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            actualiza();
        }
    }
}