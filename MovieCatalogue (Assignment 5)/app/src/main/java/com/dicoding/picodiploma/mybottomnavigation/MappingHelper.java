package com.dicoding.picodiploma.mybottomnavigation;

import android.database.Cursor;

import com.dicoding.picodiploma.mybottomnavigation.model.Show;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.dicoding.picodiploma.mybottomnavigation.database.DatabaseContract.NoteColumns.ID_SHOW;
import static com.dicoding.picodiploma.mybottomnavigation.database.DatabaseContract.NoteColumns.PATH;
import static com.dicoding.picodiploma.mybottomnavigation.database.DatabaseContract.NoteColumns.TITLE;

public class MappingHelper {

    public static ArrayList<Show> mapCursorToArrayList(Cursor cursor) {
        ArrayList<Show> listShows = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(_ID));
            int idShow = cursor.getInt(cursor.getColumnIndexOrThrow(ID_SHOW));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
            String path = cursor.getString(cursor.getColumnIndexOrThrow(PATH));
            listShows.add(new Show(id, idShow, title, path));
        }
        return listShows;
    }
}
