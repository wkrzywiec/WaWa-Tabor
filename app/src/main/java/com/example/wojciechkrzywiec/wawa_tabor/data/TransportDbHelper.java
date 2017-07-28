package com.example.wojciechkrzywiec.wawa_tabor.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Wojtek Krzywiec on 27/07/2017.
 */

public class TransportDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "transport.db";


    private static final int DATABASE_VERSION = 1;

    public TransportDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_TRANSPORT_TABLE =
                "CREATE TABLE " + TransportContract.TransportEntry.TABLE_NAME + " (" +

                        TransportContract.TransportEntry._ID   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        TransportContract.TransportEntry.COLUMN_TIME       + " TEXT NOT NULL, " +

                        TransportContract.TransportEntry.COLUMN_LINE + " TEXT NOT NULL,"  +

                        TransportContract.TransportEntry.COLUMN_BRIGADE   + " TEXT NOT NULL, " +

                        TransportContract.TransportEntry.COLUMN_LAT   + " REAL NOT NULL, " +

                        TransportContract.TransportEntry.COLUMN_LON   + " REAL NOT NULL);";


            sqLiteDatabase.execSQL(SQL_CREATE_TRANSPORT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TransportContract.TransportEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
