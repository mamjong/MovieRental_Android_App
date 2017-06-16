package com.example.mark.prog4tent.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mark.prog4tent.R;
import com.example.mark.prog4tent.domain.Movie;

import java.util.ArrayList;

/**
 * Created by mark on 16-6-2017.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {

        Movie movie = getItem(position);

        if (convertview == null) {
            convertview = LayoutInflater.from(getContext()).inflate(R.layout.movie_lv_item, parent, false);
        }

        TextView movieTitle = (TextView) convertview.findViewById(R.id.movieItem_tv_title);
        TextView movieDescription = (TextView) convertview.findViewById(R.id.movieItem_tv_desc);
        TextView movieRelease = (TextView) convertview.findViewById(R.id.movieItem_tv_release);

        movieTitle.setText(movie.getTitle());
        movieDescription.setText(movie.getDescription());
        movieRelease.setText(movie.getReleaseDate());

        return convertview;
    }
}
