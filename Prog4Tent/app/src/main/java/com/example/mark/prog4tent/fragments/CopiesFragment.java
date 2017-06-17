package com.example.mark.prog4tent.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.mark.prog4tent.R;
import com.example.mark.prog4tent.adapter.CopyAdapter;
import com.example.mark.prog4tent.domain.Copy;
import com.example.mark.prog4tent.domain.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mark on 17-6-2017.
 */

public class CopiesFragment extends Fragment {

    public static final String PREFS_NAME_TOKEN = "Prefsfile";
    private SharedPreferences preferences;
    private String ip;
    private int id;

    private Movie movie;
    private Bundle bundle;

    private ListView copyList;
    private ArrayAdapter copyAdapter;
    private ArrayList<Copy> copyArrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_copies, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferences = getActivity().getSharedPreferences(PREFS_NAME_TOKEN, Context.MODE_PRIVATE);
        ip = preferences.getString("IP", "NO IP");
        id = preferences.getInt("ID", 0);


        movie = new Movie();
        bundle = getArguments();
        movie = bundle.getParcelable("MOVIE");
        getActivity().setTitle("Copies for: " + movie.getTitle());

        copyList = (ListView) getActivity().findViewById(R.id.copies_listview);
        copyArrayList = new ArrayList<>();
        copyAdapter = new CopyAdapter(getActivity(), copyArrayList);
        copyList.setAdapter(copyAdapter);

        copyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Copy copy = copyArrayList.get(position);
                volleyRentCopy(copy);
            }
        });

        volleyCopiesRequest();
    }

    public void volleyCopiesRequest() {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = "http://" + ip + "/api/v1/filmid/" + movie.getId();
        Log.e("URL_CHECK", url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                Copy copy = new Copy();
                                copy.setInventoryId(object.getInt("inventory_id"));
                                copy.setRentalDate(object.getString("rental_date"));
                                copy.setRentalId(object.getInt("rental_id"));
                                copy.setStaffId(object.getInt("staff_id"));
                                copy.setStoreId(object.getInt("store_id"));
                                copy.setRented(object.isNull("return_date"));

                                copyArrayList.add(copy);
                                copyAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "Something went wrong");
                    }
                }) {

            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }

    public void volleyRentCopy(Copy copy) {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = "http://" + ip + "/rentals/:customerId/" + copy.getInventoryId();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                Copy copy = new Copy();
                                copy.setInventoryId(object.getInt("inventory_id"));
                                copy.setRentalDate(object.getString("rental_date"));
                                copy.setRentalId(object.getInt("rental_id"));
                                copy.setStaffId(object.getInt("staff_id"));
                                copy.setStoreId(object.getInt("store_id"));
                                copy.setRented(object.isNull("return_date"));

                                copyArrayList.add(copy);
                                copyAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "Something went wrong");
                    }
                }) {

            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }
}
