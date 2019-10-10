package com.example.movieapp.fragment;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.movieapp.R;
import com.example.movieapp.adapter.CustomAdapter;
import com.example.movieapp.model.CustomViewModel;
import com.example.movieapp.model.LoadShowsCallback;
import com.example.movieapp.model.Show;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import static com.example.movieapp.database.DatabaseContract.NoteColumns.CONTENT_URI;
import static com.example.movieapp.helper.MappingHelper.mapCursorToArrayList;

public class TvShowsFragment extends Fragment implements LoadShowsCallback {

    private static final String EXTRA_STATE1 = "EXTRA_STATE1";
    private static final String EXTRA_STATE2 = "EXTRA_STATE2";
    public static CustomViewModel customViewModel;
    public ProgressBar progressBar;
    private ArrayList<Show> listShowsObserver;
    private ArrayList<Show> listShowsFavorites;
    private CustomAdapter adapter;
    private Observer<ArrayList<Show>> getDatabase = new Observer<ArrayList<Show>>() {
        @Override
        public void onChanged(ArrayList<Show> listShow) {
            progressBar.setVisibility(View.INVISIBLE);
            listShowsObserver = listShow;
            if (listShowsObserver != null) {
                adapter.setListShowsFavorites(listShowsObserver, listShowsFavorites);
            }
        }
    };

    public TvShowsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tv_shows, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE1, listShowsObserver);
        outState.putParcelableArrayList(EXTRA_STATE2, listShowsFavorites);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvTvShows = Objects.requireNonNull(getActivity()).findViewById(R.id.rv_tvshows);
        rvTvShows.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTvShows.setHasFixedSize(true);

        customViewModel = ViewModelProviders.of(this).get(CustomViewModel.class);
        customViewModel.getDatabase().observe(this, getDatabase);

        adapter = new CustomAdapter();
        rvTvShows.setAdapter(adapter);

        progressBar = view.findViewById(R.id.progress_bar_tvshows);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, getContext());
        Objects.requireNonNull(getContext()).getContentResolver().registerContentObserver(CONTENT_URI, true, myObserver);

        if (savedInstanceState == null) {
            new LoadShowsAsync(getContext(), this).execute();
        } else {
            listShowsObserver = savedInstanceState.getParcelableArrayList(EXTRA_STATE1);
            listShowsFavorites = savedInstanceState.getParcelableArrayList(EXTRA_STATE2);
            if (listShowsObserver != null && listShowsFavorites != null) {
                adapter.setListShowsFavorites(listShowsObserver, listShowsFavorites);
            }
        }
    }

    @Override
    public void postExecute(ArrayList<Show> showsFavorites) {
        listShowsFavorites = showsFavorites;
        customViewModel.setDatabaseTvShows(showsFavorites, "");
        progressBar.setVisibility(View.VISIBLE);
    }

    private static class LoadShowsAsync extends AsyncTask<Void, Void, Cursor> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadShowsCallback> weakCallback;

        private LoadShowsAsync(Context context, LoadShowsCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return context.getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor shows) {
            super.onPostExecute(shows);
            weakCallback.get().postExecute(mapCursorToArrayList(shows));
        }
    }

    static class DataObserver extends ContentObserver {

        final Context context;

        DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadShowsAsync(context, (LoadShowsCallback) context).execute();
        }
    }

}
