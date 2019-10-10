package com.example.movieapp.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.movieapp.R;
import com.example.movieapp.model.Show;

import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_SHOW = "extra_show";
    public static final String EXTRA_POSITION = "extra_position";
    private ProgressBar progressBar;
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

        showLoading(true);
        Show show = getIntent().getParcelableExtra(EXTRA_SHOW);

        uri = getIntent().getData();
        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) show = new Show(cursor);
                cursor.close();
            }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favorite, menu);
        MenuItem actionFavorite = menu.findItem(R.id.action_favorite);
        actionFavorite.setIcon(R.drawable.ic_favorite);
        return super.onCreateOptionsMenu(menu);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_favorite) {
            item.setIcon(R.drawable.ic_favorite_border);
            getContentResolver().delete(Objects.requireNonNull(uri), null, null);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
