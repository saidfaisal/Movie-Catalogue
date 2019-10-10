package com.dicoding.picodiploma.mybottomnavigation.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dicoding.picodiploma.mybottomnavigation.R;

public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static MoviesFavoriteFragment newInstance1(int index) {
        MoviesFavoriteFragment fragment = new MoviesFavoriteFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static TvShowsFavoriteFragment newInstance2(int index) {
        TvShowsFavoriteFragment fragment = new TvShowsFavoriteFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pager, container, false);
    }
}
