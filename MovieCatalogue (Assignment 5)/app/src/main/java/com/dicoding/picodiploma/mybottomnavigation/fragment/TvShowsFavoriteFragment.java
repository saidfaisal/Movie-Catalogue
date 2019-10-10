package com.dicoding.picodiploma.mybottomnavigation.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.dicoding.picodiploma.mybottomnavigation.R;
import com.dicoding.picodiploma.mybottomnavigation.adapter.CustomAdapter;
import com.dicoding.picodiploma.mybottomnavigation.database.ShowHelper;
import com.dicoding.picodiploma.mybottomnavigation.model.CustomViewModel;
import com.dicoding.picodiploma.mybottomnavigation.model.LoadShowsCallback;
import com.dicoding.picodiploma.mybottomnavigation.model.Show;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import static com.dicoding.picodiploma.mybottomnavigation.MappingHelper.mapCursorToArrayList;
import static com.dicoding.picodiploma.mybottomnavigation.database.DatabaseContract.NoteColumns.CONTENT_URI;

public class TvShowsFavoriteFragment extends Fragment implements LoadShowsCallback {

    private static final String EXTRA_STATE1 = "EXTRA_STATE1";
    private static final String EXTRA_STATE2 = "EXTRA_STATE2";
    public static CustomViewModel customViewModel;
    public ProgressBar progressBar;
    private ShowHelper showHelper;
    private ArrayList<Show> listShowsFavorites;
    private ArrayList<Show> listShowsObserver;
    private CustomAdapter adapter;
    private Observer<ArrayList<Show>> getDatabase = new Observer<ArrayList<Show>>() {
        @Override
        public void onChanged(ArrayList<Show> listShow) {
            showLoading(false);
            listShowsObserver = listShow;
            if (listShowsObserver != null) {
                adapter.setListShowsFavorites(listShowsObserver, listShowsFavorites);
            }
        }
    };

    public TvShowsFavoriteFragment() {
        showHelper = ShowHelper.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tvshows, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        customViewModel = ViewModelProviders.of(this).get(CustomViewModel.class);
        customViewModel.getDatabase().observe(this, getDatabase);

        RecyclerView rvTvShows = Objects.requireNonNull(getActivity()).findViewById(R.id.rv_tvshows);
        rvTvShows.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTvShows.setHasFixedSize(true);

        adapter = new CustomAdapter();
        rvTvShows.setAdapter(adapter);

        progressBar = view.findViewById(R.id.progress_bar_tvshows);

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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE1, listShowsObserver);
        outState.putParcelableArrayList(EXTRA_STATE2, listShowsFavorites);
    }

    @Override
    public void preExecute() {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showLoading(true);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<Show> showsFavorites) {
        ArrayList<Show> shows = showHelper.getAllShows();
        customViewModel.setDatabaseTvShows(shows, "");
        listShowsFavorites = showsFavorites;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Title");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                customViewModel.setDatabaseTvShows(listShowsFavorites, query);
                adapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                customViewModel.setDatabaseTvShows(listShowsFavorites, query);
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private static class LoadShowsAsync extends AsyncTask<Void, Void, Cursor> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadShowsCallback> weakCallback;

        private LoadShowsAsync(Context context, LoadShowsCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
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


}
