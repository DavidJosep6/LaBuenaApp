package com.randomusers.davidjose.randomusers;

/**
 * Funcionalidad: creacion y actualizacion de la BBDD.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "AdminSQLite...java";

    //USUARIO (email,genero,title,first,last,street,city,state,postcode,registered,picture)

    public AdminSQLiteOpenHelper(Context context, String nombre, CursorFactory factory, int version) {
        super(context, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "onCreate() - INICIO");
        db.execSQL("create table usuarios(email text primary key, genero text, title text, first text, street text, city text, state text, postcode text, registered text, picture text)");
        Log.i(TAG, "onCreate() - FIN");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnte, int versionNue) {
        Log.i(TAG, "onUpgrade() - INICIO");
        db.execSQL("drop table if exists usuarios");
        db.execSQL("create table usuarios(email text primary key, genero text, title text, first text, street text, city text, state text, postcode text, registered text, picture text)");
        Log.i(TAG, "onUpgrade() - FIN");
    }

}