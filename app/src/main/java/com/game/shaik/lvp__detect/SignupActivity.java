package com.game.shaik.lvp__detect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.game.shaik.lvp__detect.utility.API;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shaik on 27-01-2019.
 */

public class SignupActivity extends AppCompatActivity {

    ProgressBar progressBar;
    EditText email,pw,cpw,name;
    String username_r,email_r;
    Button signup;
    TextView tologin;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        progressBar=findViewById(R.id.sprogressBar);
        email=findViewById(R.id.semail);
        pw=findViewById(R.id.spass);
        name=findViewById(R.id.sname);
        cpw=findViewById(R.id.scpass);
        signup=findViewById(R.id.signup);
        tologin=findViewById(R.id.tologin);

        tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send post request to register the user
                performreq(API.signup);
            }
        });

        if(!isNetworkAvailable())
        {
            Toast.makeText(SignupActivity.this,"Please connect to the internet and try again.",Toast.LENGTH_SHORT).show();
            finish();
        }



    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    int id;
    public void performreq(final String url) {

        final String username = name.getText().toString();
        final String password = pw.getText().toString();
        final String cpassword =cpw.getText().toString();
        final String email1 = email.getText().toString();

        if (!email1.matches(emailPattern))
        {   email.setError("Invalid email address");
            email.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(username)) {
            name.setError("Please enter your username");
            name.requestFocus();
            return;
        }

        if( TextUtils.isEmpty(email1))
        {
            email.setError("Please enter your email");
        }



        if (TextUtils.isEmpty(password)) {
            pw.setError("Please enter your password");
            pw.requestFocus();
            return;
        }

        if(!cpassword.equals(password))
        {
            cpw.setError("Confirm Password should be same as Password");
            cpw.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                          Toast.makeText(SignupActivity.this,response,Toast.LENGTH_LONG).show();
                        try {
                            JSONObject obj = new JSONObject(response);
                            //if response says error:false then execute if block
                            if (!obj.getBoolean("error")) {


                                JSONObject userJson = obj.getJSONObject("user");

                                id = userJson.getInt("id");
                                username_r = userJson.getString("username");



                                //reset the preference of highscore to 0
                                SharedPreferences.Editor editor = getSharedPreferences("default", MODE_PRIVATE).edit();
                                editor.putInt("highscore", 0);
                                editor.commit();


                                progressBar.setVisibility(View.GONE);
                                finish();


                                Intent intent = new Intent(SignupActivity.this,MainActivity.class);
                                startActivity(intent);
                            } else {
                               Toast.makeText(getApplicationContext(), "Error signing up", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignupActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                //post parameters
                params.put("email",email1);
                params.put("password",password);
                params.put("username",username);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


}

