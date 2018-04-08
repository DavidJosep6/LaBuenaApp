package com.randomusers.davidjose.randomusers;

/**
 * Funcionalidad: Insertar (INSERT) y obtener (SELECT) datos de la BBDD.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import net.sqlcipher.database.*;

import android.util.Log;

public class GestionDatosLogin {
    private static final String TAG = "GestionDatosLogin.java";
    GestionDatosLoginHelper myhelperLogin;


    public GestionDatosLogin(Context context) {
        myhelperLogin = new GestionDatosLoginHelper(context);
    }

    public long insertData(String username, String password) {
        Log.i(TAG, "insertData() - INICIO");
        SQLiteDatabase dbb = myhelperLogin.getWritableDatabase(Login.desencriptadoBD);
        ContentValues contentValues = new ContentValues();
        contentValues.put(GestionDatosLoginHelper.USERNAME, username);
        contentValues.put(GestionDatosLoginHelper.PASSWORD, password);
        long id = dbb.insert(GestionDatosLoginHelper.TABLE_NAME, null, contentValues);
        Log.i(TAG, "insertData() - FIN");
        dbb.close();
        return id;
    }

    public String getData() {
        Log.i(TAG, "getData() - INICIO");
        SQLiteDatabase db = myhelperLogin.getWritableDatabase(Login.desencriptadoBD);
        String[] columns = {myhelperLogin.USERNAME,myhelperLogin.PASSWORD};
        Cursor cursor = db.query(myhelperLogin.TABLE_NAME, columns, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            String username = cursor.getString(cursor.getColumnIndex(myhelperLogin.USERNAME));
            String password = cursor.getString(cursor.getColumnIndex(myhelperLogin.PASSWORD));
            buffer.append(username + ";" + password + "\n");
        }
        Log.i(TAG, "getData() - Datos de la BBDD: " + buffer.toString());
        Log.i(TAG, "getData() - FIN");
        cursor.close();
        db.close();
        return buffer.toString();
    }

    static class GestionDatosLoginHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "RandomUsersLogin";    // Database Name
        private static final String TABLE_NAME = "LOGIN";   // Table Name
        private static final int DATABASE_Version = 1;    // Database Version
        private static final String USERNAME = "USERNAME";     // Column 1 (Primary Key)
        private static final String PASSWORD = "PASSWORD";     // Column 2


        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + USERNAME + " VARCHAR(255) PRIMARY KEY," +
                PASSWORD + " VARCHAR(255));";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        public GestionDatosLoginHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            Log.i(TAG, "GestionDatosLoginHelper - INICIO");
            this.context = context;

            Log.i(TAG, "GestionDatosLoginHelper - FIN");
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                Log.i(TAG, "onCreate - INICIO");
                SQLiteDatabase.loadLibs(context);
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                Log.e(TAG, "onCreate - ERROR: " + e.toString());
            }
            Log.i(TAG, "onCreate - FIN");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                Log.i(TAG, "onUpgrade - INICIO");
                SQLiteDatabase.loadLibs(context);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                onCreate(db);
            } catch (Exception e) {
                Log.e(TAG, "onCreate - ERROR: " + e.toString());
            }
            Log.i(TAG, "onUpgrade - FIN");
        }
    }


}
