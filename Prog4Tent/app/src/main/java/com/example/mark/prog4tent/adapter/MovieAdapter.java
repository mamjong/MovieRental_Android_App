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

    private TextView movieTitle, movieDescription, movieRelease;

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_lv_item, parent, false);
        }

        movieTitle = (TextView) convertView.findViewById(R.id.movieItem_tv_title);
        movieDescription = (TextView) convertView.findViewById(R.id.movieItem_tv_desc);
        movieRelease = (TextView) convertView.findViewById(R.id.movieItem_tv_release);

        movieTitle.setText(movie.getTitle());
        movieDescription.setText(movie.getDescription());
        movieRelease.setText(movie.getReleaseDate());

        return convertView;
    }
}
