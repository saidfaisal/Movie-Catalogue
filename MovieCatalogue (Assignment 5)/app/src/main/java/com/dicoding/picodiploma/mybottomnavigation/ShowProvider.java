package com.dicoding.picodiploma.mybottomnavigation;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dicoding.picodiploma.mybottomnavigation.database.ShowHelper;

import static com.dicoding.picodiploma.mybottomnavigation.database.DatabaseContract.AUTHORITY;
import static com.dicoding.picodiploma.mybottomnavigation.database.DatabaseContract.NoteColumns.CONTENT_URI;
import static com.dicoding.picodiploma.mybottomnavigation.database.DatabaseContract.NoteColumns.TABLE_NAME;

public class ShowProvider extends ContentProvider {
    private static final int SHOW = 1;
    private static final int SHOW_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, SHOW);
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME + "/#", SHOW_ID);
    }

    private ShowHelper showHelper;

    @Override
    public boolean onCreate() {
        showHelper = ShowHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] strings, String s, String[] strings1, String s1) {
        showHelper.open();
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case SHOW:
                cursor = showHelper.queryProvider();
                break;
            case SHOW_ID:
                cursor = showHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        showHelper.open();
        long added;
        if (sUriMatcher.match(uri) == SHOW) {
            added = showHelper.insertProvider(contentValues);
        } else {
            added = 0;
        }
        return Uri.parse(CONTENT_URI + "/" + added);
    }


    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        showHelper.open();
        return showHelper.deleteProvider(uri.getLastPathSegment());
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
