package com.game.shaik.lvp__detect.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.game.shaik.lvp__detect.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by shaik on 27-01-2019.
 */
public class HScoreDialogClass extends Dialog implements
        View.OnClickListener {

    public Activity c;

    TextView hscore;


    public HScoreDialogClass(Activity a) {
        super(a);

        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hscore);

        hscore=findViewById(R.id.hscore);


        SharedPreferences preferences = c.getSharedPreferences("default", MODE_PRIVATE);
        int highscore = preferences.getInt("highscore",0);

        hscore.setText(highscore+"");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
               dismiss();
            break;


            default:
                dismiss();
                break;
        }
        dismiss();
    }


}
