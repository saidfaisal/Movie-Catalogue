package com.dicoding.picodiploma.mybottomnavigation.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.dicoding.picodiploma.mybottomnavigation.R;
import com.dicoding.picodiploma.mybottomnavigation.database.ShowHelper;
import com.dicoding.picodiploma.mybottomnavigation.model.Show;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final List<Bitmap> mWidgetItems = new ArrayList<>();
    private Context mContext;
    private ArrayList<Show> listShowsFavorites;
    private ShowHelper showHelper;


    StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        showHelper = ShowHelper.getInstance(context);
        listShowsFavorites = showHelper.getAllShows();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        listShowsFavorites = showHelper.getAllShows();
        mWidgetItems.clear();
        for (Show show : listShowsFavorites) {
            mWidgetItems.add(getBitmapFromURL(show.getBackdropPath()));
        }
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems.get(position));

        Bundle extras = new Bundle();
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    private Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
