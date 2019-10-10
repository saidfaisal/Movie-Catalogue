package com.example.movieapp.helper;

import android.database.Cursor;

import com.example.movieapp.model.Show;

import java.util.ArrayList;

import static com.example.movieapp.database.DatabaseContract.NoteColumns.ID_SHOW;
import static com.example.movieapp.database.DatabaseContract.NoteColumns.PATH;
import static com.example.movieapp.database.DatabaseContract.NoteColumns.TITLE;
import static com.example.movieapp.database.DatabaseContract.NoteColumns._ID;

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