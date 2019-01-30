package com.game.shaik.lvp__detect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.game.shaik.lvp__detect.utility.API;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shaik on 27-01-2019.
 */

public class LoginActivity extends AppCompatActivity {

    EditText email,pw;
    Button login;
    String email_in,pass_in;
    ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.editText_emailAddress);
        pw=findViewById(R.id.editText_password);
        login=findViewById(R.id.login);
        progressBar=findViewById(R.id.progressBar);

        if(!isNetworkAvailable())
        {
            Toast.makeText(LoginActivity.this,"Please connect to the internet and try again.",Toast.LENGTH_SHORT).show();
            finish();
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performreq(API.login);

            }
        });
        }

    String username_r, email_r, highscore;

    int id;
    public void performreq(final String url) {

        final String username = email.getText().toString();
        final String password = pw.getText().toString();


        if (TextUtils.isEmpty(username)) {
            email.setError("Please enter your username");
            email.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            pw.setError("Please enter your password");
            pw.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                          //  Toast.makeText(LoginActivity.this,response,Toast.LENGTH_LONG).show();
                            try {
                                JSONObject obj = new JSONObject(response);
                                if (!obj.getBoolean("error")) {


                                    JSONObject userJson = obj.getJSONObject("user");

                                    id = userJson.getInt("id");
                                    username_r = userJson.getString("username");
                                    email_r = userJson.getString("email");
                                    highscore = userJson.getString("highscore");

                                    Toast.makeText(getApplicationContext(),"Welcome back, " +username_r, Toast.LENGTH_SHORT).show();



                                    SharedPreferences.Editor editor = getSharedPreferences("default", MODE_PRIVATE).edit();
                                    editor.putBoolean("isLoggedIn", true);
                                    editor.commit();


                                    progressBar.setVisibility(View.GONE);
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                } else {
                                    Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("email",username);
                    params.put("password",password);
                    return params;
                }

            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);






    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    int doubleBackToExitPressed = 1;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressed == 2) {
            finishAffinity();
            System.exit(0);
        }
        else {
            doubleBackToExitPressed++;
            Toast.makeText(this, "Please press Back again to exit", Toast.LENGTH_SHORT).show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressed=1;
            }
        }, 2000);
    }

}
