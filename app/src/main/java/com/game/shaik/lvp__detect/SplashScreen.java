package com.game.shaik.lvp__detect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                SharedPreferences preferences = getSharedPreferences("default", MODE_PRIVATE);
                Boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

                if(!isNetworkAvailable())
                {
                    Toast.makeText(SplashScreen.this,"Please connect to the internet and try again.",Toast.LENGTH_SHORT).show();
                    finish();
                }



                if (isLoggedIn) {
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);

                } else {

                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                }

                // close this activity
                finish();
            }
        }, 1500);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
