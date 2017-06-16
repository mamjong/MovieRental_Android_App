package com.example.mark.prog4tent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameText, passwordText;
    private Button loginBtn;
    private TextView registerTextView;
    

    public static final String PREFS_NAME_TOKEN = "Prefsfile";
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editor = getSharedPreferences(PREFS_NAME_TOKEN, MODE_PRIVATE).edit();
        editor.putString("IP", "tentamenmm.herokuapp.com");
        editor.commit();



        registerTextView = (TextView) findViewById(R.id.login_tv_register);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });

        usernameText = (EditText) findViewById(R.id.login_et_username);
        passwordText = (EditText) findViewById(R.id.login_et_password);
        loginBtn = (Button) findViewById(R.id.login_btn_login);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volleyLogin(usernameText.getText().toString(), passwordText.getText().toString());

            }
        });
    }

    public void volleyLogin(String un, String pw) {

        final ProgressDialog dialog = ProgressDialog.show(LoginActivity.this, "",
                "Signing in. Please wait...", true);

        SharedPreferences  sharedPreferences = getSharedPreferences(PREFS_NAME_TOKEN, Context.MODE_PRIVATE);
        final String ip = sharedPreferences.getString("IP", "no ip");

        final String username = un;
        final String password = pw;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = "http://"+ ip + "/api/v1/login";

        Log.i("URL", url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.contains("error") && !response.isEmpty()){
                            String token = "no token found";
                            String id = "no id found";
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                token = jsonObject.getString("token");
                                id = jsonObject.getString("id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            dialog.cancel();
                            startActivity(i);
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
                return headers;
            }

            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            public byte[] getBody() throws AuthFailureError {
                String mContent = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
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
