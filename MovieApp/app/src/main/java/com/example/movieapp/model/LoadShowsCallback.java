package com.example.movieapp.model;

import java.util.ArrayList;

public interface LoadShowsCallback {
    void postExecute(ArrayList<Show> shows);
}
