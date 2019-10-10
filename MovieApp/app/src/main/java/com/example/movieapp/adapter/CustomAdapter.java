package com.example.movieapp.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.movieapp.R;
import com.example.movieapp.activity.DetailActivity;
import com.example.movieapp.model.Show;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.movieapp.database.DatabaseContract.NoteColumns.CONTENT_URI;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private final ArrayList<Show> listShows = new ArrayList<>();

    public void setListShowsFavorites(ArrayList<Show> listShows, ArrayList<Show> listShowsFavorites) {
        this.listShows.clear();
        for (int i = 0; i < listShows.size(); i++) {
            for (Show show : listShowsFavorites) {
                try {
                    if (listShows.get(i).getIdShow() == show.getIdShow()) {
                        this.listShows.add(listShows.get(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        notifyDataSetChanged();
    }

    private ArrayList<Show> getListShows() {
        return listShows;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row, viewGroup, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder customViewHolder, int i) {
        customViewHolder.bind(listShows.get(i));
    }

    @Override
    public int getItemCount() {
        return listShows.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvOverview, tvVoteAverage, tvReleaseDate;
        ImageView ivPoster;

        CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvOverview = itemView.findViewById(R.id.tv_overview);
            tvVoteAverage = itemView.findViewById(R.id.tv_vote_average);
            tvReleaseDate = itemView.findViewById(R.id.tv_release_date);
            ivPoster = itemView.findViewById(R.id.iv_poster);
        }

        void bind(Show show) {
            tvTitle.setText(show.getTitle());
            if (show.getVoteAverage() != null) {
                String voteAverage = (int) (show.getVoteAverage() * 10) + "%";
                tvVoteAverage.setText(voteAverage);
            }
            tvOverview.setText(show.getOverview());

            //changing format of date
            if (!show.getReleaseDate().equals("")) {
                Date date = null;
                SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                try {
                    date = formatter1.parse(show.getReleaseDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat formatter2 = new SimpleDateFormat("dd MMMM, YYYY", Locale.US);
                String strDate = formatter2.format(date);

                tvReleaseDate.setText(String.valueOf(strDate));
            }

            Glide.with(itemView.getContext())
                    .load(show.getPosterPath())
                    .apply(new RequestOptions().override(350, 350))
                    .into(ivPoster);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
                    Uri uri = Uri.parse(CONTENT_URI + "/" + getListShows().get(getAdapterPosition()).getTitle());
                    intent.setData(uri);
                    intent.putExtra(DetailActivity.EXTRA_SHOW, listShows.get(getAdapterPosition()));
                    intent.putExtra(DetailActivity.EXTRA_POSITION, getAdapterPosition());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
