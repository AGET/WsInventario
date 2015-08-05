package com.example.workstation.wsinventario;

/**
 * Created by workstation on 21/07/15.
 */


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class SQLHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME ="BD_Inventario.db";

    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE IF NOT EXISTS categoria ( "
                + "_idCategoria INTEGER PRIMARY KEY, "
                + " nombre TEXT )");

        db.execSQL("CREATE TABLE IF NOT EXISTS proveedor ( "
                + "_idProveedor INTEGER PRIMARY KEY, "
                + " nombre TEXT,"
                + " apPaterno TEXT,"
                + " apMaterno TEXT,"
                + " telefono TEXT,"
                + " email TEXT )");

        db.execSQL("CREATE TABLE IF NOT EXISTS productos ( "
                + " _idProducto INTEGER PRIMARY KEY, "
                + " nombre TEXT, "
                + " precio DECIMAL, "
                + " cantidad INTEGER,"
                + " idCategoria INTEGER,"
                + " idProveedor INTERGER ) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }

}
