package com.dicoding.picodiploma.mybottomnavigation.activity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dicoding.picodiploma.mybottomnavigation.R;
import com.dicoding.picodiploma.mybottomnavigation.database.ShowHelper;
import com.dicoding.picodiploma.mybottomnavigation.model.Show;
import com.dicoding.picodiploma.mybottomnavigation.widget.ImageBannerWidget;

import java.util.Objects;

import static com.dicoding.picodiploma.mybottomnavigation.database.DatabaseContract.NoteColumns.CONTENT_URI;
import static com.dicoding.picodiploma.mybottomnavigation.database.DatabaseContract.NoteColumns.ID_SHOW;
import static com.dicoding.picodiploma.mybottomnavigation.database.DatabaseContract.NoteColumns.PATH;
import static com.dicoding.picodiploma.mybottomnavigation.database.DatabaseContract.NoteColumns.TITLE;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_SHOW = "extra_show";
    public static final String EXTRA_POSITION = "extra_position";
    private Show show;
    private ShowHelper showHelper;
    private ProgressBar progressBar;
    private SharedPreferences sharedPref;
    private int favorite = 0;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView tvTitleDetail, tvOverviewDetail, tvVoteAverageDetail;
        ImageView ivBackdrop, ivPosterDetail;

        tvTitleDetail = findViewById(R.id.tv_title_detail);
        tvOverviewDetail = findViewById(R.id.tv_overview_detail);
        tvVoteAverageDetail = findViewById(R.id.tv_vote_average_detail);
        ivBackdrop = findViewById(R.id.iv_backdrop);
        ivPosterDetail = findViewById(R.id.iv_poster_detail);
        progressBar = findViewById(R.id.progress_bar_detail);
        showHelper = ShowHelper.getInstance(getApplicationContext());

        showLoading(true);
        show = getIntent().getParcelableExtra(EXTRA_SHOW);

        uri = getIntent().getData();
        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) show = new Show(cursor);
                cursor.close();
            }
        }

        sharedPref = getApplicationContext().getSharedPreferences("Movies", Context.MODE_PRIVATE);
        if ((sharedPref.getInt(show.getTitle(), 0) == 1)) {
            favorite = 1;
        } else {
            favorite = 0;
        }

        String year = show.getReleaseDate().substring(0, 4);
        String titleName = show.getTitle() + " (" + year + ")";
        tvTitleDetail.setText(titleName);
        tvOverviewDetail.setText(show.getOverview());

        String voteAverage = (int) (show.getVoteAverage() * 10) + "%";
        tvVoteAverageDetail.setText(voteAverage);
        Glide.with(this)
                .load(show.getBackdropPath())
                .apply(new RequestOptions().override(350, 350))
                .into(ivPosterDetail);

        Glide.with(this)
                .load(show.getPosterPath())
                .apply(new RequestOptions().override(350, 350))
                .into(ivBackdrop);

        showLoading(false);
    }

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.bringToFront();
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorite, menu);
        MenuItem actionFavorite = menu.findItem(R.id.action_favorite);
        if (favorite == 1) {
            actionFavorite.setIcon(R.drawable.ic_favorite);
        } else {
            actionFavorite.setIcon(R.drawable.ic_favorite_border);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_favorite) {
            if (favorite != 1) {
                item.setIcon(R.drawable.ic_favorite);
                favorite = 1;

                ContentValues values = new ContentValues();
                values.put(ID_SHOW, show.getIdShow());
                values.put(TITLE, show.getTitle());
                values.put(PATH, show.getBackdropPath());
                getContentResolver().insert(CONTENT_URI, values);

                sharedPref = getApplicationContext().getSharedPreferences("Movies", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(show.getTitle(), favorite);
                editor.apply();

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
                ComponentName thisWidget = new ComponentName(getApplicationContext(), ImageBannerWidget.class);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(EXTRA_POSITION, 1);
                startActivity(intent);
                finish();
            } else {
                item.setIcon(R.drawable.ic_favorite_border);
                favorite = 0;

                Log.d("HELLNO", String.valueOf(uri));
                getContentResolver().delete(Objects.requireNonNull(uri), null, null);

                sharedPref = getApplicationContext().getSharedPreferences("Movies", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(show.getTitle(), favorite);
                editor.apply();

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
                ComponentName thisWidget = new ComponentName(getApplicationContext(), ImageBannerWidget.class);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.stack_view);

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(EXTRA_POSITION, 1);
                startActivity(intent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
