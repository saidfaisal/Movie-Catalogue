package com.dicoding.picodiploma.mybottomnavigation.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dicoding.picodiploma.mybottomnavigation.model.Show;

import java.util.ArrayList;

import static com.dicoding.picodiploma.mybottomnavigation.database.DatabaseContract.NoteColumns.PATH;
import static com.dicoding.picodiploma.mybottomnavigation.database.DatabaseContract.NoteColumns.TABLE_NAME;
import static com.dicoding.picodiploma.mybottomnavigation.database.DatabaseContract.NoteColumns.TITLE;
import static com.dicoding.picodiploma.mybottomnavigation.database.DatabaseContract.NoteColumns._ID;

public class ShowHelper {
    private static final String DATABASE_TABLE = TABLE_NAME;
    private static DatabaseHelper dataBaseHelper;
    private static ShowHelper INSTANCE;

    private static SQLiteDatabase database;

    private ShowHelper(Context context) {
        dataBaseHelper = new DatabaseHelper(context);
    }

    public static ShowHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ShowHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();
        if (database.isOpen())
            database.close();
    }

    public ArrayList<Show> getAllShows() {
        ArrayList<Show> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null, null, null, null, null, _ID + " ASC", null);
        cursor.moveToFirst();
        Show show;
        if (cursor.getCount() > 0) {
            do {
                show = new Show();
                show.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                show.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                show.setBackdropPath(cursor.getString(cursor.getColumnIndexOrThrow(PATH)));
                arrayList.add(show);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE, null
                , _ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE
                , null
                , null
                , null
                , null
                , null
                , _ID + " ASC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }


    public int deleteProvider(String title) {
        return database.delete(DATABASE_TABLE, TITLE + " = ?", new String[]{title});
    }
}
