package com.dicoding.picodiploma.mybottomnavigation.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.dicoding.picodiploma.mybottomnavigation.BuildConfig;
import com.dicoding.picodiploma.mybottomnavigation.activity.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class CustomViewModel extends ViewModel {
    private static final String API_KEY = BuildConfig.API_KEY;
    private MutableLiveData<ArrayList<Show>> mutableLiveDataMovies = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Show>> mutableLiveDataTvShows = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Show>> mutableLiveDatabase = new MutableLiveData<>();
    private AsyncHttpClient client = new AsyncHttpClient();

    public void setMovies(String title) {
        final ArrayList<Show> listShows = new ArrayList<>();
        String url;
        if (title.equals("")) {
            if (MainActivity.localeLanguage.equals("en_US")) {
                url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&language=en-US";
            } else {
                url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&language=id-ID";
            }
        } else {
            if (MainActivity.localeLanguage.equals("en_US")) {
                url = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=en-US&query=" + title;
            } else {
                url = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=id-ID&query=" + title;
            }
        }

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody);
                    JSONObject responseObject = new JSONObject(response);
                    JSONArray listResults = responseObject.getJSONArray("results");

                    for (int i = 0; i < listResults.length(); i++) {
                        JSONObject object = listResults.getJSONObject(i);
                        Show show = new Show(object);
                        listShows.add(show);
                    }
                    mutableLiveDataMovies.postValue(listShows);
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }

    public void setTvShows(String title) {
        final ArrayList<Show> listShows = new ArrayList<>();
        String url;
        if (title.equals("")) {
            if (MainActivity.localeLanguage.equals("en_US")) {
                url = "https://api.themoviedb.org/3/discover/tv?api_key=" + API_KEY + "&language=en-US";
            } else {
                url = "https://api.themoviedb.org/3/discover/tv?api_key=" + API_KEY + "&language=id-ID";
            }
        } else {
            if (MainActivity.localeLanguage.equals("en_US")) {
                url = "https://api.themoviedb.org/3/search/tv?api_key=" + API_KEY + "&language=en-US&query=" + title;
            } else {
                url = "https://api.themoviedb.org/3/search/tv?api_key=" + API_KEY + "&language=id-ID&query=" + title;
            }
        }

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody);
                    JSONObject responseObject = new JSONObject(response);
                    JSONArray listResults = responseObject.getJSONArray("results");

                    for (int i = 0; i < listResults.length(); i++) {
                        JSONObject object = listResults.getJSONObject(i);
                        Show show = new Show(object);
                        listShows.add(show);
                    }
                    mutableLiveDataTvShows.postValue(listShows);
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }

    public void setDatabase(final ArrayList<Show> shows, String title) {
        final ArrayList<Show> listShows = new ArrayList<>();
        String url;
        if (title.equals("")) {
            for (int i = 0; i < shows.size(); i++) {
                if (MainActivity.localeLanguage.equals("en_US")) {
                    url = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=en-US&query=" + shows.get(i).getTitle();
                } else {
                    url = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=id-ID&query=" + shows.get(i).getTitle();
                }
                client.get(url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            String response = new String(responseBody);
                            JSONObject responseObject = new JSONObject(response);
                            JSONArray listResults = responseObject.getJSONArray("results");

                            for (int i = 0; i < listResults.length(); i++) {
                                JSONObject object = listResults.getJSONObject(i);
                                Show show = new Show(object);
                                listShows.add(show);
                            }
                            mutableLiveDatabase.postValue(listShows);
                        } catch (Exception e) {
                            Log.d("Exception", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d("onFailure", error.getMessage());
                    }
                });
            }
        } else {
            if (MainActivity.localeLanguage.equals("en_US")) {
                url = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=en-US&query=" + title;
            } else {
                url = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=id-ID&query=" + title;
            }
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String response = new String(responseBody);
                        JSONObject responseObject = new JSONObject(response);
                        JSONArray listResults = responseObject.getJSONArray("results");

                        for (int i = 0; i < listResults.length(); i++) {
                            JSONObject object = listResults.getJSONObject(i);
                            Show show = new Show(object);
                            listShows.add(show);
                        }
                        mutableLiveDatabase.postValue(listShows);
                    } catch (Exception e) {
                        Log.d("Exception", e.getMessage());
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("onFailure", error.getMessage());
                }
            });

        }

    }

    public void setDatabaseTvShows(final ArrayList<Show> shows, String title) {
        final ArrayList<Show> listShows = new ArrayList<>();
        String url;
        if (title.equals("")) {
            for (int i = 0; i < shows.size(); i++) {
                if (MainActivity.localeLanguage.equals("en_US")) {
                    url = "https://api.themoviedb.org/3/search/tv?api_key=" + API_KEY + "&language=en-US&query=" + shows.get(i).getTitle();
                } else {
                    url = "https://api.themoviedb.org/3/search/tv?api_key=" + API_KEY + "&language=id-ID&query=" + shows.get(i).getTitle();
                }
                client.get(url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            String response = new String(responseBody);
                            JSONObject responseObject = new JSONObject(response);
                            JSONArray listResults = responseObject.getJSONArray("results");

                            for (int i = 0; i < listResults.length(); i++) {
                                JSONObject object = listResults.getJSONObject(i);
                                Show show = new Show(object);
                                listShows.add(show);
                            }
                            mutableLiveDatabase.postValue(listShows);
                        } catch (Exception e) {
                            Log.d("Exception", e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d("onFailure", error.getMessage());
                    }
                });
            }
        } else {
            if (MainActivity.localeLanguage.equals("en_US")) {
                url = "https://api.themoviedb.org/3/search/tv?api_key=" + API_KEY + "&language=en-US&query=" + title;
            } else {
                url = "https://api.themoviedb.org/3/search/tv?api_key=" + API_KEY + "&language=id-ID&query=" + title;
            }
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String response = new String(responseBody);
                        JSONObject responseObject = new JSONObject(response);
                        JSONArray listResults = responseObject.getJSONArray("results");

                        for (int i = 0; i < listResults.length(); i++) {
                            JSONObject object = listResults.getJSONObject(i);
                            Show show = new Show(object);
                            listShows.add(show);
                        }
                        mutableLiveDatabase.postValue(listShows);
                    } catch (Exception e) {
                        Log.d("Exception", e.getMessage());
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("onFailure", error.getMessage());
                }
            });

        }
    }

    public LiveData<ArrayList<Show>> getDataMovies() {
        return mutableLiveDataMovies;
    }

    public LiveData<ArrayList<Show>> getDataTvShows() {
        return mutableLiveDataTvShows;
    }

    public LiveData<ArrayList<Show>> getDatabase() {
        return mutableLiveDatabase;
    }

}
