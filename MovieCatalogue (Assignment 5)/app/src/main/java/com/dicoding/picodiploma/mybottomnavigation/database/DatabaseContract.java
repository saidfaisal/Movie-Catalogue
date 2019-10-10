package com.dicoding.picodiploma.mybottomnavigation.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    public static final String AUTHORITY = "com.dicoding.picodiploma.mybottomnavigation";
    public static final String SCHEME = "content";

    private DatabaseContract() {
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

    public static final class NoteColumns implements BaseColumns {
        public static final String TABLE_NAME = "show";
        public static final String ID_SHOW = "id";
        public static final String TITLE = "title";
        public static final String PATH = "path";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }

}
