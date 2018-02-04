package com.randomusers.davidjose.randomusers;

/**
 * Created by DavidJose on 04/02/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MainActivity {
    private static final String TAG = "MainActivity.java";
    MainActivityHelper myhelper;

    public MainActivity(Context context) {
        myhelper = new MainActivityHelper(context);
    }

    public long insertData(String email, String gender, String title, String first, String last, String street, String city, String state, String postcode, String registered, String picture) {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MainActivityHelper.EMAIL, email);
        contentValues.put(MainActivityHelper.GENDER, gender);
        contentValues.put(MainActivityHelper.TITLE, title);
        contentValues.put(MainActivityHelper.FIRST, first);
        contentValues.put(MainActivityHelper.LAST, last);
        contentValues.put(MainActivityHelper.STREET, street);
        contentValues.put(MainActivityHelper.CITY, city);
        contentValues.put(MainActivityHelper.STATE, state);
        contentValues.put(MainActivityHelper.POSTCODE, postcode);
        contentValues.put(MainActivityHelper.REGISTERED, registered);
        contentValues.put(MainActivityHelper.PICTURE, picture);
        long id = dbb.insert(MainActivityHelper.TABLE_NAME, null, contentValues);
        return id;
    }

    static class MainActivityHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "myDatabase";    // Database Name
        private static final String TABLE_NAME = "myTable";   // Table Name
        private static final int DATABASE_Version = 1;    // Database Version
        private static final String EMAIL = "EMAIL";     // Column I (Primary Key)
        private static final String GENDER = "GENDER";    //Column II
        private static final String TITLE = "TITLE";    // Column III
        private static final String FIRST = "FIRST";    // Column III
        private static final String LAST = "LAST";    // Column III
        private static final String STREET = "STREET";    // Column III
        private static final String CITY = "CITY";    // Column III
        private static final String STATE = "STATE";    // Column III
        private static final String POSTCODE = "POSTCODE";    // Column III
        private static final String REGISTERED = "REGISTERED";    // Column III
        private static final String PICTURE = "PICTURE";    // Column III

        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " (" + EMAIL + " VARCHAR(255) PRIMARY KEY," +
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

        public MainActivityHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            Log.i(TAG, "MainActivityHelper - INICIO");
            this.context = context;
            Log.i(TAG, "MainActivityHelper - FIN");
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
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch (Exception e) {
                Log.e(TAG, "onCreate - ERROR: " + e.toString());
            }
            Log.i(TAG, "onUpgrade - FIN");
        }
    }


}
