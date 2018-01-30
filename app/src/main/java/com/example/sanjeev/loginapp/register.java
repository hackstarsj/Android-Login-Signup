package com.example.sanjeev.loginapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class register extends AppCompatActivity {

    Button login,register;
     String username="";
     String password="";
    String email="";
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        login=(Button)findViewById(R.id.button2);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });
        register=(Button)findViewById(R.id.button);
        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                EditText Input1 = (EditText) findViewById(R.id.editText5);
                username = Input1.getText().toString();
                EditText Input2 = (EditText) findViewById(R.id.editText6);
                password = Input1.getText().toString();
                EditText Input3 = (EditText) findViewById(R.id.editText8);
                email = Input3.getText().toString();

                if (username.trim().equals("")) {
                    Toast.makeText(register.this, "Username is required", Toast.LENGTH_SHORT).show();
                } else if (password.trim().equals("")) {
                    Toast.makeText(register.this, "Password is required", Toast.LENGTH_SHORT).show();
                } else if (email.trim().equals("")) {
                    Toast.makeText(register.this, "Email is required", Toast.LENGTH_SHORT).show();
                } else {
                    dialog = new ProgressDialog(register.this); // this = YourActivity
                    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    dialog.setMessage("Loading.. Please wait...");
                    dialog.setIndeterminate(true);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();


                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest sr = new StringRequest(Request.Method.POST, "https://sjapiservice.herokuapp.com/api/register", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                //Log.d("Response L",jsonObj.getString("login"));
                                //Log.d("Response R",jsonObj.getString("register"));
                                if (Objects.equals(jsonObj.getString("register"), "true")) {

                                    Toast.makeText(register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(register.this, "Error Occured try Again", Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("Response", response);
                            dialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Errors", String.valueOf(error));
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("email", email);
                            params.put("username", username);
                            params.put("password", password);
                            params.put("site", "site1");

                            return params;
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Content-Type", "application/x-www-form-urlencoded");
                            return params;
                        }
                    };
                    queue.add(sr);
                }
            }
        });
    }
}
