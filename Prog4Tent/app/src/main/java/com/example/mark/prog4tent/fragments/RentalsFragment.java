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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mark.prog4tent.DetailedRentalActivity;
import com.example.mark.prog4tent.R;
import com.example.mark.prog4tent.adapter.RentalListAdapter;
import com.example.mark.prog4tent.domain.Rental;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mark on 15-6-2017.
 */

public class RentalsFragment extends Fragment {

    public static final String PREFS_NAME_TOKEN = "Prefsfile";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rentals, container, false);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Rentals");

        volleyGetRentals();
    }

    public void volleyGetRentals() {

        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                "Loading rentals. Please wait...", true);

        final ArrayList<Rental> rentals = new ArrayList<>();


        SharedPreferences  sharedPreferences = this.getActivity().getSharedPreferences(PREFS_NAME_TOKEN, Context.MODE_PRIVATE);
        String ipTemp = "";

        if (sharedPreferences.getInt("USEIP", 0) == 0) {
            ipTemp = sharedPreferences.getString("IPLOCAL", "no ip");
        }else if(sharedPreferences.getInt("USEIP", 0) == 1) {
            ipTemp = sharedPreferences.getString("IPHEROKU", "no ip");
        }


        final String ipFinal = ipTemp;

        final String id = sharedPreferences.getString("ID", "no id");
        Log.i("ID RENT", id);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        ListView rentalListView = (ListView) getActivity().findViewById(R.id.rental_listView);
        final ArrayAdapter rentalAdapter = new RentalListAdapter(getActivity(), 0, rentals);
        rentalListView.setAdapter(rentalAdapter);
        rentalAdapter.notifyDataSetChanged();

        rentalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), DetailedRentalActivity.class);
                i.putExtra("RENTAL", rentals.get(position));
                startActivity(i);
            }
        });

        String url = "http://" + ipFinal + "/api/v1/rentals/" + id ;
        Log.i("URL", url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.contains("error") && !response.isEmpty()){

                            JSONArray jsonArray = new JSONArray();

                            try {
                                jsonArray = new JSONArray(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            for(int i = 0; i < jsonArray.length(); i++){
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Rental rental = new Rental();
                                    rental.setTitle(jsonObject.getString("title"));
                                    rental.setDescription(jsonObject.getString("description"));
                                    rental.setInventory_id(jsonObject.getString("inventory_id"));
                                    rental.setRental_date(jsonObject.getString("rental_date"));
                                    rental.setReturn_date(jsonObject.getString("return_date"));
                                    rental.setYour_release(jsonObject.getString("release_year"));
                                    rental.setRental_id(jsonObject.getString("rental_id"));
                                    rental.setCustomerId(jsonObject.getString("customer_id"));
                                    rental.setStaffId(jsonObject.getString("staff_id"));

                                    boolean isRented = jsonObject.isNull("return_date");

                                    if(isRented) {
                                        rentals.add(rental);
                                        rentalAdapter.notifyDataSetChanged();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            dialog.cancel();


                        } else {
                            Log.e("ERROR", "Response: " + response);
                            dialog.cancel();
                            Toast.makeText(getActivity(), "connection failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TEMP", "Something went wrong");
                        dialog.cancel();
                        Toast.makeText(getActivity(), "connection failed", Toast.LENGTH_SHORT).show();
                    }
                }) {

            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }


        };

        requestQueue.add(stringRequest);
    }
}