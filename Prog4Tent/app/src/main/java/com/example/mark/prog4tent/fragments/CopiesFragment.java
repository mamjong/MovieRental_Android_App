package com.example.mark.prog4tent.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mark.prog4tent.R;
import com.example.mark.prog4tent.adapter.CopyAdapter;
import com.example.mark.prog4tent.domain.Copy;
import com.example.mark.prog4tent.domain.Movie;
import com.example.mark.prog4tent.domain.Rental;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mark on 17-6-2017.
 */

public class CopiesFragment extends Fragment {

    public static final String PREFS_NAME_TOKEN = "Prefsfile";
    private SharedPreferences preferences;
    private String ip, id, token, affectedRows;

    private Movie movie;
    private Bundle bundle;

    private ListView copyList;
    private ArrayAdapter copyAdapter;
    private ArrayList<Copy> copyArrayList;

    private Toast toast;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_copies, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        preferences = getActivity().getSharedPreferences(PREFS_NAME_TOKEN, Context.MODE_PRIVATE);

        if (preferences.getInt("USEIP", 0) == 0) {
            ip = preferences.getString("IPLOCAL", "no ip");
        }else if(preferences.getInt("USEIP", 0) == 1) {
            ip = preferences.getString("IPHEROKU", "no ip");
        }

        id = preferences.getString("ID", "NO ID");
        token = preferences.getString("TOKEN", "No token");


        movie = new Movie();
        bundle = getArguments();
        movie = bundle.getParcelable("MOVIE");
        getActivity().setTitle("Copies for: " + movie.getTitle());

        copyList = (ListView) getActivity().findViewById(R.id.copies_listview);
        copyArrayList = new ArrayList<>();
        copyAdapter = new CopyAdapter(getActivity(), copyArrayList);
        copyList.setAdapter(copyAdapter);

        toast = new Toast(getActivity());
        toast.setGravity(Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);

        copyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Copy copy = copyArrayList.get(position);
                if (copy.getRented()) {
                    toast = Toast.makeText(getActivity(), "This copy is unavailable", Toast.LENGTH_LONG );
                    toast.show();
                } else {
                    volleyRentCopy(copy);
                }
            }
        });

        volleyCopiesRequest();
        copyAdapter.notifyDataSetChanged();
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
                                copy.setStoreId(object.getInt("store_id"));
                                volleyCheckRented(copy);
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

    public void volleyCheckRented(final Copy copy) {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = "http://" + ip + "/api/v1/rentalinfo/" + copy.getInventoryId();
        Log.e("URL_CHECK", url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                if (object.isNull("return_date")) {
                                    copy.setRentalDate(object.getString("rental_date"));
                                    copy.setRented(true);
                                    copyAdapter.notifyDataSetChanged();
                                }
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


    public void volleyRentCopy(final Copy copy) {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String url = "http://" + ip + "/api/v1/rent/" + id + "/" + copy.getInventoryId();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            affectedRows = jsonObject.getString("affectedRows");
                            if (affectedRows.equals("0")) {
                                toast = Toast.makeText(getActivity(), "An error occurred", Toast.LENGTH_LONG );
                                toast.show();
                            } else {
                                copy.setRented(true);
                                volleyCheckRented(copy);
                                toast = Toast.makeText(getActivity(), "Copy rented!", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            //copyAdapter.notifyDataSetChanged();
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
                headers.put("X-Access-Token", token);
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }
}
