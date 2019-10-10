package com.example.movieapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.movieapp.R;
import com.example.movieapp.adapter.SectionsPagerAdapter;
import com.example.movieapp.helper.ShowHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static String localeLanguage = "en_US";
    private ShowHelper showHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        localeLanguage = getResources().getConfiguration().locale.toString();

        showHelper = ShowHelper.getInstance(getApplicationContext());
        showHelper.open();

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);

        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showHelper.close();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_add) {
            onRestart();
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
