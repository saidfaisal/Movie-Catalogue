package com.example.movieapp.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.movieapp.database.DatabaseContract;

import org.json.JSONObject;

import static com.example.movieapp.database.DatabaseContract.getColumnInt;
import static com.example.movieapp.database.DatabaseContract.getColumnString;

public class Show implements Parcelable {
    public static final Creator<Show> CREATOR = new Creator<Show>() {
        @Override
        public Show createFromParcel(Parcel in) {
            return new Show(in);
        }

        @Override
        public Show[] newArray(int size) {
            return new Show[size];
        }
    };
    private int id;
    private int idShow;
    private String title;
    private String overview;
    private String releaseDate;
    private String posterPath = "https://image.tmdb.org/t/p/w500/";
    private String backdropPath = "https://image.tmdb.org/t/p/w500/";
    private Double voteAverage;

    public Show(int id, int idShow, String title, String path) {
        this.id = id;
        this.idShow = idShow;
        this.title = title;
        this.backdropPath = path;
    }

    public Show(Cursor cursor) {
        this.id = getColumnInt(cursor, DatabaseContract.NoteColumns._ID);
        this.idShow = getColumnInt(cursor, DatabaseContract.NoteColumns.ID_SHOW);
        this.title = getColumnString(cursor, DatabaseContract.NoteColumns.TITLE);
        this.backdropPath = getColumnString(cursor, DatabaseContract.NoteColumns.PATH);
    }


    Show(JSONObject object) {

        try {
            int idShow = object.getInt("id");
            String title = object.getString("title");
            String overview = object.getString("overview");
            String posterPath = object.getString("poster_path");
            String backdropPath = object.getString("backdrop_path");
            String releaseDate = object.getString("release_date");
            Double voteAverage = object.getDouble("vote_average");

            this.idShow = idShow;
            this.title = title;
            this.overview = overview;
            this.posterPath += posterPath;
            this.backdropPath += backdropPath;
            this.releaseDate = releaseDate;
            this.voteAverage = voteAverage;
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            int idShow = object.getInt("id");
            String name = object.getString("original_name");
            String overview = object.getString("overview");
            String posterPath = object.getString("poster_path");
            String backdropPath = object.getString("backdrop_path");
            String firstAirDate = object.getString("first_air_date");
            Double voteAverage = object.getDouble("vote_average");

            this.idShow = idShow;
            this.title = name;
            this.overview = overview;
            this.posterPath += posterPath;
            this.backdropPath += backdropPath;
            this.releaseDate = firstAirDate;
            this.voteAverage = voteAverage;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Show(Parcel in) {
        id = in.readInt();
        idShow = in.readInt();
        title = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        if (in.readByte() == 0) {
            voteAverage = null;
        } else {
            voteAverage = in.readDouble();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(idShow);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        if (voteAverage == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(voteAverage);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdShow() {
        return idShow;
    }

    public void setIdShow(int idShow) {
        this.idShow = idShow;
    }
}

