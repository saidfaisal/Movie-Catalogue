package com.dicoding.picodiploma.mybottomnavigation.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import com.dicoding.picodiploma.mybottomnavigation.model.CustomViewModel;
import com.dicoding.picodiploma.mybottomnavigation.model.Show;

import java.util.ArrayList;

public class TvShowsFragment extends Fragment {

    private CustomAdapter adapter;
    private ProgressBar progressBar;
    private CustomViewModel customViewModel;
    private Observer<ArrayList<Show>> getTvShows = new Observer<ArrayList<Show>>() {
        @Override
        public void onChanged(ArrayList<Show> listShow) {
            if (listShow != null) {
                adapter.setListShows(listShow);
                showLoading(false);
            }
        }
    };

    public TvShowsFragment() {
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
        customViewModel.getDataTvShows().observe(this, getTvShows);

        adapter = new CustomAdapter();
        adapter.notifyDataSetChanged();

        RecyclerView recyclerView = view.findViewById(R.id.rv_tvshows);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        progressBar = view.findViewById(R.id.progress_bar_tvshows);
        customViewModel.setTvShows("");
        showLoading(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Title");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                customViewModel.setTvShows(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                customViewModel.setTvShows(query);
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

}
