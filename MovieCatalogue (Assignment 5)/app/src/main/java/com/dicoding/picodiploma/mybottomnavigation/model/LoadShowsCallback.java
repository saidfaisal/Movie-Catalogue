package com.dicoding.picodiploma.mybottomnavigation.model;

import java.util.ArrayList;

public interface LoadShowsCallback {
    void preExecute();

    void postExecute(ArrayList<Show> shows);
}
