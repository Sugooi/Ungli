package com.game.shaik.lvp__detect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.game.shaik.lvp__detect.utility.API;
import com.game.shaik.lvp__detect.utility.HScoreDialogClass;
import com.game.shaik.lvp__detect.utility.MenuDialogClass;
import com.game.shaik.lvp__detect.utility.OptionsDialogClass;
import com.game.shaik.lvp__detect.utility.ScoreDialogClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements OptionsDialogClass.OptionsDialogListener,MenuDialogClass.menuDialogListener {
    Button screen;
    TextView score,tap;
    int clicks=0;
    private long mEndTime;
    ImageView menu;

    Button play,highscore,option,logout;

    private TextView mTextViewCountDown;

    public  long START_TIME_IN_MILLIS = 6000;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;
    int hs;
    public long mTimeLeftInMillis = START_TIME_IN_MILLIS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.tap);

        //initialize all the ui objects
        screen  = findViewById(R.id.screen);
        score = findViewById(R.id.score);
        tap=findViewById(R.id.TAP);
        play=findViewById(R.id.play);
        highscore=findViewById(R.id.highscore);
        option=findViewById(R.id.options);
        logout=findViewById(R.id.logout);
        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        logout = findViewById(R.id.logout);
        menu=findViewById(R.id.menu);

        SharedPreferences preferences = getSharedPreferences("default", MODE_PRIVATE);
        START_TIME_IN_MILLIS= preferences.getInt("timer",6000);
        mTimeLeftInMillis=START_TIME_IN_MILLIS;


        //update the count down text based on timer in preferences
        updateCountDownText();


        //handle highscore button clicks
        highscore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HScoreDialogClass cdd=new HScoreDialogClass(MainActivity.this);
                cdd.show();
            }
        });


        //Check for internet conectivity
        if(!isNetworkAvailable())
        {
            Toast.makeText(MainActivity.this,"Please connect to the internet and try again.",Toast.LENGTH_SHORT).show();
            finish();
        }

        //get Highscore of the User from the server
        performreq2(API.getHighscore);


        //handle option button
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //display the set timer dialog box
                OptionsDialogClass cdd=new OptionsDialogClass(MainActivity.this);

                cdd.show();




            }
        });


        //set the preference to logged in
        SharedPreferences.Editor editor = getSharedPreferences("default", MODE_PRIVATE).edit();
        editor.putBoolean("isLoggedIn", true);
        editor.commit();


        //handle log out
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));

                SharedPreferences.Editor editor = getSharedPreferences("default", MODE_PRIVATE).edit();
                editor.putBoolean("isLoggedIn", false);
                editor.commit();

                SharedPreferences preferences = getSharedPreferences("default", MODE_PRIVATE);
                hs= preferences.getInt("highscore",0);


            }
        });

        //handling the taps
        screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(play.getVisibility()!= View.VISIBLE) {
                    clicks++;
                    mp.start();
                    score.setText("Score:" + clicks);


                    tap.setVisibility(View.VISIBLE);

                    Timer t = new Timer(false);
                    t.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    tap.setVisibility(View.GONE);
                                }
                            });
                        }
                    }, 100);
                }
            }
        });

        //when clicked on the play button
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();
                play.setVisibility(View.GONE);
                screen.setVisibility(View.VISIBLE);
                highscore.setVisibility(View.GONE);
                option.setVisibility(View.GONE);
                logout.setVisibility(View.GONE);
                menu.setVisibility(View.VISIBLE);


            }
        });


        //menu or pause button while tapping
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuDialogClass cdd=new MenuDialogClass(MainActivity.this);

                cdd.show();
                cdd.setCanceledOnTouchOutside(false);

                pauseTimer();

            }
        });

        //updating the timer
        updateCountDownText();


    }



    //method to start timer
    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {


                mTimerRunning = false;
                mTimeLeftInMillis=6000;

                mTextViewCountDown.setText("00:00");
                highscore.setVisibility(View.VISIBLE);
                option.setVisibility(View.VISIBLE);
                logout.setVisibility(View.VISIBLE);
                screen.setVisibility(View.GONE);

                menu.setVisibility(View.GONE);


                SharedPreferences preferences = getSharedPreferences("default", MODE_PRIVATE);
                START_TIME_IN_MILLIS= preferences.getInt("timer",6000);

                hs=preferences.getInt("highscore",0);
                if(hs<clicks)
                {
                    SharedPreferences.Editor editor = getSharedPreferences("default", MODE_PRIVATE).edit();
                    editor.putInt("highscore", clicks);
                    editor.commit();
                    performreq(API.setHighscore);
                    hs=clicks;
                }

                ScoreDialogClass cdd=new ScoreDialogClass(MainActivity.this,hs,clicks);

                cdd.show();
                play.setVisibility(View.VISIBLE);
                clicks=0;


                score.setText("Score:");
                cdd.setCanceledOnTouchOutside(false);


            }
        }.start();

        mTimerRunning = true;

    }

    //method to pause timer
    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;

    }


    //method for resetting the timer for future purposes
    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();

    }
    //keep updating the timer
    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
    }

    //method to check network availability
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    //from optionsdialog box to set the timer from dialog box
    @Override
    public void putTimer(int timer) {
      START_TIME_IN_MILLIS=timer;
      mTimeLeftInMillis=START_TIME_IN_MILLIS;
      updateCountDownText();

    }


    //handle double back pressed continuously
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

    //volley request to server to update highscore on server for this user
    public void performreq(final String url) {


        SharedPreferences preferences = getSharedPreferences("default", MODE_PRIVATE);
        final int id= preferences.getInt("id",0);
        final int hs=preferences.getInt("highscore",0);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(LoginActivity.this,response,Toast.LENGTH_LONG).show();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {


                            } else {
                                Toast.makeText(getApplicationContext(), "Not able to upload highscore to server.", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",id+"");
                params.put("highscore",hs+"");
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);






    }

    //volley request to get the highscore of the user when logged in
    public void performreq2(final String url) {


        SharedPreferences preferences = getSharedPreferences("default", MODE_PRIVATE);
        final int id= preferences.getInt("id",0);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                        try {
                            JSONArray arr = new JSONArray(response);

                            JSONObject obj = arr.getJSONObject(0);
                             int hs= obj.getInt("highscore");

                            SharedPreferences.Editor editor = getSharedPreferences("default", MODE_PRIVATE).edit();
                            editor.putInt("highscore", hs);
                            editor.commit();



                      //      Toast.makeText(getApplicationContext(),hs+"",Toast.LENGTH_LONG).show();

                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",id+"");

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);






    }

    //after resuming from menu. from interface
    @Override
    public void resume() {
            startTimer();
    }
}
