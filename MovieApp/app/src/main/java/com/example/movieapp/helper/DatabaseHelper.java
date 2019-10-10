package com.example.movieapp.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.movieapp.database.DatabaseContract;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dbshowapp";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_NOTE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s INTEGER NOT NULL, " +
                    " %s TEXT NOT NULL, " +
                    " %s TEXT NOT NULL)",
            DatabaseContract.NoteColumns.TABLE_NAME,
            DatabaseContract.NoteColumns._ID,
            DatabaseContract.NoteColumns.ID_SHOW,
            DatabaseContract.NoteColumns.TITLE,
            DatabaseContract.NoteColumns.PATH
    );

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.NoteColumns.TABLE_NAME);
        onCreate(db);
    }
}
