package com.example.mark.prog4tent.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mark.prog4tent.MainActivity;
import com.example.mark.prog4tent.R;
import com.example.mark.prog4tent.adapter.MovieAdapter;
import com.example.mark.prog4tent.domain.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mark on 15-6-2017.
 */

public class MoviesFragment extends Fragment {

    public static final String PREFS_NAME_TOKEN = "Prefsfile";
    private SharedPreferences preferences;
    private ListView movieList;
    private ArrayList<Movie> movieArrayList;
    private ArrayAdapter movieAdapter;
    private String ip, token;
    private Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Movies");

        movieList = (ListView) getActivity().findViewById(R.id.movies_listview);
        movieArrayList = new ArrayList<>();
        movieAdapter = new MovieAdapter(getActivity(), movieArrayList);
        movieList.setAdapter(movieAdapter);

        preferences = getActivity().getSharedPreferences(PREFS_NAME_TOKEN, Context.MODE_PRIVATE);

        if (preferences.getInt("USEIP", 0) == 0) {
            ip = preferences.getString("IPLOCAL", "no ip");
        }else if(preferences.getInt("USEIP", 0) == 1) {
            ip = preferences.getString("IPHEROKU", "no ip");
        }

        token = preferences.getString("TOKEN", "No token");

        movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = movieArrayList.get(position);

                bundle = new Bundle();
                bundle.putParcelable("MOVIE", movie);

                CopiesFragment copiesFragment= new CopiesFragment();
                copiesFragment.setArguments(bundle);
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, copiesFragment, "TAG")
                        .addToBackStack(null)
                        .commit();

            }
        });

        volleyMoviesRequest();
    }

    public void volleyMoviesRequest() {

        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                "Loading rentals. Please wait...", true);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = "http://" + ip + "/api/v1/films?offset=0&count=30";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                Movie movie = new Movie();
                                movie.setId(object.getInt("film_id"));
                                movie.setTitle(object.getString("title"));
                                movie.setDescription(object.getString("description"));
                                movie.setReleaseDate(object.getString("release_year"));
                                movieArrayList.add(movie);
                                movieAdapter.notifyDataSetChanged();
                            }
                            dialog.cancel();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.cancel();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "Something went wrong");
                        dialog.cancel();
                    }
                }) {

            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("X-Access-Token", token);
                return headers;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }
}
