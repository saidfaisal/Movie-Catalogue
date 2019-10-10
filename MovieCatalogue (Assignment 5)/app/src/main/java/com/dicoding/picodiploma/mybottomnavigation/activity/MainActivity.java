package com.dicoding.picodiploma.mybottomnavigation.activity;


import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.dicoding.picodiploma.mybottomnavigation.R;
import com.dicoding.picodiploma.mybottomnavigation.database.ShowHelper;
import com.dicoding.picodiploma.mybottomnavigation.fragment.FavoritesFragment;
import com.dicoding.picodiploma.mybottomnavigation.fragment.MoviesFragment;
import com.dicoding.picodiploma.mybottomnavigation.fragment.TvShowsFragment;

import static com.dicoding.picodiploma.mybottomnavigation.activity.DetailActivity.EXTRA_POSITION;

public class MainActivity extends AppCompatActivity {

    public static String localeLanguage = "en_US";
    private ShowHelper showHelper;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;

            switch (item.getItemId()) {
                case R.id.navigation_movies:
                    fragment = new MoviesFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();

                    return true;
                case R.id.navigation_tv_shows:
                    fragment = new TvShowsFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();

                    return true;

                case R.id.navigation_favorites:
                    fragment = new FavoritesFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, fragment, fragment.getClass().getSimpleName())
                            .commit();

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        localeLanguage = getResources().getConfiguration().locale.toString();

        if (savedInstanceState == null) {
            navView.setSelectedItemId(R.id.navigation_movies);
        }
        showHelper = ShowHelper.getInstance(getApplicationContext());
        showHelper.open();

        int position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        if (position > 0) {
            navView.setSelectedItemId(R.id.navigation_favorites);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
        } else if (item.getItemId() == R.id.action_remainder_settings) {
            Intent intent = new Intent(this, ReminderActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showHelper.close();
    }


}
