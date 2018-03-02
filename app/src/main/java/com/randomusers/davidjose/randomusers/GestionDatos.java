package com.randomusers.davidjose.randomusers;

/**
 * Funcionalidad: Insertar (INSERT) y obtener (SELECT) datos de la BBDD.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GestionDatos {
    private static final String TAG = "GestionDatos.java";
    GestionDatosHelper myhelper;

    public GestionDatos(Context context) {
        myhelper = new GestionDatosHelper(context);
    }

    public long insertData(String username, String password, String email, String gender, String title, String first, String last, String street, String city, String state, String postcode, String registered, String picture) {
        Log.i(TAG, "insertData() - INICIO");
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GestionDatosHelper.USERNAME, username);
        contentValues.put(GestionDatosHelper.PASSWORD, password);
        contentValues.put(GestionDatosHelper.EMAIL, email);
        contentValues.put(GestionDatosHelper.GENDER, gender);
        contentValues.put(GestionDatosHelper.TITLE, title);
        contentValues.put(GestionDatosHelper.FIRST, first);
        contentValues.put(GestionDatosHelper.LAST, last);
        contentValues.put(GestionDatosHelper.STREET, street);
        contentValues.put(GestionDatosHelper.CITY, city);
        contentValues.put(GestionDatosHelper.STATE, state);
        contentValues.put(GestionDatosHelper.POSTCODE, postcode);
        contentValues.put(GestionDatosHelper.REGISTERED, registered);
        contentValues.put(GestionDatosHelper.PICTURE, picture);
        long id = dbb.insert(GestionDatosHelper.TABLE_NAME, null, contentValues);
        Log.i(TAG, "insertData() - FIN");
        return id;
    }

    public String getData() {
        Log.i(TAG, "getData() - INICIO");
        SQLiteDatabase db = myhelper.getWritableDatabase();
        String[] columns = {myhelper.USERNAME,myhelper.PASSWORD,myhelper.EMAIL, myhelper.GENDER, myhelper.TITLE, myhelper.FIRST, myhelper.LAST, myhelper.STREET, myhelper.CITY, myhelper.STATE, myhelper.POSTCODE, myhelper.REGISTERED, myhelper.PICTURE};
        Cursor cursor = db.query(myhelper.TABLE_NAME, columns, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()) {
            String username = cursor.getString(cursor.getColumnIndex(myhelper.USERNAME));
            String password = cursor.getString(cursor.getColumnIndex(myhelper.PASSWORD));
            String email = cursor.getString(cursor.getColumnIndex(myhelper.EMAIL));
            String gender = cursor.getString(cursor.getColumnIndex(myhelper.GENDER));
            String title = cursor.getString(cursor.getColumnIndex(myhelper.TITLE));
            String first = cursor.getString(cursor.getColumnIndex(myhelper.FIRST));
            String last = cursor.getString(cursor.getColumnIndex(myhelper.LAST));
            String street = cursor.getString(cursor.getColumnIndex(myhelper.STREET));
            String city = cursor.getString(cursor.getColumnIndex(myhelper.CITY));
            String state = cursor.getString(cursor.getColumnIndex(myhelper.STATE));
            String postcode = cursor.getString(cursor.getColumnIndex(myhelper.POSTCODE));
            String registered = cursor.getString(cursor.getColumnIndex(myhelper.REGISTERED));
            String picture = cursor.getString(cursor.getColumnIndex(myhelper.PICTURE));
            buffer.append(username + ";" + password + ";" + email + ";" + gender + ";" + title + ";" + first + " " + last + ";" + street + ", " + city + ", " + state + ", " + postcode + ";" + registered + ";" + picture + "\n");
        }
        Log.i(TAG, "getData() - Datos de la BBDD: " + buffer.toString());
        Log.i(TAG, "getData() - FIN");
        return buffer.toString();
    }

    static class GestionDatosHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "RandomUsers";    // Database Name
        private static final String TABLE_NAME = "USUARIOS";   // Table Name
        private static final int DATABASE_Version = 1;    // Database Version
        private static final String USERNAME = "USERNAME";     // Column 1 (Primary Key)
        private static final String PASSWORD = "PASSWORD";     // Column 2
        private static final String EMAIL = "EMAIL";     // Column 3
        private static final String GENDER = "GENDER";    //Column 4
        private static final String TITLE = "TITLE";    // Column 5
        private static final String FIRST = "FIRST";    // Column 6
        private static final String LAST = "LAST";    // Column 7
        private static final String STREET = "STREET";    // Column 8
        private static final String CITY = "CITY";    // Column 9
        private static final String STATE = "STATE";    // Column 10
        private static final String POSTCODE = "POSTCODE";    // Column 11
        private static final String REGISTERED = "REGISTERED";    // Column 12
        private static final String PICTURE = "PICTURE";    // Column 13


        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + USERNAME + " VARCHAR(255) PRIMARY KEY," +
                PASSWORD + " VARCHAR(255) ," +
                EMAIL + " VARCHAR(255) ," +
                GENDER + " VARCHAR(255) ," +
                TITLE + " VARCHAR(225) , " +
                FIRST + " VARCHAR(225) , " +
                LAST + " VARCHAR(225) , " +
                STREET + " VARCHAR(225) , " +
                CITY + " VARCHAR(225) , " +
                STATE + " VARCHAR(225) , " +
                POSTCODE + " VARCHAR(225) , " +
                REGISTERED + " VARCHAR(225) , " +
                PICTURE + " VARCHAR(225));";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context;

        public GestionDatosHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            Log.i(TAG, "GestionDatosHelper - INICIO");
            this.context = context;
            Log.i(TAG, "GestionDatosHelper - FIN");
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                Log.i(TAG, "onCreate - INICIO");
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
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                onCreate(db);
            } catch (Exception e) {
                Log.e(TAG, "onCreate - ERROR: " + e.toString());
            }
            Log.i(TAG, "onUpgrade - FIN");
        }
    }


}
