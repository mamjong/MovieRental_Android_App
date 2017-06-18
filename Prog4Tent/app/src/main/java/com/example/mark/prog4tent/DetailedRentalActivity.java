package com.example.mark.prog4tent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mark.prog4tent.domain.Rental;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailedRentalActivity extends AppCompatActivity {

    public static final String PREFS_NAME_TOKEN = "Prefsfile";
    TextView titleTextView, rentalDateTextView, returnDateTextView, descTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_rental);

        final Rental rental = (Rental) getIntent().getExtras().getSerializable("RENTAL");

        titleTextView = (TextView) findViewById(R.id.dra_title_tv);
        titleTextView.setText(rental.getTitle());
        rentalDateTextView = (TextView) findViewById(R.id.rental_date_tv);
        rentalDateTextView.setText(rental.getReturn_date());
        returnDateTextView = (TextView) findViewById(R.id.return_date_tv);
        returnDateTextView.setText(rental.getReturn_date());
        descTextView = (TextView) findViewById(R.id.descr_tv);
        descTextView.setText(rental.getDescription());

        Button inleverButton = (Button) findViewById(R.id.inlever_button);
        inleverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volleyHandIn(rental.getCustomerId(), rental.getInventory_id(), rental);
            }
        });













    }
    public void volleyHandIn(String cutomerID, String invenrotyID, final Rental rental) {

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME_TOKEN, Context.MODE_PRIVATE);
        String ipTemp = "";

        if (sharedPreferences.getInt("USEIP", 0) == 0) {
            ipTemp = sharedPreferences.getString("IPLOCAL", "no ip");
        }else if(sharedPreferences.getInt("USEIP", 0) == 1) {
            ipTemp = sharedPreferences.getString("IPHEROKU", "no ip");
        }


        final String ipFinal = ipTemp;

        final String token = sharedPreferences.getString("TOKEN", "no token");

        final String custID = cutomerID;
        final String invID = invenrotyID;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = "http://"+ ipFinal + "/api/v1/rentals?customerId=" + custID + "&inventoryId=" + invID;


        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.contains("error") && !response.isEmpty()){
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String changedRows = jsonObject.getString("affectedRows");
                                Log.i("ROWS", changedRows);
                                if(changedRows.equals("1") || changedRows.equals("2")){
                                    Toast.makeText(getApplicationContext(), "succesvol ingeverd", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), DetailedRentalActivity.class);
                                    startActivity(i);
                                }else{
                                    Toast.makeText(getApplicationContext(), "niet succesvol ingeverd", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }





                        } else {
                            Log.e("ERROR", "Response: " + response);
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TEMP", "Something went wrong");
                    }
                }) {

            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("X-Access-Token",token);
                return headers;
            }

            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            public byte[] getBody() throws AuthFailureError {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-dd-MM hh:mm:ss");
                String now = simpleDateFormat.format(new Date());




                String mContent = "{\"RentalDate\":\"" + rental.getRental_date() + "\",\"ReturnDate\":\"" + now +  "\",\"staffId\":\"" + rental.getStaffId() +  "\"}";
                byte[] body = new byte[0];
                try {
                    body = mContent.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return body;
            }


        };

        requestQueue.add(stringRequest);
    }
}
