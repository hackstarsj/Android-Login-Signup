package com.example.sanjeev.loginapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

public class MainActivity extends AppCompatActivity {

    Button register,login;
    public String username="";
    public String password="";
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register=(Button)findViewById(R.id.button2);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),register.class);
                startActivity(i);
            }
        });

         login=(Button)findViewById(R.id.button);
         login.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View view) {
                 EditText Input1=(EditText)findViewById(R.id.editText5);
                 username=Input1.getText().toString();
                 EditText Input2=(EditText)findViewById(R.id.editText6);
                 password=Input2.getText().toString();
                 if(username.trim().equals(""))
                 {
                     Toast.makeText(MainActivity.this, "Email is required", Toast.LENGTH_SHORT).show();
                 }
                 else if(password.trim().equals(""))
                 {
                     Toast.makeText(MainActivity.this, "Password is required", Toast.LENGTH_SHORT).show();
                 }
                 else {

                     dialog = new ProgressDialog(MainActivity.this); // this = YourActivity
                     dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                     dialog.setMessage("Loading.. Please wait...");
                     dialog.setIndeterminate(true);
                     dialog.setCanceledOnTouchOutside(false);
                     dialog.show();


                     HttpsTrustManager.allowAllSSL();
                     RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                     StringRequest sr = new StringRequest(Request.Method.POST, "https://sjapiservice.herokuapp.com/api/login", new Response.Listener<String>() {
                         @Override
                         public void onResponse(String response) {
                             try {
                                 JSONObject jsonObj = new JSONObject(response);
                                 Log.d("Response", jsonObj.getString("login"));
                                 if (Objects.equals(jsonObj.getString("login"), "true")) {
                                     Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                 } else {
                                     Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();

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
                             params.put("email", username);
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
                     sr.setRetryPolicy(new DefaultRetryPolicy(
                             20000,
                             DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                             DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                 }
             }
         });

    }
}
