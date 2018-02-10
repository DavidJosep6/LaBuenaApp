package com.randomusers.davidjose.randomusers;

/**
 * Created by DavidJose on 03/02/2018.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    public AdminSQLiteOpenHelper(Context context, String nombre, CursorFactory factory, int version) {
        super(context, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //aquí creamos la tabla de usuario (email,genero,title,first,last,street,city,state,postcode,registered,picture)
        db.execSQL("create table usuarios(email text primary key, genero text, title text, first text, street text, city text, state text, postcode text, registered text, picture text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnte, int versionNue) {
        db.execSQL("drop table if exists usuarios");
        db.execSQL("create table usuarios(email text primary key, genero text, title text, first text, street text, city text, state text, postcode text, registered text, picture text)");
    }

}